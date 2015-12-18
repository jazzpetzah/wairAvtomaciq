#!/usr/bin/env python

from cli_handlers.cli_handler_base import CliHandlerBase
from cli_handlers.gherkin_utilities import GherkinUtilities
from cli_handlers.testrail_utilities import TestrailUtilities, TESTRAIL_TAG_MAGIC


MUTE_TAG = '@mute'


class SyncMutedCasesWithTestrail(CliHandlerBase, TestrailUtilities, GherkinUtilities):
    def _build_options(self, parser):
        super(SyncMutedCasesWithTestrail, self)._build_options(parser)
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
        parser.add_argument('--features_root', required=True,
                            help='The root folder of feature files to be synchronized. Required parameter')

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        case_ids = self._filter_cases(args, 'custom_is_muted=True')
        return ','.join(self._update_feature_files(args.feature_root,
                                                   map(lambda x: '{}{}'.format(TESTRAIL_TAG_MAGIC, x),
                                                       case_ids), MUTE_TAG))

    def _is_exceptions_handled_in_invoke(self):
        return True
