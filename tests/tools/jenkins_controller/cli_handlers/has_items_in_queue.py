#!/usr/bin/env python

from cli_handlers.cli_handler_base import CliHandlerBase


class HasItemsInQueue(CliHandlerBase):
    def _build_options(self, parser):
        super(HasItemsInQueue, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to check. Required parameter')

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        queue = self._jenkins.get_queue()
        return bool(list(queue.get_queue_items_for_job(args.name)))