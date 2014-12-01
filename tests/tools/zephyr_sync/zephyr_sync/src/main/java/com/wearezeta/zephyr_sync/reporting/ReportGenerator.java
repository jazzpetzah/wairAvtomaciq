package com.wearezeta.zephyr_sync.reporting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class ReportGenerator {

	private ReportGenerator() {
		// TODO Auto-generated constructor stub
	}

	public static void generate(ReportModel model, String dstPath)
			throws IOException {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("index.template");
		FileWriter writer = new FileWriter(dstPath);
		try {
			mustache.execute(new BufferedWriter(writer), model).flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
