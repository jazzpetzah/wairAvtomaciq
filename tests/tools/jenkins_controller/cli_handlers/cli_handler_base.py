#!/usr/bin/env python

import argparse
import imp
import pprint
import random
import re
import requests
import time
import os


class CliHandlerBase(object):
    def __init__(self, jenkins):
        self._jenkins = jenkins

    def _build_options(self, parser):
        parser.add_argument('request_type',
                             help='Jenkins request type. Available types: {0}'.\
                format(pprint.pformat(get_handler_names())))

    def _wait_while_job_in_queue(self, job, interval_seconds):
        timeout = int(interval_seconds)
        if timeout < 0:
            return
        if job.is_queued():
            time.sleep(interval_seconds)
        if job.is_queued():
            raise TimeoutError('The job is still in the queue after {0} seconds'.\
                    format(interval_seconds))

    def _get_parser(self):
        parser = argparse.ArgumentParser()
        self._build_options(parser)
        return parser
    
    def _invoke(self):
        raise NotImplementedError('Should be implemented in a subclass')

    def __call__(self):
        MAX_TRY_COUNT = 5
        try_num = 0
        while True:
            try:
                return self._invoke()
            except requests.exceptions.ConnectionError as e:
                try_num += 1
                if try_num >= MAX_TRY_COUNT:
                    raise e
                time.sleep(random.randint(2, 10))


class CliHandlerNotFoundError(Exception):
    pass


class TimeoutError(Exception):
    pass


def get_handler(keyword, jenkins):
    current_module_path = os.path.dirname(__file__)
    for module_name in os.listdir(current_module_path):
        if not module_name.endswith('.py'):
            continue
        module_path = os.path.join(current_module_path, module_name)
        if os.path.isfile(module_path):
            with open(module_path, 'r') as f:
                if f.read().find('class {0}({1})'.format(keyword,
                                                         CliHandlerBase.__name__)) >= 0:
                    mod_name, _ = os.path.splitext(os.path.split(module_path)[-1])
                    module_obj = imp.load_source(mod_name, module_path)
                    return getattr(module_obj, keyword)(jenkins)
    raise CliHandlerNotFoundError('Cannot find appropriate CLI handler for {0} command'.\
                                  format(keyword))


def get_handler_names():
    current_module_path = os.path.dirname(__file__)
    result_names = []
    for module_name in os.listdir(current_module_path):
        if not module_name.endswith('.py'):
            continue
        module_path = os.path.join(current_module_path, module_name)
        if os.path.isfile(module_path):
            with open(module_path, 'r') as f:
                handler_names = re.findall(r'class (\w+)\(' + CliHandlerBase.__name__ + '\)',
                                           f.read())
                if handler_names:
                    result_names.append(handler_names[0])
    return result_names
