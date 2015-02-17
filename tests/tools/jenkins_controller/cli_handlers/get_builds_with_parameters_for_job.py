#!/usr/bin/env python

import traceback

from cli_handlers.cli_handler_base import CliHandlerBase


class GetBuildsWithParametersForJob(CliHandlerBase):
    def _build_options(self, parser):
        super(GetBuildsWithParametersForJob, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to check. Required parameter')
        parser.add_argument('--parameters', required=True,
                            help='The comma separated list of parameters and their values, '
                            'which found builds should contain. Required parameter')
        parser.add_argument('--only-running', action='store_true', default=False,
                            help='Check only builds, which are currently running',
                            dest="only_running")
        parser.add_argument('--queue_timeout',
                            help='Maximum time to wait while this job has queued items (in seconds, 300 by default). '\
                            'Set it to negative value if you want to ignore this verification.')
        parser.add_argument('--depth',
                            help='How many recent builds should we check for the particular job')
        parser.set_defaults(depth=200, queue_timeout=300)

    def _normalize_parameters(self, params_str):
        name_value = map(lambda x: x.strip(), params_str.split(','))
        return dict(map(lambda x: (x.split('=')[0], x.split('=')[1]), name_value))

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job = self._jenkins.get_job(args.name)
        self._wait_while_job_in_queue(job, args.queue_timeout)
        matched_builds = []
        expected_params = self._normalize_parameters(args.parameters)
        try:
            all_build_ids = job.get_build_ids()
            counter = 0
            for build_id in all_build_ids:
                if counter >= int(args.depth):
                    return ' '.join(map(str, matched_builds))
                else:
                    counter += 1
                build = job.get_build_metadata(build_id)
                if args.only_running and not build.is_running():
                    continue
                build_matches = True
                for expected_param_name, expected_param_value in expected_params.iteritems():
                    are_build_params_match = False
                    actions = build.get_actions()
                    if 'parameters' not in actions:
                        break
                    for params_list_item in actions['parameters']:
                        param_name = params_list_item['name']
                        param_value = params_list_item['value']
                        if expected_param_name == param_name and expected_param_value == param_value:
                            are_build_params_match = True
                            break
                    if not are_build_params_match:
                        build_matches = False
                        break
                if build_matches:
                    matched_builds.append(build_id)
            return ' '.join(map(str, matched_builds))
        except Exception:
            traceback.print_exc()
            return ' '.join(map(str, matched_builds))
