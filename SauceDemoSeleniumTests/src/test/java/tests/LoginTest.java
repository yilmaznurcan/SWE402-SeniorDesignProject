package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.Properties;
import java.util.Set;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Bu sınıf, SauceDemo web uygulamasındaki login senaryolarını
 * farklı tarayıcılar ve kullanıcı kombinasyonlarıyla test eder.
 * Paralel ve parametrik test desteği içerir.
 */

@Execution(ExecutionMode.CONCURRENT)
public class LoginTest extends BaseTest {

    // Logger nesnesi
    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    /**
     * Farklı tarayıcı ve kullanıcı kombinasyonlarıyla login senaryolarını test eder.
     *
     * @param browser        Tarayıcı tipi (chrome, edge)
     * @param username       Giriş yapılacak kullanıcı adı
     * @param password       Kullanıcı şifresi
     * @param shouldSucceed  Girişin başarılı olup olmayacağına dair beklenen sonuç
     */
    @ParameterizedTest(name = "[{index}] Tarayıcı: {0}, Kullanıcı: {1}, Başarılı mı?: {3}")
    @MethodSource("getTestDataFromProperties")
    @Description("Kullanıcı login işlemleri: geçerli ve geçersiz senaryolar")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Login işlemi testi")
    void testLogin(String browser, String username, String password, boolean shouldSucceed) {
        setUp(browser); // Tarayıcı başlatılır

        LoginPage loginPage = new LoginPage(getDriver()); // Giriş işlemi
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        logger.info("🔐 [{}] Test Başladı -> Tarayıcı: {}, Kullanıcı: {}",
                Thread.currentThread().getName(), browser.toUpperCase(), username);

        loginPage.login(username, password);
        WaitingTimes.waitForDefaultTime();

        if (shouldSucceed) {
            // Başarılı giriş sonrası URL doğrulaması
            new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlContains("inventory"));
            String currentUrl = getDriver().getCurrentUrl();

            logger.info("✅ [{}] Başarılı giriş! Yönlendirilen URL: {}", Thread.currentThread().getName(), currentUrl);
            assertTrue(currentUrl.endsWith("/inventory.html"),
                    "Login başarılı olmalıydı ancak URL doğru değil.");
        } else {
            // Hatalı giriş sonrası hata mesajı kontrolü
            String errorMsg = loginPage.getErrorMessage();
            logger.info("⚠️ [{}] Hatalı giriş mesajı: {}", Thread.currentThread().getName(), errorMsg);
            assertTrue(errorMsg != null && !errorMsg.isEmpty(),
                    "Hatalı girişte hata mesajı bekleniyordu.");
        }

        logger.info("🧪 [{}] Test Tamamlandı -> {}\n", Thread.currentThread().getName(), browser.toUpperCase());
    }

    @AfterEach
    void cleanUp() {
        tearDown(); // Tarayıcı kapatılır
    }

    // -> MethodSource ile properties dosyasından test verisi çekme
    public static Stream<Arguments> getTestDataFromProperties() {
        Properties props = ConfigReader.getAllProperties();
        Set<String> keys = props.stringPropertyNames();

        return keys.stream()
                .filter(key -> key.startsWith("logindata."))
                .map(key -> props.getProperty(key))
                .map(value -> {
                    String[] parts = value.split(",");
                    String browser = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    boolean shouldSucceed = Boolean.parseBoolean(parts[3]);
                    return Arguments.of(browser, username, password, shouldSucceed);
                });
   }
}
