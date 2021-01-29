package org.fivt.atp.bigdata

import org.apache.spark.sql.SparkSession

import scala.math.abs
import scala.util.Random

object HadoopKISTask110 {

  def main(args: Array[String]): Unit = {

    val fileName: String = "/data/ids"
    val batchSize: Integer = 5

    SparkSession.builder()
      .appName("HadoopKISTask110Tsion")
      .master("yarn")
      .getOrCreate()
      .sparkContext
      .textFile(fileName)
      .map(id => (Random.nextDouble, id.trim))
      .sortBy(_._1)
      .map(_._2)
      .zipWithIndex()
      .map(x => (x._2 / batchSize, x._1))
      .groupBy(_._1)
      .map(_._2
        .map(x => x._2)
        .slice(0, abs(Random.nextInt) % batchSize + 1)
        .mkString(",")
      )
      .take(50)
      .foreach(println)
  }
}


