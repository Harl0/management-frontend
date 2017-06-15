package models

import play.api.data.Form
import play.api.data.Forms._
import utils.Constants._

/**
  * Created by jason on 23/05/17.
  */
object Forms {

  val clientForm = Form(mapping(
    "departmentName" -> nonEmptyText,
    "redirectURIs" -> nonEmptyText,
    "imageURIs" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(text)

  )(Client.apply)(Client.unapply))

  val clients = List(
    ClientInputFields("Department Name", "departmentName", "deparmentTextHint"),
    ClientInputFields("Redirect URIs", "redirectURIs", "redirectURIsHint"),
    ClientInputFields("Image URIs", "imageURIs", "imageURIsHint"),
    ClientInputFields("Contact Name", "contactName", "contactNameHint"),
    ClientInputFields("Contact Details", "contactDetails", "contactDetailsHint"),
    ClientInputFields("Service Start Date", "serviceStartDate", "serviceStartDateHint")
  )
}
