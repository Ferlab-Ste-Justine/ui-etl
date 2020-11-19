package bio.ferlab.ui.etl.hl7

import java.time.Instant
import ca.uhn.hl7v2.model.v23.segment

case class EVN(recorded_date: Option[Instant], event_reason_code: String, operator_id: String, event_date: Option[Instant])

object EVN {
  def apply(evn: segment.EVN): EVN = {
    EVN(
      evn.getRecordedDateTime,
      evn.getEventReasonCode.getValue,
      evn.getOperatorID.getCn1_IDNumber.getValue,
      evn.getEventOccurred)
  }

}


