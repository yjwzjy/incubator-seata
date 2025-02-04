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
package io.seata.serializer.seata.protocol;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.seata.core.protocol.RegisterRMRequest;

/**
 * The type Register rm request codec.
 *
 */
public class RegisterRMRequestCodec extends AbstractIdentifyRequestCodec {

    @Override
    public Class<?> getMessageClassType() {
        return RegisterRMRequest.class;
    }

    @Override
    protected <T> void doEncode(T t, ByteBuf out) {
        super.doEncode(t, out);

        RegisterRMRequest registerRMRequest = (RegisterRMRequest)t;
        String resourceIds = registerRMRequest.getResourceIds();

        if (resourceIds != null) {
            byte[] bs = resourceIds.getBytes(UTF8);
            out.writeInt(bs.length);
            if (bs.length > 0) {
                out.writeBytes(bs);
            }
        } else {
            out.writeInt(0);
        }
    }

    @Override
    public <T> void decode(T t, ByteBuffer in) {
        RegisterRMRequest registerRMRequest = (RegisterRMRequest)t;

        if (in.remaining() < 2) {
            return;
        }
        short len = in.getShort();
        if (len > 0) {
            if (in.remaining() < len) {
                return;
            }
            byte[] bs = new byte[len];
            in.get(bs);
            registerRMRequest.setVersion(new String(bs, UTF8));
        } else {
            return;
        }
        if (in.remaining() < 2) {
            return;
        }
        len = in.getShort();

        if (len > 0) {
            if (in.remaining() < len) {
                return;
            }
            byte[] bs = new byte[len];
            in.get(bs);
            registerRMRequest.setApplicationId(new String(bs, UTF8));
        }

        if (in.remaining() < 2) {
            return;
        }
        len = in.getShort();

        if (in.remaining() < len) {
            return;
        }
        byte[] bs = new byte[len];
        in.get(bs);
        registerRMRequest.setTransactionServiceGroup(new String(bs, UTF8));

        if (in.remaining() < 2) {
            return;
        }
        len = in.getShort();

        if (len > 0) {
            if (in.remaining() < len) {
                return;
            }
            bs = new byte[len];
            in.get(bs);
            registerRMRequest.setExtraData(new String(bs, UTF8));
        }

        int iLen;
        if (in.remaining() < 4) {
            return;
        }
        iLen = in.getInt();

        if (iLen > 0) {
            if (in.remaining() < iLen) {
                return;
            }
            bs = new byte[iLen];
            in.get(bs);
            registerRMRequest.setResourceIds(new String(bs, UTF8));
        }
    }

}
