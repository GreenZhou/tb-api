package com.augurit.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

public class EncodeUtil {
    /**  加密算法 */
    public final static String hashAlgorithmName = "SHA-256";
    /**  循环次数 */
    public final static int hashIterations = 16;

    public static String encode(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }
}
