package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.PairMatchTransformer

import scala.util.matching.Regex

object ChangeTopicMessageExtractor extends RegexExtractor[(String, String)] with PairMatchTransformer {
  override protected val regex: Regex = "(.+) установил\\(а\\) тему: (.+)".r
  override val groups: (Int, Int) = (1, 2)
}
