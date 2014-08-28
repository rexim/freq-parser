package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.PairMatchTransformer

object RenameMessageExtractor extends RegexExtractor[(String, String)] with PairMatchTransformer {
  override val groups = (1, 2)
  override val regex = "(.+) сменил ник на (.+)$".r
}
