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
import com.sap.gateway.ip.core.customdev.util.Message;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

def Message processData(Message message) {
 
    // Read the date time properties
    hdrmap = message.getHeaders();
    alertTime = hdrmap.get("EH_alert_dateTime");
    
    // Set the output format; example: 2021-Sep-21 12:08:01AM MDT
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LLL-dd hh:mm:ssa zz");

    // Parse the start and end dateTimes as unzoned (localDateTime)
    alertTimeLocal = LocalDateTime.parse(alertTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    
    // Convert the unzoned times to zoned at UTC
    ZonedDateTime alertTimeUtc = ZonedDateTime.of(alertTimeLocal, ZoneId.of('Z'))
    
    // Set new properties, converting the zones to Mountain Time
    message.setHeader("EH_alert_dateTime", alertTimeUtc.withZoneSameInstant(ZoneId.of("America/Pacific")).format(formatter))
   
    return message;
}