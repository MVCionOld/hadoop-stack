package org.fivt.atp.bigdata

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object HadoopKISTask301 {

  def main(args: Array[String]): Unit = {

    val fileName = "/data/user_events"
    val diffTimeRegex = """diff_time:(\d+)""".r
    val domainRegex = """(?:https?:\/\/)?(?:[^@\/\n]+@)?(?:www\.)?([^:\/\n]+).*""".r

    val ss = SparkSession.builder()
      .master("yarn")
      .appName("HadoopKISTask301Tsion")
      .getOrCreate
      val sc = ss.sparkContext

    val rdd: org.apache.spark.rdd.RDD[(String, String)] = sc
      .textFile(fileName)
      .map(_.split("\t"))
      .collect{
        case Array(_, _, domainRegex(domain), diffTimeRegex(diffTime)) => (domain, diffTime)
      }

    val df:org.apache.spark.sql.DataFrame = ss.createDataFrame(rdd).toDF(Seq("domain", "diffTime"):_*)

    df.select(
        col("domain"),
        col("diffTime") cast("Int") as "diffTime"
      )
      .groupBy(col("domain"))
      .agg(
        callUDF(
          "percentile_approx",
          col("diffTime"),
          lit(.5)
        ) as "median",
        callUDF(
          "percentile_approx",
          col("diffTime"),
          lit(.75)
        ) as "quartile"
      )
      .orderBy(desc("median"))
      .take(10)
      .foreach((row: org.apache.spark.sql.Row)
        => println(row.mkString("\t"))
      )

  }
}


