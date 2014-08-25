package ru.org.codingteam.freqparser

object ParseHelpers {
  def extractNicknameFromMnMessage(message: String): String = {
    val Pattern = "^<(.*)>$".r
    message match {
      case Pattern(nickname) => nickname
      case _ => throw new IllegalArgumentException(s"Cannot extract nickname from this '$message'")
    }
  }

  def extractRoomFromFileName(fileName: String): Option[String] =
    "([^/]+@[^/]+)".r.findFirstMatchIn(fileName).map(_.group(1))

  def extractDateFromFileName(fileName: String): Option[String] =
    "(\\d{4})/(\\d{2})/(\\d{2})".r.findFirstMatchIn(fileName).map {
      m => s"${m.group(1)}-${m.group(2)}-${m.group(3)}"
    }

  def extractRoomJid(content: String): Option[String] =
    "<a class=\"roomjid\".*>(.+?@.+?)</a>".r.findFirstMatchIn(content).map(_.group(1))

  def extractDate(content: String): Option[String] =
    "<div class=\"logdate\">(\\d{2}).(\\d{2}).(\\d{4})".r.findFirstMatchIn(content).map {
      m => s"${m.group(3)}-${m.group(2)}-${m.group(1)}"
    }
}
