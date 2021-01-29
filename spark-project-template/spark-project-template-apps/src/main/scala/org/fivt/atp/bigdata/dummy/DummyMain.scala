package org.fivt.atp.bigdata.dummy

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

object DummyMain {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .getOrCreate()
    import spark.implicits._

    val data = spark.createDataset(0 until 1000000)
      .toDF("value")
    println("Data sample:")
    data.show(false)

    val count = data.count()
    println(s"The data has $count values in total")

    val modulo2Data = data.withColumn("modulo2", col("value") % 2)
      .groupBy("modulo2")
      .count()
    println("Even/odd numbers:")
    modulo2Data.show(false)
  }

}
