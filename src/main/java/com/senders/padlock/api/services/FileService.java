package com.senders.padlock.api.services;

public interface FileService<T> {
    T readFile(String fileName);
    void writeFile(String fileName, T content);
}
