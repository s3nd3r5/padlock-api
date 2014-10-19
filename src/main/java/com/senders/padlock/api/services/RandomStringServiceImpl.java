package com.senders.padlock.api.services;

import com.google.common.base.Preconditions;
import com.senders.padlock.api.constants.CharacterSet;

import java.util.Random;

public class RandomStringServiceImpl implements RandomStringService {
    private final Random random;
    public RandomStringServiceImpl(){
        random = new Random(System.nanoTime());
    }

    @Override
    public String generateRandomString(int length, CharacterSet characterSet) {
        Preconditions.checkArgument(length > 0, "Length must be greater than 0");
        Preconditions.checkArgument(characterSet != null, "CharacterSet cannot be null");
        String str = "", strAdd;
        char[] chars = characterSet.getCharacters();
        for(int i = 0; i < length; i++){
            int ran = random.nextInt(chars.length);
            int ranCase = Math.abs(random.nextInt()) % 3;
            strAdd = chars[ran] + "";
            if(ranCase != 0)strAdd = strAdd.toLowerCase();
            str += strAdd;
            chars = randomize(chars);
        }
        return str;
    }



    private char[] randomize(char[] chars) {
        for(int i = 0; i < chars.length; i++){
            int ran = random.nextInt(chars.length);
            char tmp = chars[i];
            chars[i] = chars[ran];
            chars[ran] = tmp;
        }
        return chars;
    }
}
