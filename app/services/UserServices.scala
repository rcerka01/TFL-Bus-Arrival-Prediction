package services

/**
 * Created by raitis on 10/09/15.
 */

import controllers.LoginForm
import models.UserDAO
import models.autogenerated.Tables.UsersRow
import models.utils.HashUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object UserServices {

  // on "success" actually have to return Some(user) instead of string.
  def verifyUser(form: LoginForm) = {
    var success = "error.internal"
    findByEmail(form.email) map { user => user match {
      case Some(user) => {
        verifyPassword(user.password, form.password) match {
          case Some(true)  =>  success = "success"
          case Some(false) => success = "error.password"
        }
      }
      case _ => success ="error.user"
    }
      success
    }
  }


  def verifyPassword(password: Option[String], passwordToVerify: String) = {
     password map ( (pv: String) => HashUtils.verifyPassword(passwordToVerify, pv) )
  }


  def findByEmail(email: String): Future[Option[UsersRow]] = {
    try {
      UserDAO.findByEmail(email)
    }
    // <- error handler must be implemented.
  }


  def emailAvailable(email: String) = {
    findByEmail(email) map (result => result match {
      case Some(UsersRow(_,_,_,_,_,_)) => false
      case _                           => true
    })
  }


  def createUser(user: UsersRow):Unit = {
    try {
      UserDAO.insert(user)
    }
    // <- error handler must be implemented.
  }



}
