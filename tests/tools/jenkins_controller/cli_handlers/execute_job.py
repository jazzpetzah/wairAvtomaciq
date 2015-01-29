#!/usr/bin/env python

from datetime import datetime
import urllib

from cli_handlers.cli_handler_base import CliHandlerBase


class ExecuteJob(CliHandlerBase):
    def _build_options(self, parser):
        super(ExecuteJob, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to trigger. Required parameter')
        parser.add_argument('--params',
                            help='Urlencoded list of job parameters separated by "&" character')
        parser.add_argument('--token',
                            help='Special security token, which allows to run the job remotely')
        parser.add_argument('--block', action='store_true',
                            help='If exists then this job will block its caller until finished')
        parser.add_argument('--invoke-queue-delay',
                            help='Timer delay for job invocation detection (default is 5 seconds)')
        parser.add_argument('--cause',
                            help='Jenkins job cause')
        parser.set_defaults(params=None, token=None, block=False, cause=None,
                            invoke_queue_delay=5)

    def _encoded_params_to_dict(self, encoded_params):
        result_dict = {}
        pairs = encoded_params.split('&')
        for pair in pairs:
            key, value = tuple(pair.split('='))
            key = urllib.unquote(key).decode('utf8')
            value = urllib.unquote(value).decode('utf8')
            result_dict[key] = value
        return result_dict

    def _timedelta_to_str(self, timedelta):
        s = timedelta.seconds
        hours, remainder = divmod(s, 3600)
        minutes, seconds = divmod(remainder, 60)
        return '{0:02d}:{1:02d}:{2:02d}'.format(hours, minutes, seconds)

    def __call__(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job = self._jenkins.get_job(args.name)
        start_time = datetime.now()
        job.invoke(securitytoken=args.token,
                   block=args.block,
                   invoke_block_delay=int(args.invoke_queue_delay),
                   build_params=self._encoded_params_to_dict(args.params),
                   cause=args.cause)
        if args.block:
            timedelta_str = self._timedelta_to_str(datetime.now() - start_time)
            return 'Jenkins job "{0}" is '\
                'completed (duration: {1})'.format(args.name,
                                                   timedelta_str)
        else:
            return 'Jenkins job "{0}" has been successfully invoked '\
                'on {1}'.format(args.name,
                                datetime.now().isoformat(' '))
