#!/usr/bin/env python

import os
import paramiko
import socket
import time
import tempfile

NODE_USER = os.getenv("NODE_USER", 'jenkins')
NODE_PASSWORD = os.getenv('NODE_PASSWORD', '123456')
MAX_NODES_PER_HOST = 5
CURRENT_HOST_IP = socket.gethostbyname(socket.gethostname())

POWER_CYCLE_SCRIPT = """#!/usr/bin/env python

import time

from brainstem import discover
from brainstem.link import Spec
from brainstem.stem import USBHub2x4


if __name__ == '__main__':
    stem = USBHub2x4()
    spec = discover.find_first_module(Spec.USB)
    if spec is None:
        raise RuntimeError("No USBHub is connected!")
    stem.connect_from_spec(spec)
    for portnum in xrange(4):
        stem.usb.setPowerDisable(portnum)
    time.sleep(1)
    for portnum in xrange(4):
        stem.usb.setPowerEnable(portnum)
    time.sleep(1)
"""


def calc_vm_master_host_ip():
    ip_as_list = CURRENT_HOST_IP.split('.')
    # Must be IPv4
    assert len(ip_as_list) == 4
    last_digit = int(ip_as_list[-1])
    return '.'.join(ip_as_list[:3] + [str(last_digit - (last_digit % MAX_NODES_PER_HOST))])


if __name__ == '__main__':
    client = paramiko.SSHClient()
    try:
        client.load_system_host_keys()
        client.set_missing_host_key_policy(paramiko.WarningPolicy())
        client.connect(calc_vm_master_host_ip(), username=NODE_USER, password=NODE_PASSWORD)
        sftp = client.open_sftp()
        _, localpath = tempfile.mkstemp(suffix='.py')
        try:
            with open(localpath, 'w') as f:
                f.write(POWER_CYCLE_SCRIPT)
            remotepath = '/tmp/' + os.path.basename(localpath)
            sftp.put(localpath, remotepath)
            sftp.close()
        finally:
            os.unlink(localpath)
        stdin, stdout, sterr = client.exec_command('python "{}"'.format(remotepath))
        stdout.channel.recv_exit_status()
        client.exec_command('rm -f "{}"'.format(remotepath))
    finally:
        client.close()
