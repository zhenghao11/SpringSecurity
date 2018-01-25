package com.my.security.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

/**
 * @Author hzheng
 * @Date 2017/12/9
 */
public class Md5Util implements PasswordEncoder {
    /**
     * 改用MD5加密
     * @param s
     * @return
     */
    public String encode(CharSequence s) {
        if(StringUtils.isEmpty(((String)s).trim())) return  null;
        try {
            byte[] btInput = ((String)s).getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16){
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5加密失败，请重试!");
        }
    }

    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(encode(charSequence));
    }
}
