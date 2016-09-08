#!/usr/bin/env python

import inspect
from multiprocessing import Process, Queue, Lock
import paramiko
from pprint import pformat
import random
import re
import smtplib
import socket
import sys
import time
import tempfile
import traceback
import os
import xml.etree.ElementTree as ET

from cli_handlers.cli_handler_base import CliHandlerBase

MAX_NODES_PER_HOST = 5
DEFAULT_VERIFICATION_JOB_TIMEOUT = 60 * 2  # seconds
IOS_SIM_VERIFICATION_JOB_TIMEOUT = 30  # seconds
MAX_VERIFICATION_JOBS = 4

normalize_labels = lambda labels_list: set(map(lambda x: x.strip(), labels_list))


def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in xrange(0, len(l), n):
        yield l[i:i + n]


class NodesCountForLabels(CliHandlerBase):
    def _build_options(self, parser):
        super(NodesCountForLabels, self)._build_options(parser)
        parser.add_argument('--labels', required=True,
                            help='List of comma-separated node labels to match')
        parser.add_argument('--apply_verification', default=None,
                            help='Whether to apply special verification for a node. '
                                 'Available verifications: see class names below')
        parser.add_argument('--ios_simulator_name', default=None,
                            help='The name of iOS simulator to verify. '
                                 'Used together with --apply_verification=IOSSimulator')
        parser.add_argument('--node_user', default=None,
                            help='Jenkins node user')
        parser.add_argument('--node_password', default=None,
                            help='Jenkins node password')
        parser.add_argument('--notification_receivers', default=None,
                            help='The comma-separated list of people to notify about problems on nodes. ' \
                                 'No notifications will be sent if this parameter is empty')

    def _get_verifier_class(self, name):
        clsmembers = inspect.getmembers(sys.modules[__name__], inspect.isclass)
        for cls_name, member_type in clsmembers:
            if cls_name.lower() == name.lower():
                return member_type
        if name is None:
            return BaseNodeVerifier
        raise RuntimeError('There is no verifier, which matches to "{}"'.format(name))

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        expected_labels = normalize_labels(args.labels.split(','))
        verifier_class = self._get_verifier_class(args.apply_verification)
        ready_nodes_queue = Queue()
        broken_nodes_queue = Queue()
        verifiers = []
        for _, node in self._jenkins.get_nodes().iteritems():
            if node.is_online():
                response = node.jenkins.requester.get_and_confirm_status(
                    "%(baseurl)s/config.xml" % node.__dict__)
                et = ET.fromstring(response.text)
                node_labels_str = et.find('label').text
                if node_labels_str:
                    node_labels = normalize_labels(node_labels_str.split(' '))
                else:
                    node_labels = set()
                if not expected_labels.issubset(node_labels):
                    continue
                verifiers.append(verifier_class(node, et.find('.//host').text, ready_nodes_queue, broken_nodes_queue,
                                                node_user=args.node_user, node_password=args.node_password,
                                                ios_simulator_name=args.ios_simulator_name,
                                                notification_receivers=args.notification_receivers))
        for verifiers_chunk in chunks(verifiers, MAX_VERIFICATION_JOBS):
            for verifier in verifiers_chunk:
                verifier.start()
            for verifier in verifiers_chunk:
                verifier.join(timeout=verifier.get_timeout())
                if verifier.is_alive():
                    sys.stderr.write(
                        '\nVerifier process for the node "{}" timed out after {} seconds. '
                        'Assuming the node as ready by default...\n'.format(verifier.node.name,
                                                                             verifier.get_timeout()))
                    ready_nodes_queue.put_nowait(verifier.node)
                    # broken_nodes_queue.put_nowait(verifier.node)
                else:
                    sys.stderr.write('\nFinished verification for the node "{}"\n'.format(verifier.node.name))
        ready_nodes = set()
        while not ready_nodes_queue.empty():
            ready_nodes.add(ready_nodes_queue.get_nowait().name)
        broken_nodes = set()
        while not broken_nodes_queue.empty():
            broken_nodes.add(broken_nodes_queue.get_nowait().name)
        for verifier in verifiers:
            if verifier.is_alive():
                verifier.terminate()
        # just to make sure the same node does not exist in both lists because of
        # concurrency issues
        ready_nodes -= broken_nodes
        return '{}|{}'.format(','.join(ready_nodes), ','.join(broken_nodes))


