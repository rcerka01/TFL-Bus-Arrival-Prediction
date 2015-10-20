package models.security.handlers

/**
 * Created by raitis on 08/09/15.
 */
import be.objectify.deadbolt.scala.{DeadboltHandler, DynamicResourceHandler}
import play.api.mvc.Request

import scala.collection.immutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
class MyDynamicResourceHandler extends DynamicResourceHandler
{
  def isAllowed[A](name: String, meta: String, handler: DeadboltHandler, request: Request[A]): Future[Boolean] = {
    MyDynamicResourceHandler.handlers(name).isAllowed(name,
      meta,
      handler,
      request)
  }

  // todo implement this when demonstrating permissions
  def checkPermission[A](permissionValue: String, deadboltHandler: DeadboltHandler, request: Request[A]): Future[Boolean] = Future(false)
}

object MyDynamicResourceHandler {
  val handlers: Map[String, DynamicResourceHandler] =
    Map(
      "pureLuck" -> new DynamicResourceHandler() {
        def isAllowed[A](name: String, meta: String, deadboltHandler: DeadboltHandler, request: Request[A]): Future[Boolean] =
          Future(System.currentTimeMillis() % 2 == 0)

        def checkPermission[A](permissionValue: String, deadboltHandler: DeadboltHandler, request: Request[A]): Future[Boolean] = Future(false)
      }
    )
}
