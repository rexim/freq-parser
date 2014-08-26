package ru.org.codingteam.test

import org.scalatest.FunSuite

import ru.org.codingteam.freqparser.ParseHelpers._
import ru.org.codingteam.freqparser.{RegularMessageType, LogMessage}

class ParseHelpersSuite extends FunSuite {
  test("extractRoomJid") {
    val testData = List(
      (Some("codingteam@conference.jabber.ru"), "Foo<a class=\"roomjid\" href=\"xmpp:codingteam@conference.jabber.ru?join\">codingteam@conference.jabber.ru</a>Bar"),
      (None, "The Cake is a Lie!")
    )

    processTestData(extractRoomJid)(testData)
  }

  test("extractDate") {
    val testData = List(
      (Some("2014-08-25"), "Foo<div class=\"logdate\">25.08.2014<span class=\"w3c\">Bar"),
      (None, "The Cake is a Lie")
    )

    processTestData(extractDate)(testData)
  }

  test("extractLogMessages") {
    val testData = List(
      (List(LogMessage("18:14:15", "nickname", RegularMessageType, "message1"),
            LogMessage("18:14:15", "nickname", RegularMessageType, "message2")),
       "Foo" +
       "<a name=\"18:14:15\" href=\"#18:14:15\" class=\"ts\">[18:14:15]</a> " +
       "<font class=\"mn\">&lt;nickname&gt;</font> message1<br/><a name=\"18:14:15\" " +
       "href=\"#18:14:15\" class=\"ts\">[18:14:15]</a> <font class=\"mn\">&lt;nickname&gt;" +
       "</font> message2<br/>" +
       "Bar"),
      (List(), "The Cake is a Lie")
    )

    processTestData(extractLogMessages)(testData)
  }

  def processTestData[E, I](f: (I) => E)(testData: List[(E, I)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
