package com.toy.diary.comn.utils;

import com.toy.diary.comn.encrptn.kisa.KISA_SHA256;

public class EncryptUtils {

    private static String CHARSET = "utf-8";
    private static final String PBUserKey = "palnet!01234567890"; //16Byte로 설정
    private static final String DEFAULT_IV = "1234567890123456"; //16Byte로 설정

    private static byte pbUserKey[] = PBUserKey.getBytes();

    private static byte bszIV[] = DEFAULT_IV.getBytes();

    public static String sha256Encrypt(String str) {

        byte[] bytes = str.getBytes();
        byte[] pszDigest = new byte[32];
        KISA_SHA256.SHA256_Encrpyt(bytes, bytes.length, pszDigest);
        StringBuffer encrypted = new StringBuffer();
        for (int i = 0; i < 32; i++) {
            encrypted.append(String.format("%02x", pszDigest[i]));
        }
        return encrypted.toString();
    }
}
