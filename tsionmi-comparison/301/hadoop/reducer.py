#!/usr/bin/python3

import sys


def compute_percentiles(entries_, levels):
    sorted_entries, size = sorted(entries_), len(entries_)
    percentiles = []
    for level in levels:
        pivot = int(level * (size - 1))
        if pivot + 1 < level * size:
            percentiles.append(sorted_entries[pivot + 1])
        elif pivot + 1 == level * size:
            percentiles.append((sorted_entries[pivot + 1] + sorted_entries[pivot]) / 2)
        else:
            percentiles.append(sorted_entries[pivot])
    return percentiles


prev_url, url = None, None
entries = []

for line in sys.stdin:
    try:
        url, time_on_page = line.split("\t")
        time_on_page = int(time_on_page)
    except Exception as e:
        continue
    if prev_url is None:
        prev_url = url
        entries = [time_on_page]
    elif prev_url != url:
        median, third_quartile = compute_percentiles(entries, [0.5, 0.75])
        print("{0}\t{1}\t{2}".format(url, int(median), int(third_quartile)), file=sys.stdout)
        prev_url, entries = url, [time_on_page]
    else:
        entries.append(time_on_page)

if entries:
    median, third_quartile = compute_percentiles(entries, [0.5, 0.75])
    print("{0}\t{1}\t{2}".format(url, int(median), int(third_quartile)), file=sys.stdout)
