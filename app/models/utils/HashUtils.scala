package models.utils

/**
 * Created by raitis on 10/09/15.
 */

import play.api.libs.Codecs;


object HashUtils {

  val SALT_LENGTH = 9;

  def createRandomString(length: Int) = {
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    val sb = new StringBuilder
    for (i <- 1 to length) {
      val randomNum = util.Random.nextInt(chars.length)
      sb.append(chars(randomNum))
    }
    sb.toString
  }

  def createPassword(plainPassword: String): String = {
    val salt = this.createRandomString(SALT_LENGTH);
    var password = salt + Codecs.sha1(plainPassword + salt);
    return password;
  }

  def verifyPassword(plainPassword: String, encryptedPassword: String): Boolean = {

    var salt = encryptedPassword.substring(0, SALT_LENGTH);
    var validHash = salt + Codecs.sha1(plainPassword + salt);
    return encryptedPassword == validHash;

  }
}