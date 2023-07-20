/**
 * 
 */
package com.truglobal.pageobjectmodel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Kumara Swamy
 *
 */
public class StepDefinitionGenerator {
	public void generateStepDefinition(Map<String, List<String>> allPageObjectsMethodNames,
			Map<String, List<List<String>>> scriptOrderOfExecution, List<String> fileNames) throws IOException {
		try (FileWriter writer = new FileWriter(
				"..\\TRUGlobalAutomation\\src\\test\\java\\com\\truglobal\\stepdefinitions\\StepDefinitions_1.java")) {
			StepDefinitionGenerator stepGenerator = new StepDefinitionGenerator();
			String importStatements = stepGenerator.generateImportStatements();
			String classCode = stepGenerator.generateClassCode();
			String launchBrowser = stepGenerator.getlaunchBrowserCode();
			String openURLCode = stepGenerator.openURLCode();

			List<String> checkDuplicateFiles = new ArrayList<>();
			for (Entry<String, List<String>> eachPageObjectMethodNames : allPageObjectsMethodNames.entrySet()) {
				String eachFileName = eachPageObjectMethodNames.getKey();
				if (checkDuplicateFiles.contains(eachFileName)) {
					continue;
				} else {
					checkDuplicateFiles.add(eachFileName);
				}
				if (eachFileName != null && !eachFileName.equals("")) {
					String pageObjects = stepGenerator.importPageObjects(eachFileName);
					importStatements = importStatements + pageObjects;
					classCode = classCode + stepGenerator.getPageObjectsDeclaration(eachFileName);
					launchBrowser = launchBrowser + stepGenerator.getPageObjectsInitialization(eachFileName);
				}
			}
			launchBrowser = launchBrowser + stepGenerator.getMethodEndBracesCode();
			writer.write(importStatements);
			writer.write(classCode);
			writer.write(launchBrowser);

			int i = 0;
			String methodSkeleton = "";
			boolean startFunctionCommandStart = false;
			String startFunctionName = null;
			Map<String, List<List<String>>> explicitFunctionsMap = new HashMap<>();
			for (Entry<String, List<List<String>>> eachScript : scriptOrderOfExecution.entrySet()) {
				String fileName = eachScript.getKey();
				List<List<String>> orderOfExecution = eachScript.getValue();
				methodSkeleton = stepGenerator.generateMethodSkeleton(fileName, fileName + " Step");
				List<List<String>> list = new ArrayList<>();
				for (List<String> eachStep : orderOfExecution) {
					String variableName = eachStep.get(0);
					String action = eachStep.get(1);
					String pageName = eachStep.get(4);
					List<String> allMethods = allPageObjectsMethodNames.get(pageName);
					String methodName = allMethods.stream()
							.filter(eachMethod -> eachMethod.startsWith(action + variableName)).findFirst().get();

					if (action.equals("startFunction")) {
						startFunctionCommandStart = true;
						String target = eachStep.get(2).substring(1, eachStep.get(2).length() - 1);
						List<String> targetArray = Arrays.asList(target.split(","));
						target = targetArray.get(targetArray.size() - 1).trim();
						startFunctionName = target;
					} else if (action.equals("endFunction")) {
						startFunctionCommandStart = false;
						explicitFunctionsMap.put(startFunctionName, list);
					} else if (action.equals("callFunction")) {
						String target = eachStep.get(2).substring(1, eachStep.get(2).length() - 1);
						List<String> targetArray = Arrays.asList(target.split(","));
						target = targetArray.get(targetArray.size() - 1).trim();
						methodSkeleton = methodSkeleton + stepGenerator.generateCallFunctionMethodInvoking(target);
					} else if (startFunctionCommandStart) {
						List<String> li = new ArrayList<>();
						li.add(pageName);
						li.add(methodName);
						list.add(li);
					} else {
						methodSkeleton = methodSkeleton + stepGenerator.getmethodInvoking(pageName, methodName);
					}
//					methodSkeleton = methodSkeleton + stepGenerator.getmethodInvoking(pageName, methodName);
				}
				methodSkeleton = methodSkeleton + stepGenerator.getMethodEndBracesCode();

				for (Entry<String, List<List<String>>> eachExplicitFunction : explicitFunctionsMap.entrySet()) {
					String functionName = eachExplicitFunction.getKey();
					List<List<String>> methodsToCall = eachExplicitFunction.getValue();
					methodSkeleton = methodSkeleton
							+ stepGenerator.generateMethodSkeleton(functionName, functionName + " Step");
					for (List<String> eachMethod : methodsToCall) {
						methodSkeleton = methodSkeleton
								+ stepGenerator.getmethodInvoking(eachMethod.get(0), eachMethod.get(1));
					}
				}
				methodSkeleton = methodSkeleton + stepGenerator.getMethodEndBracesCode();
				openURLCode = openURLCode + methodSkeleton;
				i++;
			}

			writer.write(openURLCode);
			writer.write(stepGenerator.getMethodEndBracesCode());
		}
	}

