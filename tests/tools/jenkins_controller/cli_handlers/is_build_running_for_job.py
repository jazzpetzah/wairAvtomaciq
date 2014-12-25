#!/usr/bin/env python

import sys

from cli_handlers.cli_handler_base import CliHandlerBase


class IsBuildRunningForJob(CliHandlerBase):
    def _build_options(self, parser):
        super(IsBuildRunningForJob, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to check. Required parameter')
        parser.add_argument('--build', required=True,
                            help='Build number to check. Required parameter')

    def __call__(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job = self._jenkins.get_job(args.name)
        try:
            build = job.get_build(int(args.build))
            return build.is_running()
        except Exception:
            sys.stderr.write(sys.exc_info())
            return False
