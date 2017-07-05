/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import java.text.SimpleDateFormat
import java.time._
import java.time.format.DateTimeFormatter
import java.util.Date

import data.DateConstants
import play.api.libs.json._

/**
  * Created by jason on 4/07/17.
  */
object DateConversions extends DateConstants {

  implicit val temporalWrites: Writes[OffsetDateTime] = new Writes[OffsetDateTime] {
    override def writes(o: OffsetDateTime): JsValue = Json.obj("$date" -> Instant.from(o).toEpochMilli)
  }

//  def toOffsetDateTime(epochMillis: Long): OffsetDateTime = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toOffsetDateTime
//
//  def instantFromLocalDate(timeType: String, local: LocalDate): ZonedDateTime = timeType match {
//    case START_DATE_PARAM => local.atStartOfDay().atZone(ZoneOffset.UTC)
//    case END_DATE_PARAM => local.atTime(MAX_HOUR,MAX_MINUTE,MAX_SECOND).atZone(ZoneOffset.UTC)
//  }
//
//  implicit val temporalReads: Reads[OffsetDateTime] = new Reads[OffsetDateTime] {
//    override def reads(json: JsValue): JsResult[OffsetDateTime] = {
//      (json \ "$date").validate[Long] map toOffsetDateTime
//    }
//  }
//
//  implicit val temporalWrites: Writes[OffsetDateTime] = new Writes[OffsetDateTime] {
//    override def writes(o: OffsetDateTime): JsValue = Json.obj("$date" -> Instant.from(o).toEpochMilli)
//  }
//
//  def parsedDate(date: String, timeIsStartOfDay: Boolean, dateFormat: DateTimeFormatter): LocalDateTime = {
//    val parsedDate = LocalDate.parse(date, dateFormat)
//    val minTime = LocalTime.MIN
//    val maxTime = LocalTime.MIDNIGHT.minusSeconds(1)
//
//    if(timeIsStartOfDay) LocalDateTime.of(parsedDate, minTime)
//    else LocalDateTime.of(parsedDate, maxTime)
//  }
//
//  def longDateToString(ld: Long): String = {
//    val DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss.SSSZ"
//    val sdf: SimpleDateFormat = new SimpleDateFormat(DATE_FORMAT)
//    sdf.format(new Date(ld))
//  }
//
//  def stringDateToLong(sd: String): Long = {
//    val DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss.SSSZ"
//    val sdf: SimpleDateFormat = new SimpleDateFormat(DATE_FORMAT)
//    sdf.parse(sd).getTime
//  }
//
//  def defaultTo(endDate: Option[LocalDate]): LocalDate = {
//    endDate.getOrElse(LocalDate.now(ZoneOffset.UTC))
//  }
}
