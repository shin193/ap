package Final_project;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Salted password storage with compatibility for existing library accounts. */
public final class Passwords {
    private static final String PREFIX = "$pbkdf2-sha256$";
    private static final int ITERATIONS = 210_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Passwords() {}

    public static String hash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return PREFIX + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt) + "$" +
                Base64.getEncoder().encodeToString(derive(password, salt, ITERATIONS));
    }

    public static boolean matches(String password, String stored) {
        if (password == null || stored == null) return false;
        if (!stored.startsWith(PREFIX)) {
            return MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8),
                    stored.getBytes(StandardCharsets.UTF_8));
        }
        try {
            String[] parts = stored.split("\\$");
            if (parts.length != 5) return false;
            int iterations = Integer.parseInt(parts[2]);
            if (iterations < 10_000 || iterations > 1_000_000) return false;
            byte[] salt = Base64.getDecoder().decode(parts[3]);
            byte[] expected = Base64.getDecoder().decode(parts[4]);
            return salt.length == 16 && expected.length == 32 &&
                    MessageDigest.isEqual(expected, derive(password, salt, iterations));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith(PREFIX);
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password hashing is unavailable", e);
        } finally {
            spec.clearPassword();
        }
    }
}
