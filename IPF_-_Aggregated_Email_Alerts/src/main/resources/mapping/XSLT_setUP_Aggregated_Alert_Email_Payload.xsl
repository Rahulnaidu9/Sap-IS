<!--
This XSLT combines the Email data received from the participating interfaces in aggeration process, received over the time period of aggregated time frame.

INPUT Format  - XML, Email as the root tag
OUTUT Format - HTML, the email format 

Email Format 
        Information about the iflow
				Failure of the interface, iflow name.
		Interface Details
				Aggregated infomation 
					- iflow name
					- FileName
					- Source System
					- Target System
					- Source Format
					- Target Format
					- From Date and Time
					- To Date and Time
					- Aggregate Alert Listening Period (mins)
					- Link to the iflow Monitor
					- Total Failure(s) in Period

		Summary Report
				- Error Message(s) information 
				- Number of failure(s)
				- Date and time of failure(s)

		Detailed Report
				Key 1	
				Key 2
				Key 3
				Key 4
				Key 5
				Message ID	
				Correlation ID	
				Error Message	
				Date and Time	
				SAP IS Monitor Link

    Date                    Developer                  Change Description
    2026-01-12              AnilKumar Navagana         Initial Script  
    2026-06-17              Ramani Jaddu              added fields in spanish
    2026-08-19              Niththila Mohanraj        Font and Colour changes 
    2026-08-28              Niththila Mohanraj        Width changes 
 
!-->

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"
	            indent="yes"/>
	<xsl:variable name="TotalMessages"
	              select="count(node()/node()/Email)"/>
	<xsl:variable name="interfaceName"
	              select="node()/node()/Email[1]/iFlowName"/>
	<xsl:variable name="Source"
	              select="node()/node()/Email[1]/Source"/>
	<xsl:variable name="Target"
	              select="node()/node()/Email[1]/Target"/>
	<xsl:variable name="SourceFormat"
	              select="node()/node()/Email[1]/SourceFormat"/>
	<xsl:variable name="TargetFormat"
	              select="node()/node()/Email[1]/TargetFormat"/>
	<xsl:variable name="iflowlink"
	              select="node()/node()/Email[1]/iFlowLink"/>
	<xsl:variable name="DateAndTime"
	              select="node()/node()/Email[1]/DateAndTime"/>
	<xsl:param name="EH_alert_dateTime"/>
	<xsl:param name="EP_toDateTime"/>
	<xsl:param name="EP_duration"/>
	<xsl:variable name="Key1_Value"
	              select="node()/node()/Email[1]/Key1_Value"/>
	<xsl:variable name="Key2_Value"
	              select="node()/node()/Email[1]/Key2_Value"/>
    <xsl:variable name="Key3_Value"
	              select="node()/node()/Email[1]/Key3_Value"/>
	<xsl:variable name="Key4_Value"
	              select="node()/node()/Email[1]/Key4_Value"/>
	<xsl:variable name="Key5_Value"
	              select="node()/node()/Email[1]/Key5_Value"/>

	<xsl:variable name="Key1_Name"
	              select="node()/node()/Email[1]/Key1_Name"/>
	<xsl:variable name="Key2_Name"
	              select="node()/node()/Email[1]/Key2_Name"/>
    <xsl:variable name="Key3_Name"
	              select="node()/node()/Email[1]/Key3_Name"/>
	<xsl:variable name="Key4_Name"
	              select="node()/node()/Email[1]/Key4_Name"/>
	<xsl:variable name="Key5_Name"
	              select="node()/node()/Email[1]/Key5_Name"/>



	<xsl:variable name="Key_Name"
	              select="node()/node()/Email[1]/Key_Name"/>
	              
	<!-- Bug found on 20th June 2023, problem with multiple key records...
	<xsl:variable name="Key_Value"
	              select="node()/node()/Email[1]/Key_Value"/>
