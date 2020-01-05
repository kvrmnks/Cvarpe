package com.kvrmnks.data;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Converter {

    final static Base64.Encoder encoder = Base64.getEncoder();
    final static Base64.Decoder decoder = Base64.getDecoder();

    public static String encode(String text) {
        byte[] textByte = new byte[0];
        textByte = text.getBytes(StandardCharsets.UTF_8);
        String encodedText = encoder.encodeToString(textByte);
        return encodedText;
    }

    public static String decode(String encodedText) {
        String text = null;
        text = new String(decoder.decode(encodedText), StandardCharsets.UTF_8);
        return text;
    }

}