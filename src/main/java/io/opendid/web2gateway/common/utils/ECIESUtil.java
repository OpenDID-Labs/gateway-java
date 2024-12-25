package io.opendid.web2gateway.common.utils;

import cn.hutool.core.util.HexUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

public class ECIESUtil {
    private static ECParameterSpec ecParameterSpec;
    private static IESParameterSpec iesParamSpec;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
            parameters.init(new ECGenParameterSpec("secp256k1"));
            ecParameterSpec = parameters.getParameterSpec(ECParameterSpec.class);
            iesParamSpec = new IESParameterSpec(null, null, 128);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ECPrivateKey getECPrivateKey(String privateKey) throws Exception {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        BigInteger s = new BigInteger(privateKey, 16);
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(s, ecParameterSpec);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return (ECPrivateKey) kf.generatePrivate(ecPrivateKeySpec);
    }

    private static ECPublicKey getECPublicKey(String publicKey) throws Exception {

        publicKey = dealPublicKey(publicKey);
        String hexPublicKeyX = publicKey.substring(0, publicKey.length() / 2);
        String hexPublicKeyY = publicKey.substring(publicKey.length() / 2);

        byte[] publicKeyX = Hex.decode(hexPublicKeyX);
        byte[] publicKeyY = Hex.decode(hexPublicKeyY);
        ECPoint pubPoint = new ECPoint(new BigInteger(1, publicKeyX), new BigInteger(1, publicKeyY));
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(pubPoint, ecParameterSpec);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return (ECPublicKey) kf.generatePublic(pubSpec);
    }

    /**
     * encrypt
     *
     * @param publicKey
     * @param data
     * @return
     */
    public static String hexEncrypt(String publicKey, String data) {
        try {
            ECPublicKey ecPublicKey = getECPublicKey(publicKey);
            X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(ecPublicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec2);
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey2, iesParamSpec);
            byte[] result = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexUtil.encodeHexStr(result, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * decrypt
     *
     * @param privateKey
     * @param data
     * @return
     */
    public static String hexDecrypt(String privateKey, String data) {
        try {
            ECPrivateKey ecPrivateKey = getECPrivateKey(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey2, iesParamSpec);
            byte[] result = cipher.doFinal(HexUtil.decodeHex(data));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String dealPublicKey(String publicKey) {
        if (publicKey.startsWith("0x")) {
            publicKey = publicKey.substring(2);
        }
        StringBuilder zeroStr = new StringBuilder();
        for (int i = 0; i < 128 - publicKey.length(); i++) {
            zeroStr.append("0");
        }
        zeroStr.append(publicKey);
        return zeroStr.toString();
    }

//    public static void main(String[] args) {
//        String encryptStr = hexEncrypt("0x9d055e550bccdd53ff2c61505fa6ce8ca26a1227bc6f004173ef2a0c2a8cef58d04c57ea2046b1d21abcc846b9f5ac298385bf3c0fdd032149d7cdebcc60e0b",
//                "123456789");
//        System.out.println(encryptStr);
//        String originalStr = hexDecrypt("0xeaeae0286d26c699b30a2e85d4db8c672288d35653a0c71a984b2ee4543877bf",
//                encryptStr);
//        System.out.println(originalStr);
//    }
}
