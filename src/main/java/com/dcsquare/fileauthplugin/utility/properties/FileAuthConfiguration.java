/*
 *
 *  * Copyright 2013 dc-square GmbH
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package com.dcsquare.fileauthplugin.utility.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class serves as a wrapper of the configuration.
 *
 * @author Christian Goetz
 */
public class FileAuthConfiguration {

    private static final Logger LOGGER = LogManager.getLogger();

    private boolean isHashed;
    private boolean isSalted;
    private boolean isFirst;
    private String separationChar;
    private String algorithm;
    private int iterations;
    private String credentialFileName;
    private PropertiesConfiguration propertiesConfiguration;

    public FileAuthConfiguration(String configFileName) throws IOException, ConfigurationException {
        init(configFileName);
    }

    /**
     * Init is responsible for loading all properties of fileAuthConfiguration.properties
     *
     * @param pathname absolute path of the fileAuthConfiguration.properties
     * @throws ConfigurationException thrown if there is any problem with creating the credential file.
     * @throws IOException            is thrown when fileAuthConfiguration.properties is not found.
     */
    public void init(String pathname) throws ConfigurationException, IOException {
        final File configFile = new File(pathname);

        if (!configFile.exists() || !configFile.isFile()) {
            throw new FileNotFoundException("Configuration file not found");
        }
        propertiesConfiguration = new PropertiesConfiguration(configFile);

        credentialFileName = propertiesConfiguration.getString("filename");

        final File credentialFile = new File(configFile.getParent(), credentialFileName);

        if (!credentialFile.exists()) {
            credentialFile.createNewFile();
            LOGGER.warn("Credential file not found, new one created in " + credentialFile.getAbsolutePath());
        }

        isHashed = propertiesConfiguration.getBoolean("passwordHashing.enabled", true);
        iterations = propertiesConfiguration.getInt("passwordHashing.iterations", 1000000);
        algorithm = propertiesConfiguration.getString("passwordHashing.algorithm", "SHA-512");
        separationChar = propertiesConfiguration.getString("passwordHashingSalt.separationChar", "$");
        isSalted = propertiesConfiguration.getBoolean("passwordHashingSalt.enabled", true);
        isFirst = propertiesConfiguration.getBoolean("passwordHashingSalt.isFirst", true);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getCredentialFileName() {
        return credentialFileName;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isHashed() {
        return isHashed;
    }

    public boolean isSalted() {
        return isSalted;
    }

    public int getIterations() {
        return iterations;
    }

    public String getSeparationChar() {
        return separationChar;
    }


}
