package models.security.handlers

import be.objectify.deadbolt.core.models.Subject
import be.objectify.deadbolt.scala.{DeadboltHandler, DynamicResourceHandler}
import controllers._
import models.security.User
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
class MyDeadboltHandler(dynamicResourceHandler: Option[DynamicResourceHandler] = None) extends DeadboltHandler {

def beforeAuthCheck[A](request: Request[A]) = Future(None)

override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = {
Future(dynamicResourceHandler.orElse(Some(new MyDynamicResourceHandler())))
}

override def getSubject[A](request: Request[A]): Future[Option[Subject]] = {
  request.session.get("secure.email")
     .map (email => Future(Some(new User(email))))
     .getOrElse(Future(None))
}

  def onAuthFailure[A](request: Request[A]): Future[Result] = {
    // redirect to login.
    Future.successful(Results.Redirect(routes.UsersController.login()))
 }
}