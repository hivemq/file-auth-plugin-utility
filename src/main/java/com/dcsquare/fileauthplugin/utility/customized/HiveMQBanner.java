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

package com.dcsquare.fileauthplugin.utility.customized;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * This class customizes the banner of the shell.
 *
 * @author Christian Goetz
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HiveMQBanner extends DefaultBannerProvider implements CommandMarker {
    @Override
    public String getBanner() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("# ------------------------------------------------------------------------- ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                   _    _  _              __  __   ____                    ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                  | |  | |(_)            |  \\/  | / __ \\                   ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                  | |__| | _ __   __ ___ | \\  / || |  | |                  ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                  |  __  || |\\ \\ / // _ \\| |\\/| || |  | |                  ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                  | |  | || | \\ V /|  __/| |  | || |__| |                  ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                  |_|  |_||_|  \\_/  \\___||_|  |_| \\___\\_\\                  ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#                                                                           ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# ------------------------------------------------------------------------- ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# Make sure your fileAuthConfiguration.properties holds your desired settings.").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# ------------------------------------------------------------------------- ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# USAGE:").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# configure --file <path/to/configuration>/fileAuthConfiguration.properties").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# The configured credential file is assumed to be in the same folder.").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# use commands to manipulate the credential file: ").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#    listUsers").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#    addUser").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#    updateUser").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#    addOrUpdateUser").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("#    deleteUser").append(OsUtils.LINE_SEPARATOR);
        stringBuilder.append("# ------------------------------------------------------------------------- ").append(OsUtils.LINE_SEPARATOR);
        return stringBuilder.toString();

    }

    public String getVersion() {
        return "1.1";
    }

    public String getWelcomeMessage() {
        return "Welcome to the HiveMQ File Authentication Plugin Shell";
    }

    @Override
    public String name() {
        return "HiveMQ FileAuth Plugin Shell";
    }
}
