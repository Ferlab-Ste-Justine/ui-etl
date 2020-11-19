package bio.ferlab.ui.etl.testutils

import org.apache.spark.sql.SparkSession

trait WithSparkSession extends WithOutputFolder {


  implicit lazy val spark: SparkSession = SparkSession.builder()
    .config("spark.ui.enabled", value = false)
    .master("local")
    .getOrCreate()


}
