package bio.ferlab.ui.etl.hl7

import java.time.Instant
import ca.uhn.hl7v2.model.v23.segment
case class MSH(sending_application: String, sending_facility: String, date: Instant, version_id: String, country_code: String)

object MSH {

  def apply(msh: segment.MSH): MSH = {
    new MSH(
      msh.getSendingApplication.getNamespaceID,
      msh.getSendingFacility.getNamespaceID,
      msh.getDateTimeOfMessage,
      msh.getVersionID,
      msh.getCountryCode)
  }
}
