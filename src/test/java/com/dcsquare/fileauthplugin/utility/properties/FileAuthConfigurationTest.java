package com.dcsquare.fileauthplugin.utility.properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static org.junit.Assert.*;

/**
 * @author Christian Goetz
 */
public class FileAuthConfigurationTest {

    TemporaryFolder temporaryFolder = new TemporaryFolder();
    private FileAuthConfiguration authConfiguration;
    private File config;

    @Before
    public void setUp() throws Exception {

        temporaryFolder.create();
        String newLine = System.getProperty("line.separator");
        config = temporaryFolder.newFile();
        FileWriter fw = new FileWriter(config.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("filename:conf.properties" + newLine);
        bw.write("passwordHashing.enabled:false" + newLine);
        bw.write("passwordHashing.iterations:10" + newLine);
        bw.write("passwordHashing.algorithm:SHA-256" + newLine);
        bw.write("passwordHashingSalt.separationChar:%" + newLine);
        bw.write("passwordHashingSalt.enabled:false" + newLine);
        bw.write("passwordHashingSalt.isFirst:false" + newLine);
        bw.close();


    }

    @Test
    public void test_init_config_file() throws Exception {
        authConfiguration = new FileAuthConfiguration(config.getAbsolutePath());

        assertEquals("conf.properties", authConfiguration.getCredentialFileName());
        assertEquals(false, authConfiguration.isHashed());
        assertEquals(10, authConfiguration.getIterations());
        assertEquals("SHA-256", authConfiguration.getAlgorithm());
        assertEquals("%", authConfiguration.getSeparationChar());
        assertEquals(false, authConfiguration.isSalted());
        assertEquals(false, authConfiguration.isFirst());
    }

    @Test
    public void test_config_file_not_found() throws Exception {
        try {
            authConfiguration = new FileAuthConfiguration("test");
            fail();
        } catch (IOException e) {
            assertEquals(e.getMessage(), "Configuration file not found");
            assertTrue(e instanceof FileNotFoundException);
        }
    }
}
