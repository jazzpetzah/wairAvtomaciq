#!/usr/bin/env python

import codecs
import json
import os

from cli_handlers.cli_handler_base import CliHandlerBase


class GetReportDetails(CliHandlerBase):
    def _build_options(self, parser):
        super(GetReportDetails, self)._build_options(parser)
        parser.add_argument('--report_path', required=True,
                            help='The path to json report. Required parameter')

    @staticmethod
    def _get_step_status(step):
        return step['result']['status'].strip().lower()

    @staticmethod
    def _is_given_step_type(step):
        return 'keyword' in step and step['keyword'].strip().lower() == 'given'

    @staticmethod
    def _is_test_passed(steps):
        is_pending = False
        for step in steps:
            step_status = GetReportDetails._get_step_status(step)
            if step_status == 'pending':
                # Parse test cases with pending steps as passed by default
                is_pending = True
            elif step_status != 'passed' and not is_pending:
                return False
            elif step_status == 'failed':
                if not GetReportDetails._is_given_step_type(step):
                    return False
        return True

    @staticmethod
    def _is_test_failed(steps):
        for step in steps:
            if GetReportDetails._get_step_status(step) == 'failed':
                if not GetReportDetails._is_given_step_type(step):
                    return True
        return False

    @staticmethod
    def _parse_test_status(steps):
        if GetReportDetails._is_test_passed(steps):
            return 'passed'
        elif GetReportDetails._is_test_failed(steps):
            return 'failed'
        else:
            return 'skipped'

    @staticmethod
    def _extract_details(report_path):
        result = {'passed': 0,
                  'failed': 0,
                  'skipped': 0,
                  'all': 0}
        if not os.path.exists(report_path):
            return
        with codecs.open(report_path, 'r', 'utf-8', errors='ignore') as fp:
            report_dict = json.load(fp)
        if 'elements' in report_dict and report_dict['elements']:
            for element in report_dict['elements']:
                if 'steps' in element and element['steps']:
                    tc_result = GetReportDetails._parse_test_status(element['steps'])
                    result[tc_result] += 1
                else:
                    result['skipped'] += 1
                result['all'] += 1
        return result

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        report_details = self._extract_details(args.report_path)
        if report_details:
            return '{passed} tests passed out of {all}\n'\
                   '{failed} tests failed out of {all}\n'\
                   '{skipped} tests were skipped out of {all}\n'.format(**report_details)
        else:
            return 'The report "{}" does not exist'.format(args.report_path)

    def _is_exceptions_handled_in_invoke(self):
        return True
