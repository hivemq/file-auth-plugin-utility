package com.dcsquare.fileauthplugin.utility;

import com.dcsquare.fileauthplugin.utility.properties.CredentialProperties;
import com.dcsquare.fileauthplugin.utility.properties.FileAuthConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Christian Goetz
 */
public class CommandsTest {

    Commands commands = new Commands();
    TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test_command_configure_config_not_found() throws Exception {
        final String message = commands.configure("test");
        assertEquals(message, "Configuration file not found");
        assertNull(commands.credentialProperties);
        assertNull(commands.fileAuthConfiguration);
    }

    @Test
    public void test_command_configure_config_not_readable() throws Exception {
        temporaryFolder.create();
        final File file = temporaryFolder.newFile();
        file.setReadable(false);
        final String message = commands.configure(file.getAbsolutePath());
        assertEquals(message, "Error reading configuration, try again");
        assertNull(commands.credentialProperties);
        assertNull(commands.fileAuthConfiguration);
    }

    @Test
    public void test_command_configure_credential_file_not_found_and_creatable() throws Exception {
        String newLine = System.getProperty("line.separator");
        temporaryFolder.create();
        final File config = temporaryFolder.newFile();
        FileWriter fw = new FileWriter(config.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("filename:conf.properties" + newLine);
        bw.close();
        temporaryFolder.getRoot().setWritable(false);
        final String message = commands.configure(config.getAbsolutePath());
        assertEquals(message, "Error did not find credential file and creation was not possible");
        assertNull(commands.credentialProperties);
        assertNull(commands.fileAuthConfiguration);
    }

    @Test
    public void test_command_configure_credential_file_not_readable() throws Exception {
        String newLine = System.getProperty("line.separator");
        temporaryFolder.create();
        final File config = temporaryFolder.newFile();
        FileWriter fw = new FileWriter(config.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("filename:conf.properties" + newLine);
        bw.close();
        final File file = temporaryFolder.newFile("conf.properties");
        file.setReadable(false);
        final String message = commands.configure(config.getAbsolutePath());
        assertEquals("Error reading credentials, try again", message);
        assertNull(commands.credentialProperties);
        assertNotNull(commands.fileAuthConfiguration);
    }

    @Test
    public void test_command_configure_successful() throws Exception {
        String newLine = System.getProperty("line.separator");
        temporaryFolder.create();
        final File config = temporaryFolder.newFile();
        FileWriter fw = new FileWriter(config.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("filename:conf.properties" + newLine);
        bw.close();
        temporaryFolder.newFile("conf.properties");
        final String message = commands.configure(config.getAbsolutePath());
        assertEquals("Configuration successfully read!", message);
        assertNotNull(commands.credentialProperties);
        assertNotNull(commands.fileAuthConfiguration);
    }

    @Test
    public void test_checkAvailability_true() throws Exception {
        commands.fileAuthConfiguration = mock(FileAuthConfiguration.class);
        commands.credentialProperties = mock(CredentialProperties.class);
        final boolean b = commands.checkAvailability();
        assertTrue(b);
    }

    @Test
    public void test_checkAvailability_false_1() throws Exception {
        commands.fileAuthConfiguration = null;
        commands.credentialProperties = mock(CredentialProperties.class);
        final boolean b = commands.checkAvailability();
        assertFalse(b);
    }

    @Test
    public void test_checkAvailability_false_2() throws Exception {
        commands.fileAuthConfiguration = mock(FileAuthConfiguration.class);
        commands.credentialProperties = null;
        final boolean b = commands.checkAvailability();
        assertFalse(b);
    }

    @Test
    public void test_addUser_exception() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.addUser("test", "test")).thenThrow(new ConfigurationException("test"));

        final String message = commands.addUser("test", "test");
        assertEquals("Error during saving of the configuration:test", message);
    }

    @Test
    public void test_addUser_false() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.addUser("test", "test")).thenReturn(false);

        final String message = commands.addUser("test", "test");
        assertEquals("Username test already taken", message);
    }

    @Test
    public void test_addUser_true() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.addUser("test", "test")).thenReturn(true);

        final String message = commands.addUser("test", "test");
        assertEquals("User test added", message);
    }

    @Test
    public void test_updateUser_exception() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenThrow(new ConfigurationException("test"));

        final String message = commands.updateUser("test", "test");
        assertEquals("Error during saving of the configuration:test", message);
    }

    @Test
    public void test_updateUser_false() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenReturn(false);

        final String message = commands.updateUser("test", "test");
        assertEquals("User test not existent", message);
    }

    @Test
    public void test_updateUser_true() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenReturn(true);

        final String message = commands.updateUser("test", "test");
        assertEquals("User test updated", message);
    }

    @Test
    public void test_addOrUpdateUser_update_exception() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenThrow(new ConfigurationException("test"));

        final String message = commands.addOrUpdateUser("test", "test");
        assertEquals("Error during saving of the configuration:test", message);
    }

    @Test
    public void test_addOrUpdateUser_add_exception() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenReturn(false);
        when(credentialProperties.addUser("test", "test")).thenThrow(new ConfigurationException("test"));
        final String message = commands.addOrUpdateUser("test", "test");
        assertEquals("Error during saving of the configuration:test", message);
    }

    @Test
    public void test_addOrUpdateUser_user_created() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenReturn(false);
        when(credentialProperties.addUser("test", "test")).thenReturn(true);
        final String message = commands.addOrUpdateUser("test", "test");
        assertEquals("User test added", message);
    }

    @Test
    public void test_addOrUpdateUser_user_updated() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenReturn(true);
        final String message = commands.addOrUpdateUser("test", "test");
        assertEquals("User test updated", message);
    }

    @Test
    public void test_addOrUpdateUser_user_cannot_created() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.updateUser("test", "test")).thenReturn(false);
        when(credentialProperties.addUser("test", "test")).thenReturn(false);
        final String message = commands.addOrUpdateUser("test", "test");
        assertEquals("Could not save new user!", message);
    }

    @Test
    public void test_deleteUser_exception() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.deleteUser("test")).thenThrow(new ConfigurationException("test"));

        final String message = commands.deleteUser("test");
        assertEquals("Error during saving of the configuration:test", message);
    }

    @Test
    public void test_deleteUser_false() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.deleteUser("test")).thenReturn(false);

        final String message = commands.deleteUser("test");
        assertEquals("User test not existent", message);
    }

    @Test
    public void test_deleteUser_true() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.deleteUser("test")).thenReturn(true);

        final String message = commands.deleteUser("test");
        assertEquals("User test deleted", message);
    }

    @Test
    public void testListUsers() throws Exception {
        final CredentialProperties credentialProperties = mock(CredentialProperties.class);
        commands = new Commands4Test("test");
        commands.credentialProperties = credentialProperties;

        when(credentialProperties.show()).thenReturn(3);

        final String message = commands.listUsers();
        assertEquals("Listed 3 users", message);
    }

    @Test
    public void test_set_correct_salt_length() throws Exception {
        final String message = commands.setSaltLength("4");
        assertEquals("Salt length set to 4", message);
    }

    @Test
    public void test_salt_length_exception() throws Exception {
        final String message;
        message = commands.setSaltLength("a");
        assertEquals(message, "Error: The length should be an integer, using default salt length");
    }

    public class Commands4Test extends Commands {
        private String hashedString;

        public Commands4Test(String hashedString) {
            this.hashedString = hashedString;
        }

        @Override
        protected String getHashedString(String password) {
            return hashedString;
        }
    }
}
