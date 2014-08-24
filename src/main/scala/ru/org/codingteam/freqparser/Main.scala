package ru.org.codingteam.freqparser

import org.apache.commons.lang3.StringEscapeUtils

import scala.io.Source
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery.interpolation

import ParseHelpers._
import java.io.File
import scala.util.matching.Regex

object Main {

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

  def recursiveListFiles(root: File, pattern: Regex): Array[File] = {
    val subFiles = root.listFiles
    val matchedSubFiles = subFiles.filter(f => pattern.findFirstIn(f.getName).isDefined)
    matchedSubFiles ++ subFiles.filter(_.isDirectory).flatMap(recursiveListFiles(_, pattern))
  }
  
  def getLogFiles(root: String): Array[String] = {
    recursiveListFiles(new File(root), "\\d{2}\\.html".r)
      .map(_.getAbsolutePath)
      .sortWith(_ < _)
  }

  def insertMessageIntoDatabase(timestamp: String,
                                room: String,
                                sender: String,
                                messageType: String,
                                message: String)
                               (implicit session: Session): Unit =
    sqlu"""
          INSERT INTO log (time, room, sender, type, message)
          VALUES ($timestamp, ${room.take(255)},
                  ${sender.take(255)}, $messageType, $message)
        """.first

  def convertLogFile(fileName: String)(implicit session: Session) = {
    val room = extractRoomFromFileName(fileName).get
    val date = extractDateFromFileName(fileName).get
    foreachMessageInFile(fileName) {
      case (time, "mn", msg1, msg2) => {
        val sender = extractNicknameFromMnMessage(msg1)
        val timestamp = s"$date $time"
        insertMessageIntoDatabase(timestamp, room, sender, "message", msg2)
      }

      case _ => // ignore
    }
  }

  def main(args: Array[String]) =
    Database.forURL("jdbc:h2:./hell", driver = "org.h2.Driver", user = "sa") withSession {
      implicit session => getLogFiles(".").foreach {
        logFile => {
          convertLogFile(logFile)
          println(logFile)
        }
      }
    }
}
