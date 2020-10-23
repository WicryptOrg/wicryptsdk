package com.ugarsoft.wicryptsdk_android.utils;

import org.apache.commons.codec.binary.Base32;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;

public class DefaultCodeGenerator {
    private final HashingAlgorithm algorithm;
    private final int digits;

    public DefaultCodeGenerator() {
        this(HashingAlgorithm.SHA1, 6);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm) {
        this(algorithm, 6);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm, int digits) {
        if (algorithm == null) {
            throw new InvalidParameterException("HashingAlgorithm must not be null.");
        } else if (digits < 1) {
            throw new InvalidParameterException("Number of digits must be higher than 0.");
        } else {
            this.algorithm = algorithm;
            this.digits = digits;
        }
    }

    public String generate(String key, long counter) throws CodeGenerationException {
        try {
            byte[] hash = this.generateHash(key, counter);
            return this.getDigitsFromHash(hash);
        } catch (Exception var5) {
            throw new CodeGenerationException("Failed to generate code. See nested exception.", var5);
        }
    }

    private byte[] generateHash(String key, long counter) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] data = new byte[8];
        long value = counter;

        for(int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte)((int)value);
        }

        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(key);
        SecretKeySpec signKey = new SecretKeySpec(decodedKey, this.algorithm.getHmacAlgorithm());
        Mac mac = Mac.getInstance(this.algorithm.getHmacAlgorithm());
        mac.init(signKey);
        return mac.doFinal(data);
    }

    private String getDigitsFromHash(byte[] hash) {
        int offset = hash[19] & 15;
        long truncatedHash = 0L;

        for(int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (long)(hash[offset + i] & 255);
        }

        truncatedHash &= 2147483647L;
        truncatedHash = (long)((double)truncatedHash % Math.pow(10.0D, (double)this.digits));
        return String.format("%0" + this.digits + "d", truncatedHash);
    }
}
