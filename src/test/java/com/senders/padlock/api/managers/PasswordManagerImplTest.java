package com.senders.padlock.api.managers;

import com.senders.padlock.api.services.FileService;
import com.senders.padlock.api.services.RandomStringService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PasswordManagerImplTest {
    private PasswordManager passwordManager;
    private FileService<Properties> fileService;
    private RandomStringService randomStringService;
    private String fileName;
    private Properties properties;
    @Before
    public void setup() throws Exception {
        fileService = mock(FileService.class);
        randomStringService = mock(RandomStringService.class);
        fileName = "file.properties";
        properties = mock(Properties.class);
        when(fileService.readFile(eq(fileName))).thenReturn(properties);
        passwordManager = new PasswordManagerImpl(fileService,randomStringService);
        passwordManager.load(fileName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithBlankKey(){
        passwordManager.set(StringUtils.SPACE,"valid pass");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithEmptyKey(){
        passwordManager.set(StringUtils.EMPTY,"valid pass");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithNullKey(){
        passwordManager.set(null,"valid pass");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithBlankPassword(){
        passwordManager.set("validKey",StringUtils.SPACE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithEmptyPassword(){
        passwordManager.set("validKey",StringUtils.EMPTY);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithNullPassword(){
        passwordManager.set("validKey",null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetPasswordWithNullKey(){
        passwordManager.get(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testGetPasswordWithBlankKey(){
        passwordManager.get(StringUtils.SPACE);
    }
    @Test (expected = IllegalArgumentException.class)
     public void testGetPasswordWithEmptyKey(){
        passwordManager.get(StringUtils.EMPTY);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testRemovePasswordWithNullKey(){
        passwordManager.remove(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testRemovePasswordWithBlankKey(){
        passwordManager.remove(StringUtils.SPACE);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testRemovePasswordWithEmptyKey(){
        passwordManager.remove(StringUtils.EMPTY);
    }

    @Test
    public void testGetKeysReturnsEmptyWhenNullIsReturnedFromProperties(){
        when(properties.keySet()).thenReturn(null);
        assertTrue(passwordManager.getKeys() != null);
        assertTrue(passwordManager.getKeys().isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testSaveThrowsRuntimeExceptionWhenWriteFileThrowsException(){
        doThrow(RuntimeException.class).when(fileService).writeFile(anyString(),any(Properties.class));
        passwordManager.save(fileName);
    }


}