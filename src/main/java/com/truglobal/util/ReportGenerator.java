package com.truglobal.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class ReportGenerator {

	public static void main(String[] args) throws IOException, ParseException, CsvException {
		String filename = "C:\\Users\\admin\\git\\TRUGlobalAutomation\\TRUGlobalAutomation\\output.json";
		File file = new File(filename);
		FileReader reader = new FileReader(file);

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(reader);

		String outputFileName = "C:\\Users\\admin\\Downloads\\output.csv";
		file = new File(outputFileName);
		if (!file.exists()) {
			file.delete();
			file.createNewFile();
		}

		List<String[]> allCSVLines = new ArrayList<>();
		try (FileReader filereader = new FileReader(file)) {
			try (CSVReader reader1 = new CSVReader(filereader)) {
				allCSVLines = reader1.readAll();
			}
		}

		try (FileWriter outputfile = new FileWriter(file)) {
			try (CSVWriter writer = new CSVWriter(outputfile)) {
				if (allCSVLines.isEmpty()) {
					String[] columnNames = { "Feature", "Scenario Name", "Tags", "Failed Step Line Number", "Steps",
							"Scenario Status", "Error Message" };
					allCSVLines.add(columnNames);
				}

				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject eachFeatureFile = (JSONObject) jsonArray.get(i);
					JSONArray allScenariosInAFeatureFile = (JSONArray) eachFeatureFile.get("elements");
					for (int j = 0; j < allScenariosInAFeatureFile.size(); j++) {
						JSONObject eachScenario = (JSONObject) allScenariosInAFeatureFile.get(j);
						JSONArray allSteps = (JSONArray) eachScenario.get("steps");
						JSONArray tags = new JSONArray();
						List<String> tagsBuilder = new ArrayList<String>();
						if (eachScenario.containsKey("tags")) {
							tags = (JSONArray) eachScenario.get("tags");
							for (int k = 0; k < tags.size(); k++) {
								tagsBuilder.add((String) ((JSONObject) tags.get(k)).get("name"));
							}
						}
						List<String> allStepsNames = new ArrayList<>();
						List<String> errorMessages = new ArrayList<>();
						List<String> failedStepLineNumbers = new ArrayList<>();
						String[] data = new String[7];

						for (int k = 0; k < allSteps.size(); k++) {
							JSONObject eachStep = (JSONObject) allSteps.get(k);
							JSONObject resultObject = (JSONObject) eachStep.get("result");

							allStepsNames.add((String) eachStep.get("name"));

							if (resultObject.get("status").equals("failed")) {
								failedStepLineNumbers.add(String.valueOf(eachStep.get("line")));
							}

							if (resultObject.containsKey("error_message")) {
								errorMessages.add((String) resultObject.get("error_message"));
							}
						}

						data[0] = (String) eachFeatureFile.get("name");
						data[1] = (String) eachScenario.get("name");
						data[2] = tagsBuilder.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","));
						data[3] = failedStepLineNumbers.stream().map(n -> String.valueOf(n))
								.collect(Collectors.joining(","));
						data[4] = allStepsNames.stream().map(n -> String.valueOf(n)).collect(Collectors.joining("\n"));
						data[5] = failedStepLineNumbers.size() > 0 ? "FAILED" : "PASSED";
						data[6] = errorMessages.stream().map(n -> String.valueOf(n)).collect(Collectors.joining("\n"));

						allCSVLines.add(data);
					}
				}

				writer.writeAll(allCSVLines);
			}
		}

	}

}
