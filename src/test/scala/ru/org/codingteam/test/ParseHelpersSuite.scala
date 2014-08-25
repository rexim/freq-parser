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
    processStringTestData(extractRoomFromFileName)(testData)
  }

  test("extractDateFromFileName") {
    val testData = List(
      (Some("2014-08-19"), "./logs/chat/codingteam@conference.jabber.ru/2014/08/19.html"),
      (None, "The Cake is a Lie!")
    )

    processStringTestData(extractDateFromFileName)(testData)
  }

  test("extractRoomJid") {
    val testData = List(
      (Some("codingteam@conference.jabber.ru"), "Foo<a class=\"roomjid\" href=\"xmpp:codingteam@conference.jabber.ru?join\">codingteam@conference.jabber.ru</a>Bar"),
      (None, "The Cake is a Lie!")
    )

    processStringTestData(extractRoomJid)(testData)
  }

  test("extractDate") {
    val testData = List(
      (Some("2014-08-25"), "Foo<div class=\"logdate\">25.08.2014<span class=\"w3c\">Bar"),
      (None, "The Cake is a Lie")
    )

    processStringTestData(extractDate)(testData)
  }

  def processStringTestData(f: (String) => Option[String])
                           (testData: List[(Option[String], String)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
