package bio.ferlab.ui.etl

import java.io.StringReader
import java.time.{Instant, LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.model.Message

import ca.uhn.hl7v2.model.v23.datatype.{ID, IS, ST, TS}
import ca.uhn.hl7v2.model.v23.message.ADT_AXX
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory
import ca.uhn.hl7v2.util.Hl7InputStreamMessageStringIterator

import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.language.implicitConversions

package object hl7 {

  object parse {
    val context = new DefaultHapiContext
    context.setModelClassFactory(new CanonicalModelClassFactory(classOf[ADT_AXX]))

    private val p = context.getPipeParser

    def apply[T](text: String)(implicit ev: Message => T): Seq[T] = {
      val iter = new Hl7InputStreamMessageStringIterator(new StringReader(text)).asScala
      iter.map { t =>
        val m: Message = p.parse(t)
        ev(m)
      }.toSeq

    }
  }

  def parseDate(s: String): Instant = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyyMMddHHmm")).toInstant(ZoneOffset.UTC)


  implicit def toInstant(d: TS): Instant = parseDate(d.getTimeOfAnEvent.getValue)

  implicit def toOptInstant(d: TS): Option[Instant] = {
    for {
      a <- Option(d)
      b <- Option(a.getTimeOfAnEvent)
      v <- Option(b.getValue)
    } yield parseDate(v)

  }

  implicit def STToString(d: ST): String = d.getValue

  implicit def STtoOptString(d: ST): Option[String] = Option(d).map(_.getValue)

  implicit def IStoString(d: IS): String = d.getValue

  implicit def IStoOptString(d: IS): Option[String] = Option(d).map(_.getValue)

  implicit def IDtoString(d: ID): String = d.getValue

  implicit def IDtoOptString(d: ID): Option[String] = Option(d).map(_.getValue)


}
