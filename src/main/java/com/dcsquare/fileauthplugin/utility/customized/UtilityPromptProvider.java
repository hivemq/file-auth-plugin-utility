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
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * This customizes the prompt of the shell.
 *
 * @author Christian Goetz
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UtilityPromptProvider extends DefaultPromptProvider {

    @Override
    public String getPrompt() {
        return "HiveMQ FileAuthPlugin>";
    }

    @Override
    public String name() {
        return "HiveMQ File Authentication Plugin Prompt Provider";
    }
}
