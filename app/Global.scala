import play.api.{Application, GlobalSettings}


/**
 * Created by raitis on 07/09/15.
 */
object Global extends GlobalSettings  {

  override def onStart(app: Application): Unit =  {
    InitialData.start()
  }
}


object InitialData {

  def start(): Unit = {
     println("Hello Master!")
  }


}