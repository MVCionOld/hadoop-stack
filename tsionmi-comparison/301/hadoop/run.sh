#!/usr/bin/env bash

OUT_DIR="kis-hadoop-301-mtsion-out"
NUM_REDUCERS=16

hadoop fs -rm -r -skipTrash ${OUT_DIR} >/dev/null 2>/dev/null
hadoop fs -rm -r -skipTrash ${OUT_DIR}_tmp >/dev/null 2>/dev/null

yarn jar /opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/hadoop-streaming.jar \
    -D mapreduce.job.name="kis-hadoop-301-mtsion-job-1" \
    -D mapreduce.job.reduces=${NUM_REDUCERS} \
    -files /home/hadoop2020/had2020007/mapper.py,/home/hadoop2020/had2020007/reducer.py \
    -mapper mapper.py \
    -reducer reducer.py \
    -input /data/user_events \
    -output ${OUT_DIR}_tmp

yarn jar /opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/hadoop-streaming.jar \
    -D mapreduce.job.name="kis-hadoop-301-mtsion-job-2" \
    -D mapreduce.job.reduces=1 \
    -D stream.num.map.output.key.fields=2 \
    -D stream.map.output.field.separator=\t \
    -D mapreduce.partition.keycomparator.options="-k2nr" \
    -D mapred.output.key.comparator.class=org.apache.hadoop.mapred.lib.KeyFieldBasedComparator \
    -input ${OUT_DIR}_tmp \
    -output ${OUT_DIR}\
    -mapper org.apache.hadoop.mapred.lib.IdentityMapper \
    -reducer org.apache.hadoop.mapred.lib.IdentityReducer

hdfs dfs -cat ${OUT_DIR}/part-00000 2>/dev/null | head -n 10

hadoop fs -rm -r -skipTrash ${OUT_DIR} >/dev/null 2>/dev/null
hadoop fs -rm -r -skipTrash ${OUT_DIR}_tmp >/dev/null 2>/dev/null