package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;
import java.util.Random;

public abstract class BaseTest {

    // Logger nesnesi: loglama işlemleri için
    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Her test thread'i için ayrı bir WebDriver nesnesi kullanılır
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Diğer test sınıflarının erişeceği driver getter metodu
    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Testin başında tarayıcıyı başlatır ve base URL'e yönlendirir.
     * @param browser "chrome" veya "edge" olarak belirtilmeli
     */
    public void setUp(String browser) {
        logger.info("[{}] Test için '{}' tarayıcı başlatılıyor...", Thread.currentThread().getName(), browser.toUpperCase());

        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                // Chrome için istenmeyen özellikleri devre dışı bırak
                chromeOptions.addArguments(
                        "--disable-notifications",
                        "--disable-popup-blocking",
                        "--disable-infobars",
                        "--disable-save-password-bubble",
                        "--no-default-browser-check",
                        "--no-first-run",
                        "--start-maximized",
                        "--password-store=basic",
                        "--disable-features=AutofillServerCommunication, PasswordManagerEnabled",
                        "--remote-allow-origins=*",
                        "--incognito"

                );
                // Her test için rastgele debugging portu ver (paralel çalışmalarda çakışmayı önlemek için)
                int port = 9222 + new Random().nextInt(1000); // 9222-10221 arası rastgele bir port
                chromeOptions.addArguments("--remote-debugging-port=" + port);

                // Otomasyon uzantılarını devre dışı bırak
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);

                driver = new ChromeDriver(chromeOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments(
                        "--disable-notifications",
                        "--disable-popup-blocking",
                        "--start-maximized"
                );
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                String msg = "Desteklenmeyen tarayıcı: " + browser;
                logger.error(msg);
                throw new IllegalArgumentException(msg);
        }
        driverThreadLocal.set(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        openBaseUrl();
    }

    // Ana URL'i açan yardımcı metod
    private void openBaseUrl() {
        String baseUrl = ConfigReader.get("url");
        logger.info(" [{}] Base URL açılıyor: {}", Thread.currentThread().getName(), baseUrl);
        getDriver().get(baseUrl);
    }

    /**
     * Test tamamlandıktan sonra tarayıcıyı kapatır.
     */
    public void tearDown() {
        if (getDriver() != null) {
            saveScreenshot(); // Test sonunda ekran görüntüsü al
            logger.info("[{}] Tarayıcı kapatılıyor...", Thread.currentThread().getName());
            getDriver().quit(); // Tarayıcıyı kapat
            driverThreadLocal.remove(); // ThreadLocal temizliği
        }
    }

    /**
     * Allure raporlaması için ekran görüntüsü alır.
     * @return ekran görüntüsünü byte[] olarak döner
     */
    @Attachment(value = "Ekran Görüntüsü", type = "image/png")
    public byte[] saveScreenshot() {
        try {
            return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.warn("Screenshot alınamadı: {}", e.getMessage());
            return new byte[0];
        }
    }
}