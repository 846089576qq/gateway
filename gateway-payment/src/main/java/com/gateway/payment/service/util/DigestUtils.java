package com.gateway.payment.service.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;

/**
 * 加解密算法工具类
 *
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月13日
 */
public class DigestUtils extends org.springframework.util.DigestUtils {

    /**
     * @param originalValue
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decodeBase64(String originalValue) throws UnsupportedEncodingException {
        Assert.notNull(originalValue, "the original value must not be null!");
        return new String(Base64.decodeBase64(originalValue), "UTF-8");
    }

    public static String encodeBase64(String originalValue) throws UnsupportedEncodingException {
        Assert.notNull(originalValue, "the original value must not be null!");
        return new String(Base64.encodeBase64(originalValue.getBytes("UTF-8")), "UTF-8");
    }

}
