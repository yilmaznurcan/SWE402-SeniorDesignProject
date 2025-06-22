package utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Ortama göre doğru config dosyasını yükleyip erişim sağlar.
 */
public class ConfigReader {
    private static Properties properties = new Properties();

    // Static blok: sınıf ilk yüklenirken çalışır
    static {
        String env = System.getProperty("env", "dev"); // Varsayılan ortam: dev
        String fileName = "config/" + env + ".properties";
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException(fileName + " bulunamadı!");
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Config dosyası yüklenemedi: " + fileName);
        }
    }

    /**
     * Anahtar kelimeye karşılık gelen değeri döner.
     * @param key aranacak anahtar
     * @return config değeri
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
    /**
     * Tüm config değerlerini döner.
     */
    public static Properties getAllProperties() {
        return properties;
    }

}
