#!/usr/bin/env python

from setuptools import setup, find_packages

PACKAGE_NAME = 'jira_client'
RELATIVE_CONFIG_PATH = 'etc/jira_client.config'

setup(
    name = PACKAGE_NAME,
    version = '0.1',
    description = 'Jira version updater',
    author = 'Mykola Mokhnach',
    author_email = 'mykola.mokhnach@wearezeta.com',
    url = 'http://www.wearezeta.com',
    packages = find_packages(),
    install_requires = ['jira-python>=0.16'],
    zip_safe = False,
    package_data = {'': [RELATIVE_CONFIG_PATH]},
    entry_points = {'console_scripts': ['add-jira-version = jira_client.run:start']},
)
