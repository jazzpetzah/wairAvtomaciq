#!/usr/bin/env python

import os
import pprint
import random
import sys
import time
import traceback

try:
    from jenkinsapi.jenkins import Jenkins
except ImportError:
    import subprocess
    subprocess.check_call(['easy_install', '--user', 'jenkinsapi'])
    from jenkinsapi.jenkins import Jenkins

from cli_handlers.cli_handler_base import get_handler, \
    get_handler_names, is_external_jenkins_url_required_by
from customized_requester import CustomRequester


if __name__ == '__main__':
    if len(sys.argv) < 2:
        raise Exception('Please provide correct type of Jenkins request'\
                    ' to execute. Available types are: {0}'.format(
                        pprint.pformat(get_handler_names())))
    keyword = sys.argv[1]
    base_url = os.getenv('INTERNAL_JENKINS_URL', None)
    if (base_url is None) or is_external_jenkins_url_required_by(keyword):
        base_url = os.getenv('JENKINS_URL', 'http://192.168.10.44:8080/')
    user = 'auto' + random.choice(('', '1', '2'))
    password = os.getenv('JENKINS_PASSWORD', 'aqa123456!')
    MAX_TRY_COUNT = 5
    try_num = 0
    jenkins = None
    while True:
        try:
            jenkins = Jenkins(base_url, user, password,
                              requester=CustomRequester(user, password, base_url))
            break
        except Exception as e:
            traceback.print_exc()
            try_num += 1
            if try_num >= MAX_TRY_COUNT:
                raise e
            time.sleep(random.randint(2, 10))
    print (get_handler(keyword, jenkins)())