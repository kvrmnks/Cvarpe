package com.kvrmnks.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String text) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("MD5").digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String md5 = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5.length(); i++)
            md5 = "0" + md5;
        return md5;
    }

    private static char[] hexChar = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String getHash(String fileName, String hashType) throws IOException, NoSuchAlgorithmException {

        File f = new File(fileName);
        /*
        System.out.println(" -------------------------------------------------------------------------------");
        System.out.println("|当前文件名称:"+f.getName());
        System.out.println("|当前文件大小:"+(f.length()/1024/1024)+"MB");
        System.out.println("|当前文件路径[绝对]:"+f.getAbsolutePath());
        System.out.println("|当前文件路径[---]:"+f.getCanonicalPath());
        System.out.println(" -------------------------------------------------------------------------------");
        */
        InputStream ins = new FileInputStream(f);

        byte[] buffer = new byte[8192];
        MessageDigest md5 = MessageDigest.getInstance(hashType);

        int len;
        while ((len = ins.read(buffer)) != -1) {
            md5.update(buffer, 0, len);
        }

        ins.close();

        return toHexString(md5.digest());
    }


    protected static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String getMD5OfFile(String file) {
        try {
            return getHash(file, "MD5");
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
