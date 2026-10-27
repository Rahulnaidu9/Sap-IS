/* ==============================================

DESCRIPTION:

This Groovy script is used for logging the different steps in the Interface pipeline

INPUT PARAMETERS:
EH_alert_iflowName:   This header comes in from the iflow that is calling the Aggregated Alert Framework and contains the parent iflow name

OUTPUT PARAMETERS:
logCustomHeaders


CHANGE HISTORY:

DATE            NAME                    CHANGE
2026-01-09      AnilKumar Navagana      Initial Script
=================================================== */

import com.sap.gateway.ip.core.customdev.util.Message;

def Message intStart(Message message) {
   def hdrmap = message.getHeaders();
   logCustomHeader(message, 'Logging_00_IntStart',setTimeStamp() + 'Interface started via process direct call from ' + hdrmap.get("EH_alert_iflowName"))
   return message;
}
   
def Message stdErrRouteStart(Message message) {
   logCustomHeader(message, 'Logging_10_StdErrRouteStart',setTimeStamp() + 'Preparing email for standard error alert')
   return message;
}

def Message customRouteStart(Message message) {
   logCustomHeader(message, 'Logging_10_CustomRouteStart',setTimeStamp() + 'Preparing email for custom alert')
   return message;
}

def Message intEnd(Message message) {
   logCustomHeader(message, 'Logging_99_IntEnd',setTimeStamp() + 'Email prepared, interface ending, emailing alert')
   return message;
}

def Message logCustomHeader(Message message, String headerName, String headerValue) {
    def messageLog  = messageLogFactory.getMessageLog(message)
    messageLog.addCustomHeaderProperty(headerName, headerValue)
    return message;
}

//capture the exception while processing records
def Message intProcessExcpStart(Message message) {
    def map = message.getProperties()
    def Excep = map.get("CamelExceptionCaught")
    logCustomHeader(message, 'Logging_50_exceptionStart',setTimeStamp() + "Exception branch started; exception caught: " + Excep)
   return message
}

def Message intProcessExcpEnd(Message message) {
   logCustomHeader(message, 'Logging_80_exceptionEnd',setTimeStamp() + "Exception branch ended")
   return message
}

//Log source message payload
def Message mailError(Message message){   
    def map = message.getProperties()
    def Excep = map.get("CamelExceptionCaught")
    def error = message.getBody(java.lang.String) as String;
    def body = """
    Error: ${Excep}
    
    
    Mail Payload:
    <Payload>${error}</Payload>
    """
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null)
    {
        messageLog.addAttachmentAsString("Mail Error",body,"text/plain")
    }
  
    return message
}

def String setTimeStamp() {
    //Function to record datetime
    Date date = new Date()
    String datePart = date.format("yyyy-MM-dd")
    String timePart = date.format("HH:mm:ss.sss")
    return datePart + ' | ' + timePart + ' | '
}