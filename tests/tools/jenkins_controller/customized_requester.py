#!usr/bin/env python

import requests
from jenkinsapi.jenkins import Requester

# import logging

# # these two lines enable debugging at httplib level (requests->urllib3->httplib)
# # you will see the REQUEST, including HEADERS and DATA, and RESPONSE with HEADERS but without DATA.
# # the only thing missing will be the response.body which is not logged.
# import httplib
# httplib.HTTPConnection.debuglevel = 1

# logging.basicConfig() # you need to initialize logging, otherwise you will not see anything from requests
# logging.getLogger().setLevel(logging.DEBUG)
# requests_log = logging.getLogger("requests.packages.urllib3")
# requests_log.setLevel(logging.DEBUG)
# requests_log.propagate = True


REQUEST_TIMEOUT_SECONDS = 15


class CustomRequester(Requester):
    def get_url(self, url, params=None, headers=None, allow_redirects=True):
        requestKwargs = self.get_request_dict(params=params, headers=headers, allow_redirects=allow_redirects)
        return requests.get(self._update_url_scheme(url), timeout=REQUEST_TIMEOUT_SECONDS, **requestKwargs)

    def post_url(self, url, params=None, data=None, files=None, headers=None, allow_redirects=True):
        requestKwargs = self.get_request_dict(params=params, data=data, files=files, headers=headers, allow_redirects=allow_redirects)
        return requests.post(self._update_url_scheme(url), timeout=REQUEST_TIMEOUT_SECONDS, **requestKwargs)


