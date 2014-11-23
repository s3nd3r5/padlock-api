package com.senders.padlock.api.services;

import java.io.InputStream;
import java.nio.file.Path;

public interface EncryptionService {
    void encryptFile(Path file);
    void decryptFile(Path file);
    InputStream readEncryptedFile(Path file);
}
