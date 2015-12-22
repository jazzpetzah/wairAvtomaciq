#!/usr/bin/env python

from cli_handlers.cli_handler_base import CliHandlerBase
from cli_handlers.testrail_utilities import TestrailUtilities


class VerifyTestrailRunExists(CliHandlerBase, TestrailUtilities):
    def _build_options(self, parser):
        super(VerifyTestrailRunExists, self)._build_options(parser)
        parser.add_argument('--server_url', required=True,
                            help='The url of Testrail server. Required parameter')
        parser.add_argument('--username', required=True,
                            help='The name of Testrail server user. Required parameter')
        parser.add_argument('--token', required=True,
                            help='The token of Testrail server user. Required parameter')
        parser.add_argument('--project_name', required=True,
                            help='The name of Testrail project, which contains test run. Required parameter')
        parser.add_argument('--plan_name', required=True,
                            help='The name of Testrail plan, which contains test run. Required parameter')
        parser.add_argument('--run_name', required=True,
                            help='The name of Testrail run, to verify. Required parameter')
        parser.add_argument('--config_name', default=None,
                            help='The name of Testrail config(s), to verify. This is comma-separated string.'
                                 ' Optional value')

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        return self._get_test_run_id(args)

    def _is_exceptions_handled_in_invoke(self):
        return True
