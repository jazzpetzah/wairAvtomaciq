#!/usr/bin/env python

from multiprocessing import Process
import os
import paramiko
import sys
import time
import xml.etree.ElementTree as ET

from cli_handlers.cli_handler_base import CliHandlerBase

OFFLINE_TIMEOUT_SECONDS = 60
ONLINE_TIMEOUT_SECONDS = 60 * 3


class RestartNodesWithLabels(CliHandlerBase):
    def _build_options(self, parser):
        super(RestartNodesWithLabels, self)._build_options(parser)
        parser.add_argument('--labels', required=True,
                            help='List of comma-separated node labels to match')
        parser.add_argument('--node_user', required=True,
                            help='Jenkins node user')
        parser.add_argument('--node_password', required=True,
                            help='Jenkins node password')

    def _normalize_labels(self, labels_list):
        normalized_labels = map(lambda x: x.strip(), labels_list)
        return set(normalized_labels)

    def _restart_node_and_wait(self, node, hostname, username, password):
        client = paramiko.SSHClient()
        try:
            client.load_system_host_keys()
            client.set_missing_host_key_policy(paramiko.WarningPolicy())
            client.connect(hostname, username=username, password=password)
            client.exec_command('sudo shutdown -r now')
        finally:
            client.close()
        seconds_started = time.time()
        while time.time() - seconds_started <= OFFLINE_TIMEOUT_SECONDS:
            if node.is_online():
                time.sleep(5)
            else:
                sys.stderr.write('Node "{}" has been successfully transitioned to offline state after {} seconds\n'
                                 .format(node.name, int(time.time() - seconds_started)))
                break
        if node.is_online():
            sys.stderr.write('!!! Node "{}" is still online after {} seconds timeout\n'.
                             format(node.name, OFFLINE_TIMEOUT_SECONDS))
        seconds_started = time.time()
        while time.time() - seconds_started <= ONLINE_TIMEOUT_SECONDS:
            if not node.is_online():
                time.sleep(5)
            else:
                sys.stderr.write('Node "{}" has been successfully restarted after {} seconds\n'.
                                 format(node.name, int(time.time() - seconds_started)))
                return node.name
        if not node.is_online():
            sys.stderr.write('!!! Node "{}" is still offline after {} seconds timeout\n'.
                             format(node.name, ONLINE_TIMEOUT_SECONDS))

    @staticmethod
    def _is_alive(host):
        return os.system('ping -c1 {}'.format(host)) == 0

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        expected_labels = self._normalize_labels(args.labels.split(','))
        count_of_nodes_to_restart = 0
        workers = []
        for _, node in self._jenkins.get_nodes().iteritems():
            response = node.jenkins.requester.get_and_confirm_status("%(baseurl)s/config.xml" % node.__dict__)
            et = ET.fromstring(response.text)
            node_labels_str = et.find('label').text
            if node_labels_str:
                node_labels = self._normalize_labels(node_labels_str.split(' '))
            else:
                node_labels = set()
            if expected_labels.issubset(node_labels):
                try:
                    hostname = et.find('.//host').text
                except Exception:
                    sys.stderr.write('Cannot read hostname property of "{}" node! Skipping it...\n'.format(node.name))
                    continue
                if not self._is_alive(hostname):
                    sys.stderr.write('The host {} seems to be already offline\n'.format(hostname))
                    continue
                sys.stderr.write('Found matching node "{}". Restarting...\n'.format(node.name))
                workers.append(Process(target=self._restart_node_and_wait,
                                       args=(node, hostname, args.node_user, args.node_password)))
                workers[-1].start()
                count_of_nodes_to_restart += 1
        [w.join() for w in workers]
        return count_of_nodes_to_restart
