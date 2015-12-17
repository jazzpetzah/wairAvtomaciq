#!/usr/bin/env python

from cli_handlers.cli_handler_base import CliHandlerBase
from cli_handlers.testrail_utilities import TestrailUtilities


class GetTestrailCases(CliHandlerBase, TestrailUtilities):
    def _build_options(self, parser):
        super(GetTestrailCases, self)._build_options(parser)
        parser.add_argument('--server_url', required=True,
                            help='The url of Testrail server. Required parameter')
        parser.add_argument('--username', required=True,
                            help='The name of Testrail server user. Required parameter')
        parser.add_argument('--token', required=True,
                            help='The token of Testrail server user. Required parameter')
        parser.add_argument('--project_name', required=True,
                            help='The name of Testrail project, which contains test run. Required parameter')
        parser.add_argument('--suite_name', required=True,
                            help='The name of Testrail suite, which contains test cases. Required parameter')
        parser.add_argument('--filter_by', default=None,
                            help='This is comma-separated string of possible test case field filters. '
                                 'Optional value')

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        return self._filter_cases(args)

    def _is_exceptions_handled_in_invoke(self):
        return True
