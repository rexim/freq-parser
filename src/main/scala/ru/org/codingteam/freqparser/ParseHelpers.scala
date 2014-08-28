package ru.org.codingteam.freqparser

import org.apache.commons.lang3.StringEscapeUtils
import ru.org.codingteam.freqparser.extractors._

object ParseHelpers {

  type RawMessage = (String, String, String, String)

  def extractRoomJid(content: String): Option[String] =
    "<a class=\"roomjid\".*>(.+?@.+?)</a>".r.findFirstMatchIn(content).map(_.group(1))

  def extractDate(content: String): Option[String] =
    "<div class=\"logdate\">(\\d{2}).(\\d{2}).(\\d{4})".r.findFirstMatchIn(content).map {
      m => s"${m.group(3)}-${m.group(2)}-${m.group(1)}"
    }

  def extractRawMessages(content: String): Iterator[RawMessage] = {
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
    )
  }

  def constructLogMessage(rawMessage: RawMessage): Option[LogMessage] = rawMessage match {
    case (time, "mn", RegularMessageNicknameExtractor(sender), message) => {
      Some(LogMessage(time, sender, RegularMessageType, message.tail))
    }

    case (time, "mj", EnterMessageNicknameExtractor(sender), "") => {
      Some(LogMessage(time, sender, EnterMessageType, ""))
    }

    case (time, "ml", LeaveMessageExtractor((sender, reason)), "") => {
      Some(LogMessage(time, sender, LeaveMessageType, s"User left: $reason"))
    }

    case (time, "ml", KickMessageExtractor((sender, reason)), "") => {
      Some(LogMessage(time, sender, LeaveMessageType, s"User kicked: $reason"))
    }

    case (time, "ml", BanMessageExtractor((sender, reason)), "") => {
      Some(LogMessage(time, sender, LeaveMessageType, s"User banned: $reason"))
    }

    case (time, "ml", RenameMessageExtractor((sender, newNick)), "") => {
      Some(LogMessage(time, sender, LeaveMessageType, s"renamed to $newNick"))
    }

    case _ => None
  }
}
