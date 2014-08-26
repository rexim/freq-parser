package ru.org.codingteam.freqparser

case class LogMessage(time: String,
                      sender: String,
                      messageType: LogMessageType,
                      message: String)