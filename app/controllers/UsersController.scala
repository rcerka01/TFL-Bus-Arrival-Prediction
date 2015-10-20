package controllers

import be.objectify.deadbolt.scala.ActionBuilders
import com.google.inject.Inject
import models.autogenerated.Tables._
import models.utils.HashUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc._
import services.UserServices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Forms case classes
 */
case class UserForm( name: String, password:String, email: String)
case class LoginForm( email: String, password:String)


class UsersController @Inject() (actionBuilder: ActionBuilders,val messagesApi: MessagesApi) extends Controller  with I18nSupport {

  /**
   * Forms
   */
  val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> email
    )(UserForm.apply)(UserForm.unapply)
  )


  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
  )



  /**
   * Actions
   */
  def create = Action { implicit request =>
    Ok(views.html.authenticate.create(userForm))
  }


  def save = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.authenticate.create(formWithErrors)),
      value => {

        val pw = HashUtils.createPassword(value.password)
        val ts = new java.sql.Timestamp(System.currentTimeMillis())
        val user = new UsersRow(0, Option(ts), Option(value.name), Option(value.email), Option(pw))

        // error with saving db should be handled at service level (probably).
        UserServices.createUser(user)

        Redirect(routes.UsersController.login).flashing("success" -> Messages("account.created"))
      }
    )
  }


  def login = Action { implicit request =>
    Ok(views.html.authenticate.login(loginForm))
  }


  def authenticate() = Action.async { implicit request =>

    // if returned Some(User) set session with all (needed) user values
    loginForm.bindFromRequest.fold (
     formWithErrors => Future.successful(BadRequest(views.html.authenticate.login(formWithErrors))),
      value         => {
         UserServices.verifyUser(value) flatMap {user => user match {
           case "success"        => {
                                      UserServices.findByEmail(value.email) map { user =>
                                        Redirect(routes.JobsController.selectedRoutes())
                                         .withSession("secure.email" -> user.get.email.get,
                                                      "secure.id"    -> user.get.id.toString,
                                                      "secure.name"  -> user.get.name.get
                                                      )
                                      }
                                    }
           case "error.internal" => {
                                     Future.successful(Redirect(routes.UsersController.login)
                                      .flashing("error" -> Messages("error.internal")))
                                    }
           case "error.user" =>     {
                                     Future.successful(Redirect(routes.UsersController.login)
                                      .flashing("error" -> Messages("error.user")))
                                    }
           case "error.password" => {
                                     Future.successful(Redirect(routes.UsersController.login)
                                      .flashing("error" -> Messages("error.password")))
                                    }
           case _                =>  Future.successful(Redirect(routes.UsersController.login)
                                      .flashing("error" -> Messages("error.internal")))
                          }
                        }
                      }
    )
  }


  def logout = Action {
    Redirect(routes.UsersController.login()).withNewSession
  }


  def isUsed = ???

}
