package com.senders.padlock.api.managers;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SystemClipboardManagerTest {
    private ClipboardManager clipboard;

    @Before
    public void setup(){
        clipboard = new SystemClipboardManager();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCopyNullContent(){
        clipboard.copy(null);
    }

    @Test
    public void testCopyBlankContent() throws IOException, UnsupportedFlavorException {
        clipboard.copy(StringUtils.SPACE);
        String st = paste();
        assertEquals(StringUtils.SPACE,st);
    }

    @Test
    public void testCopyEmptyContent() throws IOException, UnsupportedFlavorException {
        clipboard.copy(StringUtils.EMPTY);
        String st = paste();
        assertEquals(StringUtils.EMPTY,st);
    }

    @Test
    public void testCopyContent() throws IOException, UnsupportedFlavorException {
        clipboard.copy("Some text");
        String st = paste();
        assertEquals("Some text",st);
    }

    @Test
    public void testClearContent() throws IOException, UnsupportedFlavorException {
        clipboard.copy("Some text");
        String st = paste();
        assertEquals("Some text",st);
        clipboard.clear();
        st = paste();
        assertEquals(StringUtils.EMPTY,st);
    }

    private String paste() throws IOException, UnsupportedFlavorException {
        String result = "";
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor)
                ;
        if (hasTransferableText) {
            result = (String)contents.getTransferData(DataFlavor.stringFlavor);
        }
        return result;
    }
}