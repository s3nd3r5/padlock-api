package com.senders.padlock.api.services;

import com.senders.padlock.api.constants.CharacterSet;

public interface RandomStringService {
    String generateRandomString(int length, CharacterSet characterSet);
}
