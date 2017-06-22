package models

import play.api.data.Form
import play.api.data.Forms._
import utils.Constants._

/**
  * Created by jason on 23/05/17.
  */
object Forms {

  val clientForm = Form(mapping(
    "clientName" -> text.verifying("error.client.creation.department.required", _.nonEmpty),
    "redirect_uri" -> text.verifying("error.client.creation.redirect_uri.required", _.nonEmpty),
    "imageURI" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(localDate("dd/MM/yyyy"))
  )(Client.apply)(Client.unapply))

  val clients = List(
    ClientInputFields("Department Name", "clientName", "Mandatory"),
    ClientInputFields("Redirect URIs", "redirect_uri", "Mandatory"),
    ClientInputFields("Image URIs", "imageURI", "Optional"),
    ClientInputFields("Contact Name", "contactName", "Optional"),
    ClientInputFields("Contact Details", "contactDetails", "Optional"),
    ClientInputFields("Service Start Date", "serviceStartDate", "Optional - Format dd/MM/yyyy")
  )
}
