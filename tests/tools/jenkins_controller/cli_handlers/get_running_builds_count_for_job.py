#!/usr/bin/env python

import traceback

from cli_handlers.cli_handler_base import CliHandlerBase


class GetRunningBuildsCountForJob(CliHandlerBase):
    def _build_options(self, parser):
        super(GetRunningBuildsCountForJob, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to check. Required parameter')
        parser.add_argument('--queue_timeout',
                            help='Maximum time to wait while this job has queued items (in seconds, 300 by default). '\
                            'Set it to negative value if you want to ignore this verification.')
        parser.add_argument('--depth',
                            help='How many recent builds should we check for the particular job')
        parser.set_defaults(depth=50, queue_timeout=300)

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job = self._jenkins.get_job(args.name)
        self._wait_while_job_in_queue(job, args.queue_timeout)
        running_builds_count = 0
        try:
            all_build_ids = job.get_build_ids()
            counter = 0
            for build_id in all_build_ids:
                if counter >= int(args.depth):
                    return running_builds_count
                else:
                    counter += 1
                build = job.get_build_metadata(build_id)
                if build.is_running():
                    running_builds_count += 1
            return running_builds_count
        except Exception:
            traceback.print_exc()
            return running_builds_count
