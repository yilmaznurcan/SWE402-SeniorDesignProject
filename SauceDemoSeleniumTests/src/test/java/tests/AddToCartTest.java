package tests;

import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.InventoryPage;
import pages.LoginPage;
import utils.BaseTest;
import utils.ConfigReader;
import utils.WaitingTimes;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bu sınıf, SauceDemo uygulamasında ürün sepete ekleme senaryosunu test eder.
 * Farklı kullanıcı ve tarayıcı kombinasyonlarını parametreli olarak çalıştırır.
 */

@Epic("Ürün İşlemleri")
@Feature("Sepete Ekleme Fonksiyonu")
@Execution(ExecutionMode.CONCURRENT)
public class AddToCartTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(AddToCartTest.class);

    /**
     * Parametreli test: Tarayıcı ve kullanıcı bilgisine göre sepete ürün ekleme senaryosunu doğrular.
     */

    @ParameterizedTest(name = "🛒 AddToCart Testi - Tarayıcı: {0}, Kullanıcı: {1}")
    @MethodSource("getTestDataFromProperties")
    @Story("Kullanıcı ürünleri sepete ekler ve doğrular")
    @Description("Geçerli kullanıcı ile login olup, ürünü sepete ekledikten sonra sepette göründüğü doğrulanır.")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddToCart(String browser, String username, String password) {
        // Tarayıcı başlatılır
        setUp(browser);
        WaitingTimes.waitForDefaultTime();
        logger.info("🧪 [{}] Test Başladı - Tarayıcı: {}, Kullanıcı: {}", Thread.currentThread().getName(), browser, username);

        // Login işlemi yapılır
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);
        WaitingTimes.waitForDefaultTime();
        logger.info("🔐 Giriş yapıldı.");

        // Ürün sepete eklenir
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        String productName = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(productName);
        WaitingTimes.waitForDefaultTime();
        logger.info("📦 Ürün sepete eklendi: {}", productName);

        // Sepet ikonuna tıklanır
        inventoryPage.clickCartIcon();
        logger.info("🛍️ Sepet sayfasına gidildi.");
        WaitingTimes.waitForDefaultTime();

        // Doğrulamalar
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
        assertEquals("Your Cart", title.getText(), "❌ Sepet sayfası başlığı hatalı!");

        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']")));
        assertTrue(inventoryPage.isProductInCart(productName), "❌ Ürün sepette bulunamadı!");
        logger.info("✅ Ürün doğrulandı: {}", productName);

        logger.info("🏁 Test tamamlandı.\n");
    }

    /**
     * Her testten sonra tarayıcıyı kapatır.
     */
    @AfterEach
    void cleanUp() {
        tearDown();
    }

    /**
     * testdata.x satırlarını config dosyasından okuyarak kullanıcı ve tarayıcı bilgilerini parametre olarak döner.
     * Format: testdata.1=chrome,standard_user,secret_sauce
     */
    public static List<String[]> getTestDataFromProperties() {
        List<String[]> data = new ArrayList<>();
        Properties props = ConfigReader.getAllProperties();

        Set<String> keys = props.stringPropertyNames();
        for (String key : keys) {
            if (key.startsWith("testdata.")) {
                String value = props.getProperty(key);
                String[] parts = value.split(",");
                if (parts.length == 3) {
                    data.add(parts);
                }
            }
        }
        return data;
    }
}
