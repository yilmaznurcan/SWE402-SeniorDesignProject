package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitingTimes;

/**
 * InventoryPage sınıfı, ürün listeleme ve sepete ekleme işlemlerinin gerçekleştiği sayfayı temsil eder.
 */
public class InventoryPage {

    private WebDriver driver;

    // Constructor - Sayfa sınıfı oluşturulurken WebDriver enjekte edilir
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // Sepet simgesinin locatori
    private By cartIcon = By.className("shopping_cart_link");

    /**
     * Belirli bir ürünün "Add to Cart" butonunun locator'ını döner
     *
     * @param productName Ürün adı
     * @return By nesnesi
     */
    public By getAddToCartButtonByProductName(String productName) {
        return By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
    }

    /**
     * Belirtilen ürünü sepete ekler
     *
     * @param productName Eklenecek ürün adı
     */
    @Step("Ürün sepete ekleniyor: {productName}")
    public void addProductToCart(String productName) {
        WaitingTimes.waitForDefaultTime();
        driver.findElement(getAddToCartButtonByProductName(productName)).click();
    }

    /**
     * Sepet ikonuna tıklayarak sepet sayfasına geçiş yapar
     */
    @Step("Sepet ikonuna tıklanıyor")
    public void clickCartIcon() {
        driver.findElement(cartIcon).click();
    }

    /**
     * Belirli bir ürünün sepette olup olmadığını kontrol eder
     *
     * @param productName Ürün adı
     * @return boolean - ürün varsa true, yoksa false
     */
    @Step("Sepette ürün kontrol ediliyor: {productName}")
    public boolean isProductInCart(String productName) {
        By productLocator = By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']");
        return driver.findElements(productLocator).size() > 0;
    }


}
