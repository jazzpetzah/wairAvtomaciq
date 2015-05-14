#!/usr/bin/env python

import traceback

from cli_handlers.cli_handler_base import CliHandlerBase


class KillQueuedJobItems(CliHandlerBase):
    def _build_options(self, parser):
        super(KillQueuedJobItems, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to check. Required parameter')

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        queue = self._jenkins.get_queue()
        job_items_in_queue = queue.get_queue_items_for_job(args.name)
        killed_items_count = 0
        for job_item_in_queue in job_items_in_queue:
            queue.delete_item(job_item_in_queue)
            killed_items_count += 1
        return killed_items_count