/* ==============================================
DESCRIPTION:
This Groovy script is used for logging the custom header - EH_corrid.
This correlates the iflows.
- original execution
- retried execution
- DLQ execution
- Alert framework email
INPUT PARAMETERS: 
EH_corrid: contains correlationid
OUTPUT PARAMETERS:
CHANGE HISTORY:
DATE            NAME                        CHANGE
2026-01-09      AnilKumar Navagana          Initial Script
============================================= */

import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    
	def messageLog = messageLogFactory.getMessageLog(message);
	
	map =  message.getHeaders();
	
	if(messageLog != null){
	    def crid =  map.get("EH_MplCorrelationId")
        if(crid!=null)
           messageLog.addCustomHeaderProperty("Corrid", crid);
			
			
	}
	return message;
}