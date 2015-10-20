package models.security.handlers

import javax.inject.Singleton

import be.objectify.deadbolt.scala.cache.HandlerCache
import be.objectify.deadbolt.scala.{DeadboltHandler, HandlerKey}

@Singleton
class MyHandlerCache extends HandlerCache {

  val defaultHandler: DeadboltHandler = new MyDeadboltHandler

  override def apply(): DeadboltHandler = defaultHandler

  //  override def apply(handlerKey: HandlerKey): DeadboltHandler = handlers(handlerKey)
  override def apply(handlerKey: HandlerKey): DeadboltHandler = ???
}