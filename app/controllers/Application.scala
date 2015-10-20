package controllers

import be.objectify.deadbolt.scala.ActionBuilders
import com.google.inject.Inject
import play.api.mvc._

class Application @Inject() (actionBuilder: ActionBuilders) extends Controller {

  def index = Action {
    Redirect(routes.JobsController.selectedRoutes())
  }

}
