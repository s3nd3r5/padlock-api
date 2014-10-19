package com.senders.padlock.api.services;

import java.io.File;
import java.io.InputStream;

public interface EncryptionService {
    void encryptFile(File f);
    void decryptFile(File f);
    InputStream readEncryptedFile(File file);
}
