package com.senders.padlock.api.services;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.senders.sencryption.SEncryptor;
import com.senders.sencryption.SEncryptorImpl;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class SEncryptionService implements EncryptionService {
    private static final Logger logger = LoggerFactory.getLogger(SEncryptionService.class);
    private final SEncryptor sEncryptor;

    @Inject
    public SEncryptionService(@Named("access_key")String password, @Named("secret_key")BigInteger secret_key){
        MessageDigest digest;
        BigInteger token;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes("UTF-8"));
            token = new BigInteger(bytes);
        } catch (Exception e) {
            logger.error("Unable to create access key: ", e);
            throw new RuntimeException("Unable to create SEncryption service");
        }
        sEncryptor = new SEncryptorImpl(token,secret_key);
    }

    @Override
    public void encryptFile(File file) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
            byte[] encrypted = sEncryptor.encrypt(IOUtils.toByteArray(inputStream));
            outputStream = new FileOutputStream(file);
            outputStream.write(encrypted);
            outputStream.flush();
        } catch (Exception e){
            logger.warn("Unable to encrypt file",e);
            throw new RuntimeException("Unable to encrypt file");
        }finally {
            try { inputStream.close(); } catch (Exception ignore){}
            try { outputStream.close(); } catch (Exception ignore){}
        }
    }

    @Override
    public void decryptFile(File file) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            inputStream = new FileInputStream(file);
            byte[] decrypted = sEncryptor.decrypt(IOUtils.toByteArray(inputStream));
            outputStream = new FileOutputStream(file);
            outputStream.write(decrypted);
            outputStream.flush();
        } catch (Exception e){
            logger.warn("Unable to decrypt file",e);
            throw new RuntimeException("Unable to decrypt file");
        }finally {
            try { inputStream.close(); } catch (Exception ignore){}
            try { outputStream.close(); } catch (Exception ignore){}
        }
    }

    @Override
    public InputStream readEncryptedFile(File file) {
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
            byte[] decrypted = sEncryptor.decrypt(IOUtils.toByteArray(inputStream));
            return new ByteArrayInputStream(decrypted);
        } catch (Exception e){
            logger.warn("Unable to read and decrypt file",e);
            throw new RuntimeException("Unable to read and decrypt file");
        }finally {
            try { inputStream.close(); } catch (Exception ignore){}
        }
    }


}
