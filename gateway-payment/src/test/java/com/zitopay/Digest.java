package com.zitopay;

import java.io.UnsupportedEncodingException;

import com.zitopay.foundation.common.util.DigestUtils;

public class Digest {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(DigestUtils.encodeBase64("Alwaypay123"));
	}
	
}
