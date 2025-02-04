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
package io.seata.core.protocol;

import java.io.Serializable;

/**
 * The type Heartbeat message.
 *
 */
public class HeartbeatMessage implements MessageTypeAware, Serializable {
    private static final long serialVersionUID = -985316399527884899L;
    private boolean ping = true;
    /**
     * The constant PING.
     */
    public static final HeartbeatMessage PING = new HeartbeatMessage(true);
    /**
     * The constant PONG.
     */
    public static final HeartbeatMessage PONG = new HeartbeatMessage(false);

    private HeartbeatMessage(boolean ping) {
        this.ping = ping;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_HEARTBEAT_MSG;
    }

    @Override
    public String toString() {
        return this.ping ? "services ping" : "services pong";
    }

    public boolean isPing() {
        return ping;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }
}
