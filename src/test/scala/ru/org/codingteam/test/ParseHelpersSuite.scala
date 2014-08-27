package ru.org.codingteam.test

import org.scalatest.FunSuite

import ru.org.codingteam.freqparser.ParseHelpers._
import ru.org.codingteam.freqparser.{EnterMessageType, RegularMessageType, LogMessage}

class ParseHelpersSuite extends FunSuite {
  test("extractNicknameFromRegularMessage") {
    val testData = List(
      (Some("nickname"), "<nickname>"),
      (None, "The Cake is a Lie!")
    )

    processTestData(extractNicknameFromRegularMessage)(testData)
  }

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
      (List(LogMessage("00:21:36", "Пётр", EnterMessageType, ""),
            LogMessage("00:21:36", "Пётр Васильевич", EnterMessageType, "")),
       "<a name=\"00:21:36\" href=\"#00:21:36\" class=\"ts\">[00:21:36]</a> <font class=\"mj\">" +
       "Пётр зашёл в конференцию</font><br/><a name=\"00:21:36\" href=\"#00:21:36\" class=\"ts\">" +
       "[00:21:36]</a> <font class=\"mj\">Пётр Васильевич зашёл в конференцию</font><br/>"),
      (List(), "The Cake is a Lie")
    )

    processTestData(extractLogMessages)(testData)
  }

  test("extractNicknameFromJoinMessage") {
    val testData = List(
      (Some("Пётр"), "Пётр зашёл в конференцию"),
      (Some("Пётр Васильевич"), "Пётр Васильевич зашёл в конференцию"),
      (None, "The Cake is a Lie")
    )

    processTestData(extractNicknameFromJoinMessage)(testData)
  }

  def processTestData[E, I](f: (I) => E)(testData: List[(E, I)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
