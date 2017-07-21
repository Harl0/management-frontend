package helpers

import models.{Collector, CollectorRegistrationResponse}
import utils.Constants._

class CollectorHelper {

  def processCollectorCreateResponse(data: CollectorRegistrationResponse): String = {
    data.collectorId match {
      case NO_DATA => COLLECTOR_REGISTRATION_FAILURE
      case _ => COLLECTOR_REGISTRATION_SUCCESS
    }
  }

  def processCollectorUpdateResponse(data: Collector): String = {
    data.collectorId match {
      case NO_DATA => COLLECTOR_UPDATE_FAILURE
      case _ => COLLECTOR_UPDATE_SUCCESS
    }
  }

}
