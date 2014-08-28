package ru.org.codingteam.freqparser.extractors

import ru.org.codingteam.freqparser.extractors.transformers.LeaveMessageCommonTransformer

object KickMessageExtractor extends RegexExtractor[(String, String)]  with LeaveMessageCommonTransformer {
  override val regex = "(.+) выгнали из конференции(: (.*))?$".r
}
