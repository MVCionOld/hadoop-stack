#!/usr/bin/python3

import re
import sys

pattern_regex = re.compile(
    pattern="^(-?\\d+)\\t(\\d+)\\t(?:https?:\/\/)?(?:[^@\/\n]+@)?(?:www\.)?([^:\/\n]+).*\\tdiff_time:(\\d+)"
)

for line in sys.stdin:
    match = re.match(pattern_regex, line.strip())
    if match is None:
        continue
    try:
        *_, url, time_on_page = match.groups()
    except Exception as e:
        continue
    print("%s\t%s" % (url, time_on_page), file=sys.stdout)
