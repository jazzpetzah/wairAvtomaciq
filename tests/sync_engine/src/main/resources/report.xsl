<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<head>
<title>Sync Engine</title>
<style>
body {
font: 14pt verdana;
background-color: #EEEEEE;
}
h1 {
font: 16pt verdana;
font-weight: bold;
}
table, tr, td {
width: 100%;
font: 10pt verdana;
border: 1px;
border-style: solid;
border-collapse: collapse;
}
.no_border_table {
width: 60%;
align: center;
border-collapse: collapse;
}
th {
border: 1px;
border-style: solid;
border-collapse: collapse;
}
.no_border_table tr {
border:none;
}
.no_border_table td {
border:none;
}
.no_border_table th {
border:none;
}
.time_passed {
background-color: #C5D88A;
text-align: center;
}
.time_failed {
background-color: #D88A8A;
text-align: center;
}
.time_gray {
background-color: #AAAAAA;
text-align: center;
}
.time_average {
text-align: center;
}
</style>
</head>
<body>
<center style="width: 55%;">
<table>
<tr><td><h1>Test results</h1></td></tr>
<tr><td>
<table style="border: none;">
<tr><td style="text-align: left;">
	<xsl:choose>
		<xsl:when test="ReportData/areClientsStable = 'true'">
		   <xsl:attribute name="class">time_passed</xsl:attribute>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:attribute name="class">time_failed</xsl:attribute>
		</xsl:otherwise>
	</xsl:choose>
    &#160;&#160;&#160;All clients started -
    <xsl:choose>
		<xsl:when test="ReportData/areClientsStable = 'true'"><b>PASSED</b></xsl:when>
		<xsl:otherwise><b>FAILED</b></xsl:otherwise>
	</xsl:choose>
</td></tr>
<tr><td style="text-align: left;">
	<xsl:choose>
		<xsl:when test="ReportData/areMessagesReceived = 'true'">
		   <xsl:attribute name="class">time_passed</xsl:attribute>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:attribute name="class">time_failed</xsl:attribute>
		</xsl:otherwise>
	</xsl:choose>
    &#160;&#160;&#160;All messages received -
    <xsl:choose>
		<xsl:when test="ReportData/areMessagesReceived = 'true'"><b>PASSED</b></xsl:when>
		<xsl:otherwise><b>FAILED</b></xsl:otherwise>
	</xsl:choose>
</td></tr>
<tr><td style="text-align: left;">
	<xsl:choose>
		<xsl:when test="ReportData/areMessagesReceiveTimeCorrect = 'true'">
		   <xsl:attribute name="class">time_passed</xsl:attribute>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:attribute name="class">time_failed</xsl:attribute>
		</xsl:otherwise>
	</xsl:choose>
    &#160;&#160;&#160;All messages received in &lt; 5 sec -
    <xsl:choose>
		<xsl:when test="ReportData/areMessagesReceiveTimeCorrect = 'true'"><b>PASSED</b></xsl:when>
		<xsl:otherwise><b>FAILED</b></xsl:otherwise>
	</xsl:choose>
</td></tr>
<tr><td style="text-align: left;">
	<xsl:choose>
		<xsl:when test="ReportData/areMessagesOrderCorrect = 'true'">
		   <xsl:attribute name="class">time_passed</xsl:attribute>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:attribute name="class">time_failed</xsl:attribute>
		</xsl:otherwise>
	</xsl:choose>
    &#160;&#160;&#160;All messages are in correct order - 
    <xsl:choose>
		<xsl:when test="ReportData/areMessagesOrderCorrect = 'true'"><b>PASSED</b></xsl:when>
		<xsl:otherwise><b>FAILED</b></xsl:otherwise>
	</xsl:choose>
</td></tr>
</table>
</td></tr>

<tr><td><h1>Chat users</h1></td></tr>
<tr><td><table>
  <tr>
    <th>Platform</th>
    <th>Username</th>
    <th>Startup time</th>
  </tr>
  <xsl:for-each select="ReportData/users/UserInfo">
    <xsl:choose>
      <xsl:when test="isEnabled = 'true'">
        <tr>
          <td style="width: 25%;"><xsl:value-of select="loggedOnPlatform"/></td>
          <td style="width: 60%;"><xsl:value-of select="name"/></td>
          <td style="width: 15%;"><xsl:value-of select="startupTime"/></td>
        </tr>
      </xsl:when>
      <xsl:otherwise>
        <tr><td><xsl:value-of select="loggedOnPlatform"/></td><td colspan="4">Disabled</td></tr>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
</table></td></tr>

