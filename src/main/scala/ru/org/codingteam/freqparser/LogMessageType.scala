package ru.org.codingteam.freqparser

trait LogMessageType

case object RegularMessageType extends LogMessageType {
  override def toString = "message"
}

case object LeaveMessageType extends LogMessageType {
  override def toString = "leave"
}

case object EnterMessageType extends LogMessageType {
  override def toString = "enter"
}
