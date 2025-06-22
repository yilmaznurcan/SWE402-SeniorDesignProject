package tests;

// Allure raporlama etiketleri
import io.qameta.allure.*;
// Log4j2 kütüphanesi
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// JUnit test sonrası temizleme
import org.junit.jupiter.api.AfterEach;
// Parametrik test desteği
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
// WebDriver bekleme işlemleri
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
// Sayfa sınıfları
import pages.LoginPage;
import pages.InventoryPage;
import pages.CheckoutPage;
// Yardımcı sınıflar
import utils.BaseTest;
import utils.ConfigReader;
import utils.WaitingTimes;
// Paralel test yürütme
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
// Gerekli Java sınıfları
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Bu sınıf, SauceDemo sitesinde login > ürün sepete ekle > checkout sürecini
 * farklı kullanıcı ve tarayıcı kombinasyonlarıyla test eder.
 */

@Epic("Sipariş Süreci")
@Feature("Tam Satın Alma Akışı")
@Execution(ExecutionMode.CONCURRENT) // Testler paralel çalışsın
public class CheckoutTest extends BaseTest {

    // Logger tanımı
    private static final Logger logger = LogManager.getLogger(CheckoutTest.class);

    @ParameterizedTest(name = "[{index}] Tarayıcı: {0} | Kullanıcı: {1}")
    @MethodSource("getTestDataFromProperties")
    @Story("Kullanıcı, ürün satın alma işlemini tamamlar")
    @Description("Giriş sonrası bir ürün sepete eklenip checkout işlemi tamamlanır ve başarı mesajı doğrulanır.")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutProcess(String browser, String username, String password) {
        setUp(browser); // Tarayıcı başlatılır
        logger.info("🧪 [{}] Test Başladı -> Tarayıcı: {}, Kullanıcı: {}", Thread.currentThread().getName(), browser, username);

        // Sayfa nesneleri oluşturulur
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Giriş işlemi
        loginPage.login(username, password);
        WaitingTimes.waitForDefaultTime();
        logger.info("🔐 [{}] Giriş yapıldı.", Thread.currentThread().getName());

        // Ürün sepete eklenir
        String product = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(product);
        WaitingTimes.waitForDefaultTime();
        inventoryPage.clickCartIcon();
        WaitingTimes.waitForDefaultTime();

        // Checkout işlemleri
        checkoutPage.clickCheckout();
        String firstName = ConfigReader.get("first.name");
        String lastName = ConfigReader.get("last.name");
        String postalCode = ConfigReader.get("postal.code");
        logger.info("📋 [{}] Checkout bilgileri yüklendi: {} {} {}", Thread.currentThread().getName(), firstName, lastName, postalCode);
        checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        WaitingTimes.waitForDefaultTime();

        // Başarı mesajı doğrulaması
        String successMessage = new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        checkoutPage.successMessageLocator()
                )).getText();

        logger.info("🎯 [{}] Başarı mesajı: {}", Thread.currentThread().getName(), successMessage);
        assertEquals("Thank you for your order!", successMessage, "❌ Sipariş başarı mesajı yanlış!");

        logger.info("✅ [{}] Checkout testi tamamlandı.\n", Thread.currentThread().getName());
    }

    @AfterEach
    void cleanUp() {
        tearDown(); // Tarayıcı kapatılır
    }

    // -> MethodSource ile properties dosyasından test verisi çekme
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
