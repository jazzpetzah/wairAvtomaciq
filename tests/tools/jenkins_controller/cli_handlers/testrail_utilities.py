#!/usr/bin/env python

import requests


TESTRAIL_TAG_MAGIC = 'C'


class TestrailUtilities(object):
    @staticmethod
    def _query_testrail(args, action):
        response = requests.get('{}/index.php?/api/v2/{}'.format(args.server_url, action),
                                headers={'Content-type': 'application/json',
                                         'Accept': 'application/json'},
                                auth=(args.username, args.token))
        if response.status_code != 200:
            raise RuntimeError(response.text)
        return response.json()

    @staticmethod
    def _find_run_id(plan_info, run_name, config_name=None):
        if 'entries' in plan_info:
            for entry in plan_info['entries']:
                if 'runs' in entry:
                    for run in entry['runs']:
                        if run['name'] == run_name:
                            if config_name is not None:
                                if 'config' in run:
                                    if set(map(lambda x: x.strip(), config_name.split(','))) == \
                                      set(map(lambda x: x.strip(), run['config'].split(','))):
                                        return run['id']
                                continue
                            return run['id']

    @classmethod
    def _get_test_run_id(cls, args):
        projects = filter(lambda x: x['name'] == args.project_name,
                          cls._query_testrail(args, 'get_projects'))
        if not projects:
            raise RuntimeError('There is no "{}" project in Testrail'.format(args.project_name))
        plans = filter(lambda x: x['name'] == args.plan_name,
                       cls._query_testrail(args, 'get_plans/{}'.format(projects[0]['id'])))
        if not plans:
            raise RuntimeError('There is no "{}" plan in Testrail'.format(args.plan_name))
        plan_info = cls._query_testrail(args, 'get_plan/{}'.format(plans[0]['id']))
        run_id = cls._find_run_id(plan_info, args.run_name, args.config_name)
        if not run_id:
            raise RuntimeError('There is no "{}" test run in Testrail'.format(args.run_name)
                               if args.config_name is None else
                               'There is no "{} ({})" test run in Testrail'.format(args.run_name, args.config_name))
        return run_id

    @classmethod
    def _get_run_cases(cls, args, results_filter=None):
        run_id = cls._get_test_run_id(args)
        if results_filter is None:
            return map(lambda x: x['case_id'],
                       cls._query_testrail(args, 'get_tests/{}'.format(run_id)))
        else:
            tests_info = cls._query_testrail(args,
                                             'get_tests/{}&status_id={}'.format(run_id,
                                                                                results_filter))
            return map(lambda x: x['case_id'], tests_info)

    @classmethod
    def _filter_cases_by(cls, cases_info, filter_by):
        """
        :param cases_info: list of test cases properties received from get_cases API call
        :param filter_by: this is a string, which looks like
         testrail_param_1=testrail_param_1_value,testrail_param_2=testrail_param_2_value,...
        :return: the list of matching case ids
        """
        filters = map(lambda x: x.strip(), filter_by.split(','))
        filters_dict = dict(map(lambda x: (x.split('=')[0].strip(),
                                           x.split('=')[1].strip()), filters))
        result = []
        for case_info in cases_info:
            is_case_matched = True
            for param_name, param_value in filters_dict.iteritems():
                if str(case_info[param_name]) != param_value:
                    is_case_matched = False
                    break
            if is_case_matched is True:
                result.append(case_info['id'])
        return result

    @classmethod
    def _filter_cases(cls, args, filter_by=None):
        projects = filter(lambda x: x['name'] == args.project_name,
                          cls._query_testrail(args, 'get_projects'))
        if not projects:
            raise RuntimeError('There is no "{}" project in Testrail'.format(args.project_name))
        suites = filter(lambda x: x['name'] == args.suite_name,
                        cls._query_testrail(args, 'get_suites/{}'.format(projects[0]['id'])))
        if not suites:
            raise RuntimeError('There is no "{}" suite in Testrail'.format(args.suite_name))
        cases_info = cls._query_testrail(args, 'get_cases/{}&suite_id={}'.
                                         format(projects[0]['id'], suites[0]['id']))
        if filter_by is None:
            return map(lambda x: x['id'], cases_info)
        else:
            return cls._filter_cases_by(cases_info, filter_by)
