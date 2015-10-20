package models.security

import be.objectify.deadbolt.core.models.Subject
import play.libs.Scala


/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */

class User(val email: String) extends Subject
{

  def getRoles: java.util.List[SecurityRole] = {
     // must be replaced with list from "roles" table via DAO findByEmail.
     Scala.asJava(List(new SecurityRole("user")))
  }

  def getPermissions: java.util.List[UserPermission] = {
    Scala.asJava(List(new UserPermission("printers.edit")))
  }

 def getIdentifier = email
}
