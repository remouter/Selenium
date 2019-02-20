/*
+++перейти на сайт http://google.com/ncr;
+++выполнить поиск по слову "selenium";
+++ожидается, что первый результат в выдаче ведет на какую-то страницу сайта seleniumhq.org. Если в начале поисковой выдачи появились какие-то виджеты, то их не учитывать (виджет Википедии, виджет "Top stories" и т.п.);

+++перейти на вкладку "Images";
+++ожидается, что первая картинка в выдаче как-либо относится к сайту seleniumhq.org;
+++вернуться на вкладку "All";
+++ожидается, что первый результат по-прежнему ведет на ту же страницу, что и на шаге 3.

Правила выполнения задания:
+++- решение задания должно быть выполнено на языке Java;
- тестовый сценарий должен  выполняться с помощью Selenium WebDriver. Поверх него разрешается использовать любой фреймворк или свою собственную обёртку, если посчитаете нужным;
- допускается использование  любых дополнительных инструментов, если потребуется;
- сценарий должен быть  выполнен в виде одного теста. Если надо, то в одном тесте  может быть несколько проверок;
- решение нужно выложить  на github.com и в ответ на задание  прислать ссылку на этот репозиторий;
- к решению следует  приложить инструкцию, как запустить  тест (у проверяющего задание  могут отсутствовать необходимые  инструменты).
*/

//javac -cp .;C:\\Users\\user\\Desktop\\Selenium1\\selenium-server-standalone-3.141.59.jar Task.java
//java -cp .;C:\\Users\\user\\Desktop\\Selenium1\\selenium-server-standalone-3.141.59.jar Task
//C:\\Users\\user\\Desktop\\Selenium1\\ - путь до папки


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import java.util.*;
import org.openqa.selenium.Keys;
import java.io.*;

public class Task{
	private static final String TARGET = "seleniumhq.org";
	private static final int NUMBER = 1;
	private static FileWriter out;

	public static void main(String[] args) throws Exception
	{	
		try{
			out = new FileWriter("result.txt");
			out.write("TEST CASE 1:\n");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.get("http://google.com/ncr");
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("selenium");
		searchBox.submit();		
		Thread.sleep(1000);

		try{			
			List<WebElement> elements = driver.findElements(By.className("g"));
			if (elements == null) System.out.println("Not found");
			else System.out.println("Found");
			
			boolean found = true;
			int k = 0;
			for(WebElement e : elements){
				WebElement classR = e.findElement(By.className("r"));
				if (classR == null) found = false;
				else{			
					WebElement tagA = classR.findElement(By.tagName("a"));
					if (tagA == null) found = false;
					else {				
						if(tagA.getAttribute("href").contains(TARGET)){
							System.out.println("Interisting page: " + tagA.getAttribute("href"));
							String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
							tagA.sendKeys(selectLinkOpeninNewTab);
							break;
						}
						else found = false;
					}
				}
				if(k == NUMBER && !found) out.write("Первый результат в выдаче ведет на какую-то страницу сайта seleniumhq.org: FAILED\n");
				if(k == NUMBER && found) out.write("Первый результат в выдаче ведет на какую-то страницу сайта seleniumhq.org: PASSED\n");
				k++;
			}
			
			
			
			//Find image
			found = true;
			driver.findElement(By.xpath("//*[@id=\"ow16\"]/a")).click();
			driver.findElement(By.xpath("//*[@id=\"lb\"]/div/a[1]")).click();
		
			WebElement imageGroup = driver.findElement(By.id("rg_s"));
			
			List<WebElement> images = imageGroup.findElements(By.tagName("div"));		
			
			for(int i = 1; i < images.size(); i++){
				WebElement tagA = images.get(0).findElement(By.xpath("//*[@id=\"rg_s\"]/div[" + i + "]/a[2]/div[2]"));
				if(tagA.getText().contains(TARGET)){					
					WebElement result = driver.findElement(By.xpath("//*[@id=\"rg_s\"]/div[" + i + "]/a[1]"));
					String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN); 
					result.sendKeys(selectLinkOpeninNewTab);
					break;
				}
				else found = false;
				
				if(i == NUMBER && !found) out.write("Первая картинка в выдаче как-либо относится к сайту seleniumhq.org: FAILED\n");
				if(i == NUMBER && found) out.write("Первая картинка в выдаче как-либо относится к сайту seleniumhq.org: PASSED\n");
			}			
			
			//Came back
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"hdtb-msb-vis\"]/div[1]/a")).click();	

			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Not found");			
		}		
	}
}