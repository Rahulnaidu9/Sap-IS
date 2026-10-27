// This is Groovy Flowstep Version 2.x, running with Groovy runtime 4, Downgrade the script if older behaviour needed.

/* =====================================================================================
DESCRIPTION: 
  Converts the date times of the query to Pacific time
 
RELATED OBJECTS: 
  None

INPUT PARAMETERS:
  header.EH_alert_dateTime          UTC date times
  
OUTPUT PARAMETERS:
  header.EH_alert_dateTime          Pacific timezone date time

CHANGE HISTORY: 
DATE       	    NAME      	            CHANGE
2021-01-09      AnilKumar Navagana      Initial Script

===================================================================================== */
import com.sap.it.script.v2.api.Message;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date

def Message processData(Message message) {
 /*
    // Read the date time properties
    def hdrmap = message.getHeaders();
    def alertTime = hdrmap.get("EH_alert_dateTime");
    
    // Set the output format; example: 2021-Sep-21 12:08:01AM MDT
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LLL-dd hh:mm:ssa zz");

    // Parse the start and end dateTimes as unzoned (localDateTime)
    def alertTimeLocal = LocalDateTime.parse(alertTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    
    // Convert the unzoned times to zoned at UTC
    ZonedDateTime alertTimeUtc = ZonedDateTime.of(alertTimeLocal, ZoneId.of('Z'))
   
   //Adding logic to set timezone based on TECK_Entity -- 24042026
    def entityHeader = message.getHeaders().get("TECK_Entity")

    
    // Set new properties, converting the zones to Mountain Time
    message.setHeader("EH_alert_dateTime", alertTimeUtc.withZoneSameInstant(ZoneId.of("America/Vancouver")).format(formatter))
   */
   
   // Read header
    def entityHeader = message.getHeaders().get("TECK_Entity")

    // Split by comma and trim spaces
    def entities = entityHeader?.split(",")?.collect { it.trim() }

    String timeZoneId

    if (entities?.contains("CH")) {
        // Chile Standard Time
        timeZoneId = "America/Santiago"
    } else if (entities?.contains("CA") || entities?.contains("US") || entities?.contains("GL")) {
        // Vancouver timezone
        timeZoneId = "America/Vancouver"
    } else {
        // Fallback
        timeZoneId = "UTC"
    }

    // Format: dd-MMM-yyyy HH:mm:ss zz
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss zz")
    sdf.setTimeZone(TimeZone.getTimeZone(timeZoneId))

    String formattedDate = sdf.format(new Date())
    
    message.setHeader("EH_alert_dateTime", formattedDate)
    return message;
}