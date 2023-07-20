package com.truglobal.pageobjectmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import edu.emory.mathcs.backport.java.util.Arrays;

public class HTMLParser {
	static WebDriver driver;
	static JavascriptExecutor js;
	static List<String> validatedXpaths;
	static JSONArray finalArray = new JSONArray();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			System.setProperty("webdriver.chrome.driver","../TRUGlobalAutomation/drivers/chromedriver.exe");
			String url = "C:\\Users\\TG1719\\Downloads\\CalixCM_Lastest (2).html";

			driver = new ChromeDriver();
			js = (JavascriptExecutor) driver;

			driver.get(url);

			String script = readFile("C:\\Users\\TG1719\\Downloads\\GetCommandTargetValueFromHTML.js");
			List<List<Object>> commandTargetValuesList = (List<List<Object>>) js.executeScript(script);
			driver.quit();

			driver = new ChromeDriver();
			js = (JavascriptExecutor) driver;

			for (List<Object> commandTargetValue : commandTargetValuesList) {
				System.out.println(commandTargetValue.get(0));
				System.out.println(commandTargetValue.get(1));
				System.out.println(commandTargetValue.get(2));
				System.out.println("----------------------------------------------------------");

				String command = commandTargetValue.get(0).toString().toLowerCase();
				List<String> target = (List<String>) commandTargetValue.get(1);
				String value = (String) commandTargetValue.get(2);

				switch (command) {
				case "open":
					driver.get(target.get(0));
					break;
				case "click":
					click(target);
					break;
				case "type":
					type(target, value);
					break;
				case "sendkeys":
					sendKeys(target, value);
					break;
				case "selectwindow":
					selectWindow(target);
					break;
				default:
					findElement(target);
				}

				String[] ignoreCommands = { "open", "selectwindow" };
				List<String> ignoreCommandsList = Arrays.asList(ignoreCommands);
				JSONObject obj = new JSONObject();
				obj.put("command", command);
				obj.put("value", value);
				if (ignoreCommandsList.contains(command)) {
					obj.put("target", target);
				} else {
					obj.put("target", validatedXpaths);
				}

				finalArray.add(obj);
			}
			driver.quit();

			try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File("output.json")))) {
				writer.write(finalArray.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void click(List<String> multipleLocators) throws IOException, InterruptedException {
		WebElement element = findElement(multipleLocators);
		try {
			element.click();
		} catch (Exception e) {
			Thread.sleep(5000);
			js.executeScript("arguments[0].click();", element);
		}	
	}

	public static void type(List<String> multipleLocators, String value) throws IOException {
		WebElement element = findElement(multipleLocators);
		element.clear();
		element.sendKeys(value);
	}

	public static void sendKeys(List<String> multipleLocators, String value) throws IOException {
		WebElement element = findElement(multipleLocators);
		element.sendKeys(value);
	}

	public static void selectWindow(List<String> multipleLocators) throws InterruptedException {
		Thread.sleep(5000);
		String window = multipleLocators.get(0);
		try {
			int windowId = Integer.parseInt(window);
			if (windowId >= 0) {
				List<String> handles = Arrays.asList(driver.getWindowHandles().toArray());
				driver.switchTo().window(handles.get(windowId));
			} else {
				driver.switchTo().defaultContent();
			}
		} catch (Exception e) {
			driver.switchTo().window(window);
		}
	}

	public static WebElement findElement(List<String> multipleLocators) throws IOException {
		validatedXpaths = validateXpaths(multipleLocators);

		Optional<String> optional = validatedXpaths.stream().filter(x -> x.startsWith("//")).findFirst();
		if (!optional.isPresent()) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			return findElement(multipleLocators);
		} else {
			return driver.findElement(By.xpath(optional.get()));
		}
	}

	public static List<String> validateXpaths(List<String> multipleLocators) throws IOException {
		String script = readFile("C:\\Users\\TG1719\\Downloads\\ValidateXpaths.js");
		List<String> validatedXpaths = (List<String>) js.executeScript(script, multipleLocators);
		return validatedXpaths;
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
