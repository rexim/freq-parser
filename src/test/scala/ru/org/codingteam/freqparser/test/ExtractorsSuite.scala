package ru.org.codingteam.freqparser.test

import org.scalatest.FunSuite
import ru.org.codingteam.freqparser.extractors.{KickMessageExtractor, LeaveMessageExtractor, RegularMessageNickname, EnterMessageNicknameExtractor}

class ExtractorsSuite extends FunSuite{
  test("RegularMessageNickname") {
    val testData = List(
      (Some("nickname"), "<nickname>"),
      (None, "The Cake is a Lie!")
    )

    processTestData(RegularMessageNickname.unapply)(testData)
  }

  test("EnterMessageNickname") {
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

  def processTestData[E, I](f: (I) => E)(testData: List[(E, I)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
