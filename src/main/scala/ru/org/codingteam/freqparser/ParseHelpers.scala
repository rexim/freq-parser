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
    "([^/]+@[^/]+)".r.findFirstMatchIn(fileName).map(m => m.group(1))

  def extractDateFromFileName(fileName: String): Option[String] =
    "(\\d{4})/(\\d{2})/(\\d{2})".r.findFirstMatchIn(fileName).map {
      m => s"${m.group(1)}-${m.group(2)}-${m.group(3)}"
    }
}
