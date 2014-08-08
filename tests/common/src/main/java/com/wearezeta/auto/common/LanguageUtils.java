package com.wearezeta.auto.common;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LanguageUtils {
	public static final String ENGLISH_LANG_NAME = "english";

	
	public LanguageUtils() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * BEGIN: For parsing the XML files of other languages and inputting them in
	 * the character test as per multiple iOS user stories Currently only being
	 * used for the English language
	 */
	private static String getLanguageAlphabet(String languageName)
			throws Throwable {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setValidating(false);
		docBuilderFactory.setNamespaceAware(true);
		docBuilderFactory.setFeature("http://xml.org/sax/features/namespaces",
				false);
		docBuilderFactory.setFeature("http://xml.org/sax/features/validation",
				false);
		docBuilderFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
		docBuilderFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		InputStream languageFileStream = null;
		String languageFilePath = String.format("/LanguageFiles/%s.xml",
				languageName.toLowerCase());
		try {
			URL languageFile = CommonUtils.class.getResource(languageFilePath);
			languageFileStream = languageFile.openStream();

			if (languageFileStream == null) {
				throw new Exception(String.format(
						"Failed to load %s from resources", languageFilePath));
			}
			Document doc = docBuilder.parse(languageFileStream);
			doc.getDocumentElement().normalize();
			NodeList characterTypes = doc
					.getElementsByTagName("exemplarCharacters");
			StringBuilder alphabet = new StringBuilder(100);
			for (int i = 0; i < characterTypes.getLength(); i++) {
				Node firstTypeNode = characterTypes.item(i);
				if (firstTypeNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstTypeNode;

					if (null != firstElement.getAttribute("type")) {
						if (firstElement.getAttribute("type").equals(
								"auxiliary")) {
							continue;
						}
					}
					NodeList textFNList = firstElement.getChildNodes();
					String characters = ((Node) textFNList.item(0))
							.getNodeValue().trim();
					String[] charactersArr = characters.replaceAll("^\\[|\\]$",
							"").split("\\s");
					for (String chr : charactersArr) {
						if (chr.length() == 6
								&& chr.substring(0, 2).equals("\\u")) {
							int c = Integer.parseInt(chr.substring(2, 6), 16);
							alphabet.append(Character.toString((char) c));
						} else {
							alphabet.append(chr);
						}
					}
				}

			}
			return alphabet.toString();
		} finally {
			if (languageFileStream != null) {
				languageFileStream.close();
			}
		}
	}

	public static List<String> getUnicodeStringAsCharList(String str) {
		List<String> characters = new ArrayList<String>();
		Pattern pat = Pattern.compile("\\p{L}\\p{M}*|\\W");
		Matcher matcher = pat.matcher(str);
		while (matcher.find()) {
			characters.add(matcher.group());
		}
		return characters;
	}

	public static String generateRandomString(int len, String languageName)
			throws Throwable {
		String alphabet = getLanguageAlphabet(languageName);
		List<String> characters = getUnicodeStringAsCharList(alphabet);
		// Appium does not type characters beyond standard ASCII set, we have to
		// cut those characters
		if (languageName.toLowerCase().equals(ENGLISH_LANG_NAME)) {
			List<String> ascii_characters = new ArrayList<String>();
			for (String chr : characters) {
				if (chr.length() == 1 && (int) chr.charAt(0) <= 127) {
					ascii_characters.add(chr);
				}
			}
			characters = ascii_characters;
		}
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(characters.get(rnd.nextInt(characters.size())));
		}
		return sb.toString();
	}
}
