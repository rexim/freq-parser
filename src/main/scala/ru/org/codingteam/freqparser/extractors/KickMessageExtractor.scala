package ru.org.codingteam.freqparser.extractors

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

object KickMessageExtractor extends RegexExtractor[(String, String)] {
  override def transformMatch(m: Match): (String, String) = {
    val nickname = m.group(1)
    val reason = m.group(3)
    (nickname, if (reason == null) "" else reason)
  }
  override val regex: Regex = "(.+) выгнали из конференции(: (.*))?$".r
}
