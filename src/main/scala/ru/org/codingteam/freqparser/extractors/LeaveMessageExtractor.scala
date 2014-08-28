package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.LeaveMessageCommonTransformer

object LeaveMessageExtractor extends RegexExtractor[(String, String)] with LeaveMessageCommonTransformer {
  override val regex = "(.+) вышел из конференции(: (.*))?$".r
}
