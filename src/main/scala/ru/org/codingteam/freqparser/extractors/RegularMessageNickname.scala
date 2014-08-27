package ru.org.codingteam.freqparser.extractors

import scala.util.matching.Regex

object RegularMessageNickname extends RegexExtractor[String] {
  override def transformMatch(m: Regex.Match): String = m.group(1)
  override val regex = "^<(.*)>$".r
}