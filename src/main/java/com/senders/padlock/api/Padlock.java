package com.senders.padlock.api;
import com.senders.padlock.api.constants.CharacterSet;

import java.util.Set;

public interface Padlock {
    Set<Object> getKeys();
    void addPassword(String key, String password);
    void deletePassword(String key);
    void updateKey(String oldKey, String newKey);
    void updatePassword(String key, String newPassword);
    String generatePassword(int length, CharacterSet charSet);
    void copyPassword(String key);
    void savePasswords(String fileName);
    void loadPasswords(String fileName);
}
