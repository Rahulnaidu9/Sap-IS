/* ==============================================

DESCRIPTION:

This Groovy script is used for checking if the subscription exists

INPUT PARAMETERS:
header.EH_iflowName : This header contains the name of the iflow

OUTPUT PARAMETERS:
property.EP_SubscriptionExists : This property is used to check whether a subscription for the iflow exists.

CHANGE HISTORY:

DATE             NAME                   CHANGE
2026-03-14      Kishore Eppalagudem     Initial script.

============================================= */

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper
def Message processData(Message message) {
    
    // get the iflow name of this retry message
    def map = message.getHeaders()
    def iflowName = map.get("EH_iflowName")

    // get and parse the AEM subscription descriptions response in JSON format
    def body = message.getBody(String) as String;
   
    
    def slurper = new JsonSlurper()
    def json = slurper.parseText(body)
    
    List<String> queueNames = []
    def data = json?.data
    if (data instanceof List) {
        data.each { item ->
            def qn = item?.queueName
            if (qn instanceof String && qn.trim()) {
                queueNames.add(qn as String)
            }
        }
    }

    // Store all queue names for visibility/troubleshooting
    message.setProperty("allQueueNames", queueNames.join(","))

    // Perform comparison
    boolean matched = false

    matched = queueNames.any { it == iflowName }
    
    message.setProperty("EP_SubscriptionExists", matched)
    
    return message;
}
