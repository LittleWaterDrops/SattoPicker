import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTest {
	static int COUNT_PER_LAP = 500;
	public static void main(String[] args) throws IOException {
		ChromeOptions options = new ChromeOptions();

		options.addArguments("headless");
		options.addArguments("--blink-settings=imagesEnabled=false");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-infobars");
		options.addArguments("--start-maximized");
		options.addArguments("--disable-extensions");
		options.addArguments("incognito");
		
		WebElement submitButton;
		WebElement retryButton;
		WebElement element;
		String text;
		int correctCount;
		int[] resultArray = new int[7];
		Date startDate = new Date();
		
		loop : for(long lap = 0; lap < 100000; lap++) {
			WebDriver driver = new ChromeDriver(options);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			
			driver.get("http://localhost:80/ssato");
			
			for(long i = 0; i < COUNT_PER_LAP; i++) {
				submitButton = driver.findElement(By.id("submit"));
				submitButton.click();
	   		 
	            element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#result > div:nth-child(4)")));
	            text = element.getText();
	            correctCount = text.charAt(8) - '0';
	            resultArray[correctCount]++;
	            System.out.println((lap * COUNT_PER_LAP + (i + 1)));
	            
	            if(correctCount == 6) {
	           	 Date endDate = new Date();
	           	 long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
	           	 long second = TimeUnit.MILLISECONDS.toSeconds(diffInMillies);
	           	 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	           	 
	           	 FileHandler.copy(scrFile, new File("c:\\foo\\screenshot.png"));
	           	 System.out.println("start & end : " + startDate + " ~ " + endDate);
	           	 System.out.println("second : " + second);
	           	 System.out.println("count : " + (lap * COUNT_PER_LAP + (i + 1)));
	           	 System.out.println("result : " + Arrays.toString(resultArray));
	           	 break loop;
	            }

	        retryButton = driver.findElement(By.id("submit"));
	        retryButton.click();
			}
			driver.quit();
		}
		
		
		System.out.println("finished");
	}
}
