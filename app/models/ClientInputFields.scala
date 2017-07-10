package models

/**
  * Created by jason on 06/06/17.
  */
case class ClientInputFields(fieldTitle: String, fieldText: String, hintText: Option[String]) {
  lazy val id: String = fieldText + "Ctl"
  lazy val lableId: String = fieldText + "Lbl"
}
