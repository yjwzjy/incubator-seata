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
package io.seata.rm.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;

import java.util.List;

/**
 * The type Tcc action.
 *
 */
public class TccActionImpl implements TccAction {

    @Override
    public boolean prepare(BusinessActionContext actionContext,
                           int a,
                           List b,
                           TccParam TccParam  ) {
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext,
                          @BusinessActionContextParameter("tccParam") TccParam param, @Param("a") Integer a) {
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext,
                            @BusinessActionContextParameter("tccParam") TccParam param) {
        return true;
    }
}