class BaseNodeVerifier(Process):
    def __init__(self, node, node_hostname, ready_nodes_queue, broken_nodes_queue, **kwargs):
        super(BaseNodeVerifier, self).__init__()
        self._node = node
        self._node_hostname = node_hostname
        self._ready_nodes_queue = ready_nodes_queue
        self._broken_nodes_queue = broken_nodes_queue
        self._verification_kwargs = kwargs

    @property
    def node(self):
        return self._node

    def _send_email_notification(self, subject, body):
        if 'notification_receivers' not in self._verification_kwargs or \
                        self._verification_kwargs['notification_receivers'] is None:
            return
        TO = map(lambda x: x.strip(),
                 self._verification_kwargs['notification_receivers'].split(','))
        gmail_user = 'smoketester@wire.com'
        gmail_pwd = 'aqa123456!'
        FROM = gmail_user
        SUBJECT = subject
        TEXT = body
        message = """\From: %s\nTo: %s\nSubject: %s\n\n%s
        """ % (FROM, ", ".join(TO), SUBJECT, TEXT)
        try:
            server = smtplib.SMTP("smtp.gmail.com", 587)
            server.ehlo()
            server.starttls()
            server.login(gmail_user, gmail_pwd)
            server.sendmail(FROM, TO, message)
            server.close()
        except Exception:
            traceback.print_exc()

    def _is_verification_passed(self):
        return True

    def get_timeout(self):
        return DEFAULT_VERIFICATION_JOB_TIMEOUT

    def run(self):
        MAX_TRY_COUNT = 2
        try_num = 0
        is_passed = False
        while True:
            try:
                is_passed = self._is_verification_passed()
                break
            except Exception as e:
                traceback.print_exc()
                try_num += 1
                if try_num >= MAX_TRY_COUNT:
                    self._broken_nodes_queue.put_nowait(self._node)
                    raise e
                sys.stderr.write('Sleeping a while before retry #{} of {}...\n'.format(try_num, MAX_TRY_COUNT))
                time.sleep(random.randint(2, 10))
        if is_passed:
            self._ready_nodes_queue.put_nowait(self._node)
        else:
            self._broken_nodes_queue.put_nowait(self._node)


class RealAndroidDevice(BaseNodeVerifier):
    def _is_connected_to_internet(self, ssh_client):
        _, stdout, _ = ssh_client.exec_command('/usr/local/bin/adb shell ping -c 5 8.8.8.8')
        output1 = stdout.read()
        result = output1.find('bytes from 8.8.8.8') > 0
        if not result:
            _, stdout, _ = ssh_client.exec_command('/usr/local/bin/adb shell netstat')
            output2 = stdout.read()
            result = len(output2.strip().split('\n')) == 1 or \
                     output2.find('ESTABLISHED') > 0 or output2.find('LISTEN') > 0
            return (result, '{}\n\n{}'.format(output1, output2))
        return (result, output1)

    def _is_verification_passed(self):
        result = super(RealAndroidDevice, self)._is_verification_passed()
        if not result:
            return False
        client = paramiko.SSHClient()
        try:
            client.load_system_host_keys()
            client.set_missing_host_key_policy(paramiko.WarningPolicy())
            client.connect(self._node_hostname, username=self._verification_kwargs['node_user'],
                           password=self._verification_kwargs['node_password'])
            _, stdout, _ = client.exec_command('/usr/local/bin/adb devices -l')
            output = stdout.read()
            result = output.find('device:') > 0 and output.find('offline') < 0
            if not result:
                msg = 'The device connected to node "{}" seems to be not accessible:\n{}'. \
                    format(self._node.name, output)
                sys.stderr.write(msg)
                self._send_email_notification('"{}" node is broken'.format(self._node.name), msg)
                return False
            result, output = self._is_connected_to_internet(client)
            if not result:
                msg = 'The device connected to node "{}" seems to be offline:\n{}'. \
                    format(self._node.name, output)
                sys.stderr.write(msg)
                self._send_email_notification('"{}" node is broken'.format(self._node.name), msg)
                return False
            return result
        finally:
            client.close()


class IOSSimulator(BaseNodeVerifier):
    def _get_installed_simulators(self, ssh_client):
        _, stdout, _ = ssh_client.exec_command('/usr/bin/instruments -s')
        matches = re.findall(r'([\w\s\(\)\.]+)\[(\w{8}\-\w{4}\-\w{4}\-\w{4}\-\w{12})\]', stdout.read())
        result = {}
        if matches:
            for match in matches:
                result[match[0].strip()] = match[1].strip()
        return result

    def get_timeout(self):
        return IOS_SIM_VERIFICATION_JOB_TIMEOUT

    def _adjust_simulator_size(self, ssh_client, scale_factor):
        """
        :param ssh_client:
        :param scale_factor: 1 to 5, where 1 is 100% and 5 is 25%
        :return:
        """
        simulator_size_setter_script = \
            """#!/bin/bash
            /usr/bin/osascript -e "tell application \\"System Events\\"" \\
                        -e "tell application \\"Simulator\\" to activate" \\
                        -e "do shell script \\"/bin/sleep 3\\"" \\
                        -e "tell application \\"System Events\\" to keystroke \\"{}\\" using {{command down}}" \\
                        -e "end tell"
            """.format(scale_factor)
        _, path = tempfile.mkstemp(suffix=".sh")
        with open(path, 'w') as f:
            f.write(simulator_size_setter_script)
        sftp = ssh_client.open_sftp()
        try:
            dst_path = '/tmp/simscale.sh'
            sftp.put(path, dst_path)
        finally:
            sftp.close()
        ssh_client.exec_command('/bin/chmod u+x ' + dst_path)
        ssh_client.exec_command('/usr/bin/open -a Terminal ' + dst_path)

    def _is_verification_passed(self):
        result = super(IOSSimulator, self)._is_verification_passed()
        if not result:
            return False
        client = paramiko.SSHClient()
        try:
            client.load_system_host_keys()
            client.set_missing_host_key_policy(paramiko.WarningPolicy())
            client.connect(self._node_hostname, username=self._verification_kwargs['node_user'],
                           password=self._verification_kwargs['node_password'])
            simulator_name = self._verification_kwargs['ios_simulator_name']

            available_simulators = self._get_installed_simulators(client)
            dst_name = filter(lambda x: x.lower().find(simulator_name.lower()) >= 0, available_simulators.iterkeys())
            result = True
            if dst_name:
                simulator_name = dst_name[0]
            else:
                msg = 'There is no "{}" simulator available. The list of available simulators for the node "{}":\n{}\n'. \
                    format(simulator_name, self._node.name, pformat(available_simulators))
                sys.stderr.write(msg)
                self._send_email_notification('Non-existing simulator name "{}" has been provided'.
                                              format(simulator_name), msg)
                return False
            if result is True:
                sys.stderr.write('Adjusting simulator scale...')
                scale_factor = 4
                if simulator_name.lower().find('iphone') >= 0 and simulator_name.lower().find('plus') < 0:
                    scale_factor = 3
                self._adjust_simulator_size(client, scale_factor)
            return result
        finally:
            client.close()


