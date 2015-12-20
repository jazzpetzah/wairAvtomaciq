#!/usr/bin/env python

import argparse
import imp
import pprint
import random
import re
import sys
import time
import traceback
import os


class CliHandlerBase(object):
    def __init__(self, jenkins):
        self._jenkins = jenkins

    def _build_options(self, parser):
        parser.add_argument('request_type',
                            help='Jenkins request type. Available types: {0}'.
                            format(pprint.pformat(get_handler_names())))

    def _wait_while_job_in_queue(self, job, timeout):
        timeout = int(timeout)
        if timeout < 0:
            return
        current_timestamp = time.time()
        while time.time() - current_timestamp < timeout:
            try:
                if job.is_queued():
                    time.sleep(7)
                else:
                    return
            except Exception:
                traceback.print_exc()
                time.sleep(7)
        raise TimeoutError('The job is still in the queue after {0} seconds timeout'.format(timeout))

    def _get_parser(self):
        parser = argparse.ArgumentParser()
        self._build_options(parser)
        return parser

    def _normalize_job_name(self, raw_name):
        at_pos = raw_name.find('@')
        if at_pos >= 0:
            return raw_name[:at_pos]
        else:
            return raw_name

    def _invoke(self):
        raise NotImplementedError('Should be implemented in a subclass')

    def __call__(self):
        MAX_TRY_COUNT = 5
        try_num = 0
        while True:
            try:
                return self._invoke()
            except Exception as e:
                if self._is_exceptions_handled_in_invoke():
                    raise e
                traceback.print_exc()
                try_num += 1
                if try_num >= MAX_TRY_COUNT:
                    raise e
                sys.stderr.write('Sleeping a while before retry #{} of {}...\n'.format(try_num, MAX_TRY_COUNT))
                time.sleep(random.randint(2, 10))

    @classmethod
    def is_external_jenkins_url_required(cls):
        """Set this ti true in subclasses in case if
        jenkinsapi calls make job invocation"""
        return False

    def _is_exceptions_handled_in_invoke(self):
        """Set this to true in a subclass if you don't
        want to retry automatically on exception inside _invoke method"""
        return False


class CliHandlerNotFoundError(Exception):
    pass


class TimeoutError(Exception):
    pass


_CLASS_CACHE = {}


def _get_keyword_class(keyword):
    if keyword in _CLASS_CACHE:
        return _CLASS_CACHE[keyword]
    current_module_path = os.path.dirname(__file__)
    for module_name in os.listdir(current_module_path):
        if not module_name.endswith('.py'):
            continue
        module_path = os.path.join(current_module_path, module_name)
        if os.path.isfile(module_path):
            with open(module_path, 'r') as f:
                if f.read().find('class {0}({1}'.format(keyword, CliHandlerBase.__name__)) >= 0:
                    mod_name, _ = os.path.splitext(os.path.split(module_path)[-1])
                    module_obj = imp.load_source(mod_name, module_path)
                    _CLASS_CACHE[keyword] = getattr(module_obj, keyword)
                    return _CLASS_CACHE[keyword]
    raise CliHandlerNotFoundError('Cannot find appropriate CLI handler for {0} command'.
                                  format(keyword))


def get_handler(keyword, jenkins):
    return _get_keyword_class(keyword)(jenkins)


def is_external_jenkins_url_required_by(keyword):
    keyword_class = _get_keyword_class(keyword)
    return keyword_class.is_external_jenkins_url_required()


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
