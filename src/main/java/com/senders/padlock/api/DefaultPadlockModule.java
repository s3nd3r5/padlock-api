package com.senders.padlock.api;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.senders.padlock.api.managers.ClipboardManager;
import com.senders.padlock.api.managers.PasswordManager;
import com.senders.padlock.api.managers.PasswordManagerImpl;
import com.senders.padlock.api.managers.SystemClipboardManager;
import com.senders.padlock.api.services.*;

import java.math.BigInteger;
import java.util.Properties;

public class DefaultPadlockModule extends AbstractModule{

    private final String access_key;
    private final BigInteger secret_key;

    public DefaultPadlockModule(String access_key, BigInteger secret_key){
        this.access_key = access_key;
        this.secret_key = secret_key;
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("access_key")).toInstance(access_key);
        bind(BigInteger.class).annotatedWith(Names.named("secret_key")).toInstance(secret_key);

        bind(RandomStringService.class).to(RandomStringServiceImpl.class);
        bind(EncryptionService.class).to(SEncryptionService.class);
        bind(new TypeLiteral<FileService<Properties>>(){}).to(PropertiesFileService.class);

        bind(ClipboardManager.class).to(SystemClipboardManager.class);
        bind(PasswordManager.class).to(PasswordManagerImpl.class);

        bind(Padlock.class).to(PadlockApi.class);
    }
}
