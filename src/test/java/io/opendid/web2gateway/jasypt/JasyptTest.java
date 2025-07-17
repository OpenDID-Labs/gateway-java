package io.opendid.web2gateway.jasypt;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.junit.jupiter.api.Test;

public class JasyptTest {



  @Test
  public void testJasypt() {

    // Configuration parameters
    String input = "LoTsrcg5Bnvr";
    String password = "EFsMHMN4DSZYTrhdKe";
    String algorithm = "PBEWITHHMACSHA512ANDAES_256";
    int keyObtentionIterations = 1000;

    // Creating an Encryptor
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    // Setting encryption parameters
    encryptor.setPassword(password);
    encryptor.setAlgorithm(algorithm);
    encryptor.setKeyObtentionIterations(keyObtentionIterations);
    encryptor.setIvGenerator(new RandomIvGenerator());
    encryptor.setSaltGenerator(new RandomSaltGenerator());

    // Encryption
    String encryptedText = encryptor.encrypt(input);

    // Output
    System.out.println("Result:");
    System.out.println(encryptedText);

    // Optional: Verify decryption
    String decryptedText = encryptor.decrypt(encryptedText);
    System.out.println("\nDecryption Verification:");
    System.out.println("Raw Input: " + input);
    System.out.println("Decryption results: " + decryptedText);
    System.out.println("Verify that it matches: " + input.equals(decryptedText));

  }

}
