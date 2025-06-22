# 🧪 SauceDemo Selenium Test Automation Project

## 📌 Overview

This project is a **Selenium WebDriver** based test automation framework developed for the [SauceDemo](https://www.saucedemo.com/) e-commerce website. It follows modern best practices in test automation such as:

- ✅ Page Object Model (POM)
- ✅ Parameterized cross-browser testing (Chrome & Edge)
- ✅ JUnit 5 test lifecycle
- ✅ Parallel test execution
- ✅ Centralized configuration and test data
- ✅ Allure reporting and Log4j2 logging

---

## 📁 Project Structure

```
SauceDemoSeleniumTests/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │    ├── pages/              # Page classes (LoginPage, InventoryPage, CheckoutPage)
│   │   │   └── utils/               # Utilities (BaseTest, ConfigReader, WaitingTimes)
│   │   │
│   │   └── resources/
│   │        └── log4j2.xml          # Log4j2 configuration file
│   │
│   └── test/
│       └── java/
│           └── tests/              # Test classes (LoginTest, AddToCartTest, CheckoutTest)
│
├── src/test/resources/
│   ├── config/                     # Environment-based config files (dev, test, prod)
│   └── junit-platform.properties   # JUnit parallel execution config
│
├── logs/                           # Log files (excluded via .gitignore)
├── allure-results/                 # Allure raw test results
├── allure-report/                  # Allure generated report
├── pom.xml                         # Maven dependencies and plugin setup
├── .gitignore                      # Files and folders excluded from version control
└── README.md                       # 📄 This file
```

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Maven 3.6+
- Chrome & Edge browsers installed
- IntelliJ IDEA (Community or Ultimate)

### ⚙️ How to Run Tests

```bash
# Run all tests using Maven
mvn clean test
```

To execute in **parallel**, the `junit-platform.properties` is already configured to run both test methods and classes concurrently.

### 🌐 Change Test Environment

You can switch between environments by updating the system property in Maven:

```bash
mvn clean test -Denv=dev
mvn clean test -Denv=test
mvn clean test -Denv=prod
```

> Default: `dev`

---

## 🧪 Test Scenarios

### ✔️ Login Tests (`LoginTest.java`)
- Valid credentials (multiple users)
- Invalid credentials (wrong password, locked out, invalid user)
- Empty fields and format checks

### 🛒 Cart Tests (`AddToCartTest.java`)
- Add items to cart
- Verify item visibility in cart
- Negative scenario: Adding out-of-stock items

### 💳 Checkout Tests (`CheckoutTest.java`)
- Complete checkout flow with valid inputs
- Missing information validations
- Empty cart checkout validation

---

## 🧱 Technologies Used

- **Java 17**
- **Selenium 4.13.0**
- **JUnit 5.9.1**
- **WebDriverManager 5.7.0**
- **Log4j2**
- **Allure Reporting**
- **Maven**
- **Page Object Model**

---

## 📊 Reporting

### 🧾 Allure Reports

```bash
# Generate and open Allure report
allure serve allure-results
```

Make sure [Allure CLI](https://docs.qameta.io/allure/#_installing_a_commandline) is installed.

---

## 📚 Configuration Files

- `src/test/resources/config/*.properties`: Test data, browser types, and credentials
- `junit-platform.properties`: Parallel test execution settings
- `pom.xml`: Manages dependencies and build lifecycle
- `log4j2.xml` (not shown here): Logging configuration (if added)

---

## ✅ Best Practices Followed

- Waits centralized via `WaitingTimes.java`
- Comments and documentation in all classes
- Separation of concerns: Pages, Tests, Utilities
- Environment-based config for flexible test runs
- `@ParameterizedTest` for clean and scalable input combinations

---

## 👩‍💻 Author

**Nurcan YILMAZ**  
Senior Software Engineering Student | Software QA Specialist & Business Analyst @Toyota&32Bit
📧 [nurcan.yilmaz5@ogr.sakarya.edu.tr]  
📍 Sakarya University

---

## 📝 License

This project is for educational and demonstrative purposes. No commercial use of SauceDemo is intended.
