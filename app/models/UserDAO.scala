package models

import models.autogenerated.Tables._
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by raitis on 10/09/15.
 */
object UserDAO {

  val db = Database.forConfig("db_tfl")
  val users: TableQuery[Users] = TableQuery[Users]
  val roles: TableQuery[Roles] = TableQuery[Roles]


  def findByEmail(email: String) = {
      // headOption as it is the unique result.
      db.run(Users.filter(_.email === email).result).map(_.headOption)
  }


  def insert(usersRow: UsersRow) = {
    db.run {
      ((users returning users.map(_.id)) += usersRow)
        .flatMap (id => roles += RolesRow(0, Option("user"), id))
    }
  }


}
