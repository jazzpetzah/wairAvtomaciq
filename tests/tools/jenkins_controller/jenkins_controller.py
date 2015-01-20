#!/usr/bin/env python

import os
import pprint
import sys

try:
    from jenkinsapi.jenkins import Jenkins
except ImportError:
    import subprocess
    subprocess.check_call(['sudo', 'easy_install', 'jenkinsapi'])
    from jenkinsapi.jenkins import Jenkins

from cli_handlers.cli_handler_base import get_handler, get_handler_names


if __name__ == '__main__':
    jenkins = Jenkins(os.getenv('JENKINS_URL', 'http://192.168.10.44:8080'),
                      os.getenv('JENKINS_USER', 'root'),
                      os.getenv('JENKINS_PASSWORD', 'aqa123456'))
    if len(sys.argv) < 2:
        raise Exception('Please provide correct type of Jenkins request'\
                        ' to execute. Available types are: {0}'.format(
                            pprint.pformat(get_handler_names())))
    print (get_handler(sys.argv[1], jenkins)())