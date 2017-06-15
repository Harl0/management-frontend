package controllers


import com.google.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class LanguageAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    val languageCode = request.getQueryString("ui_locales") match {
      case Some("cy") => "cy"
      case _ => "en"
    }
    val newRequest = request.withHeaders(Headers(("Accept-Language", languageCode)))
    block(newRequest)
  }
}