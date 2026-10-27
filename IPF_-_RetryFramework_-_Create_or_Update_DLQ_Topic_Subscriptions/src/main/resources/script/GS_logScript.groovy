/* ==============================================

DESCRIPTION:

This Groovy script is used for logging the different steps in the Interface pipeline

INPUT PARAMETERS:


OUTPUT PARAMETERS:


CHANGE HISTORY:

DATE             NAME                   CHANGE
2026-03-14      Kishore Eppalagudem     Initial script

============================================= */
import com.sap.gateway.ip.core.customdev.util.Message;
def Message logIntStart(Message message) {
    //Logs int start
    logCustomHeader(message, "Logging_00_intStart", setTimeStamp() + "Interface processing started" );
   
    return message;
}


def Message logDLQStart(Message message) {
    //Logs DLQ call start
    def hdrmap = message.getHeaders();

    logCustomHeader(message, "Logging_30_callDLQStart", setTimeStamp() + "Preparing to store failed message to dead letter topic in the " + hdrmap.get("EH_iflowName") + " queue.")
   
    return message;
}

def Message logRouteStart(Message message) {
    //Logs route where  subscription exists
    logCustomHeader(message, "Logging_40_routeStart", setTimeStamp() + "Topic Subscription for this iflow exists")
   
    return message;
}

def Message logSubNotExists(Message message) {
    //Logs route where no subscription exists
    def hdrmap = message.getHeaders()
   logCustomHeader(message, "Logging_40_routeStart", setTimeStamp() +  "Topic Subscription for this iflow: " + hdrmap.get("EH_iflowName") + " does not exist")
   
    return message;
}


def Message logSubCreate(Message message) {
    //Logs creation of subscription
    
    logCustomHeader(message, "Logging_45_routeEnd", setTimeStamp() + "Topic Subscription and Filter created")
   
    return message;
}

def Message logIntEnd(Message message) {
    //Logs interface end
    logCustomHeader(message, "Logging_99_intEnd", setTimeStamp() + "Failed message stored, ending interface")
   
    return message;
}

def Message errorStart(Message message) {
    //Logs exception start time

   logCustomHeader(message, 'Logging_50_errorStart', setTimeStamp() + 'Entering exception handling')
   return message;
}

def Message callAlert(Message message) {
    //Logs Alert call time
    
   logCustomHeader(message, 'Logging_55_callAlertStart', setTimeStamp() + 'Calling IPF Alert Handler')
   return message;
}

def Message callAlertEnd(Message message) {
    //Logs Alert call time
    
   logCustomHeader(message, 'Logging_60_callAlertEnd', setTimeStamp() + 'Calling IPF Alert Handler complete')
   return message;
}

def Message errorEnd(Message message) {
    //Logs exception end time
     
   logCustomHeader(message, 'Logging_75_errorEnd', setTimeStamp() + 'Exception handling complete, interface ending')
   return message;
}

def Message logCustomHeader(Message message, String headerName, String headerValue) {
    //Adds custom  header property
    def messageLog  = messageLogFactory.getMessageLog(message)
    messageLog.addCustomHeaderProperty(headerName, headerValue)
    return message;
}

def String setTimeStamp() {
    //Function to record datetime
    Date date = new Date()
    String datePart = date.format("yyyy-MM-dd")
    String timePart = date.format("HH:mm:ss.sss")
    return datePart + ' | ' + timePart + ' | '
}
