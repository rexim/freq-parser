package ru.org.codingteam.freqparser.extractors.transformers

import ru.org.codingteam.freqparser.extractors.RegexExtractor

import scala.util.matching.Regex

trait PairMatchTransformer extends RegexExtractor[(String, String)] {
  val groups: (Int, Int)

  override def transformMatch(m: Regex.Match): (String, String) = {
    val (firstGroup, secondGroup) = groups
    val first = m.group(firstGroup)
    val second = m.group(secondGroup)
    (first, if (second == null) "" else second)
  }
}
