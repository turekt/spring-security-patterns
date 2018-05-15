package hr.foi.thesis;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.IvParameterSpec;

public class Consts {

    public static final byte[] SALT = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
    public static final byte[] IV_VALUE = new byte[] { 0x00, 0x01, 0x03, 0x03, 0x04, 0x0A, 0x01, 0x06, 0x02, 0x08, 0x0F, 0x05, 0x0B, 0x04, 0x0C, 0x0F };
    public static final IvParameterSpec IV_SPEC = new IvParameterSpec(IV_VALUE);

    public static final int ITERATION_COUNT = 1000;
    public static final int KEY_LENGTH = 128;

    public static final String ENCRYPTION_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = String.format("%s/CBC/PKCS5Padding", ENCRYPTION_ALGORITHM);
    public static final String DIGEST_ALGORITHM = "SHA-512";
    public static final String SIGNATURE_ALGORITHM = "SHA512withRSA";
    public static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";

    public static final String KEY_STORE_TYPE = "JKS";
    public static final String KEY_STORE_PATH = "thesis-keystore";
    public static final String KEY_STORE_ALIAS = "patterns";
    
    public static final String ROLE_USER_STRING = "ROLE_USER";
    public static final String ROLE_ADMIN_STRING = "ROLE_ADMIN";

    public static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority(ROLE_USER_STRING);
    public static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority(ROLE_ADMIN_STRING);
    
    public static final String TOKEN_PARAMETER = "token";
    public static final String SECRET_PARAMETER = "secret";
    public static final String NAME_PARAMETER = "name";
}
