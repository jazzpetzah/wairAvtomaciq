package com.wearezeta.auto.sync.report;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.report.data.ReportData;

public class ReportGenerator {
	public static void generate(ReportData data) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();

		Source xslDoc = new StreamSource(
				ReportGenerator.class.getResourceAsStream("/report.xsl"));
		Source xmlDoc = new StreamSource(new ByteArrayInputStream(ReportData
				.toXml(data).getBytes()));

		String outputFileName = SyncEngineUtil
				.getAcceptanceReportPathFromConfig(ReportGenerator.class);

		OutputStream htmlFile;
		try {
			htmlFile = new FileOutputStream(outputFileName);

			Transformer transform;
			transform = tFactory.newTransformer(xslDoc);

			transform.transform(xmlDoc, new StreamResult(htmlFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
