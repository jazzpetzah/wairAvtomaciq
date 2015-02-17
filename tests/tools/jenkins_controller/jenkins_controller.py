#!/usr/bin/env python

import os
import pprint
import random
import requests
import sys
import time

try:
    from jenkinsapi.jenkins import Jenkins
except ImportError:
    import subprocess
    subprocess.check_call(['sudo', 'easy_install', 'jenkinsapi'])
    from jenkinsapi.jenkins import Jenkins

from cli_handlers.cli_handler_base import get_handler, get_handler_names


if __name__ == '__main__':
    MAX_TRY_COUNT = 5
    try_num = 0
    while True:
        try:
            jenkins = Jenkins(
                  os.getenv('JENKINS_URL', 'http://192.168.10.44:8080'),
                  os.getenv('JENKINS_USER', 'root'),
                  os.getenv('JENKINS_PASSWORD', 'aqa123456'))
            break
        except requests.exceptions.ConnectionError as e:
            try_num += 1
            if try_num >= MAX_TRY_COUNT:
                raise e
            time.sleep(random.randint(2, 10))
    if len(sys.argv) < 2:
        raise Exception('Please provide correct type of Jenkins request'\
                        ' to execute. Available types are: {0}'.format(
                            pprint.pformat(get_handler_names())))
    print (get_handler(sys.argv[1], jenkins)())