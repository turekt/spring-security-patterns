package hr.foi.thesis.util;

public class HexUtil {

    public static byte[] hexToBytes(String saltString) {
        int length = saltString.length();
        int radix = 16;
        byte[] salt = new byte[length/2];

        for(int i = 0; i < length; i += 2) {
            salt[i/2] = (byte) ((Character.digit(saltString.charAt(i), radix) << 4)
                    + Character.digit(saltString.charAt(i + 1), radix));
        }

        return salt;
    }

    public static String bytesToHex(byte[] encoded) {
        StringBuilder builder = new StringBuilder();
        for(byte b : encoded) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }
}
