package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage sınıfı, kullanıcı adı ve şifre girilerek giriş yapılan sayfayı temsil eder.
 */
public class LoginPage {

    private WebDriver driver;

    // Constructor - WebDriver nesnesi ile LoginPage başlatılır
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Giriş işlemi için gerekli locators
    private By usernameInput = By.id("user-name");
    private By passwordInput = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("[data-test='error']");

    /**
     * Kullanıcı adı ve şifre bilgilerini girerek login işlemini gerçekleştirir
     *
     * @param username Kullanıcı adı
     * @param password Şifre
     */
    @Step("Login işlemi: username = {username}, password = {password}")
    public void login(String username, String password) {
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    /**
     * Hatalı girişte görüntülenen hata mesajını döner
     *
     * @return Hata mesajı metni
     */
    @Step("Hata mesajı okunuyor")
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}
