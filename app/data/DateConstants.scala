package data
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
trait DateConstants {

  val MAX_HOUR = 23
  val MAX_MINUTE = 59
  val MAX_SECOND = 59
  val df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
  val LOCAL_START_DATE = LocalDate.now().minusDays(1)
  val LOCAL_END_DATE = LocalDate.now()
  val DEFAULT_SEARCH_DATE = LocalDateTime.now.minusHours(3)
}