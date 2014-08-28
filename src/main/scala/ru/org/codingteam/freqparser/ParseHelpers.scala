package ru.org.codingteam.freqparser

import org.apache.commons.lang3.StringEscapeUtils
import ru.org.codingteam.freqparser.extractors._

object ParseHelpers {

  def extractRoomJid(content: String): Option[String] =
    "<a class=\"roomjid\".*>(.+?@.+?)</a>".r.findFirstMatchIn(content).map(_.group(1))

  def extractDate(content: String): Option[String] =
    "<div class=\"logdate\">(\\d{2}).(\\d{2}).(\\d{4})".r.findFirstMatchIn(content).map {
      m => s"${m.group(3)}-${m.group(2)}-${m.group(1)}"
    }

  def extractLogMessages(content: String): List[LogMessage] = {
    val rawMessagePattern =
      "<a.*?>\\[(\\d{2}:\\d{2}:\\d{2})\\]</a> <font class=\"(.*?)\">(.*?)</font>(.*?)<br/>".r

    rawMessagePattern.findAllMatchIn(content).map(
      m => {
        val time = m.group(1)
        val messageClass = m.group(2)
        val firstChunk = StringEscapeUtils.unescapeHtml4(m.group(3))
        val secondChunk = StringEscapeUtils.unescapeHtml4(m.group(4))
        (time, messageClass, firstChunk, secondChunk)
      }
    ).collect(constructMessage).toList
  }

  private type RawMessage = (String, String, String, String)

  private val constructMessage: PartialFunction[RawMessage, LogMessage] = {
    case (time, "mn", RegularMessageNickname(sender), message) => {
      LogMessage(time, sender, RegularMessageType, message.tail)
    }

    case (time, "mj", EnterMessageNicknameExtractor(sender), "") => {
      LogMessage(time, sender, EnterMessageType, "")
    }

    case (time, "ml", LeaveMessageExtractor((sender, reason)), "") => {
      LogMessage(time, sender, LeaveMessageType, s"User left: $reason")
    }

    case (time, "ml", KickMessageExtractor((sender, reason)), "") => {
      LogMessage(time, sender, LeaveMessageType, s"User kicked: $reason")
    }

    case (time, "ml", BanMessageExtractor((sender, reason)), "") => {
      LogMessage(time, sender, LeaveMessageType, s"User banned: $reason")
    }
  }
}
