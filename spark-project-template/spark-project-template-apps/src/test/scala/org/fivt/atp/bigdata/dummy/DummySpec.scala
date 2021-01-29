package org.fivt.atp.bigdata.dummy

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DummySpec extends AnyFlatSpec with Matchers {
  // TODO Log configuration - by default Spark is very verbose
  private val spark = SparkSession.builder()
    .master("local[2]")
    .appName(this.getClass.getSimpleName)
    .getOrCreate()

  "A test" should "access Spark version" in {
    println(s"Spark version: ${spark.version}")
    spark.version should not be empty
  }

}
