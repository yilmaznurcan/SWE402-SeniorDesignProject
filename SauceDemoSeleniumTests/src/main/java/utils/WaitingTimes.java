package utils; // Bu paketin adı dosyanın konumuna göre ayarlanmalı

/**
 * Sabit bekleme süresiyle testlerde zamanlama kontrolü sağlar.
 * Genellikle geçişler veya yavaş yüklenen sayfalar arasında kullanılır.
 */
public class WaitingTimes {

    // Sabit bekleme süresi (saniye cinsinden)
    private static final int DEFAULT_WAIT_SECONDS = 2;

    /**
     * Sabit süre kadar bekleme yapar.
     * (Thread.sleep kullanarak senkron bekleme yapılır)
     */
    public static void waitForDefaultTime() {
        try {
            Thread.sleep(DEFAULT_WAIT_SECONDS * 1000L); // 2 saniyelik bekleme
        } catch (InterruptedException e) {
            // Bekleme kesilirse hata loglanır ve thread yeniden işaretlenir
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Thread'i tekrar işaretle
        }
    }
}
