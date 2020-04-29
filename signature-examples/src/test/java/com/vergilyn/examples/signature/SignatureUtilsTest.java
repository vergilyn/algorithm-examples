package com.vergilyn.examples.signature;

import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

/**
 * @author vergilyn
 * @date 2020-04-29
 */
public class SignatureUtilsTest{
    private String appKey = "app-00001";
    private String appSecret = "app-secret";
    private Map<String, String> data;

    @BeforeTest
    public void init(){
        data = Maps.newHashMap();
        data.put("field-01", "1");
        data.put("field-02", "2");
        data.put("field-03", "3");
        data.put("appKey", appKey);
        data.put(SignatureUtils.SignSpecialField.TIMESTAMP.field, System.currentTimeMillis() + "");
    }

    @Test
    public void generateSignature() {
        try {
            String signature = SignatureUtils.generateSignature(data, appSecret);
            System.out.println(signature);

            data.put(SignatureUtils.SignSpecialField.SIGN.field, signature);

            System.out.println(SignatureUtils.isSignatureValid(data, appSecret, SignatureUtils.SignType.MD5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void signatureValidTime() {
        // SignatureUtils.SING_VALID_TIME = 3000;  // 适当缩短好测试

        try {
            String signature = SignatureUtils.generateSignature(data, appSecret);
            System.out.println(signature);

            data.put(SignatureUtils.SignSpecialField.SIGN.field, signature);
            System.out.println(SignatureUtils.isSignatureValid(data, appSecret, SignatureUtils.SignType.MD5));

            new Semaphore(0).tryAcquire(SignatureUtils.SING_VALID_TIME, TimeUnit.MILLISECONDS);

            System.out.println(SignatureUtils.isSignatureValid(data, appSecret, SignatureUtils.SignType.MD5));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}