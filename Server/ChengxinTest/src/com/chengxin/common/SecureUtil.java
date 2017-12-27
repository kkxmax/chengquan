
package com.chengxin.common;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class SecureUtil {
    private static CachedObjectService cachedObjectService = new CachedObjectService();
    
    private static String _executeProcess(String processPath) {
        String result = "";
        
        try { 
            Process process = Runtime.getRuntime().exec(processPath);
            
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line = null; 
            
            while ((line = bufferedReader.readLine()) != null) { 
                result += line + "\r\n";
            } 
        } catch (Exception e) { 
            System.err.println(e); 
        } 
        
        return result.trim();
    }

    public static String scriptFilter(String text) {

        String result = text;

        result = result.replaceAll("(?i)eval\\((.*)\\)", "");
        result = result.replaceAll("(?i)[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        result = result.replaceAll("(?i)script", "");

        return result;
    }
    public static String XSSFilter(Object sInvalid) {
        if (null == sInvalid) {
            return null;
        }

        return XSSFilter(sInvalid.toString());
    }

    public static String XSSFilter(String sInvalid) {
        String sValid = sInvalid;

        if (sValid == null || "".equals(sValid)) {
            return sValid;
        }

        sValid = sValid.replaceAll("&amp;", "#amp;");
        sValid = sValid.replaceAll("&", "&amp;");
        sValid = sValid.replaceAll("<", "&lt;");
        sValid = sValid.replaceAll(">", "&gt;");
        sValid = sValid.replaceAll("\"", "&quot;");
        sValid = sValid.replaceAll("\'", "&#039;");
        sValid = sValid.replaceAll("#amp;", "&amp;");

        return sValid;
    }

    public static String XSSUnfilter(String sInvalid) {
        String sValid = sInvalid;

        if (sValid == null || "".equals(sValid)) {
            return " ";
        }

        sValid = sValid.replaceAll("&amp;", "&");
        sValid = sValid.replaceAll("&lt;", "<");
        sValid = sValid.replaceAll("&gt;", ">");
        sValid = sValid.replaceAll("&quot;", "\"");
        sValid = sValid.replaceAll("&#039;", "\'");
        sValid = sValid.replaceAll("&middot;", "Â·");
        sValid = sValid.replaceAll("&nbsp;", " "); 

        return sValid;
    }

    public static String htmlViewOriginFilter(String sInvalid) {
        String sValid = sInvalid;

        if (sValid == null || "".equals(sValid)) {
            return " ";
        }

        sValid = sValid.replaceAll("<br />", "<br/>");
        sValid = sValid.replaceAll("#", "&nbsp;"); //FCKì—�ë””í„°ë¡œ ë“¤ì–´ì˜¬ ê²½ìš°ì—�ëŠ” ë¬¸ì œê°€ ë�¨(APIë¥¼ í†µí•´ì„œ ë°›ì�„ ê²½ìš°)

        return sValid;
    }
    public static String SQLInjectionFilter(Object sInvalid) {
        if (null == sInvalid) {
                return null;
        }

        return SQLInjectionFilter(sInvalid.toString());
    }

    public static String SQLInjectionFilter(String sInvalid) {
        String sValid = sInvalid;

        if (sValid == null || "".equals(sValid)) {
            return sValid;
        }

        sValid = sValid.replaceAll("[oO][rR]|[aA][nN][dD]|[;]|[:]|[-][-]", "");
        sValid = sValid.replaceAll("'", "''");

        return sValid;
    }

    public static String HttpResponseFilter(Object sInvalid) {
        if (null == sInvalid) {
            return null;
        }

        return HttpResponseFilter(sInvalid.toString());
    }

    public static String HttpResponseFilter(String sInvalid) {
        String sValid = sInvalid;

        if (sValid == null || "".equals(sValid)) {
            return sValid;
        }

        sValid = sValid.replaceAll("\\r", "");
        sValid = sValid.replaceAll("%0d", "");
        sValid = sValid.replaceAll("\\n", "");
        sValid = sValid.replaceAll("%0a", "");

        return sValid;
    }
    
    public static String getMD5(String str) {
        byte[] bpara = new byte[str.length()];
        byte[] rethash;

        for(int i = 0; i < str.length(); i++) {
            bpara[i] = (byte)(str.charAt(i) & 0xff);
        }

        try {
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            rethash = md5er.digest(bpara);
        } catch(GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        StringBuffer r = new StringBuffer(32);
        for(int i = 0; i < rethash.length; i++) {
            String x = Integer.toHexString(rethash[i] & 0xff).toUpperCase();
            if(x.length() < 2)
                r.append("0");

            r.append(x);
        }

        return r.toString();
    }

    public static String getSHA1(String str) {
        byte[] bpara = new byte[str.length()];
        byte[] rethash;

        for(int i = 0; i < str.length(); i++) {
            bpara[i] = (byte)(str.charAt(i) & 0xff);
        }

        try {
            MessageDigest sha1er = MessageDigest.getInstance("SHA1");
            rethash = sha1er.digest(bpara);
            rethash = sha1er.digest(rethash);
        } catch(GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        StringBuffer r = new StringBuffer(41);
        r.append("*");

        for(int i = 0; i < rethash.length; i++) {
            String x = Integer.toHexString(rethash[i] & 0xff).toUpperCase();
            if(x.length() < 2)
                r.append("0");

            r.append(x);
        }

        return r.toString();
    }
    
    public static String decrypt(String key, String value) throws Exception {
        return value;
    }
    
    public static String encrypt(String key, String value) throws Exception {
        return value;
    }
    
    public static String decrypt(String[] keyArray, String value) throws Exception {
        String _value = value;
        
        for(int i = keyArray.length - 1; i >= 0; i--) {
            _value = decrypt(keyArray[i], _value);
        }
        
        return _value;
    }
    
    public static String encrypt(String[] keyArray, String value) throws Exception {
        String _value = value;
        
        for(String key : keyArray) {
            _value = encrypt(key, _value);
        }
        
        return _value;
    }
    

    private static byte[] convertHexString(String value) {
        
        byte digest[] = new byte[value.length() / 2];
        
        for (int i = 0; i < digest.length; i++) {
            String byteString = value.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        
        return digest;
    }

    private static String toHexString(byte b[]) {
        
        StringBuilder stringBuilder = new StringBuilder();
        
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);

            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }

            stringBuilder.append(plainText);
        }
        
        return stringBuilder.toString();
    }
}
