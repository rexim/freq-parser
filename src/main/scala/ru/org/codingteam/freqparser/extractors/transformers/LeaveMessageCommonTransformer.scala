package ru.org.codingteam.freqparser.extractors.transformers

import ru.org.codingteam.freqparser.extractors.RegexExtractor

import scala.util.matching.Regex

trait LeaveMessageCommonTransformer extends RegexExtractor[(String, String)] {
  override def transformMatch(m: Regex.Match): (String, String) = {
    val nickname = m.group(1)
    val reason = m.group(3)
    (nickname, if (reason == null) "" else reason)
  }
}
