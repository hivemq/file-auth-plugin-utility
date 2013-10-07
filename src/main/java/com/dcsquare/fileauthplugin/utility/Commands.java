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

package com.dcsquare.fileauthplugin.utility;

import com.dcsquare.fileauthplugin.utility.hashing.PasswordHasher;
import com.dcsquare.fileauthplugin.utility.properties.CredentialProperties;
import com.dcsquare.fileauthplugin.utility.properties.FileAuthConfiguration;
import com.google.common.base.Charsets;
import org.apache.commons.configuration.ConfigurationException;
import org.bouncycastle.util.encoders.Base64;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * All available commands are defined in this class.
 *
 * @author Christian Goetz
 */
@Component
public class Commands implements CommandMarker {

    FileAuthConfiguration fileAuthConfiguration;
    CredentialProperties credentialProperties;
    private static int DEFAULT_SALT_LENGTH = 50;

    int saltLength = DEFAULT_SALT_LENGTH;


    /**
     * This method sets the path to the configuration file.
     *
     * @param path to configuration file
     * @return message if loading of file was successful or not
     */
    @CliCommand(value = "configure", help = "reads File Authentication Plugin Configuration File")
    public String configure(
            @CliOption(key = {"file"}, mandatory = true, help = "The absolute path to the fileAuthentication.properties file") final String path) {
        try {
            fileAuthConfiguration = new FileAuthConfiguration(path);
        } catch (ConfigurationException e) {
            return "Error reading configuration, try again";
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e) {
            return "Error did not find credential file and creation was not possible";
        }

        final File file = new File(new File(path).getParent(), fileAuthConfiguration.getCredentialFileName());

        try {
            credentialProperties = new CredentialProperties(file.getAbsolutePath());
        } catch (ConfigurationException e) {
            return "Error reading credentials, try again";
        }

        return "Configuration successfully read!";
    }


    /**
     * This method ensures that it is only possible to add, update,
     * delete or list users if the configuration file was found.
     *
     * @return true, if the configuration file is loaded, false if not.
     */
    @CliAvailabilityIndicator({"addUser", "addOrUpdateUser", "listUsers", "deleteUser"})
    public boolean checkAvailability() {
        if (fileAuthConfiguration == null || credentialProperties == null) {
            return false;
        }
        return true;

    }

    /**
     * Add User Command
     *
     * @param username username
     * @param password plaintext password
     * @return message if adding the user was successful or not
     */
    @CliCommand(value = "addUser", help = "adds a user in the credential file")
    public String addUser(
            @CliOption(key = {"username"}, mandatory = true, help = "The username") final String username,
            @CliOption(key = {"password"}, mandatory = true, help = "The password to hash") final String password) {

        String hashedString = getHashedString(password);

        final boolean returnValue;
        try {
            returnValue = credentialProperties.addUser(username, hashedString);
        } catch (ConfigurationException e) {
            return "Error during saving of the configuration:" + e.getMessage();
        }
        String returnString;
        if (returnValue) {
            returnString = "User " + username + " added";
        } else {
            returnString = "Username " + username + " already taken";
        }

        return returnString;
    }

    /**
     * Decides according to the configuration file how to hash the plaintext password.
     *
     * @param password plaintext password
     * @return hashed string
     */
    protected String getHashedString(String password) {
        String hashedString;
        final String salt;
        if (fileAuthConfiguration.isHashed()) {
            if (!fileAuthConfiguration.isSalted()) {
                hashedString = PasswordHasher.hashPassword(fileAuthConfiguration.getAlgorithm(), password, fileAuthConfiguration.getIterations(), null);
            } else {
                salt = getSalt();
                final String saltBase64 = encodeSalt(salt);

                if (!fileAuthConfiguration.isFirst()) {
                    hashedString = PasswordHasher.hashPassword(fileAuthConfiguration.getAlgorithm(), password, fileAuthConfiguration.getIterations(), salt) + fileAuthConfiguration.getSeparationChar() + saltBase64;

                } else {
                    hashedString = saltBase64 + fileAuthConfiguration.getSeparationChar() + PasswordHasher.hashPassword(fileAuthConfiguration.getAlgorithm(), password, fileAuthConfiguration.getIterations(), salt);
                }
            }
        } else {
            hashedString = password;
        }
        return hashedString;
    }

