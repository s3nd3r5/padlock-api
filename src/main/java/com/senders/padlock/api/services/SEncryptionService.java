package com.senders.padlock.api.services;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.senders.sencryption.SEncryptor;
import com.senders.sencryption.SEncryptorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
    public void encryptFile(Path file) {
        try{
            byte[] encrypted = sEncryptor.encrypt(Files.readAllBytes(file));
            Files.write(file,encrypted, StandardOpenOption.CREATE);
        } catch (Exception e){
            logger.warn("Unable to encrypt file",e);
            throw new RuntimeException("Unable to encrypt file");
        }
    }

    @Override
    public void decryptFile(Path file) {
        try{
            byte[] decrypted = sEncryptor.decrypt(Files.readAllBytes(file));
            Files.write(file,decrypted, StandardOpenOption.CREATE);
        } catch (Exception e){
            logger.warn("Unable to decrypt file",e);
            throw new RuntimeException("Unable to decrypt file");
        }
    }

    @Override
    public InputStream readEncryptedFile(Path file) {
        try{
            byte[] decrypted = sEncryptor.decrypt(Files.readAllBytes(file));
            return new ByteArrayInputStream(decrypted);
        } catch (Exception e){
            logger.warn("Unable to read and decrypt file",e);
            throw new RuntimeException("Unable to read and decrypt file");
        }
    }


}
