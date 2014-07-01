#!/usr/bin/env python

from datetime import datetime
import logging
import optparse
import os
from pprint import pformat
import re
import sys
import traceback

from jira_client.client import JiraClient
from jira_client.config import ConfigReader


LOCAL_CONFIG_PATH = os.path.join(os.path.dirname(__file__),
                                 'etc/jira_client.config')
CONFIG_PATH = '/etc/jira_client.config'


class ProjectNotFoundError(Exception):
    pass

class SprintNotFoundError(Exception):
    pass


def prepare_options_parser():
    usage = 'usage: %prog [options] <project_key> <build_type> <build_number>'
    parser = optparse.OptionParser(usage)
    parser.add_option('-v', '--verbose', action='store_true', default=False,
                      help='Enable verbose logging', dest="verbose")
    return parser


def get_project_name(client_obj, key):
    projects = client_obj.get_projects()
    for proj in projects:
        if proj.key == key:
            return proj.name
    raise ProjectNotFoundError('{0} key is not found in Jira project list:\n{1}'.\
                               format(key, pformat(projects)))

def get_active_sprint_name(client_obj, project_name):
    boards = client_obj.get_boards()
    for board in boards:
        query = board.filter.query
        if query.find('"') > 0:
            pattern = r'project\s+=\s+"(.+)"'
        else:
            pattern = r'project\s+=\s+([^\s]+)'
        board_project = re.findall(pattern, query, re.I)
        if board_project and board_project[0] == project_name:
            sprints = client_obj.get_board_sprints(board.id)
            for sprint in sprints:
                if sprint.state == 'ACTIVE':
                    return sprint.name
    raise SprintNotFoundError('Currently there is no active sprint(s) for '\
                              'the project {0}'.format(project_name))

def read_config(path):
    cr = ConfigReader(path)
    return dict(cr.get_section('jira'))

def start():
    parser = prepare_options_parser()
    (options, args) = parser.parse_args()
    if len(args) < 3:
        parser.print_help()
        sys.exit(1)
    PROJECT_KEY = args[0]
    BUILD_TYPE = args[1]
    BUILD_NUMBER = args[2]
    if options.verbose:
        logging.getLogger().setLevel(logging.INFO)
    else:
        logging.getLogger().setLevel(logging.WARNING)

    ver_name = '{0}.{1}'.format(BUILD_TYPE, BUILD_NUMBER)
    logging.info('Adding the new version "{0}" to the project "{1}"...'.\
                            format(ver_name, PROJECT_KEY))
    if os.path.exists(CONFIG_PATH):
        config = read_config(CONFIG_PATH)
    else:
        logging.warning('There are no main config file "{0}". Will use '\
                        'default settings from "{1}" instead.'.format(CONFIG_PATH,
                                                                LOCAL_CONFIG_PATH))
        config = read_config(LOCAL_CONFIG_PATH)
    jc = JiraClient(config['url'], config['user'], config['password'])
    project_name = get_project_name(jc, PROJECT_KEY)
    try:
        active_sprint = get_active_sprint_name(jc, project_name)
        logging.info('Found active sprint "{0}"...'.format(active_sprint))
    except Exception:
        # traceback.print_exc()
        logging.warning('Active sprint name for the project "{0}" has not been found. '\
                        'Will use an empty value instead.'.format(PROJECT_KEY))
        active_sprint = None

    if active_sprint:
        ver_description = active_sprint
    else:
        ver_description = ''
    start_date = datetime.strftime(datetime.now(), '%Y-%m-%d')
    logging.info('Writing new version info...')
    jc.create_version(PROJECT_KEY, ver_name, ver_description, start_date)
    logging.info('Well done!')
    exit(0)


logging.basicConfig(format='%(asctime)s %(message)s')

if __name__ == '__main__':
    start()