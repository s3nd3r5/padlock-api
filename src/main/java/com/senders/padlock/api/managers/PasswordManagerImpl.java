package com.senders.padlock.api.managers;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.senders.padlock.api.constants.CharacterSet;
import com.senders.padlock.api.services.FileService;
import com.senders.padlock.api.services.RandomStringService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PasswordManagerImpl implements PasswordManager {
    private final Logger logger = LoggerFactory.getLogger(PasswordManagerImpl.class);
    private final FileService<Properties> fileService;
    private Properties properties;
    private final RandomStringService randomStringService;

    @Inject
    public PasswordManagerImpl(FileService<Properties> fileService, RandomStringService randomStringService){
        Preconditions.checkArgument(randomStringService != null, "Random String service cannot be null");
        Preconditions.checkArgument(fileService != null, "File Service cannot be null");
        this.fileService = fileService;
        this.randomStringService = randomStringService;
        this.properties = new Properties();
    }

    @Override
    public void set(String key, String password) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(password),"Password cannot be blank");
        properties.setProperty(key,password);
    }

    @Override
    public String get(String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        return properties.getProperty(key);
    }

    @Override
    public Set<Object> getKeys() {
        return Objects.firstNonNull(properties.keySet(),new HashSet<>());
    }

    @Override
    public void remove(String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        properties.remove(key);
    }

    @Override
    public String generateNewPassword(int length, CharacterSet characterSet) {
        Preconditions.checkArgument(length > 0, "Length must be greater than 0");
        Preconditions.checkArgument(null != characterSet, "Character Set cannot be null");
        return randomStringService.generateRandomString(length,characterSet);
    }

    @Override
    public void save(String fileName) {
        try{
            fileService.writeFile(fileName,properties);
        }catch (Exception e){
            logger.warn("Unable to save file: " + fileName,e);
            throw new RuntimeException("Unable to save file: " + fileName);
        }
    }

    @Override
    public void load(String fileName) {
        try{
            Properties newProperties = fileService.readFile(fileName);
            if(newProperties == null){
                throw new RuntimeException("Null properties were loaded for file: " + fileName);
            }
            properties = newProperties;
        }catch(Exception e){
            logger.warn("Unable to load file: " + fileName);
            throw new RuntimeException("Unable to load file: " + fileName);
        }
    }
}
