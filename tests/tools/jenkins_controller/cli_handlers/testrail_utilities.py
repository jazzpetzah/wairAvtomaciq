#!/usr/bin/env python

import requests


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
    def _find_run_id(plan_info, run_name, config_name):
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
    def _get_run_cases(cls, args):
        run_id = cls._get_test_run_id(args)
        if args.filter_by_result is None:
            case_ids = map(lambda x: x['case_id'],
                           cls._query_testrail(args, 'get_tests/{}'.format(run_id)))
            return ','.join(map(str, case_ids))
        else:
            results_info = cls._query_testrail(args,
                                               'get_results_for_run/{}&status_id={}'.format(run_id,
                                                                                            args.filter_by_result))
            result = []
            for result_info in results_info:
                test_id = result_info['test_id']
                case_id = cls._query_testrail(args, 'get_test/{}'.format(test_id))['case_id']
                result.append(case_id)
            return ','.join(map(str, result))

    @classmethod
    def _filter_cases_by(cls, cases_info, filter_by):
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
    def _filter_cases(cls, args):
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
        if args.filter_by is None:
            return ','.join(map(lambda x: str(x['id']), cases_info))
        else:
            return ','.join(map(str, cls._filter_cases_by(cases_info, args.filter_by)))
