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
package io.seata.server.storage.redis.lock;

import io.seata.common.ConfigurationKeys;
import io.seata.config.Configuration;
import io.seata.config.ConfigurationFactory;
import io.seata.core.lock.Locker;

import static io.seata.common.Constants.STORE_REDIS_TYPE_PIPELINE;

/**
 */
public class RedisLockerFactory {

    private static final Configuration CONFIG = ConfigurationFactory.getInstance();

    /**
     * The locker.
     */
    private static volatile Locker locker;

    public static Locker getLocker() {
        if (locker == null) {
            synchronized (RedisLockerFactory.class) {
                if (locker == null) {
                    String storeRedisType = CONFIG.getConfig(ConfigurationKeys.STORE_REDIS_TYPE, STORE_REDIS_TYPE_PIPELINE);
                    locker = STORE_REDIS_TYPE_PIPELINE.equals(storeRedisType) ? new RedisLocker() : new RedisLuaLocker();
                }
            }
        }
        return locker;
    }
}