    /**
     * Update User Command.
     *
     * @param username username
     * @param password new plaintext password
     * @return message if updating the user was successful or not
     */
    @CliCommand(value = "updateUser", help = "changes the password of a user in the credential file")
    public String updateUser(
            @CliOption(key = {"username"}, mandatory = true, help = "The username") final String username,
            @CliOption(key = {"password"}, mandatory = true, help = "The password to hash") final String password) {

        String hashedString = getHashedString(password);

        final boolean returnValue;
        try {
            returnValue = credentialProperties.updateUser(username, hashedString);
        } catch (ConfigurationException e) {
            return "Error during saving of the configuration:" + e.getMessage();
        }
        String returnString;
        if (returnValue) {
            returnString = "User " + username + " updated";
        } else {
            returnString = "User " + username + " not existent";
        }

        return returnString;
    }

    /**
     * Add or Update User Command
     *
     * @param username username
     * @param password plaintext password
     * @return message if creating/updating of the user was successful or not
     */
    @CliCommand(value = "addOrUpdateUser", help = "changes the password of a user or create a new user in the credential file")
    public String addOrUpdateUser(
            @CliOption(key = {"username"}, mandatory = true, help = "The username") final String username,
            @CliOption(key = {"password"}, mandatory = true, help = "The password to hash") final String password) {

        String hashedString = getHashedString(password);

        final boolean returnValue;
        try {
            returnValue = credentialProperties.updateUser(username, hashedString);
        } catch (ConfigurationException e) {
            return "Error during saving of the configuration:" + e.getMessage();
        }
        String returnString;
        if (returnValue) {
            returnString = "User " + username + " updated";
        } else {

            final boolean returnValueNewUser;
            try {
                returnValueNewUser = credentialProperties.addUser(username, hashedString);
            } catch (ConfigurationException e) {
                return "Error during saving of the configuration:" + e.getMessage();
            }
            if (!returnValueNewUser) {
                return "Could not save new user!";
            }

            returnString = "User " + username + " added";

        }

        return returnString;
    }

    /**
     * Delete User Command
     *
     * @param username username
     * @return message if deletion of user was successful or not
     */
    @CliCommand(value = "deleteUser", help = "deletes a user in the credential file")
    public String deleteUser(
            @CliOption(key = {"username"}, mandatory = true, help = "The username") final String username) {

        final boolean returnValue;
        try {
            returnValue = credentialProperties.deleteUser(username);
        } catch (ConfigurationException e) {
            return "Error during saving of the configuration:" + e.getMessage();
        }
        String returnString;
        if (returnValue) {
            returnString = "User " + username + " deleted";
        } else {
            returnString = "User " + username + " not existent";
        }
        return returnString;
    }

    /**
     * List Users Command
     *
     * @return message of how many users are in the file
     */
    @CliCommand(value = "listUsers", help = "shows all users in the credential file")
    public String listUsers() {

        final int count = credentialProperties.show();

        return "Listed " + count + " users";
    }

    /**
     * This command sets the salt length that should be used, when hashing a password.
     *
     * @param length length of salt
     * @return message if setting the length accordingly was successful or not
     */
    @CliCommand(value = "salt", help = "length of the salt applied")
    public String setSaltLength(@CliOption(key = {"length"}, mandatory = true, help = "The absolute path to the fileAuthentication.properties file") final String length) {
        int parsedInteger;
        try {
            parsedInteger = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            saltLength = DEFAULT_SALT_LENGTH;
            return "Error: The length should be an integer, using default salt length";
        }
        saltLength = parsedInteger;
        return "Salt length set to " + saltLength;

    }

    /**
     * Generates a random salt.
     *
     * @return salt
     */
    private String getSalt() {
        final RandomSaltGenerator randomSaltGenerator = new RandomSaltGenerator();
        return new String(randomSaltGenerator.generateSalt(saltLength), Charsets.UTF_8);
    }

    /**
     * Encodes a salt with base64
     *
     * @param salt raw salt
     * @return base64 encoded salt
     */
    private String encodeSalt(String salt) {
        return new String(Base64.encode(salt.getBytes()), Charsets.UTF_8);
    }


}
