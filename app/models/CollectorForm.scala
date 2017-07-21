package models

import java.time.LocalDate

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

/**
  * Created by jason on 23/05/17.
  */
object CollectorForm {

  case class CollectorRegistrationForm(
                                        collectorName: String,
                                        redirectURIs: String,
                                        imageURI: Option[String] = None,
                                        contactName: Option[String] = None,
                                        contactDetails: Option[String] = None,
                                        serviceStartDate: Option[LocalDate] = None
                                      )

  object CollectorRegistrationForm {
    implicit val collectorRegisterFormat = Json.format[CollectorRegistrationForm]
  }


  case class CollectorDeleteForm(
                                  collectorName: String,
                                  redirectURIs: String,
                                  imageURI: Option[String] = None,
                                  contactName: Option[String] = None,
                                  contactDetails: Option[String] = None,
                                  serviceStartDate: Option[LocalDate] = None
                             )

  object CollectorDeleteForm {
    implicit val collectorDeleteFormat = Json.format[CollectorDeleteForm]
  }

  val collectorRegistrationForm = Form(mapping(
    "collectorName" -> text.verifying("error.collector.creation.department.required", _.nonEmpty),
    "redirect_uri" -> text.verifying("error.collector.creation.redirect_uri.required", _.nonEmpty),
    "imageURI" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(localDate("yyyy-MM-dd"))
  )(CollectorRegistrationForm.apply)(CollectorRegistrationForm.unapply))

  val collectorViewForm = Form(mapping(
    "_id" -> text.verifying("_id is required", _.nonEmpty),
    "collectorName" -> text.verifying("Collector Name is requried", _.nonEmpty),
    "redirectURIs" -> text.verifying("error.collector.creation.redirect_uri.required", _.nonEmpty),
    "collectorId" -> text,
    "collectorSecret" -> text,
    "imageURI" -> optional(text),
    "contactName" -> optional(text),
    "contactDetails" -> optional(text),
    "serviceStartDate" -> optional(localDate("yyyy-MM-dd"))
  )(Collector.apply)(Collector.unapply))

  val collectors = List(
    CollectorInputFields("Department Name", "collectorName", None),
    CollectorInputFields("Redirect URIs", "redirect_uri", Some("Multiple URIs should be space separated")),
    CollectorInputFields("Image URIs", "imageURI", Some("Optional")),
    CollectorInputFields("Contact Name", "contactName", Some("Optional")),
    CollectorInputFields("Contact Details", "contactDetails", Some("Optional")),
    CollectorInputFields("Service Start Date", "serviceStartDate", Some("Optional - Format yyyy-MM-dd"))
  )

  val collectorsView = List(
    CollectorInputFields("Collector ID", "collectorID", None),
    CollectorInputFields("Collector Name", "collectorName", None)
  )
}
