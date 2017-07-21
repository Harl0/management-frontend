package models

/**
  * Created by jason on 06/06/17.
  */
case class CollectorInputFields(fieldTitle: String, fieldText: String, hintText: Option[String]) {
  lazy val id: String = fieldText + "Ctl"
  lazy val lableId: String = fieldText + "Lbl"
}
