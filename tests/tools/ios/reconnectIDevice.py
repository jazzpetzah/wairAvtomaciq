#!/usr/bin/env python

import os
import paramiko
import socket
import time

NODE_USER = os.getenv("NODE_USER", 'jenkins')
NODE_PASSWORD = os.getenv('NODE_PASSWORD', '123456')
MAX_NODES_PER_HOST = 5
OPERATION_TIMEOUT = 40

APPLE_SCRIPT = """tell application "System Events" to tell application process "Parallels Desktop"
                    set frontmost to false
                    set frontmost to true
                end tell
                delay 1
                tell application "System Events"
                    tell process "Parallels Desktop"
                        tell menu bar item "Window" of menu bar 1
                            click
                            click (menu item 1 where its name contains "Real") of menu 1
                        end tell
                        delay 1
                        repeat 2 times
                            tell menu bar item "Devices" of menu bar 1
                                click
                                click menu item "External Devices" of menu 1
                                click (menu item 1 where its name starts with "Apple iPhone") of menu 1 of menu item "External Devices" of menu 1
                            end tell
                            delay 10
                        end repeat
                    end tell
                end tell"""


def format_apple_script_as_command():
    formatted_script = ['/usr/bin/osascript']
    for line in APPLE_SCRIPT.splitlines():
        formatted_script.append('    -e "' + line.replace('"', '\\"') + '"')
    return ' \\\n'.join(formatted_script)


def calc_vm_master_host_ip():
    current_ip = socket.gethostbyname(socket.gethostname())
    ip_as_list = current_ip.split('.')
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
        client.exec_command(format_apple_script_as_command())
        time.sleep(OPERATION_TIMEOUT)
    finally:
        client.close()
