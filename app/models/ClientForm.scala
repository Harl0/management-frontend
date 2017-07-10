package models

import java.time.LocalDate

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

/**
  * Created by jason on 23/05/17.
  */
object ClientForm {

  case class ClientRegistrationForm(
                                     clientName: String,
                                     redirectURIs: String,
                                     imageURI: Option[String] = None,
                                     contactName: Option[String] = None,
                                     contactDetails: Option[String] = None,
                                     serviceStartDate: Option[LocalDate] = None
                                   )

  object ClientRegistrationForm {
    implicit val clientRegisterFormat = Json.format[ClientRegistrationForm]
  }


  case class ClientDeleteForm(
                                     clientName: String,
                                     redirectURIs: String,
                                     imageURI: Option[String] = None,
                                     contactName: Option[String] = None,
                                     contactDetails: Option[String] = None,
                                     serviceStartDate: Option[LocalDate] = None
                                   )

  object ClientDeleteForm {
    implicit val clientDeleteFormat = Json.format[ClientDeleteForm]
  }

  val clientRegistrationForm = Form(mapping(
    "clientName" -> text.verifying("error.client.creation.department.required", _.nonEmpty),
    "redirect_uri" -> text.verifying("error.client.creation.redirect_uri.required", _.nonEmpty),
    "imageURI" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(localDate("yyyy-MM-dd"))
  )(ClientRegistrationForm.apply)(ClientRegistrationForm.unapply))

  val clientViewForm = Form(mapping(
    "_id" -> text.verifying("_id is required", _.nonEmpty),
    "clientName" -> text.verifying("Client Name is requried", _.nonEmpty),
    "redirectURIs" -> text.verifying("error.client.creation.redirect_uri.required", _.nonEmpty),
    "clientId" -> text,
    "clientSecret" -> text,
    "imageURI" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(localDate("yyyy-MM-dd"))
  )(Client.apply)(Client.unapply))

  val clients = List(
    ClientInputFields("Department Name", "clientName", None),
    ClientInputFields("Redirect URIs", "redirect_uri", Some("Multiple URIs should be space separated")),
    ClientInputFields("Image URIs", "imageURI", Some("Optional")),
    ClientInputFields("Contact Name", "contactName", Some("Optional")),
    ClientInputFields("Contact Details", "contactDetails", Some("Optional")),
    ClientInputFields("Service Start Date", "serviceStartDate", Some("Optional - Format yyyy-MM-dd"))
  )

  val clientsView = List(
    ClientInputFields("Client ID", "clientID", None),
    ClientInputFields("Client Name", "clientName", None)
  )
}
