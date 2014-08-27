package ru.org.codingteam.freqparser

import org.apache.commons.lang3.StringEscapeUtils

object ParseHelpers {
  def extractNicknameFromRegularMessage(message: String): String = {
    val Pattern = "^<(.*)>$".r
    message match {
      case Pattern(nickname) => nickname
      case _ => throw new IllegalArgumentException(s"Cannot extract nickname from this '$message'")
    }
  }

  def extractNicknameFromJoinMessage(message: String): Option[String] =
    "(.+) зашёл в конференцию$".r.findFirstMatchIn(message).map(_.group(1))

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
    case (time, "mn", firstChunk, secondChunk) => {
      val sender = extractNicknameFromRegularMessage(firstChunk)
      LogMessage(time, sender, RegularMessageType, secondChunk.tail)
    }

    case (time, "mj", firstChunk, "") => {
      val sender = extractNicknameFromJoinMessage(firstChunk)
        .getOrElse(throw new IllegalArgumentException("Cannot extract nickname from a join message"))
      LogMessage(time, sender, EnterMessageType, "")
    }
  }
}
