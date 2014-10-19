package com.senders.padlock.api.services;

import com.senders.padlock.api.constants.CharacterSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringServiceImplTest {
    RandomStringService stringService = new RandomStringServiceImpl();
    @Test (expected = IllegalArgumentException.class)
    public void testGenerateRandomStringWithNegativeLength(){
         stringService.generateRandomString(-1, CharacterSet.ALPHA);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGenerateRandomStringWithZeroLength(){
        stringService.generateRandomString(-1, CharacterSet.ALPHA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRandomStringWithNullCharSet(){
        stringService.generateRandomString(10,null);
    }

    @Test
    public void testGenerateRandomString(){
        String str = stringService.generateRandomString(10,CharacterSet.ALPHA);
        assertEquals(10,str.length());
    }

}