package simple

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{upper, col, substring}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.StringType

object SimpleTransformMain {

  def main(args: List[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().getOrCreate()

    val kafkaBrokers = "mipt-node04.atp-fivt.org:9092"
    val inputTopic = "simple-input"
    val inputStream = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaBrokers)
      .option("subscribe", inputTopic)
      .option("startingOffsets", "latest")
      .load()

    val kafkaValueColumn = "value"
    val deserializedValueColumn = "deserialized_value"
    val deserializedStream = inputStream
      .select(
        col(kafkaValueColumn) cast StringType as deserializedValueColumn
      )

    val transformedValueColumn = "transformed_value"
    val transformedStream = deserializedStream
      .select(upper(col(deserializedValueColumn)) as transformedValueColumn)

    val kafkaKeyColumn = "key"
    val outputStream = transformedStream
      .select(
        substring(col(transformedValueColumn), 0, 1)
          as kafkaKeyColumn,
        col(transformedValueColumn)
          as kafkaValueColumn
      )

    val outputTopic = "simple-output"
    val checkpointDir = "checkpoints/simple"
    val query = outputStream.writeStream
      .queryName(this.getClass.getSimpleName)
      .outputMode(OutputMode.Append())
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaBrokers)
      .option("topic", outputTopic)
      .option("checkpointLocation", checkpointDir)
      .start()

    query.awaitTermination()
  }
}
