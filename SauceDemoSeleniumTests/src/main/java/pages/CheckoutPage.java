package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitingTimes;

/**
 * CheckoutPage sınıfı, kullanıcı checkout adımlarını gerçekleştirirken etkileşime geçilen web öğelerini ve işlemleri tanımlar.
 */
public class CheckoutPage {

    private WebDriver driver;

    // Constructor - CheckoutPage nesnesi oluşturulurken WebDriver enjekte edilir
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // Web element locator tanımlamaları
    private By checkoutButton = By.id("checkout");
    private By firstNameInput = By.id("first-name");
    private By lastNameInput = By.id("last-name");
    private By postalCodeInput = By.id("postal-code");
    private By continueInput = By.id("continue");
    private By finishButton = By.id("finish");
    private By successMessage = By.className("complete-header");

    /**
     * Checkout butonuna tıklar
     */
    @Step("Checkout butonuna tıklanıyor")
    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }

    /**
     * Checkout form bilgilerini doldurur
     *
     * @param firstName   Ad
     * @param lastName    Soyad
     * @param postalCode  Posta Kodu
     */
    @Step("Checkout bilgileri dolduruluyor: {firstName} {lastName}, {postalCode}")
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        WaitingTimes.waitForDefaultTime();
        driver.findElement(firstNameInput).sendKeys(firstName);
        WaitingTimes.waitForDefaultTime();
        driver.findElement(lastNameInput).sendKeys(lastName);
        WaitingTimes.waitForDefaultTime();
        driver.findElement(postalCodeInput).sendKeys(postalCode);
        WaitingTimes.waitForDefaultTime();
    }

    /**
     * Continue butonuna tıklar
     */
    @Step("Devam et (Continue) butonuna tıklanıyor")
    public void clickContinue() {
        WaitingTimes.waitForDefaultTime();
        driver.findElement(continueInput).click();
    }

    /**
     * Finish (Siparişi tamamla) butonuna tıklar
     */
    @Step("Siparişi tamamla (Finish) butonuna tıklanıyor")
    public void clickFinish() {
        WaitingTimes.waitForDefaultTime();
        driver.findElement(finishButton).click();
    }

    /**
     * Sipariş başarı mesajını döner
     *
     * @return Başarı mesajı metni
     */
    @Step("Sipariş başarı mesajı alınıyor")
    public String getSuccessMessage() {
        return driver.findElement(successMessage).getText();
    }

    /**
     * Başarı mesajı locator'ını döner
     *
     * @return By nesnesi
     */
    public By successMessageLocator() {
        return successMessage;
    }

}
