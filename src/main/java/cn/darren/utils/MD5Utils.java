package cn.darren.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    /**
     * MD5摘要
     * @param source
     * @param salt
     * @return
     */
    public static String md5Digest(String source, Integer salt){
        char[] chars = source.toCharArray();
        //混淆源数据
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] + salt);
        }
        String target = new String(chars);
        String md5 = DigestUtils.md5Hex(target);
        return md5;
    }
}
