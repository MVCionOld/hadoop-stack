#!/usr/bin/env bash

set -o nounset
set -o errexit
set -o pipefail

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
conf_dir="$base_dir/conf"

spark_conf_file="$conf_dir/spark.conf"
log_conf_file_name="log4j.xml"
log_conf_file="$conf_dir/$log_conf_file_name"

# Cloudera distribution by default sets SPARK_KAFKA_VERSION to 0.9
# This leads to incorrect Kafka libs in classpath.
export SPARK_KAFKA_VERSION='0.10'

spark2-submit \
  -v \
  --properties-file "$spark_conf_file" \
  --master yarn \
  --deploy-mode cluster \
  --files "$log_conf_file" \
  --driver-java-options "-Dlog4j.configuration=$log_conf_file_name" \
  --conf spark.executor.extraJavaOptions="-Dlog4j.configuration=$log_conf_file_name" \
  --name Dummy \
  --class org.fivt.atp.bigdata.dummy.DummyMain \
  "$lib_dir/spark-project-template-apps-@{project.version}.jar"
