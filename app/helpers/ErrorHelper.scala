package helpers

class ErrorHelper {

  def non200Error(status: Int, service: String): String = s"Non 200 response returned from $service: $status"

  def internalError(message: String): String = s"Internal error occurred: $message"

}
