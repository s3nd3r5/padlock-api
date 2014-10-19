package com.senders.padlock.api.managers;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class SystemClipboardManager implements ClipboardManager {
    private final Clipboard clipboard;
    public SystemClipboardManager(){
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }
    @Override
    public void copy(String content) {
        Preconditions.checkArgument(null != content, "Content cannot be null");
        clipboard.setContents(new StringSelection(content),null);
    }

    @Override
    public void clear() {
        clipboard.setContents(new StringSelection(""),null);
    }
}
