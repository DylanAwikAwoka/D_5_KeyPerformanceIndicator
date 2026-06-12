package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility hashing password (SHA-256).
 *
 * Tujuan: password tidak tersimpan sebagai teks terbaca di database
 * (disembunyikan), TANPA mengubah tipe data kolom — hasil hash berupa
 * string heksadesimal yang tetap muat di kolom VARCHAR/String yang ada.
 *
 * Catatan: SHA-256 dipilih karena ringan & bawaan JDK (tanpa library
 * tambahan). Untuk produksi sebenarnya lebih baik memakai salt + bcrypt,
 * namun untuk kebutuhan demo ini sudah cukup menyembunyikan password.
 */
public final class PasswordHasher {

    // SHA-256 menghasilkan 32 byte = 64 karakter hex.
    private static final int HASH_HEX_LENGTH = 64;

    private PasswordHasher() {
        // Utility class — tidak untuk diinstansiasi.
    }

    /** Mengubah password mentah menjadi hash hex SHA-256. */
    public static String hash(String raw) {
        String input = (raw == null) ? "" : raw;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(HASH_HEX_LENGTH);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Praktiknya tidak akan terjadi: SHA-256 wajib ada di setiap JVM.
            throw new RuntimeException("Algoritma SHA-256 tidak tersedia.", e);
        }
    }

    /** True bila nilai sudah berupa hash SHA-256 (64 karakter hex). */
    public static boolean isHashed(String value) {
        if (value == null || value.length() != HASH_HEX_LENGTH) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            boolean isHex = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
            if (!isHex) {
                return false;
            }
        }
        return true;
    }

    /** Hash hanya bila belum berupa hash — mencegah double-hash saat update. */
    public static String hashIfNeeded(String value) {
        return isHashed(value) ? value : hash(value);
    }

    /**
     * Mencocokkan password mentah dengan nilai tersimpan.
     * Bila nilai tersimpan sudah hash, bandingkan hash-nya; bila masih
     * plaintext (data lama, transisi), bandingkan langsung.
     */
    public static boolean matches(String raw, String stored) {
        if (stored == null) {
            return false;
        }
        if (isHashed(stored)) {
            return hash(raw).equals(stored);
        }
        return stored.equals(raw);
    }
}
