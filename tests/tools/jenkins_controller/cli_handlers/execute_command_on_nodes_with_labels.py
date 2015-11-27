#!/usr/bin/env python

import paramiko
from multiprocessing import Process
import sys
import xml.etree.ElementTree as ET

from cli_handlers.cli_handler_base import CliHandlerBase


class ExecuteCommandOnNodesWithLabels(CliHandlerBase):
    def _build_options(self, parser):
        super(ExecuteCommandOnNodesWithLabels, self)._build_options(parser)
        parser.add_argument('--labels', required=True,
                            help='List of comma-separated node labels to match')
        parser.add_argument('--node_user', required=True,
                            help='Jenkins node user')
        parser.add_argument('--node_password', required=True,
                            help='Jenkins node password')
        parser.add_argument('--command', required=True,
                            help='Bash command to execute')

    def _normalize_labels(self, labels_list):
        normalized_labels = map(lambda x: x.strip(), labels_list)
        return set(normalized_labels)

    def _execute_shell_command(self, node, hostname, username, password, command):
        client = paramiko.SSHClient()
        try:
            client.load_system_host_keys()
            client.set_missing_host_key_policy(paramiko.WarningPolicy())
            client.connect(hostname, username=username, password=password)
            client.exec_command(command)
        finally:
            client.close()

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        expected_labels = self._normalize_labels(args.labels.split(','))
        count_of_nodes = 0
        workers = []
        for _, node in self._jenkins.get_nodes().iteritems():
            if node.is_online():
                response = node.jenkins.requester.get_and_confirm_status(
                                                "%(baseurl)s/config.xml" % node.__dict__)
                et = ET.fromstring(response.text)
                node_labels_str = et.find('label').text
                if node_labels_str:
                    node_labels = self._normalize_labels(node_labels_str.split(' '))
                else:
                    node_labels = set()
                if expected_labels.issubset(node_labels):
                    sys.stderr.write('Found matching node "{}". Executing shell command "{}"...\n'.\
                                     format(node.name, args.command))
                    hostname = et.find('.//host').text
                    workers.append(Process(target=self._execute_shell_command,
                                args=(node, hostname, args.node_user, args.node_password,
                                      args.command)))
                    workers[-1].start()
                    count_of_nodes += 1
        [w.join() for w in workers]
        return count_of_nodes
