package bio.ferlab.ui.etl

import bio.ferlab.ui.etl.testutils.WithSparkSession
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ParesHL7Spec extends AnyFlatSpec with WithSparkSession with Matchers {

  import spark.implicits._

  "run" should "create a table with the same patients as input" in {
    val input = getClass.getResource("/hl7").getFile
    withOutputFolder("adt") { output =>
      ParseHL7.run(input, s"$output")
      val result = spark.table("adt").select("pid.patient_id")
      result.as[String].collect() should contain theSameElementsAs Seq("5955ZYiN", "7783LtKD", "2895KvrN", "4873Ignb", "4873Ignb")

    }


  }

}
