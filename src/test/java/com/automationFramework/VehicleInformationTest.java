package com.automationFramework;

import com.pages.VehicleInformationPage;
import com.service.fileUtil.ExcelUtil;
import com.service.impl.FileInfo;
import com.service.impl.FileResource;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * This class implements a test flow for
 * retrieving Vehicle Information from an excel file and asserts the info on DVLA site
 */
public class VehicleInformationTest {

    // Global domain values and tes-specific variables
    private WebDriver driver;
    private WebDriverWait wait;
    private VehicleInformationPage page;
    private ExcelUtil excelUtil;

    private String baseUrl = "https://www.gov.uk/get-vehicle-information-from-dvla";
    private String pathToFolder = "resources";
    private String fileName = "VehicleData.xlsx";
    private String sheetName = "Sheet1";
    private int timeoutSecs = 10;

    @Before
    public void setup() throws Throwable {

        System.setProperty("webdriver.chrome.driver", "utilities/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(timeoutSecs, TimeUnit.SECONDS);
        this.wait = new WebDriverWait(driver, timeoutSecs);
        excelUtil = new ExcelUtil();
        page = new VehicleInformationPage(driver);
        driver.get(baseUrl);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Reads vehicle plate number from an excel file and verifies the make and colour
     * and takes a screen shot for each vehicle and stores them in screenshots directory
     *
     * @throws IOException
     * @throws InvalidFormatException
     * @see ExcelUtil
     */

    @Test
    public void testVehicleInfortmation() throws IOException, InvalidFormatException {

        // Retrieve the file
        FileResource resource = new FileResource(pathToFolder);
        List<String> supportedTypes = new ArrayList<String>();
        supportedTypes.add("xlsx");
        List<FileInfo> files = resource.getSupportedFiles(supportedTypes);

        for (FileInfo fileInfo : files) {
            if (fileInfo.getName().equals(fileName)) {
                String fullFilePath = pathToFolder + "/" + fileInfo.getName();
                excelUtil.setExcelFile(fullFilePath, sheetName);
            } else {
                return;
            }
            assertTrue("Start button is not displayed", page.START_BUTTON.isDisplayed());
            page.clickOnStartButton();

            // Read the file
            int rowCount = excelUtil.getRowCount(sheetName);
            for (int rowNum = 1; rowNum < rowCount; rowNum++) {
                int colNum = 0; // column number
                String cellData = excelUtil.getCellData(rowNum, colNum);

                assertTrue("Registration number text area is not displayed", page.QUERY_TEXTAREA.isDisplayed());
                page.enterRegisterationNumber(cellData);
                assertTrue("Submit button is not displayed", page.QUERY_SUBMIT_BUTTON.isDisplayed());
                page.submitRegisterationNumber();

                WebElement confirmationMessage = wait
                        .until(ExpectedConditions.visibilityOf(page.VERIFICATION_QUESTION));
                assertTrue("Vehicle details could not be found", confirmationMessage.isDisplayed());

                // Verify make of the vehicle
                colNum++;
                String makeOnFile = excelUtil.getCellData(rowNum, colNum);
                String makeOnPage = page.VEHICLE_MAKE.getText();
                assertTrue("Make does not confirm",
                        makeOnFile.trim().equalsIgnoreCase(makeOnPage.trim()));

                // Verify colour of the vehicle
                colNum++;
                String colourOnFile = excelUtil.getCellData(rowNum, colNum);
                String colourOnPage = page.VEHICLE_COLOUR.getText();
                assertTrue("Colour does not confirm",
                        colourOnFile.trim().equalsIgnoreCase(colourOnPage.trim()));

                // Screenshot of output
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File("screenshots/screenshot" + rowNum + ".png"));

                driver.navigate().back();

            }
        }

    }

}