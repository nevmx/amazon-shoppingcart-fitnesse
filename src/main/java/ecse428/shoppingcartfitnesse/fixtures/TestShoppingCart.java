package ecse428.shoppingcartfitnesse.fixtures;

import org.eclipse.jdt.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestShoppingCart {
	private String itemName, itemQuantity, initialQuantity;
	
	private WebDriver wd;
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
		
		// Open a new browser
		wd = new FirefoxDriver();
		wd.get("http://www.amazon.ca");
		
		// If we have an initial quanitity needed, add it first
		if (this.initialQuantity != null) {
			final String itemNameFinal = this.itemName;
			// The following code avoids the StaleElementReferenceException
			new WebDriverWait(wd, 5)
		    .ignoring(StaleElementReferenceException.class)
		    .until(new Predicate<WebDriver>() {
		        public boolean apply(@Nullable WebDriver driver) {
		        	// Search for the item
		            WebElement searchInput = driver.findElement(By.id("twotabsearchtextbox"));
		            searchInput.click();
		            searchInput.sendKeys(itemNameFinal);
		    		searchInput.submit();
		            
		            return true;
		        }
		    });
			
			// Click the item name in the results
			WebElement itemResult = fluentWait(By.id("result_0"));
			itemResult.findElement(By.tagName("h2")).click();
						
			// Find the quantity dropdown
			Select quantityDropdown = new Select(fluentWait(By.id("quantity")));
			
			// Select the right quantity
			quantityDropdown.selectByValue(this.initialQuantity);
			
			// Add the item to the cart
			WebElement addToCart = fluentWait(By.id("add-to-cart-button"));
			addToCart.click();
		}
		
		// Go to the page for that item
		final String itemNameFinal = this.itemName;
		// The following code avoids the StaleElementReferenceException
		new WebDriverWait(wd, 5)
	    .ignoring(StaleElementReferenceException.class)
	    .until(new Predicate<WebDriver>() {
	        public boolean apply(@Nullable WebDriver driver) {
	        	// Search for the item
	            WebElement searchInput = driver.findElement(By.id("twotabsearchtextbox"));
	            searchInput.click();
	            searchInput.sendKeys(itemNameFinal);
	    		searchInput.submit();
	            
	            return true;
	        }
	    });
				
		// Click the item name in the results
		WebElement itemResult = fluentWait(By.id("result_0"));
		itemResult.findElement(By.tagName("h2")).click();
	}
	
	public void setInitialQuantity(String initialQuantity) {
		this.initialQuantity = initialQuantity;
	}
	
	public void setItemQuantity(String itemQuantity) {
		this.itemQuantity = itemQuantity;
		
		// Find the quantity dropdown
		Select quantityDropdown = new Select(fluentWait(By.id("quantity")));
		
		// Select the right quantity
		quantityDropdown.selectByValue(itemQuantity);
		
		// Add the item to the cart
		WebElement addToCart = fluentWait(By.id("add-to-cart-button"));
		addToCart.click();
	}
	
	public void setItemQuantityForOutOfStock(String itemQuantity) {
		this.itemQuantity = itemQuantity;
		
		// Add the item to the cart
		WebElement addToCart = fluentWait(By.id("add-to-wishlist-button-submit"));
		addToCart.click();
	}
	
	public String shoppingCartItemQuantity() {
		// Go to the shopping cart page
		fluentWait(By.id("hlb-view-cart-announce")).click();
		
		// Verify quantity of the item
		String quantityStr = fluentWait(By.className("a-dropdown-prompt")).getText();
		wd.quit();
		this.initialQuantity = null;
		return quantityStr;
	}
	
	public String emptyCart() {
		// Go to home
		WebElement homeLink = fluentWait(By.className("a-link-nav-icon"));
		homeLink.click();
		
		// Verify that the cart is empty
		String cartCount = fluentWait(By.id("nav-cart-count")).getText();
		
		// Close the browser
		wd.quit();
		
		if (cartCount.equals("0")) {
			return "yes";
		}
		return "no";
	}
	
	// Wait fluently for an element
	private WebElement fluentWait(final By locator) {
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(wd)
	            .withTimeout(30, TimeUnit.SECONDS)
	            .pollingEvery(2, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class);

	    WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
	        public WebElement apply(WebDriver driver) {
	            return driver.findElement(locator);
	        }
	    });

	    return  foo;
	}
	
	public void table(List<List<String>> table) throws InterruptedException {
		// Entry method - set property for gecko driver
		System.setProperty("webdriver.gecko.driver", "C:\\geckodriver-v0.14.0-win32\\geckodriver.exe");
	}
}
