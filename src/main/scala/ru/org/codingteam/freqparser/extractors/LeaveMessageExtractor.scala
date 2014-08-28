package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.PairMatchTransformer

object LeaveMessageExtractor extends RegexExtractor[(String, String)] with PairMatchTransformer {
  override val groups = (1, 3)
  override val regex = "(.+) вышел из конференции(: (.*))?$".r
}
