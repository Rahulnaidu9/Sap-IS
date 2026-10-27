/* ==============================================

DESCRIPTION:

This Groovy script is used for logging the different steps in the Interface pipeline

INPUT PARAMETERS:
Eh_iflowname:   This header comes in from the iflow that is calling the Aggregated Alert Framework and contains the parent iflow name
Eh_toemail:     This header comes in from the iflow that is calling the Aggregated Alert Framework and contains the To Email recipients
Eh_ccemail:     This header comes in from the iflow that is calling the Aggregated Alert Framework and contains the CC Email recipients
EP_fromDateTime: This parameter is used to store the start time of the interface
EP_alert_defaultRecipients:   This header contains the To Email recipients  
EP_alert_defaultCCRecipients:   This header contains the CC Email recipients  


OUTPUT PARAMETERS:
logCustomHeaders


CHANGE HISTORY:

DATE            NAME                    CHANGE
2026-01-09      AnilKumar Navagana      Initial Script
============================================= */

import com.sap.gateway.ip.core.customdev.util.Message;


def Message intStart(Message message) {
    //Logs start of interface
    def hdrmap = message.getHeaders();
    logCustomHeader(message, 'Logging_00_intStart', setTimeStamp() + 'Interface started via process direct call from ' + hdrmap.get("EH_iflowName"))
    return message;
}
   
def Message aggregateStart(Message message) {
    //Logs start of aggregation of errors
    logCustomHeader(message, 'Logging_10_aggregationStart', setTimeStamp() + 'Aggregating errors started')
    return message;
}

def Message aggregateEnd(Message message) {
    //Logs end of aggregation of errors
    logiFlowStartTime(message)
    logCustomHeader(message, 'Logging_20_aggregationEnd', setTimeStamp() + 'Aggregation of errors completed')
    intEnd(message)
    return message;
}

def Message intEnd(Message message) {
    //Logs end of interface
    def defToRecipients = message.getProperty("EP_alert_defaultRecipients")
    def defCCRecipients = message.getProperty("EP_alert_defaultCCRecipients")
    def headers = message.getHeaders();
    def logTxt = 'Email prepared, interface ending, emailing aggregated alert \n'
    
    //Logs toEmail and ccEmail in the custom headers
    if(defToRecipients!=null){
        logCustomHeader(message, 'Logging_defaultToAddresses', defToRecipients)
    }
    if(defCCRecipients!=null){
        logCustomHeader(message, 'Logging_defaultCCAddresses', defCCRecipients)
    }
    
    def tom = headers.get('Eh_toemail');
    if (tom!=null)
        logCustomHeader(message, 'Logging_applicationSpecificToAddresses', tom)
    
    def toc = headers.get('Eh_ccemail');    
    if(toc!=null)
        logCustomHeader(message, 'Logging_applicationSpecificCCAddresses', toc)
    
    def toRecipients = message.getProperty("EP_toRecipients")
    def ccRecipients = message.getProperty("EP_ccRecipients")
    
    if((toRecipients==',') && (ccRecipients==',')){
        throw new Exception("There should be at least one email address configured to send an alert!")
    }
    
    logCustomHeader(message, 'Logging_99_intEnd', setTimeStamp() + logTxt)
    return message;
}

def Message logCustomHeader(Message message, String headerName, String headerValue) {
    //Generic function to add custom header property
    def messageLog  = messageLogFactory.getMessageLog(message)
    messageLog.addCustomHeaderProperty(headerName, headerValue)
    return message;
}

def Message logiFlowStartTime(Message message) {
    //Getting start time
    def iflowStartTime = message.getProperty("EP_fromDateTime")
    
    //Getting the iflow name
    def hdrmap = message.getHeaders();
    def logMsg = 'Interface started via process direct call from ' + hdrmap.get("EH_iflowName")
    
    //Logging custom header
    logCustomHeader(message, 'Logging_00_intStart', iflowStartTime + logMsg)
   return message;
}

def String setTimeStamp() {
    //Function to record datetime
    Date date = new Date()
    String datePart = date.format("yyyy-MM-dd")
    String timePart = date.format("HH:mm:ss.sss")
    return datePart + ' | ' + timePart + ' | '
}