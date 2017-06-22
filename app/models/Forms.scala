package models

import play.api.data.Form
import play.api.data.Forms._
import utils.Constants._

/**
  * Created by jason on 23/05/17.
  */
object Forms {

  val clientForm = Form(mapping(
    "clientName" -> nonEmptyText,
    "redirect_uri" -> nonEmptyText,
    "imageURI" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(text)

  )(Client.apply)(Client.unapply))

  val clients = List(
    ClientInputFields("Department Name", "clientName", "deparmentTextHint"),
    ClientInputFields("Redirect URIs", "redirect_uri", "redirectURIsHint"),
    ClientInputFields("Image URIs", "imageURI", "imageURIsHint"),
    ClientInputFields("Contact Name", "contactName", "contactNameHint"),
    ClientInputFields("Contact Details", "contactDetails", "contactDetailsHint"),
    ClientInputFields("Service Start Date", "serviceStartDate", "serviceStartDateHint")
  )
}
