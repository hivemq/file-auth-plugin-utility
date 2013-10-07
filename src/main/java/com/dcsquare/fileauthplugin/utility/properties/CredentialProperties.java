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
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import java.io.File;
import java.util.Iterator;

/**
 * This class contains all methods, which operate with the credential file.
 *
 * @author Christian Goetz
 */
public class CredentialProperties {

    private PropertiesConfiguration propertiesConfiguration;


    /**
     * Initialize properties file
     *
     * @param filename credential file name
     * @throws ConfigurationException is thrown if an error is encountered during loading of the file
     */
    public CredentialProperties(String filename) throws ConfigurationException {
        propertiesConfiguration = new PropertiesConfiguration();

        PropertiesConfigurationLayout propertiesConfigurationLayout = new PropertiesConfigurationLayout(propertiesConfiguration);
        propertiesConfigurationLayout.setGlobalSeparator(":");
        propertiesConfiguration.setFile(new File(filename));
        propertiesConfiguration.load();

    }

    /**
     * Add a user
     *
     * @param username username of the user, which should be created
     * @param password password, which should be set
     * @return true, if user doesn't exists and was created, false if user already exists.
     * @throws ConfigurationException is thrown if there is a problem during save
     */
    public boolean addUser(String username, String password) throws ConfigurationException {
        if (!propertiesConfiguration.containsKey(username)) {
            propertiesConfiguration.addProperty(username, password);
            propertiesConfiguration.save();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the user username with given password
     *
     * @param username    username of the user, which should be updated
     * @param newPassword new password, which should be set
     * @return true, if user exists and was updated, false if user doesn't exist.
     * @throws ConfigurationException is thrown if there is a problem during save
     */
    public boolean updateUser(String username, String newPassword) throws ConfigurationException {
        if (propertiesConfiguration.containsKey(username)) {
            propertiesConfiguration.setProperty(username, newPassword);
            propertiesConfiguration.save();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a user from the credential file
     *
     * @param username username
     * @return true, if user is existent and deleted, false if user doesn't exist.
     * @throws ConfigurationException is thrown if there is a problem during save
     */
    public boolean deleteUser(String username) throws ConfigurationException {
        if (propertiesConfiguration.containsKey(username)) {
            propertiesConfiguration.clearProperty(username);
            propertiesConfiguration.save();
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method lists all present users in the credential file
     *
     * @return amount of users
     */
    public int show() {
        final Iterator<String> keys = propertiesConfiguration.getKeys();
        int userCount = 0;
        while (keys.hasNext()) {
            userCount++;
            final String next = keys.next();
            System.out.println(next);
        }
        return userCount;
    }
}
