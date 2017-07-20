package models

import play.api.libs.json.Json

/**
  * Created by jason on 07/07/17.
  */
object Log {
  implicit val formats = Json.format[Log]
  implicit def jsonify(event: Log):String = s"${Json.toJson(event)}"

  // Generic Request Methods
  def HTTPRequestReceived(action: String, headers: String): Log =
    Log(action = Some(action), headers = Some(headers))

  def HTTPPOSTRequestReceived(action: String, headers: String): Log =
    Log(action = Some(action), headers = Some(headers))

  // Generic Response Methods
   def processResponse(action: String, details: String, responseCode: Int = 0): Log =
    Log(action = Some(action), details = Some(details), responseCode = Some(responseCode))

  sealed case class Log(
                         action: Option[String] = None,
                         headers: Option[String] = None,
                         formFields: Option[String] = None,
                         details: Option[String] = None,
                         responseCode: Option[Int] = None
                       )
}
