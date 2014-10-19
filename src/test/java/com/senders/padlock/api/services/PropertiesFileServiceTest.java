package com.senders.padlock.api.services;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PropertiesFileServiceTest {
    private FileService<Properties> propertiesFileService;
    private EncryptionService encryptionService;

    @Before
    public void setup(){
        encryptionService = mock(EncryptionService.class);
        propertiesFileService = new PropertiesFileService(encryptionService);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testReadFileWithNullFileName(){
        propertiesFileService.readFile(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testReadFileWithEmptyFileName(){
        propertiesFileService.readFile(StringUtils.EMPTY);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testReadFileWithBlankFileName(){
        propertiesFileService.readFile(StringUtils.SPACE);
    }

    @Test
    public void testReadFile() throws Exception {
        Properties p = new Properties();
        p.setProperty("test", "propertyValue");
        p.store(new FileOutputStream("test-read-file.properties"), null);
        when(encryptionService.readEncryptedFile(any(File.class))).thenReturn(new FileInputStream("test-read-file.properties"));
        Properties properties = propertiesFileService.readFile("test-read-file.properties");
        assertEquals("propertyValue",properties.getProperty("test"));
    }

    @Test
    public void testWriteFile() throws Exception {
        File f = new File("test-write-file.properties");
        if(f.exists()) f.delete();
        Properties p = new Properties();
        p.setProperty("test", "propertyValue");
        doNothing().when(encryptionService).encryptFile(any(File.class));
        propertiesFileService.writeFile("test-write-file.properties", p);
        Properties properties = new Properties();
        properties.load(new FileInputStream("test-write-file.properties"));
        assertEquals("propertyValue",properties.getProperty("test"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWriteFileWithNullFileName(){
        propertiesFileService.writeFile(null,new Properties());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWriteFileWithEmptyFileName(){
        propertiesFileService.writeFile(StringUtils.EMPTY,new Properties());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWriteFileWithBlankFileName(){
        propertiesFileService.writeFile(StringUtils.SPACE,new Properties());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWriteFileWithNullContent(){
        propertiesFileService.writeFile("somefile",null);
    }

    @Test (expected = RuntimeException.class)
    public void testWriteFileThrowsException(){
        doThrow(new RuntimeException("Exception")).when(encryptionService).encryptFile(any(File.class));
        propertiesFileService.writeFile("test-write-file.properties",new Properties());
    }

    @After
    public void destroy(){
        new File("test-read-file.properties").delete();
        new File("test-write-file.properties").delete();
    }


}
