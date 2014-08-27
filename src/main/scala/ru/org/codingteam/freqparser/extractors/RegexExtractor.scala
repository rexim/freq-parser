package ru.org.codingteam.freqparser.extractors

import scala.util.matching.Regex

trait RegexExtractor[T] {
  def unapply(text: String): Option[T] =
    regex.findFirstMatchIn(text).map(transformMatch)

  protected def transformMatch(m: Regex.Match): T
  protected val regex: Regex
}
