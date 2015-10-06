#!/usr/bin/env python

from datetime import datetime
import random
import sys
import time
import traceback
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
        parser.add_argument('--cause',
                            help='Jenkins job cause')
        parser.set_defaults(params=None, token=None, block=False, cause=None)

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

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job_name = self._normalize_job_name(args.name)
        MAX_TRY_COUNT = 3
        try_num = 0
        queue_item = None
        start_time = datetime.now()
        while True:
            try:
                job = self._jenkins.get_job(job_name)
                queue_item = job.invoke(securitytoken=args.token,
                           build_params=self._encoded_params_to_dict(args.params),
                           cause=args.cause)
            except Exception as e:
                traceback.print_exc()
                try_num += 1
                if try_num >= MAX_TRY_COUNT:
                    raise e
                sys.stderr.write('Sleeping a while before retry #{} of {}...\n'.format(try_num, MAX_TRY_COUNT))
                time.sleep(random.randint(10, 20))
            if args.block:
                try:
                    queue_item.block_until_complete(delay=5)
                    break
                except Exception:
                    traceback.print_exc()
                    sys.stderr.write('The script has failed to block the queued item "{}". Trying to restart...'.format(
                        queue_item
                    ))
                    try_num += 1
                    try:
                        queue_item.get_build().stop()
                    except Exception:
                       pass

            timedelta_str = self._timedelta_to_str(datetime.now() - start_time)
            return 'Jenkins job "{0}" is completed (duration: {1})'.format(args.name, timedelta_str)
        else:
            return 'Jenkins job "{0}" has been successfully invoked '\
                'on {1}'.format(args.name, datetime.now().isoformat(' '))

    @classmethod
    def is_external_jenkins_url_required(cls):
        return True

    def _is_exceptions_handled_in_invoke(self):
        """Set this to true in a subclass if you don't
        want to retry automatically on exception inside _invoke method"""
        return True