#!/usr/bin/env bash

bin_dir=
if echo "$0" | grep -q /
then
  # Started with absolute or relative path
  bin_dir="$(cd "$(dirname -- "$0")" ; pwd)"
else
  # Started from PATH
  bin_dir="$(cd "$(dirname -- "$(command -v "$0")")" ; pwd)"
fi
base_dir="$(dirname -- "$bin_dir")"
lib_dir="$base_dir/lib"

spark2-submit \
  --master yarn \
  --num-executors 4 \
  --name HadoopKISTask110 \
  --class org.fivt.atp.bigdata.HadoopKISTask110 \
  "$lib_dir/spark-project-template-apps-@{project.version}.jar" \
  2>/dev/null
