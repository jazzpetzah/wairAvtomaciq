#!/usr/bin/env python

import codecs
import os

# This requires pip install gherkin-parser
from gherkin_parser import parse_from_filename


class GherkinUtilities(object):
    @staticmethod
    def _gherkin_to_dict(path):
        return parse_from_filename(path)

    @classmethod
    def _scan_tags_occurrences(cls, path, expected_tags):
        """
        :param path: path to a .feature file to be scanned
        :param expected_tags: the list of tags to match. These should not contain starting '@' character
        :return: the list of line numbers (first line number is zero)
        """
        result = []
        as_dict = cls._gherkin_to_dict(path)
        if 'scenarios' in as_dict and as_dict['scenarios']:
            for scenario in as_dict['scenarios']:
                if 'tags' in scenario and scenario['tags'] and 'content' in scenario['tags']:
                    if set(expected_tags) & set(scenario['tags']['content']):
                        result.append(scenario['tags']['index'])
        return result

    @classmethod
    def _update_feature_files(cls, root_path, case_ids, tags_to_add):
        if isinstance(tags_to_add, basestring):
            tags_to_add = [tags_to_add]
        changed_features = []
        for root, dirs, files in os.walk(root_path):
            for fname in files:
                if fname.endswith('.feature'):
                    occurrences = cls._scan_tags_occurrences(os.path.join(root, fname), case_ids)
                    if not occurrences:
                        continue
                    with codecs.open(os.path.join(root, fname), 'r', 'utf-8') as f:
                        content = f.read()
                    result = []
                    for idx, line in enumerate(content.splitlines()):
                        if idx in occurrences:
                            # keep spacing
                            leading_spaces_count = len(line) - len(line.lstrip(' '))
                            result.append((leading_spaces_count * ' ') +
                                          ' '.join(set(line.split() + tags_to_add)))
                        else:
                            result.append(line)
                    with codecs.open(os.path.join(root, fname), 'w', 'utf-8') as f:
                        f.write('\n'.join(result))
                    changed_features.append(os.path.join(root, fname))
        return changed_features
