package ru.org.codingteam.freqparser.extractors

import scala.util.matching.Regex

object LeaveMessageExtractor extends RegexExtractor[(String, String)] {
  override def transformMatch(m: Regex.Match): (String, String) = {
    val nickname = m.group(1)
    val reason = m.group(3)
    (nickname, if (reason == null) "" else reason)
  }
  override val regex = "(.+) вышел из конференции(: (.*))?$".r
}