POWER_CYCLE_SCRIPT = lambda devnum: \
"""
import time

from brainstem import discover
from brainstem.link import Spec
from brainstem.stem import USBHub2x4


stem = USBHub2x4()
spec = discover.find_first_module(Spec.USB)
if spec is None:
    raise RuntimeError("No USBHub is connected!")
stem.connect_from_spec(spec)
stem.usb.setPowerDisable({0})
time.sleep(1)
stem.usb.setPowerEnable({0})
time.sleep(1)
""".format(devnum)


class IOSRealDevice(BaseNodeVerifier):
    LOCK = Lock()

    def _get_connected_devices(self, ssh_client):
        _, stdout, _ = ssh_client.exec_command('/usr/sbin/system_profiler SPUSBDataType')
        return re.findall(r'Serial Number: ([0-9a-f]{40})', stdout.read())

    def _get_dst_vm_ip(self):
        return socket.gethostbyname(self._node_hostname)

    def _get_dst_vm_number(self):
        ip_as_list = self._get_dst_vm_ip().split('.')
        # Must be IPv4
        assert len(ip_as_list) == 4
        last_digit = int(ip_as_list[-1])
        return last_digit % MAX_NODES_PER_HOST

    def _get_dst_vm_host_ip(self):
        ip_as_list = self._get_dst_vm_ip().split('.')
        # Must be IPv4
        assert len(ip_as_list) == 4
        last_digit = int(ip_as_list[-1])
        return '.'.join(ip_as_list[:3] + [str(last_digit - (last_digit % MAX_NODES_PER_HOST))])

    def _run_device_power_cycle(self, ssh_client):
        sftp = ssh_client.open_sftp()
        _, localpath = tempfile.mkstemp(suffix='.py')
        try:
            with open(localpath, 'w') as f:
                f.write(POWER_CYCLE_SCRIPT(self._get_dst_vm_number() - 1))
            remotepath = '/tmp/' + os.path.basename(localpath)
            sftp.put(localpath, remotepath)
        finally:
            sftp.close()
            os.unlink(localpath)
        stdin, stdout, stderr = ssh_client.exec_command('python "{}"'.format(remotepath))
        if stdout.channel.recv_exit_status() != 0:
            sys.stderr.write('!!! Error on power cycle script execution:\n')
            sys.stderr.write(stderr.read() + '\n')
        ssh_client.exec_command('rm -f "{}"'.format(remotepath))

    def _is_verification_passed(self):
        result = super(IOSRealDevice, self)._is_verification_passed()
        if not result:
            return False

        client = paramiko.SSHClient()
        self.LOCK.acquire()
        try:
            client.load_system_host_keys()
            client.set_missing_host_key_policy(paramiko.WarningPolicy())
            client.connect(self._get_dst_vm_host_ip(),
                           username=self._verification_kwargs['node_user'],
                           password=self._verification_kwargs['node_password'])
            self._run_device_power_cycle(client)
        finally:
            client.close()
            self.LOCK.release()

        client = paramiko.SSHClient()
        try:
            client.load_system_host_keys()
            client.set_missing_host_key_policy(paramiko.WarningPolicy())
            client.connect(self._node_hostname, username=self._verification_kwargs['node_user'],
                           password=self._verification_kwargs['node_password'])
            seconds_started = time.time()
            available_devices = []
            while time.time() - seconds_started <= 10:
                available_devices = self._get_connected_devices(client)
                if available_devices:
                    break
                time.sleep(0.5)
            if not available_devices:
                msg = 'There are no real iOS device(s) connected to the node "{}"'.format(self._node.name)
                sys.stderr.write(msg)
                self._send_email_notification('No real iOS device(s) are connected', msg)
                return False
            return result
        finally:
            client.close()
