/* ==============================================

DESCRIPTION:

This Groovy script is used for generatecurrenices pairs form input properties

INPUT PARAMETERS:
TECK_Entity: This header contains the TECK_Entity name

OUTPUT PARAMETERS:


CHANGE HISTORY:

DATE             NAME                   CHANGE

2026-04-08      AnilKumar Navagana     Initial script
============================================= */
import com.sap.gateway.ip.core.customdev.util.Message
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date

Message processData(Message message) {

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

    // Set result
    message.setProperty("EP_toDateTime", formattedDate)

    return message
}