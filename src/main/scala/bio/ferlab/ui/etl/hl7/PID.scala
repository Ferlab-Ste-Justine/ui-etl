package bio.ferlab.ui.etl.hl7

import java.time.Instant

import ca.uhn.hl7v2.model.v23.segment

case class PID(
                patient_id: String,
                first_name: String,
                last_name: String,
                sex: String,
                date_of_birth: Instant,
                birth_place: Option[String],
                nationality: Option[String],
                marital_satus: Option[String],
                patient_death_indicator: Option[String])

object PID {

  def apply(pid: segment.PID): PID = {
    PID(
      pid.getPatientIDInternalID(0).getID,
      pid.getPatientName(0).getGivenName,
      pid.getPatientName(0).getFamilyName,
      pid.getSex.getValue,
      pid.getDateOfBirth,
      pid.getBirthPlace,
      pid.getNationalityCode.getIdentifier,
      pid.getMaritalStatus(0),
      pid.getPatientDeathIndicator
    )
  }
}
