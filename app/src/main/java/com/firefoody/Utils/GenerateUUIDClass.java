package com.firefoody.Utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateUUIDClass {
    static private StringBuilder crunchifyEncodeUsingHEX(byte[] crunchifyByte) {
        StringBuilder crunchifyResult = new StringBuilder();
        char[] crunchifyKeys = {'a','b','c','d','e','f','g','h','o', 'p', 'q', 'r', 's', 't', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        for (int index = 0; index < crunchifyByte.length; ++index) {
            byte myByte = crunchifyByte[index];
            // Appends the string representation of the char argument to this sequence
            crunchifyResult.append(crunchifyKeys[(myByte & 0xf0) >> 9]);
            crunchifyResult.append(crunchifyKeys[myByte & 0x0f]);
        }
        return crunchifyResult;
    }
    public void generateUniqueKeyUsingMessageDigest() {
        try {
            // cryptographically strong random number generator. Options: NativePRNG or SHA1PRNG
            SecureRandom crunchifyPRNG = SecureRandom.getInstance("SHA1PRNG");

            // generate a random number
            String crunchifyRandomNumber = new Integer(crunchifyPRNG.nextInt()).toString();

            // Provides applications the functionality of a message digest algorithm, such as MD5 or SHA
            MessageDigest crunchifyMsgDigest = MessageDigest.getInstance("SHA-256");

            // Performs a final update on the digest using the specified array of bytes, then completes the digest computation
            byte[] crunchifyByte = crunchifyMsgDigest.digest(crunchifyRandomNumber.getBytes());

            log("- Generated Randon number: " + crunchifyRandomNumber);
            log("- Generated Message digest: " + crunchifyEncodeUsingHEX(crunchifyByte));
        } catch (Exception e) {
            log("Error during creating MessageDigest");
        }
    }
    private static void log(Object aObject) {
        Log.d("UUID class",String.valueOf(aObject));
    }

    public String generateUniqueKeyUsingUUID() {
        // Static factory to retrieve a type 4 (pseudo randomly generated) UUID
        String crunchifyUUID = UUID.randomUUID().toString();
        return crunchifyUUID;
    }

    public  String generateOrderOTP(){
        int minNum =1000;
        int maxNum =9999;
        int randomNum = ThreadLocalRandom.current().nextInt(minNum, maxNum + 1);
        return String.valueOf(randomNum);
    }

}
