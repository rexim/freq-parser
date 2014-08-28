package ru.org.codingteam.freqparser.test

import org.scalatest.FunSuite
import ru.org.codingteam.freqparser.ParseHelpers._
import ru.org.codingteam.freqparser.{LeaveMessageType, EnterMessageType, LogMessage, RegularMessageType}

class ParseHelpersSuite extends FunSuite {
  test("extractRoomJid") {
    val testData = List(
      (Some("codingteam@conference.jabber.ru"),
       "Foo<a class=\"roomjid\" href=\"xmpp:codingteam@conference.jabber.ru?join\">" +
       "codingteam@conference.jabber.ru</a>Bar"),
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

  def processTestData[E, I](f: (I) => E)(testData: List[(E, I)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
