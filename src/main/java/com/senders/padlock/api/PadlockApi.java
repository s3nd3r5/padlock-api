package com.senders.padlock.api;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.senders.padlock.api.constants.CharacterSet;
import com.senders.padlock.api.managers.ClipboardManager;
import com.senders.padlock.api.managers.PasswordManager;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PadlockApi implements Padlock {
    private static final long DELAY = 5l;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private final PasswordManager passwordManager;
    private final ClipboardManager clipboardManager;

    @Inject
    public PadlockApi(PasswordManager passwordManager, ClipboardManager clipboardManager){
        this.passwordManager = passwordManager;
        this.clipboardManager = clipboardManager;
    }

    @Override
    public Set<Object> getKeys() {
        Set<Object> keys = passwordManager.getKeys();
        return Objects.firstNonNull(keys,new HashSet<>());
    }

    @Override
    public void addPassword(String key, String password) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(password),"Password cannot be blank");
        passwordManager.set(key, password);
    }

    @Override
    public void deletePassword(String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        passwordManager.remove(key);
    }

    @Override
    public void updateKey(String oldKey, String newKey) {
        Preconditions.checkArgument(StringUtils.isNotBlank(oldKey),"Old Key cannot be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(newKey),"New Key cannot be blank");
        String password = passwordManager.get(oldKey);
        passwordManager.remove(oldKey);
        passwordManager.set(newKey,password);
    }

    @Override
    public void updatePassword(String key, String newPassword) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(newPassword),"Password cannot be blank");
        passwordManager.set(key,newPassword);
    }

    @Override
    public String generatePassword(int length, CharacterSet charSet) {
        Preconditions.checkArgument(length > 0, "Length must be greater than 0");
        Preconditions.checkArgument(null != charSet, "Character Set cannot be null");
        return passwordManager.generateNewPassword(length,charSet);
    }

    @Override
    public void copyPassword(String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key),"Key cannot be blank");
        String password = passwordManager.get(key);
        clipboardManager.copy(password);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule((Runnable) clipboardManager::clear, DELAY, UNIT);
        executorService.shutdown();
    }

    @Override
    public void savePasswords(String fileName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "FileName cannot be blank");
        passwordManager.save(fileName);
    }

    @Override
    public void loadPasswords(String fileName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "FileName cannot be blank");
        passwordManager.load(fileName);
    }
}
