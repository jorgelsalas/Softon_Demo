package com.softonitg.jorge.softondemo.Crypto;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by Jorge on 15/05/2015.
 *Used to generate keys and encrypt passwords
 */
public class HashUtils {

    //Taken from: http://android-developers.blogspot.com/2013/02/using-cryptography-to-store-credentials.html
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        return keyGenerator.generateKey();
    }

    public static String encrypt(String pass, String salt) throws NoSuchAlgorithmException {
        String toEncrypt = salt + pass;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(toEncrypt.getBytes());
        return Base64.encodeToString(md.digest(), Base64.DEFAULT);
    }

}
