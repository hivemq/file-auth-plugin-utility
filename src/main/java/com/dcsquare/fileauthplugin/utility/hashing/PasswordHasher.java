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

package com.dcsquare.fileauthplugin.utility.hashing;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.digest.config.SimpleDigesterConfig;
import org.jasypt.salt.FixedStringSaltGenerator;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * Hashes the plaintext password.
 *
 * @author Dominik Obermaier
 * @author Christian Goetz
 */
public class PasswordHasher {

    private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();

    public static String hashPassword(final String algorithm, final String password, final int iterations, final String salt) {

        final ConfigurablePasswordEncryptor encryptor = getEncryptor(algorithm, iterations, salt);

        return encryptor.encryptPassword(password);
    }

    private static ConfigurablePasswordEncryptor getEncryptor(final String algorithm, final int iterations, final String salt) {
        final ConfigurablePasswordEncryptor encryptor = new ConfigurablePasswordEncryptor();

        final SimpleDigesterConfig config = new SimpleDigesterConfig();
        config.setProvider(PROVIDER);
        config.setAlgorithm(algorithm);
        config.setIterations(iterations);

        if (salt != null) {
            final FixedStringSaltGenerator saltGenerator = new FixedStringSaltGenerator();
            saltGenerator.setSalt(salt);
            config.setSaltGenerator(saltGenerator);
            config.setSaltSizeBytes(salt.length());
        }

        encryptor.setConfig(config);
        return encryptor;
    }

}
