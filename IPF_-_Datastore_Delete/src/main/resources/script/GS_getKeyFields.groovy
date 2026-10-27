import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

def Message processData(Message message) {

    def body = message.getBody(String)
    def dataStoreProp = message.getProperty("EP_DataStoreName") as String

    def xml = new XmlSlurper().parseText(body)

    boolean found = false

    xml.entry.each { entry ->

        def props = entry.content.children().find {
            it.name().toString().contains('properties')
        }

        if (props) {

            def dsName = props.children().find {
                it.name().toString().contains('DataStoreName')
            }?.text()

            if (dsName == dataStoreProp) {

                message.setHeader("DataStoreName", dsName)

                message.setHeader(
                    "IntegrationFlow",
                    props.children().find {
                        it.name().toString().contains('IntegrationFlow')
                    }?.text() ?: ""
                )

                message.setHeader(
                    "Type",
                    props.children().find {
                        it.name().toString().contains('Type')
                    }?.text() ?: ""
                )

                message.setHeader("DataStoreFound", "Yes")

                found = true
            }
        }
    }

    if (!found) {
        message.setHeader("DataStoreFound", "No")
    }

    return message
}