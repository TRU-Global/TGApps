/**
 *
 */
package com.truglobal.seleniumcommands;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

//import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.truglobal.util.LoggingUtils;
import com.truglobal.util.ServiceLocator;
import com.truglobal.util.Util;

/**
 * @author Kumara Swamy
 *
 */
public class SeleniumCommands {
	private static final Logger logger = LogManager.getLogger(SeleniumCommands.class);
	WebDriver driver;
	Util util;
	LoggingUtils loggingUtils;
	ServiceLocator service = ServiceLocator.getInstance();

	public SeleniumCommands(WebDriver driver, Util util) {
		this.driver = driver;
		this.util = util;
	}

	/**
	 * Check if the element is visible on the screen The
	 *
	 * @param element
	 * @return boolean
	 */
	public boolean checkElementIsVisibleOnPage(String variableName) {
		try {
			WebElement element = util.findElement(variableName);
			logger.info("Start of Check Element Visibility");
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.visibilityOf(element));
			logger.info("End of Check Element Visibility on Screen");
			logger.info("Element present on Screen");
			return true;
		} catch (Exception e) {
			logger.info("End of Check Element Visibility on Screen");
		}
		return false;
	}

	public boolean checkElementIsNotVisibleOnPage(String variableName) {
		try {
			WebElement element = util.findElement(variableName);
			logger.info("Start of Check Element Not Visible");
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.invisibilityOf(element));
			logger.info("End of Check Element Not Visible");
			return true;
		} catch (Exception e) {
			logger.info("End of Check Element Not Visible");
		}
		return false;
	}

	public boolean checkElementIsEnabledOnPage(String variableName) {
		try {
			WebElement element = util.findElement(variableName);
			logger.info("Start of Check Element Enabled");
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			logger.info("End of Check Element Enabled on Screen");
			return true;
		} catch (Exception e) {
			logger.info("End of Check Element Enabled on Screen");
		}
		return false;
	}

	/**
	 * The Click command Implementation
	 *
	 * Here we'll try with WebElement click first. If it fails then we'll go with
	 * Javascript click
	 *
	 * @param variableName
	 */
	public void click(String variableName) {
		logger.info("Start of Command Execution||CommandName=Click");
		WebElement element = util.findElement(variableName);

		try {
			element.click();
		} catch (Exception e) {
			try {
				logger.info("WebElement click failed. Applying Autoheal..");
				if (element != null) {
					element = util.findElement(variableName);
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click()", element);
				} else {
					Assert.fail("Element Not Found||Error=" + e);
				}
			} catch (Exception ex) {
				driver.quit();
				logger.error("Javascript click also failed||Error=", ex);
				Assert.fail("Unable to click on element : " + variableName);
			}
		} finally {
			captureScreenshot();
		}
		logger.info("End of Command Execution||CommandName=Click");
	}

	public void clickWithJavaScript(String variableName) {
		logger.info("Start of Command Execution||CommandName=clickWithJavaScript");

		try {
			WebElement element = util.findElement(variableName);
			Thread.sleep(5000);
			element = util.findElement(variableName);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", element);
		} catch (Exception ex) {
			driver.quit();
			logger.error("Javascript click also failed||Error=", ex);
			Assert.fail("Unable to click on element : " + variableName);
		} finally {
			captureScreenshot();
		}

	}

	public void clickOnDynamicXpath(String xpath) {
		WebElement element = driver.findElement(By.xpath(xpath));
		try {
			element.click();
		} catch (Exception e) {
			try {
				Thread.sleep(2000);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click()", element);
			} catch (Exception ex) {
				driver.quit();
				logger.error("Javascript click also failed||Error=", ex);
				Assert.fail("Unable to click on element : " + xpath);
			}
		} finally {
			captureScreenshot();
		}
		logger.info("End of Command Execution||CommandName=Click");
	}

	/**
	 *
	 * @param variableName
	 * @param str
	 */
	public void sendKeys(String variableName, String str) {
		logger.info("Start of Command Execution||CommandName=SendKeys");
		WebElement element = util.findElement(variableName);

		try {
			element.sendKeys(str);
			logger.info("End of Command Execution||CommandName=SendKeys");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=SendKeys||Error=", e);
			Assert.fail("Unable to send keys to " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void sendKeys(String variableName, Keys key) throws InterruptedException {
		logger.info("Start of Command Execution||CommandName=SendKeys");
		WebElement element = util.findElement(variableName);

		try {
			element.sendKeys(key);
			logger.info("End of Command Execution||CommandName=SendKeys");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=SendKeys||Error=", e);
			Assert.fail("Unable to send keys to " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	/**
	 *
	 * @param variableName
	 * @param str
	 */
//	public void type(String variableName, String str) {
//		logger.info("Start of Command Execution||CommandName=Type");
//		WebElement element = util.findElement(variableName);
//
//		try {
//			try {
//				element.clear();
//			} catch (Exception e) {
//			}
//			element.sendKeys(str);
//			logger.info("End of Command Execution||CommandName=Type");
//		} catch (Exception e) {
//			logger.error("End of Command Execution||CommandName=Type||Error=", e);
//		} finally {
//			captureScreenshot();
//		}
//	}
	public void type(String variableName, String str) {
		logger.info("Start of Command Execution||CommandName=Type");
		WebElement element = util.findElement(variableName);

		if (str.startsWith("src/test/resources/UploadFiles/")) {
			str = new File(str).getAbsolutePath();
		}

		try {
			try {
				element.clear();
				element.sendKeys(Keys.CONTROL, "a");
				element.sendKeys(Keys.DELETE);
			} catch (Exception e) {
			}
			element.sendKeys(str);
			logger.info("End of Command Execution||CommandName=Type");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=Type||Error=", e);
			Assert.fail("Unable to send keys to " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void selectDropdown(String variableName, String selectBy) {
		logger.info("Start of Command Execution||CommandName=Select");
		WebElement element = util.findElement(variableName);

		try {
			Select select = new Select(element);
			if (selectBy.startsWith("index=")) {
				select.selectByIndex(Integer.parseInt(selectBy.substring(6)));
			} else if (selectBy.startsWith("value=")) {
				select.selectByValue(selectBy.substring(6));
			} else if (selectBy.startsWith("label=")) {
				select.selectByVisibleText(selectBy.substring(6));
			} else {
				select.selectByVisibleText(selectBy.substring(6));
			}
			logger.info("End of Command Execution||CommandName=Select");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=Select||Error=", e);
			Assert.fail("Unable to select item from " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void selectFrame(String variableName) {
		logger.info("Start of Command Execution||CommandName=SelectFrame");
		String originalVariable = variableName;
		variableName = util.getElementIdentifierString(variableName, "id");

		try {
			if (variableName.startsWith("index=")) {
				int index = Integer.parseInt(variableName.substring(6));
				if (index == -1) {
					driver.switchTo().defaultContent();
				} else {
					driver.switchTo().frame(index);
				}
			} else if (variableName.startsWith("name=")) {
				driver.switchTo().frame(variableName.substring(5));
			} else if (variableName.startsWith("id=")) {
				driver.switchTo().frame(variableName.substring(3));
			} else {
				WebElement element = util.findElement(originalVariable);
				driver.switchTo().frame(element);
			}
			logger.info("End of Command Execution||CommandName=SelectFrame");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=SelectFrame||Error=", e);
			Assert.fail("unable to switch to frame " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void closeWindow() {
		logger.info("Start of Command Execution||CommandName=CloseWindow");
		try {
			driver.close();
			driver.switchTo().defaultContent();
			logger.info("End of Command Execution||CommandName=CloseWindow");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=CloseWindow||Error=", e);
			Assert.fail("Unable to close window");
		} finally {
			captureScreenshot();
		}
	}

	public void mouseOver(String variableName) {
		logger.info("Start of Command Execution||CommandName=MouseOver");
		try {
			WebElement element = util.findElement(variableName);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).build().perform();
			logger.info("End of Command Execution||CommandName=MouseOver");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=MouseOver||Error=", e);
			Assert.fail("Unable to move mouse to " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void isActiveButton(String variableName) {
		logger.info("Start of Command Execution||CommandName=isActiveButton");
		try {
			WebElement element = util.findElement(variableName);
			if (element.isSelected()) {
				logger.info("Toggle is in active state. ");
			}
			logger.info("End of Command Execution||CommandName=isActiveButton");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=isActiveButton||Error=", e);
			Assert.fail("Unable to check state of " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void mouseOut(String variableName) {
		logger.info("Start of Command Execution||CommandName=MouseOver");
		try {
			WebElement element = util.findElement(variableName);
			Actions actions = new Actions(driver);
			actions.release().build().perform();
			logger.info("End of Command Execution||CommandName=MouseOver");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=MouseOver||Error=", e);
			Assert.fail("mouse movement not working for " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void selectWindow(String variableName) {
		logger.info("Start of Command Execution||CommandName=SelectWindow");
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
		}
		List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
		variableName = util.getElementIdentifierString(variableName, "xpath");
		try {
			if (variableName.startsWith("index=")) {
				int index = Integer.parseInt(variableName.substring(6));
				if (index == -1) {
					driver.switchTo().window(ServiceLocator.getSessionMap("originalWindowHandle"));
				} else if (windowHandles.size() == 2) {
					if (!(windowHandles.get(index + 1)).equals(ServiceLocator.getSessionMap("originalWindowHandle"))) {
						driver.switchTo().window(windowHandles.get(index + 1));
					} else {
						driver.switchTo().window(windowHandles.get(index));
					}
				} else {
					driver.switchTo().window(windowHandles.get(index + 1));
				}
			} else if (variableName.startsWith("name=")) {
				driver.switchTo().window(variableName.substring(5));
			} else {
				driver.switchTo().window(variableName);
			}
			logger.info("End of Command Execution||CommandName=SelectWindow");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=SelectWindow||Error=", e);
			Assert.fail("Unable to select window " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void scrollToViewElement(String variableName) {
		logger.info("Start of Command Execution||CommandName=ScrollToView");
		WebElement element = util.findElement(variableName);
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView()", element);
			logger.info("End of Command Execution||CommandName=ScrollToView");
		} catch (Exception e) {
			driver.quit();
			logger.error("End of Command Execution||CommandName=ScrollToView||Error=", e);
			Assert.fail("Unable to scroll to element " + variableName);
		} finally {
			captureScreenshot();
		}
	}

	public void waitForAttribute(String variableName, String attributeName, String str) {
		logger.info("Start of Command Execution||CommandName=waitForAttribute");
		WebDriverWait wait = new WebDriverWait(driver, 120);
		while (true) {
			try {
				WebElement element = util.findElement(variableName);
				wait.until(ExpectedConditions.attributeToBe(element, attributeName, str));
				logger.info("End of Command Execution||CommandName=waitForAttribute");
				break;
			} catch (Exception e) {
				driver.quit();
				Assert.fail(
						"Wait for attribute failed for element " + variableName + " and attribute " + attributeName);
				logger.error("End of Command Execution||CommandName=waitForAttribute||Error=", e);
			}
		}
		captureScreenshot();
	}

	public String getAttribute(String variableName, String attributeName) {
		logger.info("Start of Command Execution||CommandName=GetAttribute");
		String value = null;
		try {
			WebElement element = util.findElement(variableName);
			logger.info("End of Command Execution||CommandName=GetAttribute");
			value = element.getAttribute(attributeName);
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetAttribute||Error=", e);
		} finally {
			captureScreenshot();
		}
		return value;
	}

	@SuppressWarnings("finally")
	public String getText(String variableName) {
		logger.info("Start of Command Execution||CommandName=GetText");
		WebElement element = util.findElement(variableName);
		String text = null;
		try {
			text = element.getText();
			logger.info("End of Command Execution||CommandName=GetText");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetText||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	@SuppressWarnings("finally")
	public String getCssValueFont(String variableName) {
		logger.info("Start of Command Execution||CommandName=GetCssValuefont");
		WebElement element = util.findElement(variableName);
		String text = null;
		try {
			text = element.getCssValue("font-weight");
			logger.info("End of Command Execution||CommandName=GetCssValuefont");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetCssValuefont||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	@SuppressWarnings("finally")
	public String getCssValueColor(String variableName) {
		logger.info("Start of Command Execution||CommandName=GetCssValuecolor");
		WebElement element = util.findElement(variableName);
		String text = null;
		try {
			text = element.getCssValue("color");
			logger.info("End of Command Execution||CommandName=GetCssValuecolor");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetCssValuecolor||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	@SuppressWarnings("finally")
	public String getValue(String variableName) {
		logger.info("Start of Command Execution||CommandName=GetValue");
		WebElement element = util.findElement(variableName);
		String text = null;
		try {
			text = element.getAttribute("value");
			logger.info("End of Command Execution||CommandName=GetValue");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetValue||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	@SuppressWarnings("finally")
	public int getElementCount(String variableName) {
		logger.info("Start of Command Execution||CommandName=GetElementCount");
		List<WebElement> elements = util.findElements(variableName);
		int count = 0;
		try {
			count = elements.size();
			logger.info("End of Command Execution||CommandName=GetElementCount");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetElementCount||Error=", e);
		} finally {
			captureScreenshot();
			return count;
		}
	}

	public String generateRandomNumber() {
		Random random = new Random();
		String randomNumber = String.valueOf(random.nextInt());
		return randomNumber;
	}

	@SuppressWarnings("finally")
	public String getCurrentDate() {
		logger.info("Start of Command Execution||CommandName=GetCurrentDate");
		String text = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			text = formatter.format(date);

			logger.info("End of Command Execution||CommandName=GetCurrentDate");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetCurrentDate||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	@SuppressWarnings("finally")
	public String getCurrentTime() {
		logger.info("Start of Command Execution||CommandName=GetCurrentDate");
		String text = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			text = formatter.format(date);

			logger.info("End of Command Execution||CommandName=GetCurrentDate");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetCurrentDate||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	public void captureScreenshot() {
//		String screenshotFolder = "Screenshots";
//		File file = new File(screenshotFolder);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
		try {
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
//		File destFile = new File(screenshotFolder + "/screenshot" + ".png");
//		Files.copy(srcFile, destFile);
			LoggingUtils.log(srcFile, "Snapshot");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("finally")
	public String getCssValueBackgroundColor(String variableName) {
		logger.info("Start of Command Execution||CommandName=GetCssValuecolor");
		WebElement element = util.findElement(variableName);
		String text = null;
		try {
			text = element.getCssValue("background-color");
			logger.info("End of Command Execution||CommandName=GetCssValuecolor");
		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetCssValuecolor||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	@SuppressWarnings("finally")
	public void clear(String variableName) {
		logger.info("Start of Command Execution||CommandName=Clear");
		WebElement element = util.findElement(variableName);

		try {
			// element.clear();
			element.sendKeys(Keys.CONTROL, "a");
			element.sendKeys(Keys.DELETE);
		} catch (Exception e) {
			try {
				logger.info("WebElement click failed. Applying Autoheal..");
				Thread.sleep(5000);
				element = util.findElement(variableName);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].clear()", element);
			} catch (Exception ex) {
				driver.quit();
				logger.error("Javascript click also failed||Error=", ex);
				Assert.fail("Unable to cleat text for element " + variableName);
			}
		} finally {
			captureScreenshot();
		}
		logger.info("End of Command Execution||CommandName=Clear");
	}

	public String getYesterdayDate() {
		logger.info("Start of Command Execution||CommandName=GetCurrentDate");
		String text = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date yesterday = calendar.getTime();
			text = dateFormat.format(yesterday);
			logger.info("End of Command Execution||CommandName=GetCurrentDate");

		} catch (Exception e) {
			logger.error("End of Command Execution||CommandName=GetCurrentDate||Error=", e);
		} finally {
			captureScreenshot();
			return text;
		}
	}

	public void sendKeyboardKey(String Key, String variableName) {
		String availableKeys = Key.toLowerCase();
		WebElement element = util.findElement(variableName);

		switch (availableKeys) {
		case "enter":
			element.sendKeys(Keys.ENTER);
		}
	}
	public void customWait(int timeInSeconds) {
        try {
            Thread.sleep(timeInSeconds * 1000L);
        } catch (Exception e) {
        }
    }
}
