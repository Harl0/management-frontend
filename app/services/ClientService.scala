package services

import com.google.inject.Inject
import connectors.ClientConnector
import models.Client

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 30/06/17.
  */
class ClientService @Inject()(clientConnector: ClientConnector) {

//  def executeGetClients: Future[Clients] = {
//    for {
//      clientResult <- clientConnector.retrieveClientList
//    } yield {
//      Clients(
//        clientResult
//      )
//    }
//  }

  def retrieveClientDetails(id: String): Future[Client] = {
    for {
      clientResult <- clientConnector.retrieveClientDetail(id)
    } yield {
      clientResult
    }
  }

}
