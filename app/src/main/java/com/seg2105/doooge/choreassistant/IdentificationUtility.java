package com.seg2105.doooge.choreassistant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fredpan on 2017/11/25.
 */

class IdentificationUtility {

    private IdentificationUtility() {
    }

    public static String generateIdentification(String a, String b) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] aByte = a.getBytes();
        byte[] bByte = b.getBytes();
        md.update(concatenation(aByte, bByte));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).toUpperCase().substring(1));
        }
        return sb.toString();
    }

    public static String generateIdentification(String a, String b, String c) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] aByte = a.getBytes();
        byte[] bByte = b.getBytes();
        byte[] cByte = c.getBytes();
        md.update(concatenation(concatenation(aByte, bByte), cByte));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).toUpperCase().substring(1));
        }
        return sb.toString();

    }


    private static byte[] concatenation(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
