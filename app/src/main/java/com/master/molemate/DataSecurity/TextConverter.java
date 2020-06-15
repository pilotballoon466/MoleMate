package com.master.molemate.DataSecurity;

import android.util.Log;

import java.lang.Math;
import java.nio.charset.StandardCharsets;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TextConverter {

    private static final String TAG = "TextConverter";
    private static String key;

    public TextConverter(String tmpKey){
        key = tmpKey.trim();

        Log.d(TAG, "TextConverter: key " + key);
    }

    public String decrypt(String encryptedText) throws Exception {
        String decryptedText="";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decordedValue = Base64.decode(encryptedText.getBytes(), Base64.DEFAULT);
            byte[] decrypted=cipher.doFinal(decordedValue);
            String decryptedValueString = new String(decrypted);
            decryptedText= new String(Base64.decode(decryptedValueString, Base64.DEFAULT));

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return decryptedText;
    }

    public String encrypt(String decryptedText) throws Exception {
        String encryptedText="";

        try {
            String message=Base64.encodeToString(decryptedText.getBytes(),Base64.DEFAULT);
            SecretKeySpec skeyspec=new SecretKeySpec(key.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(message.getBytes());
            encryptedText=Base64.encodeToString(encrypted, Base64.DEFAULT);;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return encryptedText;
    }
}