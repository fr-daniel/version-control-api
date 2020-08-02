package br.com.atlantico.versioncontrolapi.util;

import br.com.atlantico.versioncontrolapi.exception.VersionControlException;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Component
public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static  byte[] encrypt(String key, byte[] inputBytes)
            throws VersionControlException {
        return doCrypto(Cipher.ENCRYPT_MODE, key, inputBytes);
    }

    public static  byte[] decrypt(String key, byte[] inputBytes)
            throws VersionControlException {
        return doCrypto(Cipher.DECRYPT_MODE, key, inputBytes);
    }

    private static  byte[] doCrypto(int cipherMode, String key, byte[] inputBytes) throws VersionControlException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            return outputBytes;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException ex) {
            throw new VersionControlException(ex.getMessage());
        }
    }

}
