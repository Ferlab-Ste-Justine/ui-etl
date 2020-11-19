package bio.ferlab.ui.etl

import bio.ferlab.ui.etl.hl7.{parse, ADT}
import org.apache.spark.sql.{Dataset, SparkSession}

object ParseHL7 extends App {
  private implicit val spark: SparkSession = SparkSession.builder()
    .enableHiveSupport()
    .getOrCreate()

  val Array(input, output) = args
  spark.sql("use ui")
  run(input, output)

  def run(input: String, output: String)(implicit spark: SparkSession): Unit = {
    import spark.implicits._
    val inputDS: Dataset[String] = spark.read.option("wholetext", value = true).textFile(input)
    val parsed: Dataset[ADT] = inputDS.flatMap { t =>
      parse[ADT](t)
    }

    parsed.write.mode("append")
      .format("avro")
      .option("path", s"$output/adt")
      .saveAsTable("adt")
  }


}
