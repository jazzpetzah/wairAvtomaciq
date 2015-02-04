#!/usr/bin/env python

import xml.etree.ElementTree as ET

from cli_handlers.cli_handler_base import CliHandlerBase


class NodesCountForLabels(CliHandlerBase):
    def _build_options(self, parser):
        super(NodesCountForLabels, self)._build_options(parser)
        parser.add_argument('--labels', required=True,
                            help='List of comma-separated node labels to match')
    
    def _normalize_labels(self, labels_list):
        normalized_labels = map(lambda x: x.strip(), labels_list)
        return set(normalized_labels)
    
    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        expected_labels = self._normalize_labels(args.labels.split(','))
        result_nodes_count = 0
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
                    result_nodes_count += 1
        return result_nodes_count
