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
package io.seata.serializer.protobuf.convertor;

import io.seata.serializer.protobuf.generated.MergedWarpMessageProto;
import io.seata.core.protocol.AbstractMessage;
import io.seata.core.protocol.MergedWarpMessage;
import io.seata.core.protocol.transaction.GlobalBeginRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 */
public class MergeMessageConvertorTest {

    @Test
    public void test() {
        MergedWarpMessage mergedWarpMessage = new MergedWarpMessage();
        final ArrayList<AbstractMessage> msgs = new ArrayList<>();
        final GlobalBeginRequest globalBeginRequest = buildGlobalBeginRequest();
        msgs.add(globalBeginRequest);
        mergedWarpMessage.msgs = msgs;

        MergedWarpMessageConvertor pbConvertor = new MergedWarpMessageConvertor();
        MergedWarpMessageProto globalBeginRequestProto = pbConvertor.convert2Proto(
            mergedWarpMessage);

        MergedWarpMessage model = pbConvertor.convert2Model(globalBeginRequestProto);

        GlobalBeginRequest decodeModel = (GlobalBeginRequest)model.msgs.get(0);
        assertThat(decodeModel.getTransactionName()).isEqualTo(
            globalBeginRequest.getTransactionName());
        assertThat(decodeModel.getTimeout()).isEqualTo(globalBeginRequest.getTimeout());
        assertThat(
            decodeModel.getTypeCode()).isEqualTo(globalBeginRequest.getTypeCode());

    }

    private GlobalBeginRequest buildGlobalBeginRequest() {
        final GlobalBeginRequest globalBeginRequest = new GlobalBeginRequest();
        globalBeginRequest.setTransactionName("xx");
        globalBeginRequest.setTimeout(3000);
        return globalBeginRequest;
    }
}
