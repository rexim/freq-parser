package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.PairMatchTransformer

object BanMessageExtractor extends RegexExtractor[(String, String)] with PairMatchTransformer {
  override val groups = (1, 3)
  override val regex = "(.+) запретили входить в конференцию(: (.*))?$".r
}
