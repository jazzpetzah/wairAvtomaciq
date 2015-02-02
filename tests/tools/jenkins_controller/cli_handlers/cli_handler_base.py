#!/usr/bin/env python

import argparse
import imp
import pprint
import re
import time
import os

import socket
socket.setdefaulttimeout(300)


class CliHandlerBase(object):
    def __init__(self, jenkins):
        self._jenkins = jenkins

    def _build_options(self, parser):
        parser.add_argument('request_type',
                             help='Jenkins request type. Available types: {0}'.\
                format(pprint.pformat(get_handler_names())))
    
    def _wait_while_job_in_queue(self, job, timeout):
        timeout = int(timeout)
        if timeout < 0:
            return
        current_timestamp = time.time()
        MAX_WAIT = timeout
        while job.is_queued() and time.time() - current_timestamp < MAX_WAIT:
            time.sleep(5)
        if job.is_queued():
            raise TimeoutError('The job is still in the queue after {0} seconds timeout'.format(MAX_WAIT))

    def _get_parser(self):
        parser = argparse.ArgumentParser()
        self._build_options(parser)
        return parser


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
