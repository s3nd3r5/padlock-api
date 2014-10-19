package com.senders.padlock.api;

import com.senders.padlock.api.managers.ClipboardManager;
import com.senders.padlock.api.managers.PasswordManager;
import com.senders.padlock.api.managers.PasswordManagerImpl;
import com.senders.padlock.api.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigInteger;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class PadlockApiTest {

    private PasswordManager passwordManager;
    private ClipboardManager mockClipboard;
    private FileService<Properties> fileService;
    private RandomStringService randomStringService;
    private String fileName;
    private Padlock api;
    private BigInteger secret;

    @Before
    public void setup(){
        String password = "password";
        secret = BigInteger.valueOf(9137491373914l);
        fileService = new PropertiesFileService(new SEncryptionService(password,secret));
        randomStringService = new RandomStringServiceImpl();
        fileName = "./testFile.properties";
        passwordManager = new PasswordManagerImpl(fileService,randomStringService);
        mockClipboard = new TestClipboard();
        api = new PadlockApi(passwordManager,mockClipboard);
        api.addPassword("baseKey","baseValue");
        api.savePasswords(fileName);
    }

    @Test
    public void testSave(){
        api.addPassword("key1","test value");
        api.addPassword("key2","New Other Value");
        api.savePasswords(fileName);
        api.copyPassword("key1");
        assertEquals(((TestClipboard)mockClipboard).peek(),"test value");
    }

    @After
    public void destroy(){
        File f = new File(fileName);
        if(f.exists()) f.delete();
    }

    @Test
    public void testLoad(){
        api.loadPasswords(fileName);
        api.copyPassword("baseKey");
        assertEquals(((TestClipboard)mockClipboard).peek(),"baseValue");
    }



    private class TestClipboard implements ClipboardManager {
        private String content;

        @Override
        public void copy(String content) {
            this.content = content;
        }

        @Override
        public void clear() {
            this.content = null;
        }

        public String peek(){
            return this.content;
        }
    }
}