/**
 * 
 */
package com.truglobal.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

/**
 * @author Kumara Swamy
 *
 */
public class Util {
	private static final Logger logger = LogManager.getLogger(Util.class);
	public List<String[]> csvAllRows;
	public List<String> headers;
	WebDriver driver;
	private List<String[]> testCaseData;
	private List<String> testCaseDataColumns;
	ServiceLocator service = ServiceLocator.getInstance();
	LoggingUtils loggingUtils;

	public Util(WebDriver driver) {
		this.driver = driver;
	}

	public void readCSV(String filename) throws IOException, CsvException {
		try (CSVReader reader = new CSVReader(
				new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filename)))) {

			csvAllRows = reader.readAll();
			headers = Arrays.asList(csvAllRows.get(0));
			headers = headers.stream().map(String::toLowerCase).map(String::trim).collect(Collectors.toList());
//			logger.info(String.format("CSV Headers %s", headers));
		}
	}

	public WebElement findElement(String variableName) {
		Optional<String[]> optional = csvAllRows.stream().filter(eachRow -> eachRow[0].equalsIgnoreCase(variableName))
				.findFirst();
		String[] row = null;
		if (optional.isPresent()) {
			row = optional.get();
		}
		Date startTime = new Date();
		int timeout = Integer.parseInt(service.getProperty("findElementTimeOut"));
		long maxTime = startTime.getTime() / 1000 + timeout;
//		logger.info(String.format("Filtered row from the CSV file %s", Arrays.asList(row)));
		if (row != null) {
			while (true) {
				for (int i = 1; i < row.length; i++) {
					try {
						By locator = getElementIdentifier(row, headers.get(i));
						WebElement element = driver.findElement(locator);
						WebDriverWait wait = new WebDriverWait(driver, 10);
//						wait.until(ExpectedConditions.presenceOfElementLocated(locator));
						try {
							wait.until(ExpectedConditions.visibilityOf(element));
						} catch (Exception e) {
						}
//						if (element.isDisplayed()) {
//							logger.info("The element is visible on the screen");
//							return element;
//						}
						if (element != null) {
							return element;
						}
						Thread.sleep(1000);
						Date endTime = new Date();
						long responseTime = endTime.getTime() / 1000;
						if (responseTime >= maxTime) {
							return null;
						}
					} catch (Exception e) {
						logger.info(String.format("%s failed with %s", row[0], headers.get(i)));
						logger.info("######################  Trying to SELFHEAL..  #######################");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							Thread.currentThread().interrupt();
						}
						Date endTime = new Date();
						long responseTime = endTime.getTime() / 1000;
						if (responseTime >= maxTime) {
							return null;
						}
					}
				}
			}
		} else {
			return null;
		}
	}

	public String getXpathFromCSV(String variableName, String locator) {
		String xpath = null;
		try {
			Optional<String[]> optional = csvAllRows.stream()
					.filter(eachRow -> eachRow[0].equalsIgnoreCase(variableName)).findFirst();
			String[] row = null;
			if (optional.isPresent()) {
				row = optional.get();
			}

			if (row != null) {
				int index = headers.indexOf(locator);
				xpath = row[index];
			}

		} catch (Exception e) {
			logger.info("Unable to find xpath " + variableName);
		}
		return xpath;
	}

	public List<WebElement> findElements(String variableName) {
		Optional<String[]> optional = csvAllRows.stream().filter(eachRow -> eachRow[0].equalsIgnoreCase(variableName))
				.findFirst();
		String[] row = null;
		if (optional.isPresent()) {
			row = optional.get();
		}
//		logger.info(String.format("Filtered row from the CSV file %s", Arrays.asList(row)));
		if (row != null) {
			while (true) {
				Date startTime = new Date();
				int timeout = Integer.parseInt(service.getProperty("findElementTimeOut"));
				long maxTime = startTime.getTime() / 1000 + timeout;
				for (int i = 1; i < row.length; i++) {
					try {
						By locator = getElementIdentifier(row, headers.get(i));
						return driver.findElements(locator);
					} catch (Exception e) {
						logger.info(String.format("%s failed with %s", row[0], headers.get(i)));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							Thread.currentThread().interrupt();
						}
						Date endTime = new Date();
						long responseTime = endTime.getTime() / 1000;
						if (responseTime >= maxTime) {
							return null;
						}
					}
				}
			}
		} else {
			return null;
		}
	}

	public By getElementIdentifier(String[] row, String locatorType) {
		logger.info(String.format("%s trying with %s", row[0], locatorType));
		if (row.length > 1) {
			locatorType = locatorType.toLowerCase().trim();
			int index = 0;
			index = headers.indexOf(locatorType);

			if (locatorType.equalsIgnoreCase("default")) {
				locatorType = row[index];
				if (locatorType != null && !locatorType.equalsIgnoreCase("")) {
					index = headers.indexOf(locatorType);
					logger.info(String.format("%s default is %s", row[0], locatorType));
				} else {
					return null;
				}
			}

			if (row[index] == null || row[index].equalsIgnoreCase("")) {
				return null;
			}

			switch (locatorType) {
			case "id":
				return By.id(row[index]);
			case "css":
				return By.cssSelector(row[index]);
			case "name":
				return By.name(row[index]);
			case "linktext":
				return By.linkText(row[index]);
			case "partiallinktext":
				return By.partialLinkText(row[index]);
			case "classname":
				return By.className(row[index]);
			default:
				return By.xpath(row[index]);
			}
		} else {
			return null;
		}
	}

	public String getElementIdentifierString(String variableName, String locatorType) {
		Optional<String[]> row = csvAllRows.stream().filter(eachRow -> eachRow[0].equalsIgnoreCase(variableName))
				.findFirst();
		if (row.isPresent()) {
//			variableName,id,css,xpath,name,linkText,partialLinkText,className
			locatorType = locatorType.toLowerCase().trim();
			int index = 0;
			index = headers.indexOf(locatorType);
			return row.get()[index];
		} else {
			return null;
		}
	}

	public void readDataFile(String dataFileName) throws IOException, CsvException {
		try (CSVReader reader = new CSVReader(
				new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(dataFileName)))) {

			testCaseData = reader.readAll();
			if (!testCaseData.isEmpty()) {
				testCaseDataColumns = Arrays.asList(testCaseData.remove(0));
			}
		}
	}

	public String getTestCaseDataColumn(int rowIndex, int columnIndex) {
		String[] eachRow = testCaseData.get(rowIndex);
		return eachRow[columnIndex];
	}

	public int getTestCaseDataColumnIndex(String columnName) {
		return testCaseDataColumns.indexOf(
				testCaseDataColumns.stream().filter(column -> column.equalsIgnoreCase(columnName)).findFirst().get());
	}

	public String getTestCaseDataColumn(int rowIndex, String columnName) {
		String[] eachRow = testCaseData.get(rowIndex);
		int columnIndex = getTestCaseDataColumnIndex(columnName);
		return eachRow[columnIndex];
	}

	public String getDynamicXpath(String data, String xpath) {
		// String xpath="//div[@class='ant-picker-cell-inner' and
		// text()='<texttoreplace>']";
		xpath = xpath.replace("<texttoreplace>", data);
		return xpath;
	}

	public String getCurrentMonth() {
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		return currentMonth.toString().toLowerCase();

	}

	public void captureScreenshot() {
//		String screenshotFolder = "Screenshots";
//		File file = new File(screenshotFolder);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
//		File destFile = new File(screenshotFolder + "/screenshot" + ".png");
//		Files.copy(srcFile, destFile);
		loggingUtils.log(srcFile, "Snapshot");
	}

	public void setSessionMap(String string, String quantity) {
		Map<String, Object> sessionMap = new HashMap<>();
		sessionMap.put(Thread.currentThread().getName() + "_" + string, quantity);
	}

	public Object getSessionMap(String string) {
		Map<String, Object> sessionMap = new HashMap<>();
		return sessionMap.get(Thread.currentThread().getName() + "_" + string);
	}


}