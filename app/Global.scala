import actors.{PollEmail, MailActor}
import akka.actor.Props
import com.google.inject.{Guice, AbstractModule}
import play.api._
import play.api.GlobalSettings
import play.api.libs.concurrent.Akka
import services.{SimpleUUIDGenerator, UUIDGenerator}
import play.api.Play.current

/**
 * Set up the Guice injector and provide the mechanism for return objects from the dependency graph.
 */
object Global extends GlobalSettings {

  /**
   * Bind types such that whenever UUIDGenerator is required, an instance of SimpleUUIDGenerator will be used.
   */
  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[UUIDGenerator]).to(classOf[SimpleUUIDGenerator])
    }
  })

  /**
   * Controllers must be resolved through the application context. There is a special method of GlobalSettings
   * that we can override to resolve a given controller. This resolution is required by the Play router.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onStart(app: Application) = {
    Logger.info("bettermeeting app has started")
    Logger.info("Starting E-Mail Actor")
    val mailActor = Akka.system.actorOf(Props(new MailActor(
      app.configuration.getString("gmail.host").getOrElse("imap.gmail.com"),
      app.configuration.getInt("gmail.port").getOrElse(993),
      app.configuration.getString("gmail.useremail").getOrElse("ibettermeeting@gmail.com"),
      app.configuration.getString("gmail.accountid").getOrElse("426063849992-nvaj5tjtimgs39kt1q5mipbg07546tnt@developer.gserviceaccount.com"),
      app.configuration.getString("gmail.certpath").getOrElse("certs/gmail_ibettermeeting.p12"),
      app.configuration.getBoolean("gmail.debug").getOrElse(true))
    ), name = "mailActor")
    mailActor ! PollEmail
  }

  override def onStop(app: Application) =
    Logger.info("bettermeeting app shutdown...")
}
