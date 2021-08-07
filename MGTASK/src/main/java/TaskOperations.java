
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaskOperations {

	public static WebDriver driver;
	public static int row;
	public static void login() throws Exception {
		
		for(row = 4;row>=1;row--) {
			System.out.println("******************* Current session is of : "+ readExcelData(row, 0)+" *********************");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement phoneNumber = driver.findElement(By.xpath("//*[@placeholder = 'Enter phone number']"));
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].value ='';", phoneNumber);
		phoneNumber.sendKeys(readExcelData(row, 1));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement loginPassword = driver.findElement(By.xpath("//*[@placeholder = 'Please enter the password']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].value ='';", loginPassword);
		loginPassword.sendKeys(readExcelData(row, 2));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//*[@id='loginBtn']")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement close = driver.findElement(By.xpath("//*[@id='yes_btn']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
			
		try {
			js.executeScript("arguments[0].click();",close);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			WebElement recordPage = driver.findElement(By.xpath("/html/body/div[2]/div/a[4]/span"));
			recordPage.click();
			//System.out.println("CLosed popup");
		}catch(Exception popup) {
			login();
		}
		taskPage();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//*[contains(text(),'Me')]")));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'end'});",driver.findElement(By.xpath("//*[contains(text(),'Logout')]")));
		js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//*[contains(text(),'Logout')]")));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//a[contains(text(),'Confirm')]")));
		//System.out.println(readExcelData(row,0));
		
		}
	}


public static void taskPage() throws NumberFormatException, Exception {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		String s;
		do {
		s = driver.findElement(By.xpath("//*[@class= 'dayTaskNum']")).getText();
		}
		while(s== "");
		int taskNo = Integer.parseInt(s);
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println("---- "+ taskNo + " task remaining -----"+ "  "+sdf.format(date));	
		if(taskNo != 0) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//span[@i18n='i18n.taskFooter']")));
		task();
		}
		else {
			try {
				driver.navigate().refresh();
				youtubeTask();
			}catch(Exception e) {
			}
		}

	}

	public static void task() throws NumberFormatException, Exception {
			//System.out.println("Switched to goTask");
		JavascriptExecutor js = (JavascriptExecutor) driver;

			try{
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//*[contains(text(),'Receive')]"));
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//*[contains(text(),'The daily quota is finished')]"));
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//*[contains(text(),'No Record')]"));
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				js.executeScript("arguments[0].click();",driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/li[2]")));
				driver.findElement(By.xpath("//*[contains(text(),'No Record')]"));
				System.exit(0);
				
			}
			catch(Exception g) {
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			WebElement element = driver.findElement(By.xpath("//*[contains(text(),'Receive')]"));
			js.executeScript("arguments[0].click();", element);
			youtubeTask();
			}

	}

	public static String readExcelData(int row, int column) throws Exception {
		
		String excelPath = "D:\\eclipse-workspace\\MGTASK\\src\\main\\resources\\ExcelData\\LoginDetails.xlsx";
		FileInputStream fis = new FileInputStream(excelPath);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheet("Sheet1");
		if(column == 1 || column == 3) {
			return sheet.getRow(row).getCell(column).getRawValue();
		}
		else
		return sheet.getRow(row).getCell(column).getStringCellValue();
}

	public static void screenShot() throws InterruptedException {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// String ScreenshotPath = "";

		try {
			FileHandler.copy(screenshot,
					new File(System.getProperty("user.dir") + "\\src\\main\\resources\\ScreenShots\\" + "ss" + ".png"));
			//System.out.println("Taken Screenshot");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void youtubeTask() throws NumberFormatException, Exception {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//*[contains(text(),'Open video link')]")).click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
			try {
		
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				WebElement likeButton = driver.findElement(By.xpath(
						"/html/body/ytd-app/div/ytd-page-manager/ytd-watch-flexy/div[5]/div[1]/div/div[6]/div[2]/ytd-video-primary-info-renderer/div/div/div[3]/div/ytd-menu-renderer/div/ytd-toggle-button-renderer[1]/a/yt-icon-button/button/yt-icon"));
				js.executeScript("arguments[0].click();", likeButton);
				screenShot();
				js.executeScript("arguments[0].click();", likeButton);
				driver.navigate().back();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//*[@id='recordList1']/div[1]/div[2]/a[1]")).click();
				Thread.sleep(1500);
				
				Runtime.getRuntime()
				.exec("D:\\eclipse-workspace\\MGTASK\\src\\main\\resources\\screenshotFIleScript.exe");
				Thread.sleep(3000);
				js.executeScript("arguments[0].click();",
						driver.findElement(By.xpath("//*[@id='recordList1']/div[1]/div[2]/a[2]")));
				//System.out.println("Task submitted");
				Thread.sleep(1000);
			}
			catch(Exception x) {
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
					screenShot();
					driver.navigate().back();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					driver.findElement(By.xpath("//*[@id='recordList1']/div[1]/div[2]/a[1]")).click();
					Thread.sleep(1500);
					
					Runtime.getRuntime()
							.exec("D:\\eclipse-workspace\\MGTASK\\src\\main\\resources\\screenshotFIleScript.exe");
					Thread.sleep(3000);
					js.executeScript("arguments[0].click();",
							driver.findElement(By.xpath("//*[@id='recordList1']/div[1]/div[2]/a[2]")));
					Thread.sleep(1000);
					
			}
			taskPage();
	}
	 public static void mail(int row) throws Exception {

	        // Recipient's email ID needs to be mentioned.
	        String to = readExcelData(row, 4);

	        // Sender's email ID needs to be mentioned
	        String from = "mgtaskautomation@gmail.com";

	        // Assuming you are sending email from through gmails smtp
	        String host = "smtp.gmail.com";

	        // Get system properties
	        Properties properties = System.getProperties();

	        // Setup mail server
	        properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", "465");
	        properties.put("mail.smtp.ssl.enable", "true");
	        properties.put("mail.smtp.auth", "true");

	        // Get the Session object.// and pass username and password
	        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

	            protected PasswordAuthentication getPasswordAuthentication() {

	                return new PasswordAuthentication("mgtaskautomation@gmail.com", "8294574633");

	            }

	        });

	        // Used to debug SMTP issues
	        session.setDebug(true);

	        try {
	            // Create a default MimeMessage object.
	            MimeMessage message = new MimeMessage(session);

	            // Set From: header field of the header.
	            message.setFrom(new InternetAddress(from));

	            // Set To: header field of the header.
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	            // Set Subject: header field
	            message.setSubject("Task Completion Mailer");

	            // Now set the actual message
	            message.setText("Your daily tasks have been completed.");

	            System.out.println("sending...");
	            // Send message
	            Transport.send(message);
	            System.out.println("Sent message successfully....");
	        } catch (Exception mex) {
	            mex.printStackTrace();
	        }

	    }
	public static void run() {
		try {
			login();
			}catch(Exception e) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//*[contains(text(),'Me')]")));
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'end'});",driver.findElement(By.xpath("//*[contains(text(),'Logout')]")));
				js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//*[contains(text(),'Logout')]")));
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				js.executeScript("arguments[0].click();",driver.findElement(By.xpath("//a[contains(text(),'Confirm')]")));
				run();
			}
	}
	public static void main(String[] args) throws Exception {
		try {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\src\\main\\resources\\drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("--headless");
		options.addArguments("user-data-dir=C:\\Users\\Satyam Kumar\\AppData\\Local\\Google\\Chrome\\User Data");

		options.addArguments("--disable-notifications");

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.get("https://apps.mgtask.in/");
		run();
		}catch(Exception E) {
			driver.quit();
			System.out.println("Restarted driver");
			main(args);
		}
}}
