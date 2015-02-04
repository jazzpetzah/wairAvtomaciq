#!/usr/bin/env python

import traceback

from cli_handlers.cli_handler_base import CliHandlerBase


class IsAnyNodeWithLabelsRunningJob(CliHandlerBase):
    def _build_options(self, parser):
        super(IsAnyNodeWithLabelsRunningJob, self)._build_options(parser)
        parser.add_argument('--labels-param-name', required=True,
                            help='Name of job parameter containing node labels')
        parser.add_argument('--labels-param-value', required=True,
                            help='List of comma-separated node labels to match')
        parser.add_argument('--name', required=True,
                            help='Name of the job to check')
        parser.add_argument('--queue_timeout',
                            help='Maximum time to wait while this job has queued items (in seconds, 300 by default). '\
                            'Set it to negative value if you want to ignore this verification.')
        parser.add_argument('--depth',
                            help='How many recent builds should we check for the particular job')
        parser.set_defaults(depth=50, queue_timeout=300)

    def _normalize_labels(self, labels_list):
        normalized_labels = map(lambda x: x.strip(), labels_list)
        return set(normalized_labels)

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job = self._jenkins.get_job(args.name)
        self._wait_while_job_in_queue(job, args.queue_timeout)
        try:
            all_build_ids = job.get_build_ids()
            counter = 0
            for build_id in all_build_ids:
                if counter >= int(args.depth):
                    return False
                else:
                    counter += 1
                build = job.get_build_metadata(build_id)
                if build.is_running():
                    params = build.get_actions().get('parameters', [])
                    dest_param = filter(lambda x: x['name'] == args.labels_param_name, params)
                    if dest_param:
                        actual_param_value = dest_param[0]['value']
                        if self._normalize_labels(args.labels_param_value.split(',')).\
                           issubset(self._normalize_labels(actual_param_value.split('&&'))):
                            return True
            return False
        except Exception:
            traceback.print_exc()
            return True
