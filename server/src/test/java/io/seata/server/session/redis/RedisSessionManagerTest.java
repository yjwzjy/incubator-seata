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
package io.seata.server.session.redis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import io.seata.common.XID;
import io.seata.common.loader.EnhancedServiceLoader;
import io.seata.common.util.CollectionUtils;
import io.seata.core.exception.TransactionException;
import io.seata.core.model.BranchStatus;
import io.seata.core.model.BranchType;
import io.seata.core.model.GlobalStatus;
import io.seata.server.UUIDGenerator;
import io.seata.server.session.BranchSession;
import io.seata.server.session.GlobalSession;
import io.seata.server.session.SessionCondition;
import io.seata.server.session.SessionManager;
import io.seata.server.storage.redis.session.RedisSessionManager;
import io.seata.server.storage.redis.store.RedisTransactionStoreManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


import static io.seata.common.DefaultValues.DEFAULT_TX_GROUP;

/**
 */
@SpringBootTest
public class RedisSessionManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionManagerTest.class);
    private static SessionManager sessionManager = null;

    @BeforeAll
    public static void start(ApplicationContext context) throws IOException {
        MockRedisServer.getInstance();
        EnhancedServiceLoader.unloadAll();
        RedisTransactionStoreManager transactionStoreManager = RedisTransactionStoreManager.getInstance();
        RedisSessionManager redisSessionManager = new RedisSessionManager();
        redisSessionManager.setTransactionStoreManager(transactionStoreManager);
        sessionManager = redisSessionManager;
    }

    @Test
    public void test_addGlobalSession() throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);

        sessionManager.removeGlobalSession(session);
    }

    //Cause the jedismock can not mock the watch command,so I annotation it after I had tested this method and had successed.
    @Test
    public void test_updateGlobalSessionStatus() throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);
        sessionManager.updateGlobalSessionStatus(session, GlobalStatus.Committing);
    }

    @Test
    public void test_removeGlobalSession() throws Exception {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);
        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(session.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(session, branchSession);
        sessionManager.removeBranchSession(session, branchSession);
        sessionManager.removeGlobalSession(session);
    }

    @Test
    public void test_addBranchSession() throws TransactionException {
        GlobalSession globalSession = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(globalSession.getTransactionId());
        globalSession.setXid(xid);
        globalSession.setTransactionId(globalSession.getTransactionId());
        globalSession.setBeginTime(System.currentTimeMillis());
        globalSession.setApplicationData("abc=878s");
        globalSession.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(globalSession);

        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(globalSession.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(globalSession,branchSession);

        sessionManager.removeBranchSession(globalSession, branchSession);
        sessionManager.removeGlobalSession(globalSession);
    }

    @Test
    public void test_updateBranchSessionStatus() throws Exception {
        GlobalSession globalSession = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(globalSession.getTransactionId());
        globalSession.setXid(xid);
        globalSession.setTransactionId(globalSession.getTransactionId());
        globalSession.setBeginTime(System.currentTimeMillis());
        globalSession.setApplicationData("abc=878s");
        globalSession.setStatus(GlobalStatus.Begin);

        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(globalSession.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setStatus(BranchStatus.PhaseOne_Done);
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(globalSession, branchSession);
        branchSession.setStatus(BranchStatus.PhaseOne_Timeout);
        sessionManager.updateBranchSessionStatus(branchSession, BranchStatus.PhaseOne_Timeout);

        sessionManager.removeBranchSession(globalSession, branchSession);
        sessionManager.removeGlobalSession(globalSession);
    }

    @Test
    public void testReadSession() throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);
        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(session.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(session, branchSession);

        GlobalSession globalSession = sessionManager.findGlobalSession(xid);
        Assertions.assertEquals(session.getXid(), globalSession.getXid());
        Assertions.assertEquals(session.getTransactionId(), globalSession.getTransactionId());
        Assertions.assertEquals(branchSession.getXid(), globalSession.getBranchSessions().get(0).getXid());
        Assertions.assertEquals(branchSession.getBranchId(), globalSession.getBranchSessions().get(0).getBranchId());
        Assertions.assertEquals(branchSession.getClientId(), globalSession.getBranchSessions().get(0).getClientId());

        sessionManager.removeBranchSession(globalSession, branchSession);
        sessionManager.removeGlobalSession(globalSession);
    }

    @Test
    public void testReadSessionWithCondition() throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);
        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(session.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(session, branchSession);

        SessionCondition condition = new SessionCondition();
        condition.setXid(xid);

        List<GlobalSession> globalSessions = sessionManager.findGlobalSessions(condition);
        Assertions.assertEquals(session.getXid(), globalSessions.get(0).getXid());
        Assertions.assertEquals(session.getTransactionId(), globalSessions.get(0).getTransactionId());
        Assertions.assertEquals(branchSession.getXid(), globalSessions.get(0).getBranchSessions().get(0).getXid());
        Assertions.assertEquals(branchSession.getBranchId(), globalSessions.get(0).getBranchSessions().get(0).getBranchId());
        Assertions.assertEquals(branchSession.getClientId(), globalSessions.get(0).getBranchSessions().get(0).getClientId());

        condition.setXid(null);
        condition.setTransactionId(session.getTransactionId());
        globalSessions = sessionManager.findGlobalSessions(condition);
        Assertions.assertEquals(session.getXid(), globalSessions.get(0).getXid());
        Assertions.assertEquals(session.getTransactionId(), globalSessions.get(0).getTransactionId());
        Assertions.assertEquals(branchSession.getXid(), globalSessions.get(0).getBranchSessions().get(0).getXid());
        Assertions.assertEquals(branchSession.getBranchId(), globalSessions.get(0).getBranchSessions().get(0).getBranchId());
        Assertions.assertEquals(branchSession.getClientId(), globalSessions.get(0).getBranchSessions().get(0).getClientId());

        condition.setTransactionId(null);
        globalSessions = sessionManager.findGlobalSessions(condition);
        Assertions.assertNull(globalSessions);

        sessionManager.removeBranchSession(session, branchSession);
        sessionManager.removeGlobalSession(session);
    }

    @Test
    public void testReadSessionWithConditionStatus() throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);
        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(session.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(session, branchSession);

        SessionCondition condition = new SessionCondition();
        condition.setStatus(GlobalStatus.Begin);
        sessionManager.findGlobalSessions(condition);

        condition.setStatus(null);
        GlobalStatus[] statuses = {GlobalStatus.Begin};
        condition.setStatuses(statuses);
        sessionManager.findGlobalSessions(condition);

        sessionManager.removeBranchSession(session, branchSession);
        sessionManager.removeGlobalSession(session);
    }

    @Test
    public void testReadSessionWithBranch() throws TransactionException, NoSuchFieldException, IllegalAccessException {
        GlobalSession session = GlobalSession.createGlobalSession("test", "test", "test123", 100);
        String xid = XID.generateXID(session.getTransactionId());
        session.setXid(xid);
        session.setTransactionId(session.getTransactionId());
        session.setBeginTime(System.currentTimeMillis());
        session.setApplicationData("abc=878s");
        session.setStatus(GlobalStatus.Begin);
        sessionManager.addGlobalSession(session);
        BranchSession branchSession = new BranchSession();
        branchSession.setBranchId(UUIDGenerator.generateUUID());
        branchSession.setXid(xid);
        branchSession.setTransactionId(session.getTransactionId());
        branchSession.setBranchId(1L);
        branchSession.setResourceGroupId(DEFAULT_TX_GROUP);
        branchSession.setResourceId("tb_1");
        branchSession.setLockKey("t_1");
        branchSession.setBranchType(BranchType.AT);
        branchSession.setApplicationData("{\"data\":\"test\"}");
        branchSession.setClientId("storage-server:192.168.158.80:11934");
        sessionManager.addBranchSession(session, branchSession);

        GlobalSession globalSession = sessionManager.findGlobalSession(xid,false);
        Assertions.assertEquals(session.getXid(),globalSession.getXid());
        Assertions.assertEquals(session.getTransactionId(),globalSession.getTransactionId());
        Class<?> clz = globalSession.getClass();
        Field branchSessions = clz.getDeclaredField("branchSessions");
        branchSessions.setAccessible(true);
        Assertions.assertTrue(CollectionUtils.isEmpty((List<BranchSession>)branchSessions.get(globalSession)));

        globalSession = sessionManager.findGlobalSession(xid, true);
        Assertions.assertEquals(branchSession.getXid(), globalSession.getBranchSessions().get(0).getXid());
        Assertions.assertEquals(branchSession.getBranchId(), globalSession.getBranchSessions().get(0).getBranchId());
        Assertions.assertEquals(branchSession.getClientId(), globalSession.getBranchSessions().get(0).getClientId());

        sessionManager.removeBranchSession(session, branchSession);
        sessionManager.removeGlobalSession(session);
    }

}
