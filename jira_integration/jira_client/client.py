#!/usr/bin/env python

# pip install jira
# http://jira-python.readthedocs.org/en/latest/
# https://docs.atlassian.com/jira/REST/latest
from jira.client import GreenHopper
from jira.client import JIRA


class JiraClient(object):
    def __init__(self, url, user, password):
        self._url = url
        self._user = user
        self._password = password
        self._authenticate()

    def _authenticate(self):
        kwargs = {'options': {'server': self._url},
                  'basic_auth': (self._user, self._password)}
        self._client = JIRA(**kwargs)
        self._agile = GreenHopper(**kwargs)

    def get_boards(self):
        return self._agile.boards()

    def get_board_sprints(self, bid):
        return self._agile.sprints(bid)

    def get_projects(self):
        return self._client.projects()

    def create_version(self, project_key, name, description,
                       start_date, **kwargs):
        new_version = self._client.create_version(name=name,
                                                  project=project_key,
                                                  description=description)
        new_version.update(startDate=start_date, **kwargs)
        return new_version

    def get_project_versions(self, key):
        proj_obj = self._client.project(key)
        return self._client.project_versions(proj_obj)
