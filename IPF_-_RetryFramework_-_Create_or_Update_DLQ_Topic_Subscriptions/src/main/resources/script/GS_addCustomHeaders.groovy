/* ==============================================

DESCRIPTION:

This Groovy script is used for adding custom headers

INPUT PARAMETERS:


OUTPUT PARAMETERS:


CHANGE HISTORY:

DATE             NAME                   CHANGE

2021-10-11      Kishore Eppalagudem     Initial script
2022-03-23      Kishore Eppalagudem     Updated front matter. Renamed custom headers

============================================= */

import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {
    
    def retryEnvelopeText = message.getBody(String.class)
    def root = new XmlSlurper().parseText(retryEnvelopeText)
    def retryEnvelopeNode = root;
    
    
    def messageLog = messageLogFactory.getMessageLog(message);
    messageLog.addCustomHeaderProperty("Corrid", retryEnvelopeNode.correlationId.text())
    messageLog.addCustomHeaderProperty("Retry_AttemptNum", retryEnvelopeNode.attemptNum.text())
    messageLog.addCustomHeaderProperty("Retry_LastError", retryEnvelopeNode.lastError.text())
    messageLog.addCustomHeaderProperty("Retry_MaxRetries", retryEnvelopeNode.maxRetries.text())
    messageLog.addCustomHeaderProperty("Retry_ManualRetry", retryEnvelopeNode.isManuallyRetried.text())
    
    return message;
}
