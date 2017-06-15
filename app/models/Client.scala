package models

/**
  * Created by jason on 24/05/17.
  */
case class Client(
                   department_name: String,
                   redirectURIs: String,
                   imageURIs: Option[String] = Some(""),
                   contactname: Option[String] = Some(""),
                   contactDetails: Option[String] = Some(""),
                   serviceStartDate: Option[String] = Some("")
                 )
