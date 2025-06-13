package com.example.boardstudy2.Utils;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;

public class KeyStoreUtil {
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String KEY_ALIAS = "mykey";
    private static final String KEYSTORE_PASSWORD = "changeit";  // 키스토어 비밀번호
    private static final String KEY_PASSWORD = "changeit";      // 키 비밀번호

    /**
     * KeyStore에 RSA 키쌍 생성 후 저장
     * @param keystorePath 저장할 키스토어 파일 경로
     * @throws Exception
     */
    public static void createKeyStoreWithKeyPair(String keystorePath) throws Exception {

        // 1. KeyStore 생성 (PKCS12 형식)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            // 기존 파일 있으면 로딩
            java.io.FileInputStream fis = new java.io.FileInputStream(keystorePath);
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
            fis.close();
        } catch (Exception e) {
            // 없으면 새로 생성
            keyStore.load(null, null);
        }

        // 2. KeyPair 생성
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 3. Self-signed certificate 생성 (KeyStore에 키를 저장하려면 인증서가 필요함)
        X509Certificate cert = generateSelfSignedCertificate(keyPair);

        // 4. KeyStore에 개인키 + 인증서 저장
        keyStore.setKeyEntry(KEY_ALIAS, keyPair.getPrivate(), KEYSTORE_PASSWORD.toCharArray(), new java.security.cert.Certificate[]{cert});

        // 5. KeyStore 파일 저장
        try (FileOutputStream fos = new FileOutputStream(keystorePath)) {
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
        }
    }

    /**
     * 자기 서명 인증서 생성 (간단 테스트용)
     */
    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 24 * 60 * 60 * 1000); // 1년

        X500Principal dnName = new X500Principal("CN=Test, OU=Test, O=Test, L=Seoul, ST=Seoul, C=KR");

        BigInteger certSerialNumber = new BigInteger(64, new SecureRandom());

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                dnName,
                certSerialNumber,
                startDate,
                endDate,
                dnName,
                keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }

    /**
     * KeyStore에서 개인키 읽기
     * @param keystorePath
     * @return PrivateKey
     * @throws Exception
     */
    public static PrivateKey loadPrivateKeyFromKeyStore(String keystorePath) throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystorePath)) {
            ks.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }
        Key key = ks.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        if (key instanceof PrivateKey) {
            return (PrivateKey) key;
        }
        throw new RuntimeException("개인키를 KeyStore에서 찾을 수 없습니다.");
    }

    /**
     * KeyStore에서 공개키 읽기
     * @param keystorePath
     * @return PublicKey
     * @throws Exception
     */
    public static PublicKey loadPublicKeyFromKeyStore(String keystorePath) throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystorePath)) {
            ks.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }
        java.security.cert.Certificate cert = ks.getCertificate(KEY_ALIAS);
        if (cert != null) {
            return cert.getPublicKey();
        }
        throw new RuntimeException("공개키를 KeyStore에서 찾을 수 없습니다.");
    }
}

