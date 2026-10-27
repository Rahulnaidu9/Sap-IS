/* ==============================================

DESCRIPTION:

This Groovy script is used for logging the different steps in the Interface pipeline

INPUT PARAMETERS:



OUTPUT PARAMETERS:


CHANGE HISTORY:

DATE             NAME                   CHANGE
2022-03-22      Kishore Eppalagudem     Initial script
2026-03-15		Kishore Eppalagudem		Updated script
============================================= */


import com.sap.gateway.ip.core.customdev.util.Message;




def Message logInterfaceStartEvent(Message message) {
    //Log interface start
    def messageLog = messageLogFactory.getMessageLog(message);
    messageLog.addCustomHeaderProperty("Logging_00_intStart", setTimeStamp() + "Interface started processing") 
   
    return message;
}

def Message logPDStart(Message message) {
  //Log interface end
  def messageLog = messageLogFactory.getMessageLog(message);
  def mainiFlow = message.getProperty("EP_iflowName");
    messageLog.addCustomHeaderProperty("Logging_20_mainiFlowStart", setTimeStamp() + "Calling main iFlow: " + mainiFlow)	 
       return message;
}

def Message logInterfaceEndEvent(Message message) {
  //Log interface end
  def messageLog = messageLogFactory.getMessageLog(message);
	messageLog.addCustomHeaderProperty("Logging_30_mainiFlowEnd", setTimeStamp() + "Message forwarded to main iFlow successfully")	 
    messageLog.addCustomHeaderProperty("Logging_99_intEnd", setTimeStamp() + "Interface processing ends")	 
       return message;
}

def Message errorStart(Message message) {
   //Logs error start
   def messageLog = messageLogFactory.getMessageLog(message)
    
    if(messageLog != null){
        messageLog.addCustomHeaderProperty('Logging_50_errorStart', setTimeStamp() + 'Entering exception handling')
    }
   return message;
}

def Message callAlert(Message message) {
    //Logs Alert Handler call start
   def messageLog = messageLogFactory.getMessageLog(message)
    
    if(messageLog != null){
        messageLog.addCustomHeaderProperty('Logging_55_callAlertStart', setTimeStamp() + 'Calling IPF Alert Handler started')
    } 
    
   return message;
}


def Message callAlertEnd(Message message) {
   //Logs Alert Handler call end
   def messageLog = messageLogFactory.getMessageLog(message)
    
    if(messageLog != null){
        messageLog.addCustomHeaderProperty('Logging_60_callAlertEnd', setTimeStamp() + 'Calling IPF Alert Handler ended')
    }  
    errorEnd(message)
   return message;
}

def Message errorEnd(Message message) {
    //Logs error end
  def messageLog = messageLogFactory.getMessageLog(message)
    
    if(messageLog != null){
        messageLog.addCustomHeaderProperty('Logging_65_errorEnd', setTimeStamp() + 'Exception handling complete, interface ending')
    }
    
   return message;
}

def String setTimeStamp() {
    //Function to record datetime
    Date date = new Date()
    String datePart = date.format("yyyy-MM-dd")
    String timePart = date.format("HH:mm:ss.sss")
    return datePart + ' | ' + timePart + ' | '
}
