package ru.org.codingteam.freqparser.test

import org.scalatest.FunSuite
import ru.org.codingteam.freqparser.extractors.{RegularMessageNickname, EnterMessageNickname}

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

    processTestData(EnterMessageNickname.unapply)(testData)
  }

  def processTestData[E, I](f: (I) => E)(testData: List[(E, I)]) =
    for ((expected, input) <- testData) {
      assertResult(expected)(f(input))
    }
}
