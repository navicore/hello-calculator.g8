package my.org.mycalculator

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {

  def main(args: Array[String]) {

    val conf = ConfigFactory.load()
    val urlpath = conf.getString("main.path")
    val port = conf.getInt("main.port")

    implicit val system = ActorSystem("rest-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path(urlpath) {
        logRequest(urlpath) {
          get {
            logger.debug(s"get $urlpath")
            complete(HttpEntity(ContentTypes.`application/json`, "{\"msg\": \"Say hello to akka-http\"}\n"))
          } ~
          post {
            logger.debug(s"post $urlpath")
            complete(HttpEntity(ContentTypes.`application/json`, "{\"msg\": \"Say whoa to akka-http\"}\n"))
          }
        }
      }

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