<tr><td><h1>Messages statistics</h1></td></tr>
<tr><td><table>
   <tr>
     <th colspan="2">&#160;</th><th colspan="3">Receive time (acceptable &lt; 5s)</th>
    </tr>
    <tr>
      <th>Message</th>
      <th>Sent from</th>
      <th>iOS</th>
      <th>OSX</th>
      <th>Android</th>
    </tr>
  <xsl:for-each select="ReportData/messages/MessageReport">
  <tr>
          <td style="width: 46%;"><xsl:value-of select="message"/></td>
          <td style="width: 15%;"><xsl:value-of select="sentFrom"/></td>
          <td style="width: 13%;">
          	<xsl:choose>
                  <xsl:when test="isIosReceiveTimeOK = 'true' and iosReceiveTime = '-1'">
                    <xsl:attribute name="class">time_gray</xsl:attribute>
                    -
                  </xsl:when>
                  <xsl:when test="isIosReceiveTimeOK = 'true'">
                    <xsl:attribute name="class">time_passed</xsl:attribute>
                    <xsl:value-of  select="iosReceiveTime"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="class">time_failed</xsl:attribute>
                    <xsl:value-of  select="iosReceiveTime"/>
                  </xsl:otherwise>
                </xsl:choose>
          </td>
          <td style="width: 13%;">
          	<xsl:choose>
                  <xsl:when test="isOsxReceiveTimeOK = 'true' and osxReceiveTime = '-1'">
                    <xsl:attribute name="class">time_gray</xsl:attribute>
                    -
                  </xsl:when>
                  <xsl:when test="isOsxReceiveTimeOK = 'true'">
                    <xsl:attribute name="class">time_passed</xsl:attribute>
                    <xsl:value-of  select="osxReceiveTime"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="class">time_failed</xsl:attribute>
                    <xsl:value-of  select="osxReceiveTime"/>
                  </xsl:otherwise>
                </xsl:choose>
          </td>
          <td style="width: 13%;">
          	<xsl:choose>
                  <xsl:when test="isAndroidReceiveTimeOK = 'true' and androidReceiveTime = '-1'">
                    <xsl:attribute name="class">time_gray</xsl:attribute>
                    -
                  </xsl:when>
                  <xsl:when test="isAndroidReceiveTimeOK = 'true'">
                    <xsl:attribute name="class">time_passed</xsl:attribute>
                    <xsl:value-of  select="androidReceiveTime"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="class">time_failed</xsl:attribute>
                    <xsl:value-of  select="androidReceiveTime"/>
                  </xsl:otherwise>
                </xsl:choose>
          </td>
  </tr>
  </xsl:for-each>
  <tr>
  	<td colspan="2" style="text-align: right; font-weight: bold;">Average time:&#160;&#160;</td>
  	<td style="width: 13%;">
  		<xsl:attribute name="class">time_average</xsl:attribute>
  		<xsl:value-of select="ReportData/averageIosReceiveTime" />s</td>
  	<td style="width: 13%;">
  		<xsl:attribute name="class">time_average</xsl:attribute>
  		<xsl:value-of select="ReportData/averageOsxReceiveTime" />s</td>
  	<td style="width: 13%;">
  		<xsl:attribute name="class">time_average</xsl:attribute>
  		<xsl:value-of select="ReportData/averageAndroidReceiveTime" />s</td>
  	
  </tr>
</table></td></tr>
</table>
</center>
<center>
<tr><td><h1>Messages list (for order check)</h1></td></tr>
<tr><td><table>
<tr>
    <th style="width: 25%;">Sending order</th>
    <th style="width: 25%;">iOS</th>
    <th style="width: 25%;">OSX</th>
    <th style="width: 25%;">Android</th>
  </tr>

	<tr><td style="width: 25%;">
	<table>
	<xsl:for-each select="ReportData/messages/MessageReport">
		<tr><td><xsl:value-of select="message"/></td></tr>
	</xsl:for-each></table></td>
	<td style="width: 25%;"><table><xsl:for-each select="ReportData/iosMessages/string">
		<tr><td><xsl:value-of select="."/></td></tr>
	</xsl:for-each></table></td>
	<td style="width: 25%;"><table><xsl:for-each select="ReportData/osxMessages/string">
		<tr><td><xsl:value-of select="."/></td></tr>
	</xsl:for-each></table></td>
	<td style="width: 25%;"><table><xsl:for-each select="ReportData/androidMessages/string">
		<tr><td><xsl:value-of select="."/></td></tr>
	</xsl:for-each></table></td></tr>
</table></td></tr>
</center>
</body>
</html>
</xsl:template>
</xsl:stylesheet>