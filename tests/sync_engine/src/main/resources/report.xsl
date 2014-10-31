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
					align:
					center;
					border-collapse: collapse;
					}
					th {
					border: 1px;
					border-style:
					solid;
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
					.test_passed {
					background-color: #C5D88A;
					text-align:
					center;
					border-color: black;
					}
					.test_failed {
					background-color:
					#D88A8A;
					text-align: center;
					border-color: black;
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
					<table style="border: none;">
						<tr style="border: none;">
							<td style="border: none;">
								<h1>Test results</h1>
							</td>
						</tr>
						<tr>
							<td>
								<table style="border: none;">
									<tr>
										<th style="width: 40%;">&#160;</th>
										<th style="width: 20%;">iOS</th>
										<th style="width: 20%;">OSX</th>
										<th style="width: 20%;">Android</th>
									</tr>
									<tr>
										<td style="text-align: left; width: 40%;">&#160;&#160;&#160;Client started and work stable
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isIosStable = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isOsxStable = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isAndroidStable = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
									<tr>
										<td style="text-align: left; width: 40%;">&#160;&#160;&#160;Messages received</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isIosReceiveMessages = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isOsxReceiveMessages = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isAndroidReceiveMessages = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>




									<tr>

										<td style="text-align: left; width: 40%;">&#160;&#160;&#160;Messages received in &lt; 5 sec
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isIosReceiveMessagesInTime = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isOsxReceiveMessagesInTime = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when
													test="ReportData/isAndroidReceiveMessagesInTime = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
									<tr>

										<td style="text-align: left; width: 40%;">&#160;&#160;&#160;Messages are in correct order
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isIosMessagesOrderCorrect = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when test="ReportData/isOsxMessagesOrderCorrect = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td style="width: 20%;">
											<xsl:choose>
												<xsl:when
													test="ReportData/isAndroidMessagesOrderCorrect = 'true'">
													<xsl:attribute name="class">test_passed</xsl:attribute>
													<b>PASSED</b>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">test_failed</xsl:attribute>
													<b>FAILED</b>
												</xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr style="border: none;">
							<td style="border: none;">
								<br/><h1>Chat users</h1>
							</td>
						</tr>
						<tr>
							<td>
								<table>
									<tr>
										<th>Platform</th>
										<th>Username</th>
										<th>Startup time</th>
									</tr>
									<xsl:for-each select="ReportData/users/UserInfo">
										<xsl:choose>
											<xsl:when test="isEnabled = 'true'">
												<tr>
													<td style="width: 25%;">
														<xsl:value-of select="loggedOnPlatform" />
													</td>
													<td style="width: 60%;">
														<xsl:value-of select="name" />
													</td>
													<td style="width: 15%;">
														<xsl:value-of select="startupTime" />
													</td>
												</tr>
											</xsl:when>
											<xsl:otherwise>
												<tr>
													<td>
														<xsl:value-of select="loggedOnPlatform" />
													</td>
													<td colspan="4">Disabled</td>
												</tr>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</table>
							</td>
						</tr>
						<tr style="border: none;">
							<td style="border: none;">
								<br/><h1>Device and client information</h1>
							</td>
						</tr>
						<tr>
							<td>
								<ul>
									<xsl:for-each select="ReportData/users/UserInfo">
										<xsl:choose>
											<xsl:when test="isEnabled = 'true'">
												<li>
													<h3 style="margin-bottom: 0px;"><xsl:value-of select="loggedOnPlatform" />:</h3>
						
													<ul>
													<xsl:if test="loggedOnPlatform != 'Mac'"><li><b>Device</b> -&#160;<xsl:value-of select="deviceData/deviceName" /></li></xsl:if>
													<li><b>OS version</b> -&#160;<xsl:value-of select="deviceData/operatingSystemName" />&#160;<xsl:value-of select="deviceData/operatingSystemBuild" /></li>
													<xsl:if test="loggedOnPlatform = 'Android'">
													<li><b>Network type</b> -
														<xsl:choose>
														<xsl:when test="deviceData/isWifiEnabled = 'true'">
														WiFi&#160;<xsl:choose><xsl:when test="loggedOnPlatform != 'Mac'">(Mobile network type: <xsl:value-of select="deviceData/gsmNetworkType" />)</xsl:when>
														</xsl:choose>
														</xsl:when>
														<xsl:otherwise><xsl:value-of select="deviceData/gsmNetworkType" />
														</xsl:otherwise>
														</xsl:choose></li></xsl:if>
													<li><b>Client</b> -&#160;<xsl:value-of select="buildVersion/clientBuildNumber" />&#160;(<i>SyncEngine</i> - <xsl:value-of select="buildVersion/zmessagingBuildNumber" />)</li>
													</ul>
												</li>
											</xsl:when>
											<xsl:otherwise>
												<tr>
													<td>
														<xsl:value-of select="loggedOnPlatform" />
													</td>
													<td colspan="4">Disabled</td>
												</tr>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</ul>
							</td>
						</tr>
						<tr style="border: none;">
							<td style="border: none;"><br/>
								<h1>Messages statistics</h1>
							</td>
						</tr>
						<tr>
							<td>
								<table>
									<tr>
										<th colspan="2">&#160;</th>
										<th colspan="3">Receive time (acceptable &lt; 5s)</th>
									</tr>
									<tr>
										<th>Message</th>
										<th>Sent from</th>
										<th>
											<xsl:choose>
												<xsl:when test="ReportData/isIosStable = 'true'">
													iOS
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">time_failed</xsl:attribute>
													iOS
													<br />
													(unstable)
												</xsl:otherwise>
											</xsl:choose>
										</th>
										<th>
											<xsl:choose>
												<xsl:when test="ReportData/isOsxStable = 'true'">
													OSX
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">time_failed</xsl:attribute>
													OSX
													<br />
													(unstable)
												</xsl:otherwise>
											</xsl:choose>
										</th>
										<th>
											<xsl:choose>
												<xsl:when test="ReportData/isAndroidStable = 'true'">
													Android
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">time_failed</xsl:attribute>
													Android
													<br />
													(unstable)
												</xsl:otherwise>
											</xsl:choose>
										</th>
									</tr>

									<xsl:for-each select="ReportData/messages/MessageReport">
										<xsl:if test="checkTime = 'true'">
											<tr>
												<td style="width: 46%;">
													<xsl:value-of select="message" />
												</td>
												<td style="width: 15%;">
													<xsl:value-of select="sentFrom" />
												</td>
												<td style="width: 13%;">

													<xsl:choose>
														<xsl:when test="/ReportData/isIosStable = 'true'">
															<xsl:choose>
																<xsl:when
																	test="isIosReceiveTimeOK = 'true' and iosReceiveTime = '-1'">
																	<xsl:attribute name="class">time_gray</xsl:attribute>
																	-
																</xsl:when>
																<xsl:when test="isIosReceiveTimeOK = 'true'">
																	<xsl:attribute name="class">time_passed</xsl:attribute>
																	<xsl:value-of select="iosReceiveTime" />
																</xsl:when>
																<xsl:otherwise>
																	<xsl:attribute name="class">time_failed</xsl:attribute>
																	<xsl:value-of select="iosReceiveTime" />
																</xsl:otherwise>
															</xsl:choose>
														</xsl:when>
														<xsl:otherwise>
															<xsl:attribute name="class">time_gray</xsl:attribute>
															-
														</xsl:otherwise>
													</xsl:choose>

												</td>
												<td style="width: 13%;">
													<xsl:choose>
														<xsl:when test="/ReportData/isOsxStable = 'true'">
															<xsl:choose>


																<xsl:when
																	test="isOsxReceiveTimeOK = 'true' and osxReceiveTime = '-1'">
																	<xsl:attribute name="class">time_gray</xsl:attribute>
																	-
																</xsl:when>
																<xsl:when test="isOsxReceiveTimeOK = 'true'">
																	<xsl:attribute name="class">time_passed</xsl:attribute>
																	<xsl:value-of select="osxReceiveTime" />
																</xsl:when>
																<xsl:otherwise>
																	<xsl:attribute name="class">time_failed</xsl:attribute>
																	<xsl:value-of select="osxReceiveTime" />
																</xsl:otherwise>
															</xsl:choose>
														</xsl:when>
														<xsl:otherwise>
															<xsl:attribute name="class">time_gray</xsl:attribute>
															-
														</xsl:otherwise>
													</xsl:choose>
												</td>
												<td style="width: 13%;">
													<xsl:choose>
														<xsl:when test="/ReportData/isAndroidStable = 'true'">
															<xsl:choose>
																<xsl:when
																	test="isAndroidReceiveTimeOK = 'true' and androidReceiveTime = '-1'">
																	<xsl:attribute name="class">time_gray</xsl:attribute>
																	-
																</xsl:when>
																<xsl:when test="isAndroidReceiveTimeOK = 'true'">
																	<xsl:attribute name="class">time_passed</xsl:attribute>
																	<xsl:value-of select="androidReceiveTime" />
																</xsl:when>
																<xsl:otherwise>
																	<xsl:attribute name="class">time_failed</xsl:attribute>
																	<xsl:value-of select="androidReceiveTime" />
																</xsl:otherwise>
															</xsl:choose>
														</xsl:when>
														<xsl:otherwise>
															<xsl:attribute name="class">time_gray</xsl:attribute>
															-
														</xsl:otherwise>
													</xsl:choose>

												</td>
											</tr>
										</xsl:if>
									</xsl:for-each>
									<tr>
										<td colspan="2" style="text-align: right; font-weight: bold;">Average time:&#160;&#160;</td>
										<td style="width: 13%;">
											<xsl:attribute name="class">time_average</xsl:attribute>
											<xsl:value-of select="ReportData/averageIosReceiveTime" />
											s
										</td>
										<td style="width: 13%;">
											<xsl:attribute name="class">time_average</xsl:attribute>
											<xsl:value-of select="ReportData/averageOsxReceiveTime" />
											s
										</td>
										<td style="width: 13%;">
											<xsl:attribute name="class">time_average</xsl:attribute>
											<xsl:value-of select="ReportData/averageAndroidReceiveTime" />
											s
										</td>

									</tr>
								</table>
							</td>
						</tr>
					</table>
				</center>
				<center>
					<tr>
						<td>
							<h1>Messages list (for order check)</h1>
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<th style="width: 25%;">Sending order</th>
									<th style="width: 25%;">iOS</th>
									<th style="width: 25%;">OSX</th>
									<th style="width: 25%;">Android</th>
								</tr>

								<tr>
									<td style="width: 25%;">
										<table>
											<xsl:for-each select="ReportData/messages/MessageReport">
												<tr>
													<td>
														<xsl:value-of select="message" />
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</td>
									<td style="width: 25%;">
										<table>
											<xsl:for-each select="ReportData/iosMessages/string">
												<tr>
													<td>
														<xsl:value-of select="." />
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</td>
									<td style="width: 25%;">
										<table>
											<xsl:for-each select="ReportData/osxMessages/string">
												<tr>
													<td>
														<xsl:value-of select="." />
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</td>
									<td style="width: 25%;">
										<table>
											<xsl:for-each select="ReportData/androidMessages/string">
												<tr>
													<td>
														<xsl:value-of select="." />
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</center>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>