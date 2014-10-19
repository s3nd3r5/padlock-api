package com.senders.padlock.api.managers;

import com.senders.padlock.api.constants.CharacterSet;

import java.util.Set;

public interface PasswordManager {
    void set(String key, String password);
    String get(String key);
    Set<Object> getKeys();
    void remove(String key);
    String generateNewPassword(int length, CharacterSet characterSet);
    void save(String fileName);
    void load(String fileName);
}