-->
	<xsl:variable name="Key_Value" select="string-join(node()/node()/Email/Key_Value, ' , ')"/>
	<xsl:variable name="Key1New"
	              select="node()/node()/Email/Key1"/>
	<xsl:variable name="Key2New"
	              select="node()/node()/Email/Key2"/>
	              
	              
	<xsl:param name="tenantDomain"/>
	<xsl:param name="tenantName"/>
	
	<xsl:param name="EP_keyValueInformation"/>
	
	
	<xsl:template match="/">
		<p>Hi,</p>
		<p> </p>
		<p>iFlow <xsl:value-of select="$interfaceName"/> has failed on SAP Integration Suite (IS). Details are given below.</p>
		<!--Interface Details Table-->
		<p>
			<strong>Interface details </strong>
		</p>
		<p> </p>
		   <table style="border-collapse: collapse; width: 66.6667%;" border="1">
			<tbody>
				<tr>
					<td style="background-color: #4267B2;"><strong>iflow Name</strong></td>
					<td><xsl:value-of select="$interfaceName"/></td>
				</tr>
		        <tr>
					<td style="background-color: #4267B2;"><strong><xsl:value-of select="$Key_Name"></xsl:value-of></strong></td>
					<td><xsl:value-of select="$EP_keyValueInformation"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Source System</strong></td>
					<td><xsl:value-of select="$Source"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Target System</strong></td>
					<td><xsl:value-of select="$Target"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Source Format</strong></td>
					<td><xsl:value-of select="$SourceFormat"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Target Format</strong></td>
					<td><xsl:value-of select="$TargetFormat"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>From Date and Time</strong></td>
					<td><xsl:value-of select="$EH_alert_dateTime"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>To Date and Time</strong></td>
					<td><xsl:value-of select="$EP_toDateTime"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Aggregate Alert Listening Period (mins)</strong></td>
					<td><xsl:value-of select="$EP_duration"/></td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Link to the iflow Monitor</strong></td>
					<td>
					    <a>
                            <xsl:attribute name="href"><!-- create the href attribute -->
                                <xsl:value-of select="concat('https://',$tenantName,'.integrationsuite.',$tenantDomain,':443/shell/monitoring/Messages/%7B%22artifact%22:%22',$iflowlink,'%22%7D')"/>
                            </xsl:attribute>
                            <xsl:text>Link</xsl:text>
                        </a>
					</td>
				</tr>
				<tr>
					<td style="background-color: #4267B2;"><strong>Total Failure(s) in Period</strong></td>
					<td><xsl:value-of select="$TotalMessages"/></td>
				</tr>
			</tbody>
		</table>
		<p> </p>
		<!--Summary Report Table-->
		<p>
			<strong>Summary Report </strong>
		</p>
		<p> </p>
		<table style="border-collapse: collapse; width: 100%;" border="3">
			<tbody>
				    <tr style="background-color: #4267B2;">
				    <td><strong>Error Message(s)</strong></td>
					<td><strong>Number of Failure(s)</strong></td>
					<td><strong>Date and Time of First Failure</strong></td>
				    </tr>
				<xsl:for-each-group select="node()/node()/Email"
				                    group-by="ExceptionMessage">
					<tr>
						<td><xsl:value-of select="./ExceptionMessage"/></td>
						<td><xsl:value-of select="count(current-group())"/></td>
						<td><xsl:value-of select="./DateAndTime"/></td>
					</tr>
				</xsl:for-each-group>
			</tbody>
		</table>
		<p> </p>
		<!--Detailed Report Table-->
		<p>
			<strong>Detailed Report</strong>
		</p>
		<p> </p>
     <table style="border-collapse: collapse; width: 100%;" border="1">
			<tbody>
			     <tr style="background-color: #4267B2;">
					<td><strong><xsl:value-of select="$Key1_Name"></xsl:value-of></strong></td>
					<td><strong><xsl:value-of select="$Key2_Name"></xsl:value-of></strong></td>
					<td><strong><xsl:value-of select="$Key3_Name"></xsl:value-of></strong></td>
					<td><strong><xsl:value-of select="$Key4_Name"></xsl:value-of></strong></td>
					<td><strong><xsl:value-of select="$Key5_Name"></xsl:value-of></strong></td>
					<td><strong>Message ID</strong></td>
					<td><strong>Correlation ID</strong></td>
					<td><strong>Error Message</strong></td>
					<td><strong>Date and Time</strong></td>
					<td><strong>SAP IS Monitor Link</strong></td>
				</tr>
				
            <xsl:for-each select="node()/node()/Email">
                <tr>
                    <td><xsl:value-of select="./Key1_Value"/></td>
                    <td><xsl:value-of select="./Key2_Value"/></td>
                    <td><xsl:value-of select="./Key3_Value"/></td>
                    <td><xsl:value-of select="./Key4_Value"/></td>
                    <td><xsl:value-of select="./Key5_Value"/></td>
                    <td><xsl:value-of select="./MessageID"/></td>
                    <td><xsl:value-of select="./CorrelationID"/></td>
                    <td>
                        <xsl:value-of select="./ExceptionMessage"/>
                        <br/>
                        <h4>
                            &#x2022; In <xsl:value-of select="$Key_Name"/>, 
                            <xsl:value-of select="./Key_Value"/>
                        </h4>
                    </td>
                    <td><xsl:value-of select="./DateAndTime"/></td>
                    <td>
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="concat('https://',$tenantName,'.integrationsuite.',$tenantDomain,':443/shell/monitoring/MessageDetails/%7B%22messageGuid%22%3A%22',./MessageID,'%22%7D')"/>
                            </xsl:attribute>
                            Link
                        </a>
                    </td>
                </tr>
            </xsl:for-each>
			</tbody>
		</table>
		<p> </p>
		<p>***This is an automated email, please do not reply****</p>
		<p>---------------------------------------------------------------------------------------------------</p>
		
		<p>Hola,</p>
    <p> </p>
    <p>El iFlow <xsl:value-of select="$interfaceName"/> ha fallado en SAP Integration Suite (IS). Los detalles se muestran a continuación.</p>

    <!--Interface Details Table-->
    <p><strong>Detalles de la interfaz</strong></p>
    <p> </p>

    <table style="border-collapse: collapse; width: 66.6667%;" border="1">
        <tbody>
            <tr>
                <td style="background-color: #4267B2;"><strong>Nombre del iFlow</strong></td>
                <td><xsl:value-of select="$interfaceName"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;">
                    <strong><xsl:value-of select="$Key_Name"/></strong>
                </td>
                <td><xsl:value-of select="$EP_keyValueInformation"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Sistema de origen</strong></td>
                <td><xsl:value-of select="$Source"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Sistema destino</strong></td>
                <td><xsl:value-of select="$Target"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Formato de origen</strong></td>
                <td><xsl:value-of select="$SourceFormat"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Formato destino</strong></td>
                <td><xsl:value-of select="$TargetFormat"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Fecha y hora de inicio</strong></td>
                <td><xsl:value-of select="$EH_alert_dateTime"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Fecha y hora de fin</strong></td>
                <td><xsl:value-of select="$EP_toDateTime"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Periodo de alerta (minutos)</strong></td>
                <td><xsl:value-of select="$EP_duration"/></td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Enlace al monitor del iFlow</strong></td>
                <td>
                    <a>
                        <xsl:attribute name="href">
                            <xsl:value-of select="concat('https://',$tenantName,'.integrationsuite.',$tenantDomain,':443/shell/monitoring/Messages/%7B%22artifact%22:%22',$iflowlink,'%22%7D')"/>
                        </xsl:attribute>
                        Enlace
                    </a>
                </td>
            </tr>
            <tr>
                <td style="background-color: #4267B2;"><strong>Total de fallos en el período</strong></td>
                <td><xsl:value-of select="$TotalMessages"/></td>
            </tr>
        </tbody>
    </table>

    <p> </p>

    <!--Summary Report Table-->
    <p><strong>Resumen</strong></p>
    <p> </p>

    <table style="border-collapse: collapse; width: 100%;" border="3">
        <tbody>
            <tr style="background-color: #4267B2;">
                <td><strong>Mensajes de error</strong></td>
                <td><strong>Número de fallos</strong></td>
                <td><strong>Fecha y hora del primer fallo</strong></td>
            </tr>

            <xsl:for-each-group select="node()/node()/Email" group-by="ExceptionMessage">
                <tr>
                    <td><xsl:value-of select="./ExceptionMessage"/></td>
                    <td><xsl:value-of select="count(current-group())"/></td>
                    <td><xsl:value-of select="./DateAndTime"/></td>
                </tr>
            </xsl:for-each-group>
        </tbody>
    </table>

    <p> </p>

    <!--Detailed Report Table-->
    <p><strong>Reporte detallado</strong></p>
    <p> </p>

    <table style="border-collapse: collapse; width: 100%;" border="1">
        <tbody>
            <tr style="background-color: #4267B2;">
                <td><strong><xsl:value-of select="$Key1_Name"/></strong></td>
                <td><strong><xsl:value-of select="$Key2_Name"/></strong></td>
                <td><strong><xsl:value-of select="$Key3_Name"/></strong></td>
                <td><strong><xsl:value-of select="$Key4_Name"/></strong></td>
                <td><strong><xsl:value-of select="$Key5_Name"/></strong></td>
                <td><strong>ID de mensaje</strong></td>
                <td><strong>ID de correlación</strong></td>
                <td><strong>Mensaje de error</strong></td>
                <td><strong>Fecha y hora</strong></td>
                <td><strong>Enlace SAP IS</strong></td>
            </tr>

            <xsl:for-each select="node()/node()/Email">
                <tr>
                    <td><xsl:value-of select="./Key1_Value"/></td>
                    <td><xsl:value-of select="./Key2_Value"/></td>
                    <td><xsl:value-of select="./Key3_Value"/></td>
                    <td><xsl:value-of select="./Key4_Value"/></td>
                    <td><xsl:value-of select="./Key5_Value"/></td>
                    <td><xsl:value-of select="./MessageID"/></td>
                    <td><xsl:value-of select="./CorrelationID"/></td>
                    <td>
                        <xsl:value-of select="./ExceptionMessage"/>
                        <br/>
                        <h4>
                            &#x2022; En <xsl:value-of select="$Key_Name"/>, 
                            <xsl:value-of select="./Key_Value"/>
                        </h4>
                    </td>
                    <td><xsl:value-of select="./DateAndTime"/></td>
                    <td>
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="concat('https://',$tenantName,'.integrationsuite.',$tenantDomain,':443/shell/monitoring/MessageDetails/%7B%22messageGuid%22%3A%22',./MessageID,'%22%7D')"/>
                            </xsl:attribute>
                            Enlace
                        </a>
                    </td>
                </tr>
            </xsl:for-each>
        </tbody>
    </table>

    <p> </p>
    <p>***Este es un correo automático, por favor no responder****</p>
    <p> </p>
	</xsl:template>
</xsl:stylesheet>
