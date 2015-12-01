#!/usr/bin/env python

import inspect
from multiprocessing import Process, Queue
import paramiko
from pprint import pformat
import random
import re
import smtplib
import socket
import sys
import time
import traceback
import xml.etree.ElementTree as ET

from cli_handlers.cli_handler_base import CliHandlerBase


VERIFICATION_JOB_TIMEOUT = 60 * 3 #seconds
MAX_VERIFICATION_JOBS = 4

normalize_labels = lambda labels_list: set(map(lambda x: x.strip(), labels_list))

def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in xrange(0, len(l), n):
        yield l[i:i+n]


class NodesCountForLabels(CliHandlerBase):
    def _build_options(self, parser):
        super(NodesCountForLabels, self)._build_options(parser)
        parser.add_argument('--labels', required=True,
                            help='List of comma-separated node labels to match')
        parser.add_argument('--apply_verification', default=None,
                            help='Whether to apply special verification for a node. '
                                 'Available verifications: RealAndroidDevice, IOSSimulator')
        parser.add_argument('--ios_simulator_name', default=None,
                            help='The name of iOS simulator to verify. '
                                 'Used together with --apply_verification=IOSSimulator')
        parser.add_argument('--node_user', default=None,
                            help='Jenkins node user')
        parser.add_argument('--node_password', default=None,
                            help='Jenkins node password')
        parser.add_argument('--notification_receivers', default=None,
                            help='The comma-separated list of people to notify about problems on nodes. '\
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
                verifier.join(timeout=VERIFICATION_JOB_TIMEOUT)
                if verifier.is_alive():
                    sys.stderr.write(
                        'Verifier process for the node "{}" timed out. Assuming the node as broken by default...\n'.\
                         format(verifier.node.name))
                    # broken_nodes_queue.put_nowait(verifier.node)
                    verifier.terminate()
                else:
                    sys.stderr.write('Finished verification for the node "{}"\n'.format(verifier.node.name))
        ready_nodes = []
        while not ready_nodes_queue.empty():
            ready_nodes.append(ready_nodes_queue.get_nowait().name)
        broken_nodes = []
        while not broken_nodes_queue.empty():
            broken_nodes.append(broken_nodes_queue.get_nowait().name)
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
                msg = 'The device connected to node "{}" seems to be not accessible:\n{}'.\
                                 format(self._node.name, output)
                sys.stderr.write(msg)
                self._send_email_notification('"{}" node is broken'.format(self._node.name), msg)
                return False
            result, output = self._is_connected_to_internet(client)
            if not result:
                msg = 'The device connected to node "{}" seems to be offline:\n{}'.\
                                 format(self._node.name, output)
                sys.stderr.write(msg)
                self._send_email_notification('"{}" node is broken'.format(self._node.name), msg)
                return False
            return result
        finally:
            client.close()


IOS_SIMULATOR_BOOT_TIMEOUT = 60 * 2 # seconds

class IOSSimulator(BaseNodeVerifier):
    def _get_installed_simulators(self, ssh_client):
        _, stdout, _ = ssh_client.exec_command('/usr/bin/xcrun instruments -s')
        matches = re.findall(r'([\w\s\(\)\.]+)\[(\w{8}\-\w{4}\-\w{4}\-\w{4}\-\w{12})\]', stdout.read())
        result = {}
        if matches:
            for match in matches:
                result[match[0].strip()] = match[1].strip()
        return result

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

            client.exec_command('/usr/bin/killall "iOS Simulator"')
            time.sleep(1)

            available_simulators = self._get_installed_simulators(client)
            dst_name = filter(lambda x: x.lower().find(simulator_name.lower()) >= 0,
                              available_simulators.iterkeys())
            result = True
            if dst_name:
                simulator_name = dst_name[0]
            else:
                msg = 'There is no "{}" simulator available. The list of available simulators for the node "{}":\n{}\n'.\
                                 format(simulator_name, self._node.name, pformat(available_simulators))
                sys.stderr.write(msg)
                self._send_email_notification('Non-existing simulator name "{}" has been provided'.\
                                              format(simulator_name), msg)
                return False
            try:
                client.exec_command('/usr/bin/xcrun instruments -w "{}"'.format(simulator_name),
                                    timeout=IOS_SIMULATOR_BOOT_TIMEOUT)
            except (socket.timeout, paramiko.SSHException) as e:
                msg = 'The "{}" simulator is still booting after {} seconds timeout.\n'.\
                                 format(simulator_name, IOS_SIMULATOR_BOOT_TIMEOUT)
                sys.stderr.write(msg)
                self._send_email_notification('"{}" node is broken'.format(self._node.name), msg)
                result = False
            client.exec_command('/usr/bin/killall "iOS Simulator"')
            return result
        finally:
            client.close()
