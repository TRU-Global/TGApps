/**
 * 
 */
package com.truglobal.pageobjectmodel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

/**
 * @author Kumara Swamy
 *
 */
public class PageObjectGenerator {

	/**
	 * @param args
	 * @return
	 * @throws IOException
	 */
	public List<String> generatePageObject(List<List<String>> pageObjectData, String pageName) throws IOException {
		List<String> methodNames = new ArrayList<>();
		try (FileWriter writer = new FileWriter(
				"..\\TRUGlobalAutomation\\src\\main\\java\\com\\truglobal\\pageobjectmodel\\" + pageName + ".java")) {
			writer.write("package com.truglobal.pageobjectmodel;\r\n" + "\r\n" + "import java.io.IOException;\r\n"
					+ "import java.util.Random;\r\n" + "\r\n" + "import org.apache.logging.log4j.LogManager;\r\n"
					+ "import org.apache.logging.log4j.Logger;\r\n" + "import org.junit.Assert;\r\n"
					+ "import org.openqa.selenium.WebDriver;\r\n"
					+ "import org.openqa.selenium.support.PageFactory;\r\n" + "\r\n"
					+ "import com.opencsv.exceptions.CsvException;\r\n"
					+ "import com.truglobal.seleniumcommands.SeleniumCommands;\r\n"
					+ "import com.truglobal.util.ServiceLocator;\r\n" + "import com.truglobal.util.Util;\r\n" + "\r\n"
					+ "/**\r\n" + " * @author Code Generator\r\n" + " *\r\n" + " */\r\n" + "public class " + pageName
					+ " {\r\n" + "	private static final Logger logger = LogManager.getLogger(" + pageName
					+ ".class);\r\n" + "	WebDriver driver;\r\n"
					+ "	ServiceLocator service = ServiceLocator.getInstance();\r\n" + "	Util util;\r\n"
					+ "	SeleniumCommands selenium;\r\n" + "\r\n" + "	public " + pageName
					+ "(WebDriver driver) throws IOException, CsvException {\r\n" + "		this.driver = driver;\r\n"
					+ "		PageFactory.initElements(driver, this);\r\n" + "		util = new Util(driver);\r\n"
					+ "		util.readCSV(\"" + pageName + ".csv\");\r\n"
					+ "		selenium = new SeleniumCommands(driver, util);\r\n" + "	}\r\n\n" + "");

			PageObjectGenerator generator = new PageObjectGenerator();
			List<String> methodNamesCheck = new ArrayList<>();

			for (List<String> eachList : pageObjectData) {
				String command = eachList.get(1).trim().toLowerCase();
				String methodName = eachList.get(1) + eachList.get(0);
				String testData = eachList.get(4);
				if (methodNamesCheck.contains(methodName)) {
					continue;
				} else {
					methodNamesCheck.add(methodName);
				}
				methodNames.add(methodName);
				switch (command) {
				case "sendkeys":
					writer.write(generator.generateSendKeysMethod(methodName, eachList.get(0), testData));
					String temp = methodNames.remove(methodNames.size() - 1);
					temp = temp + "(i);";
					methodNames.add(temp);
					break;
				case "click":
					writer.write(generator.generateClickMethod(methodName, eachList.get(0)));
					break;
				case "type":
					writer.write(generator.generateTypeMethod(methodName, eachList.get(0), testData));
					temp = methodNames.remove(methodNames.size() - 1);
					temp = temp + "(i);";
					methodNames.add(temp);
					break;
				case "storetext":
					writer.write(generator.generateStoreTextMethod(methodName, eachList.get(0), eachList.get(2)));
					break;
				case "storeeval":
					writer.write(generator.generateStoreEvalMethod(methodName, eachList.get(0), eachList.get(2)));
					break;
				case "selectwindow":
					writer.write(generator.generateSelectWindowMethod(methodName, eachList.get(0)));
					break;
				case "selectframe":
					writer.write(generator.generateSelectFrameMethod(methodName, eachList.get(0)));
					break;
				case "echo":
					writer.write(generator.generateEchoCommand());
					break;
				case "select":
					writer.write(generator.generateSelectDropdown(methodName, eachList.get(0), testData));
					temp = methodNames.remove(methodNames.size() - 1);
					temp = temp + "(i);";
					methodNames.add(temp);
					break;
				case "startfunction":
					break;
				case "endfunction":
					break;
				default:
					writer.write(generator.unsupportedCommand(methodName));
					break;
				}

			}

			writer.write("}");
		}
		return methodNames;
	}

	public String generateSendKeysMethod(String methodName, String variableName, String testData) {
		return "	public void " + methodName + "(int i) {\r\n" + "		selenium.sendKeys(\"" + variableName
				+ "\", service.getTestCaseDataColumn(i, \"" + testData + "\"));\r\n" + "	}\r\n\n";
	}

	public String generateClickMethod(String methodName, String variableName) {
		return "	public void " + methodName + "() {\r\n" + "		selenium.click(\"" + variableName + "\");\r\n"
				+ "	}\r\n\n" + "";
	}

	public String generateTypeMethod(String methodName, String variableName, String testData) {
		return "	public void " + methodName + "(int i) {\r\n" + "		selenium.type(\"" + variableName
				+ "\", service.getTestCaseDataColumn(i, \"" + testData + "\"));\r\n" + "	}\r\n\n";
	}

	public String generateStoreTextMethod(String methodName, String variableName, String testData) {
		return "	public void " + methodName + "() {\r\n" + "		String " + testData + " = selenium.getText(\""
				+ variableName + "\");\r\n" + "		service.setSessionMap(\"" + testData + "\", " + testData + ");\r\n"
				+ "	}\r\n\n";
	}

	public String generateStoreEvalMethod(String methodName, String variableName, String testData) {
		return "	public void " + methodName + "() {\r\n" + "		String " + testData + " = selenium.Eval(\""
				+ variableName + "\");\r\n" + "		service.setSessionMap(\"" + testData + "\", " + testData + ");\r\n"
				+ "	}\r\n\n";
	}

	public String generateSelectWindowMethod(String methodName, String variableName) {
		return "	public void " + methodName + "() {\r\n		selenium.selectWindow(\"" + variableName
				+ "\");\r\n	}\r\n\n";
	}

	public String generateSelectFrameMethod(String methodName, String variableName) {
		return "	public void " + methodName + "() {\r\n		selenium.selectFrame(\"" + variableName
				+ "\");\r\n	}\r\n\n";
	}

	public String generateEchoCommand() {
		return "";
	}

	public String generateSelectDropdown(String methodName, String variableName, String testData) {
		return "	public void " + methodName + "(int i) {\r\n" + "		selenium.selectDropdown(\"" + variableName
				+ "\", service.getTestCaseDataColumn(i, \"" + testData + "\"));\r\n" + "	}\r\n\n";
	}

	public String unsupportedCommand(String methodName) {
		return "	public void " + methodName + "(String str) {\r\n" + "		"
				+ "//		Command not supported by Code Generator. Please write your own implementation here\r\n"
				+ "	}\r\n\n";
	}

}
