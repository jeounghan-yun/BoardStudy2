package com.example.boardstudy2.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtil {
    private static final String INSTANCE_TYPE = "RSA";

    /**
     * RSA 공개키로 평문 암호화
     * @param plainText
     * @param publicKey
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String rsaEncode (String plainText, String publicKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(INSTANCE_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, convertPublicKey(publicKey));

        byte[] plainTextByte = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return base64EncodeToString(plainTextByte);
    }

    /**
     * RSA 개인키로 암호문을 복호화
     * @param encryptedPlainText
     * @param privateKey
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String rsaDecode (String encryptedPlainText, String privateKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] encryptedPlainTextByte = Base64.getDecoder().decode(encryptedPlainText.getBytes());
        Cipher chipher = Cipher.getInstance(INSTANCE_TYPE);
        chipher.init(Cipher.DECRYPT_MODE, convertPrivateKey(privateKey));

        return new String(chipher.doFinal(encryptedPlainTextByte), StandardCharsets.UTF_8);
    }

    /**
     * Base64 인코딩된 문자열을 PublicKey 객체로 변환
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey convertPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(INSTANCE_TYPE);
        byte[] publicKeybyte = Base64.getDecoder().decode(publicKey.getBytes());

        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeybyte));
    }

    /**
     * Base64 인코딩된 문자열을 PrivateKey 객체로 변환
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey convertPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(INSTANCE_TYPE);
        byte[] privateKeybyte = Base64.getDecoder().decode(privateKey.getBytes());

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeybyte));
    }

    /**
     * 바이트 배열을 Base64 문자열로 인코딩
     * @param byteData
     * @return
     */
    public static String base64EncodeToString(byte[] byteData) {
        return Base64.getEncoder().encodeToString(byteData);
    }

}
