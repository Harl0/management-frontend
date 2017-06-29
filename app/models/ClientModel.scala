package models

import java.time.OffsetDateTime

/**
  * Created by jason on 29/06/17.
  */
case class ClientModel(_id: String,
                       clientName: String,
                       redirectURIs: String,
                       clientId: String,
                       clientSecret: String,
                       imageURI: Option[String] = None,
                       contactName: Option[String] = None,
                       contactDetails: Option[String] = None,
                       serviceStartDate: Option[OffsetDateTime] = None
                      )