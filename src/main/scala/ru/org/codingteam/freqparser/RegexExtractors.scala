package ru.org.codingteam.freqparser

object RegexExtractors {
  val KickMessage = "(.+) выгнали из конференции(?:: (.*))?$".r
  val RegularMessageNickname = "^<(.*)>$".r
  val BanMessage = "(.+) запретили входить в конференцию(?:: (.*))?$".r
  val ChangeTopic = "(.+) установил\\(а\\) тему: (.+)".r
  val EnterMessageNickname = "(.+) зашёл в конференцию$".r
  val LeaveMessage = "(.+) вышел из конференции(?:: (.*))?$".r
  val RenameMessage = "(.+) сменил ник на (.+)$".r
}
