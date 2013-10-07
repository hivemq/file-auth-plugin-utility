package com.dcsquare.fileauthplugin.utility.properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Christian Goetz
 */
public class CredentialPropertiesTest {

    CredentialProperties credentialProperties;

    TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        temporaryFolder.create();
        final File file = temporaryFolder.newFile();
        credentialProperties = new CredentialProperties(file.getAbsolutePath());
    }

    @Test
    public void test_add_new_user() throws Exception {
        final boolean returnValue = credentialProperties.addUser("test", "test");
        assertTrue(returnValue);
    }

    @Test
    public void test_add_existing_user() throws Exception {
        credentialProperties.addUser("test", "test");
        final boolean returnValue = credentialProperties.addUser("test", "test");
        assertFalse(returnValue);
    }

    @Test
    public void test_update_existing_user() throws Exception {
        credentialProperties.addUser("test", "test");
        final boolean returnValue = credentialProperties.updateUser("test", "test1");
        assertTrue(returnValue);
    }

    @Test
    public void test_update_non_existing_user() throws Exception {
        final boolean returnValue = credentialProperties.updateUser("test", "test1");
        assertFalse(returnValue);
    }

    @Test
    public void test_delete_existing_user() throws Exception {
        credentialProperties.addUser("test", "test");
        final boolean returnValue = credentialProperties.deleteUser("test");
        assertTrue(returnValue);
    }

    @Test
    public void test_delete_non_existing_user() throws Exception {
        final boolean returnValue = credentialProperties.deleteUser("test");
        assertFalse(returnValue);
    }

    @Test
    public void test_show_users() throws Exception {
        credentialProperties.addUser("test", "test");
        int amountUsers = credentialProperties.show();
        assertEquals(1, amountUsers);
        credentialProperties.addUser("test1", "test");
        amountUsers = credentialProperties.show();
        assertEquals(2, amountUsers);
    }
}
