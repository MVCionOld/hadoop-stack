USE tsion_hadoop_hw1;

ADD JAR /opt/cloudera/parcels/CDH/lib/hive/lib/hive-contrib.jar;

DROP TABLE IF EXISTS 
    user_events;

CREATE EXTERNAL TABLE user_events(
    user_id STRING
    ,tmstamp STRING
    ,url STRING
    ,time_on_page INT
)
ROW FORMAT
    SERDE 'org.apache.hadoop.hive.serde2.RegexSerDe'
WITH SERDEPROPERTIES (
    "input.regex"='^(-?\\d+)\\t(\\d+)\\t(?:https?:\/\/)?(?:[^@\/\n]+@)?(?:www\.)?([^:\/\n]+).*\\tdiff_time:(\\d+)'
)
STORED AS
    TEXTFILE
LOCATION
    '/data/user_events';

SELECT
    url
    ,percentile(cast(time_on_page as BIGINT), 0.5) AS median
    ,percentile(cast(time_on_page as BIGINT), 0.75) AS quartile
FROM
    user_events
GROUP BY
    url
ORDER BY
    median
    DESC
LIMIT
    10;
