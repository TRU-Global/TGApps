package com.truglobal.pageobjectmodel;

/**
 * @author Kumara Swamy
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.opencsv.CSVWriter;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CSVGenerator {

	public static String testURL = null;

	public static void main(String[] args) throws IOException {
		WebDriverManager.chromedriver().setup();
		String folderpath = "C:\\Users\\TG1698\\Downloads\\testing";

		File file = new File(folderpath);
		List<String> fileNames = new ArrayList<>();
		Map<String, List<String>> allPageObjectsMethodNames = new HashMap<>();
		List<String> testCaseDataHeadersList = new ArrayList<>();
		List<String> testCaseDataRows = new ArrayList<>();
		Map<String, List<String[]>> allPagesCSVRows = new HashMap<>();
		Map<String, List<List<String>>> allPagesPageObjects = new HashMap<>();
		Map<String, List<List<String>>> scriptOrderOfExecution = new HashMap<>();
		for (int k = 0; k < file.listFiles().length; k++) {
			File eachFile = file.listFiles()[k];
			String fileName = eachFile.getName();
			String filepath = eachFile.getPath();

			fileName = fileName.substring(0, fileName.indexOf(".html")).replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
			fileNames.add(fileName);

			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			ChromeDriver driver = new ChromeDriver(options);
			JavascriptExecutor js = driver;

			driver.get(filepath);

			String script = readFile(CSVGenerator.class.getClassLoader().getResource("GetCommandTargetValueFromHTML.js").getPath());
			List<List<Object>> commandTargetValuesList = (List<List<Object>>) js.executeScript(script);
			driver.quit();

			String[] header = { "variableName", "default", "id", "css", "name", "linkText", "partialLinkText",
					"className", "xpath" };
			List<String> headersList = new ArrayList<>();
			Collections.addAll(headersList, header);

			for (List<Object> commandTargetValue : commandTargetValuesList) {
				List<List<String>> dataForPageObject = new ArrayList<>();
				List<String> pageObject = new ArrayList<>();
				String pageName = commandTargetValue.get(4).toString();
				try {
					pageName = String.valueOf(pageName.charAt(0)).toUpperCase() + pageName.substring(1);
				} catch (Exception e) {
				}
				pageObject.add(commandTargetValue.get(3).toString());
				pageObject.add(commandTargetValue.get(0).toString());
				pageObject.add(commandTargetValue.get(1).toString());
				pageObject.add(commandTargetValue.get(2).toString());
				pageObject.add(pageName);

				if (commandTargetValue.get(0).equals("open")) {
					String target = commandTargetValue.get(1).toString().substring(1,
							commandTargetValue.get(1).toString().length() - 1);
					List<String> targetArray = Arrays.asList(target.split(","));
					testURL = targetArray.get(targetArray.size() - 1).trim();
					continue;
				}
				System.out.println(commandTargetValue.get(0));
				System.out.println(commandTargetValue.get(1));
				System.out.println(commandTargetValue.get(2));
				System.out.println(commandTargetValue.get(3));
				System.out.println(pageName);
				System.out.println("----------------------------------------------------------");

				List<String> row = new ArrayList<>();
				List<String> multipleLocators = (List<String>) commandTargetValue.get(1);

				row.add(commandTargetValue.get(3).toString());
				row.add("xpath");
				for (int i = 0; i < 7; i++) {
					row.add("");
				}
				for (int i = multipleLocators.size() - 1; i >= 0; i--) {
					String eachLocator = multipleLocators.get(i);
					if (eachLocator != null && !eachLocator.equals("")) {
						if (eachLocator.startsWith("//")) {
							row.add(8, eachLocator);
						} else if (eachLocator.startsWith("id=")) {
							row.remove(2);
							row.add(2, eachLocator.substring(3));
						} else if (eachLocator.startsWith("css=")) {
							row.remove(3);
							row.add(3, eachLocator.substring(4));
						} else if (eachLocator.startsWith("name=")) {
							row.remove(4);
							row.add(4, eachLocator.substring(5));
						} else if (eachLocator.startsWith("xpath=")) {
							row.add(8, eachLocator.substring(6));
						}
					}
				}
				System.out.println(row);
				List<String[]> allCSVrows = new ArrayList<>();
				if (allPagesCSVRows.containsKey(pageName)) {
					allCSVrows = allPagesCSVRows.get(pageName);
				}
				allCSVrows.add(row.toArray(new String[row.size()]));
				allPagesCSVRows.put(pageName, allCSVrows);

				if (allPagesPageObjects.containsKey(pageName)) {
					dataForPageObject = allPagesPageObjects.get(pageName);
				}
				dataForPageObject.add(pageObject);
				allPagesPageObjects.put(pageName, dataForPageObject);

				if (scriptOrderOfExecution.containsKey(fileName)) {
					dataForPageObject = scriptOrderOfExecution.get(fileName);
				} else {
					dataForPageObject = new ArrayList<>();
				}
				dataForPageObject.add(pageObject);
				scriptOrderOfExecution.put(fileName, dataForPageObject);
			}
		}

		for (Entry<String, List<String[]>> eachPageCSVRows : allPagesCSVRows.entrySet()) {
			String fileName = eachPageCSVRows.getKey();
			List<String[]> allCSVrows = eachPageCSVRows.getValue();
			String[] header = { "variableName", "default", "id", "css", "name", "linkText", "partialLinkText",
					"className", "xpath" };
			List<String> headersList = new ArrayList<>();
			Collections.addAll(headersList, header);
			try (CSVWriter writer = new CSVWriter(
					new FileWriter(new File("..\\TRUGlobalAutomation\\src\\main\\resources\\" + fileName + ".csv")))) {
				int maxCount = 0;
				for (String[] eachRow : allCSVrows) {
					if (eachRow.length > maxCount) {
						maxCount = eachRow.length;
					}
				}

				for (int i = 1; i <= maxCount; i++) {
					headersList.add("xpath" + i);
				}

				allCSVrows.add(0, headersList.toArray(new String[headersList.size()]));
				writer.writeAll(allCSVrows);
			}
		}

		for (Entry<String, List<List<String>>> eachDataForPageObject : allPagesPageObjects.entrySet()) {
			List<List<String>> dataForPageObject = eachDataForPageObject.getValue();
			for (List<String> eachList : dataForPageObject) {
				if (eachList.get(3) != null && !eachList.get(3).equals("")) {
					String header = eachList.get(4) + "_" + eachList.get(0);
					if (testCaseDataHeadersList.indexOf(header) == -1) {
						testCaseDataHeadersList.add(eachList.get(4) + "_" + eachList.get(0));
						testCaseDataRows.add(eachList.get(3));
						eachList.add(eachList.get(4) + "_" + eachList.get(0));
					} else {
						eachList.add("");
					}
				} else {
					eachList.add("");
				}
			}
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(new File("..\\TRUGlobalAutomation\\src\\main\\resources\\TestCaseData_1.csv")))) {
			writer.writeNext(testCaseDataHeadersList.toArray(new String[testCaseDataHeadersList.size()]));
			writer.writeNext(testCaseDataRows.toArray(new String[testCaseDataRows.size()]));
		}

		for (Entry<String, List<List<String>>> eachDataForPageObject : allPagesPageObjects.entrySet()) {
			List<List<String>> dataForPageObject = eachDataForPageObject.getValue();
			String pageName = eachDataForPageObject.getKey();
			PageObjectGenerator generator = new PageObjectGenerator();
			List<String> methodNames = generator.generatePageObject(dataForPageObject, pageName);
			allPageObjectsMethodNames.put(pageName, methodNames);
		}
		File file1 = new File("..\\TRUGlobalAutomation\\src\\main\\java\\com\\truglobal\\pageobjectmodel\\.java");
		if (file1.exists()) {
			file1.delete();
		}
		StepDefinitionGenerator stepGenerator = new StepDefinitionGenerator();
		stepGenerator.generateStepDefinition(allPageObjectsMethodNames, scriptOrderOfExecution, fileNames);
	}

	public static String readFile(String filename) throws IOException {
		StringBuilder script = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				script.append(line);
			}
		}
		return script.toString();
	}

}
