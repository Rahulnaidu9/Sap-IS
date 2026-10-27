/* ==============================================

DESCRIPTION:

This Groovy script is used for loggging the message processing steps 

INPUT Parameters : Message (payload)
OUTPUT Parameters: Message (modified) with various properties and headers, depending on which function is used from the groovy.


CHANGE HISTORY:

DATE             NAME                               CHANGE
2026-05-15      Piyush Goyal           Initial script
============================================= */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.SimpleDateFormat
import java.util.Calendar;



//logs interface start time
def Message intStart(Message message) {
    def map = message.getHeaders()
    
    def teckEntity = map.get('TECK_Entity')
    logCustomHeader(message, 'TECK_Entity', teckEntity)
    logCustomHeader(message, 'Logging_00_intStart' , setTimeStamp() + " | This interface receives payload from Uri endpoint of CPI -> Delete datastore(delete datastore is not longer required)")
    return message
}

//log source message payload
def Message logSourceData(Message message)
{   
    
    def flag = message.getProperty("EP_EnableLogging")
    if (flag == "true")
    {
        def body = message.getBody(java.lang.String) as String;
        def messageLog = messageLogFactory.getMessageLog(message);
        if(messageLog != null)
        {
            //messageLog.setStringProperty("log1","Printing Payload As Attachment")
            messageLog.addAttachmentAsString("DataStore Payload",body,"text/plain")
        }
    }
    
    return message
}

//logs interface end
def Message intNoRecord(Message message) {
    logCustomHeader(message, 'Logging_27_noRecords', setTimeStamp() + ' | Datastore value in payload is empty/null.')
     logCustomHeader(message, 'Logging_99_intEnd', setTimeStamp() + ' | Processing completed, interface ending')
    return message
}

//capture the exception while processing records
def Message intProcessExcpStart(Message message) {
    def map = message.getProperties()
    def Excep = map.get("CamelExceptionCaught")
    logCustomHeader(message, 'Logging_50_exceptionStart',setTimeStamp() + " | Exception branch started; exception caught: " + Excep)
   return message
}


//Log exception end
def Message intProcessExcpEnd(Message message) {
    def map = message.getProperties()
    def Excep = map.get("EP_Exception")
    logCustomHeader(message, 'Logging_55_exceptionEnd',setTimeStamp() + " | Exception branch ended")
    message.setProperty("Exception", false)
    return message
}

//log error payload
def Message exceptionSaveErrorPayload(Message message) {

    def messageLog = messageLogFactory.getMessageLog(message)
    def map = message.getProperties()
    def sourceAfterTransformation = map.get("EP_SaveXmlPayload")

    def exceptionPayload = message.getProperty("EP_Exception")
    def dataStoreName = message.getProperty("EP_DataStoreName")

    if (exceptionPayload != null && exceptionPayload.toUpperCase().equals("TRUE")) {

        if (messageLog != null) {
            
    
            def target = message.getBody(java.lang.String) as String;
        
            // Dynamic attachment name
            def attachmentName = "Target Payload : ${dataStoreName}"

            // Target Payload Attachment
            if(target != null)
            {
            //messageLog.setStringProperty("log1","Printing Payload As Attachment")
            messageLog.addAttachmentAsString(attachmentName,target,"text/xml")
            }
        }
    }
    return message
}

//log initiation of exception handling
def Message errorStart(Message message) {
    logCustomHeader(message, 'Logging_40_errorStart', setTimeStamp() + ' | Entering exception handling')
    return message
}

//log end of exception handling
def Message errorEnd(Message message) {
    logCustomHeader(message, 'Logging_80_errorEnd', setTimeStamp() + ' | Exception handling complete, interface ending')
    return message
}

//logs DS being deleted 
def Message deletedDS(Message message) {
    
    def datastorename = message.getProperty("EP_DataStoreName")
    logCustomHeader(message, 'Logging_95_deletedDatastore', setTimeStamp() + ' | Datastore ' + datastorename +' being deleted!')
    return message
}

//logs interface end 
def Message intEnd(Message message) {
    logCustomHeader(message, 'Logging_99_intEnd', setTimeStamp() + ' | Completed all processing, interface ending')
    return message
}

//Custom function for adding custom header
def Message logCustomHeader(Message message, String headerName, String headerValue) {
    def messageLog  = messageLogFactory.getMessageLog(message)
    messageLog.addCustomHeaderProperty(headerName, headerValue)
    return message
}

//Custom function for setting the time step in custom headers
def String setTimeStamp() {
    Date date = new Date()
    String datePart = date.format("yyyy-MM-dd")
    String timePart = date.format("HH:mm:ss.sss")
    return datePart + ' | ' + timePart
}