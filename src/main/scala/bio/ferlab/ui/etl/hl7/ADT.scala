package bio.ferlab.ui.etl.hl7

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v23.message.ADT_AXX

case class ADT(msh: MSH, evn: EVN, pid: Option[PID])

object ADT {
  implicit def fromMessage(message: Message): ADT = {
    val axx = message.asInstanceOf[ADT_AXX]
    val pid = Option(axx.getPID).map(p => PID(p))
    val msh = MSH(axx.getMSH)
    val evn = EVN(axx.getEVN)
    ADT(msh, evn, pid)
  }
}


