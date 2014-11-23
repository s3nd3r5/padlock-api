package com.senders.padlock.api.services;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesFileService implements FileService<Properties> {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesFileService.class);
    private final EncryptionService encryptionService;
    @Inject
    public PropertiesFileService(EncryptionService encryptionService){
        this.encryptionService = encryptionService;
    }

    @Override
    public Properties readFile(String fileName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "File name cannot be blank");
        Properties properties = new Properties();
        try {
            Path f = Paths.get(fileName);
            properties.load(encryptionService.readEncryptedFile(f));
            return properties;
        } catch (Exception e) {
            logger.warn("Unable to read file: " + fileName, e);
        }
        throw new RuntimeException("Unable to read file");
    }

    @Override
    public void writeFile(String fileName, Properties content) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName),"File Name cannot be blank");
        Preconditions.checkArgument(null != content,"Content cannot be null");

        try {
            Path path = Paths.get(fileName);
            content.store(Files.newOutputStream(path), null);
            encryptionService.encryptFile(path);
        } catch (Exception e) {
            logger.error("Unable to save file: " + fileName,e);
            throw new RuntimeException("Error saving file:"  + fileName);
        }
    }
}
