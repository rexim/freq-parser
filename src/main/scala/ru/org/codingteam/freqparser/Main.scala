package ru.org.codingteam.freqparser

import org.apache.commons.lang3.StringEscapeUtils

import scala.io.Source
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery.interpolation

object Main {

  def extractRoomAndDateFromFileName(fileName: String): (String, String) = {
    val Pattern = ".*/(.+?@.+?)/(\\d{4})/(\\d{2})/(\\d{2}).html".r
    fileName match {
      case Pattern(room, year, month, day) => (room, s"$year-$month-$day")
      case _ => throw new IllegalArgumentException(s"File name '$fileName' doesn't match '$Pattern' regexp")
    }
  }

  def foreachMessageInFile(fileName: String)(f: (String, String, String, String) => Unit): Unit = {
    val Pattern = "<a.*?>\\[(\\d{2}:\\d{2}:\\d{2})\\]</a> <font class=\"(.*?)\">(.*?)</font>(.*?)<br/>".r
    Pattern.findAllMatchIn(Source.fromFile(fileName).mkString).foreach {
      m => f(
        m.group(1), m.group(2),
        StringEscapeUtils.unescapeHtml4(m.group(3)),
        StringEscapeUtils.unescapeHtml4(m.group(4))
      )
    }
  }

  def extractNicknameFromMnMessage(message: String): String = {
    val Pattern = "^<(.*)>$".r
    message match {
      case Pattern(nickname) => nickname
      case _ => throw new IllegalArgumentException(s"Cannot extract nickname from this '$message'")
    }
  }

  def main(args: Array[String]) = {

    Database.forURL("jdbc:h2:./hell", driver = "org.h2.Driver", user = "sa") withSession {
      implicit session => {
        val fileName = "./logs/chat/codingteam@conference.jabber.ru/2013/04/27.html"
        val (room, date) = extractRoomAndDateFromFileName(fileName)
        foreachMessageInFile(fileName) {
          case (time, "mn", msg1, msg2) => {
            val sender = extractNicknameFromMnMessage(msg1)
            val timestamp = s"$date $time"
            sqlu"""
                   INSERT INTO LOG (time, room, sender, type, message)
                   VALUES ($timestamp, $room, $sender, 'message', $msg2)
            """.first
          }

          case _ => // ignore
        }
      }
    }
  }
}