	public String generateImportStatements() {
		return "package com.truglobal.stepdefinitions;\r\n" + "\r\n" + "import java.io.IOException;\r\n"
				+ "import java.util.concurrent.TimeUnit;\r\n" + "\r\n"
				+ "import org.apache.logging.log4j.LogManager;\r\n" + "import org.apache.logging.log4j.Logger;\r\n"
				+ "import org.openqa.selenium.WebDriver;\r\n" + "import org.openqa.selenium.chrome.ChromeDriver;\r\n"
				+ "import org.testng.annotations.Listeners;\r\n" + "\r\n"
				+ "import com.epam.reportportal.testng.ReportPortalTestNGListener;\r\n"
				+ "import com.opencsv.exceptions.CsvException;\r\n" + "import com.truglobal.util.ServiceLocator;\r\n"
				+ "\r\n" + "import io.cucumber.java.en.And;\r\n" + "import io.cucumber.java.en.Given;\r\n"
				+ "import io.cucumber.java.en.When;\r\n" + "import io.github.bonigarcia.wdm.WebDriverManager;";
	}

	public String generateClassCode() {
		return "\n\n@Listeners({ ReportPortalTestNGListener.class })\r\n" + "public class StepDefinitions {\r\n"
				+ "	private static final Logger logger = LogManager.getLogger(StepDefinitions.class);\r\n" + "\r\n"
				+ "	ServiceLocator service = ServiceLocator.getInstance();\r\n" + "	WebDriver driver;";
	}

	public String getPageObjectsDeclaration(String pageName) {
		return "\n	" + pageName + " " + pageName.toLowerCase() + "Obj;";
	}

	public String getPageObjectsInitialization(String pageName) {
		return "\n		" + pageName.toLowerCase() + "Obj = new " + pageName + "(driver);";
	}

	public String getmethodInvoking(String pageName, String methodName) {
		return "\n		" + pageName.toLowerCase() + "Obj."
				+ (methodName.endsWith("(i);") ? methodName : methodName + "();");
	}

	public String importPageObjects(String pageName) {
		return "\nimport com.truglobal.pageobjectmodel." + pageName + ";";
	}

	public String getlaunchBrowserCode() {
		return "\n\n	@Given(\"^launch the browser$\")\r\n"
				+ "	public void launchBrowser() throws IOException, CsvException {\r\n"
				+ "		WebDriverManager.chromedriver().setup();\r\n"
				+ "		logger.info(\"Starting ChromeDriver\");\r\n" + "		driver = new ChromeDriver();\r\n"
				+ "		logger.info(\"ChromeDriver Started Successfully\");\r\n"
				+ "		logger.info(\"Start of Window Maximize\");\r\n"
				+ "		driver.manage().window().maximize();\r\n"
				+ "		logger.info(\"End of Window Maximize\");\r\n"
				+ "		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);\r\n" + "		";
	}

	public String openURLCode() {
		return "\n\n	@Given(\"^The Aplication is loaded$\")\r\n" + "	public void loadApplication() {\r\n"
				+ "		logger.info(\"Start of Open URL\");\r\n" + "		driver.get(\""
				+ (CSVGenerator.testURL != null ? CSVGenerator.testURL : "URL to open") + "\");\r\n"
				+ "		logger.info(\"End of Open URL\");\r\n"
				+ "		service.setSessionMap(\"originalWindowHandle\", driver.getWindowHandle());\r\n" + "	}";
	}

	public String generateMethodSkeleton(String methodName, String message) {
		return "\n\n	@When(\"" + message + " {int}\")\r\n" + "	public void " + methodName
				+ "(Integer i) throws Throwable {" + "		";
	}

	public String generateCallFunctionMethodInvoking(String methodName) {
		return "\n		" + methodName + "(i);";
	}

	public String getMethodEndBracesCode() {
		return "\n	}";
	}
}
