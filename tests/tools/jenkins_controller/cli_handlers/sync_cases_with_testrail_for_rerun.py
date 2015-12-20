#!/usr/bin/env python

from cli_handlers.cli_handler_base import CliHandlerBase
from cli_handlers.gherkin_utilities import GherkinUtilities
from cli_handlers.testrail_utilities import TestrailUtilities, TESTRAIL_TAG_MAGIC


RERUN_TAG = '@rerun'


class SyncCasesWithTestrailForRerun(CliHandlerBase, TestrailUtilities, GherkinUtilities):
    def _build_options(self, parser):
        super(SyncCasesWithTestrailForRerun, self)._build_options(parser)
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
        parser.add_argument('--features_root', required=True,
                            help='The root folder of feature files to be synchronized. Required parameter')

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        # 4 - means Retest status for Testrail
        case_ids = self._get_run_cases(args, '4')
        return ','.join(self._update_feature_files(args.features_root,
                                                   map(lambda x: '{}{}'.format(TESTRAIL_TAG_MAGIC, x),
                                                       case_ids), RERUN_TAG))

    def _is_exceptions_handled_in_invoke(self):
        return True
