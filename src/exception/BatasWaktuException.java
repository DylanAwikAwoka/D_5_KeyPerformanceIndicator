package exception;

/**
 * User-defined exception untuk pelanggaran batas waktu operasional,
 * mis. Tutup Kasir setelah pukul 17:00.
 *
 * @author hp
 */
public class BatasWaktuException extends Exception {
    public BatasWaktuException(String message) {
        super(message);
    }
}
