package ru.org.codingteam.test

import org.scalatest.FunSuite

import ru.org.codingteam.freqparser.ParseHelpers._

class ParseHelpersSuite extends FunSuite {
  test("extractRoomFromFileName") {
    val testData = List(
      (Some("codingteam@conference.jabber.ru"), "./logs/chat/codingteam@conference.jabber.ru/2014/08/19.html"),
      (Some("foo@bar"), "foo@bar"),
      (Some("herp@derp"), "./logs/herp@derp"),
      (Some("hello@world"), "hello@world/"),
      (None, "The Cake is a Lie!")
    )
    testData.foreach {
      case (expected, input) =>
        assertResult(expected)(extractRoomFromFileName(input))
    }
  }

  test("extractDateFromFileName") {
    val testData = List(
      (Some("2014-08-19"), "./logs/chat/codingteam@conference.jabber.ru/2014/08/19.html"),
      (None, "The Cake is a Lie!")
    )

    testData.foreach {
      case (expected, input) =>
        assertResult(expected)(extractDateFromFileName(input))
    }
  }
}
