package models

/**
  * Created by jason on 06/06/17.
  */
case class ClientInputFields(fieldTitle: String, fieldText: String, hintText: String) {
  lazy val id: String = fieldText + "Ctl"
  lazy val lableId: String = fieldText + "Lbl"
}
