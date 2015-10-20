package modules

import be.objectify.deadbolt.scala.cache.HandlerCache
import models.security.handlers.MyHandlerCache
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}

class CustomDeadboltHook extends Module {

  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
                                  bind[HandlerCache].to[MyHandlerCache]
                                  )
}