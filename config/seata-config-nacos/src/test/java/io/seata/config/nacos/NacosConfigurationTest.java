/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.seata.config.nacos;

import java.lang.reflect.Method;
import java.util.Properties;

import io.seata.common.util.ReflectionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * The type Nacos configuration test
 *
 */
public class NacosConfigurationTest {

    @Test
    public void testGetConfigProperties() throws Exception {
        Method method = ReflectionUtil.getMethod(NacosConfiguration.class, "getConfigProperties");
        Properties properties = (Properties) ReflectionUtil.invokeMethod(null, method);
        Assertions.assertThat(properties.getProperty("contextPath")).isEqualTo("/bar");
        System.setProperty("contextPath", "/foo");
        properties = (Properties) ReflectionUtil.invokeMethod(null, method);
        Assertions.assertThat(properties.getProperty("contextPath")).isEqualTo("/foo");
    }


}
