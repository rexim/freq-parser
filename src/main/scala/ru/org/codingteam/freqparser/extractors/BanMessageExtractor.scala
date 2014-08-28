package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.LeaveMessageCommonTransformer

object BanMessageExtractor extends RegexExtractor[(String, String)] with LeaveMessageCommonTransformer {
  override val regex = "(.+) запретили входить в конференцию(: (.*))?$".r
}
