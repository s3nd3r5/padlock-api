package com.senders.padlock.api.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class SEncryptionServiceTest {
    private static BigInteger TOKEN = BigInteger.valueOf(1234567890L);
    private EncryptionService encryptionService;
    private File testFile;
    private static String TEST_FILE_NAME = "unit-test.properties";

    @Before
    public void setup() throws URISyntaxException {
        encryptionService = new SEncryptionService("password",TOKEN);
        testFile = new File(TEST_FILE_NAME);
        if(testFile.exists()){
            testFile.delete();
        }
    }

    /**
     * You think of a good way to do this test: 4 bucks.
     */
    @Test
    public void testEncryptDecrypt() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("test","value");
        properties.store(new FileOutputStream(TEST_FILE_NAME), null);
        encryptionService.decryptFile(Paths.get(TEST_FILE_NAME));
        assertEquals("value",properties.getProperty("test"));
    }

    @After
    public void destroy() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_NAME));
    }
}