package ru.org.codingteam.freqparser.test

import org.scalatest.FunSuite
import ru.org.codingteam.freqparser.extractors._

class ExtractorsSuite extends FunSuite{
  test("RegularMessageNicknameExtractor") {
    val testData = List(
      (Some("nickname"), "<nickname>"),
      (None, "The Cake is a Lie!")
    )

    processTestData(RegularMessageNicknameExtractor.unapply)(testData)
  }

  test("EnterMessageNicknameExtractor") {
    val testData = List(
      (Some("Пётр"), "Пётр зашёл в конференцию"),
      (Some("Пётр Васильевич"), "Пётр Васильевич зашёл в конференцию"),
      (None, "The Cake is a Lie")
    )

    processTestData(EnterMessageNicknameExtractor.unapply)(testData)
  }

  test("LeaveMessageExtractor") {
    val testData = List(
      (Some(("Пётр", "Ушёл спать")), "Пётр вышел из конференции: Ушёл спать"),
      (Some(("Пётр Васильевич", "Ушёл спать")), "Пётр Васильевич вышел из конференции: Ушёл спать"),
      (Some(("Пётр Васильевич", "")), "Пётр Васильевич вышел из конференции"),
      (None, "The Cake is a Lie")
    )

    processTestData(LeaveMessageExtractor.unapply)(testData)
  }

  test("KickMessageExtractor") {
    val testData = List(
      (Some(("Пётр", "Ушёл спать")), "Пётр выгнали из конференции: Ушёл спать"),
      (Some(("Пётр Васильевич", "Ушёл спать")), "Пётр Васильевич выгнали из конференции: Ушёл спать"),
      (Some(("Пётр Васильевич", "")), "Пётр Васильевич выгнали из конференции"),
      (None, "The Cake is a Lie")
    )

    processTestData(KickMessageExtractor.unapply)(testData)
  }

  test("RenameMessageExtractor") {
    val testData = List(
      (Some(("Foo", "Bar")), "Foo сменил ник на Bar"),
      (Some(("Herp Derp", "Hello World")), "Herp Derp сменил ник на Hello World"),
      (Some(("сменил ник на", "сменил ник")), "сменил ник на сменил ник на сменил ник"),
      (None, "The Cake is a Lie")
    )

    processTestData(RenameMessageExtractor.unapply)(testData)
  }

  test("ChangeTopicMessageExtractor") {
    val testData = List(
      (Some(("Василий Петрович", "Hello, World")), "Василий Петрович установил(а) тему: Hello, World"),
      (None, "The Cake is a Lie!")
    )

    processTestData(ChangeTopicMessageExtractor.unapply)(testData)
  }

  def processTestData[E, I](f: (I) => E)(testData: List[(E, I)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
