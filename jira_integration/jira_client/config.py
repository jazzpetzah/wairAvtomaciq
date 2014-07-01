#!/usr/bin/env python

from ConfigParser import ConfigParser

class ConfigReader(object):
    def __init__(self, path):
        self._config = ConfigParser()
        self._config.read(path)

    def get_value(self, section, option):
        return self._config.get(section, option)

    def get_section(self, section):
        return self._config.items(section)