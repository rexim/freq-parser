package ru.org.codingteam.freqparser.extractors

import scala.util.matching.Regex

object EnterMessageNicknameExtractor extends RegexExtractor[String] {
  override def transformMatch(m: Regex.Match): String = m.group(1)
  override val regex = "(.+) зашёл в конференцию$".r
}
