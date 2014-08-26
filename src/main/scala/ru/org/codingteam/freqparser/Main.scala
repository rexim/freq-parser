package ru.org.codingteam.freqparser

import scala.io.Source
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery.interpolation

import ParseHelpers._
import java.io.File
import scala.util.matching.Regex

object Main {

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

  def convertLogFile(fileName: String)(implicit session: Session) = {
    val content = Source.fromFile(fileName).mkString
    val room = extractRoomJid(content).getOrElse(throw new IllegalArgumentException("Cannot extract room JID"))
    val date = extractDate(content).getOrElse(throw new IllegalArgumentException("Cannot extract date"))
    extractLogMessages(content).foreach {
      case LogMessage(time, sender, messageType, message) => {
        val timestamp = s"$date $time"
        sqlu"""
          INSERT INTO log (time, room, sender, type, message)
          VALUES ($timestamp, ${room.take(255)},
                  ${sender.take(255)}, ${messageType.toString}, $message)
        """.first
      }
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
