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
package io.seata.rm.datasource;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * The type Abstract statement proxy.
 *
 *
 * @param <T> the type parameter
 */
public abstract class AbstractStatementProxy<T extends Statement> implements Statement {

    /**
     * The Connection proxy.
     */
    protected AbstractConnectionProxy connectionProxy;

    /**
     * The Target statement.
     */
    protected T targetStatement;

    /**
     * The Target sql.
     */
    protected String targetSQL;

    /**
     * The cache of scrollable generatedKeys
     */
    protected CachedRowSet scrollableGeneratedKeysCache;

    /**
     * Instantiates a new Abstract statement proxy.
     *
     * @param connectionProxy the connection proxy
     * @param targetStatement the target statement
     * @param targetSQL       the target sql
     * @throws SQLException the sql exception
     */
    public AbstractStatementProxy(AbstractConnectionProxy connectionProxy, T targetStatement, String targetSQL)
        throws SQLException {
        this.connectionProxy = connectionProxy;
        this.targetStatement = targetStatement;
        this.targetSQL = targetSQL;
    }

    /**
     * Instantiates a new Abstract statement proxy.
     *
     * @param connectionProxy the connection proxy
     * @param targetStatement the target statement
     * @throws SQLException the sql exception
     */
    public AbstractStatementProxy(ConnectionProxy connectionProxy, T targetStatement) throws SQLException {
        this(connectionProxy, targetStatement, null);
    }

    /**
     * Gets connection proxy.
     *
     * @return the connection proxy
     */
    public AbstractConnectionProxy getConnectionProxy() {
        return connectionProxy;
    }

    /**
     * Gets target statement.
     *
     * @return the target statement
     */
    public T getTargetStatement() {
        return targetStatement;
    }

    /**
     * Gets target sql.
     *
     * @return the target sql
     */
    public String getTargetSQL() {
        return targetSQL;
    }

    @Override
    public void close() throws SQLException {
        targetStatement.close();

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return targetStatement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        targetStatement.setMaxFieldSize(max);

    }

    @Override
    public int getMaxRows() throws SQLException {
        return targetStatement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        targetStatement.setMaxRows(max);

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        targetStatement.setEscapeProcessing(enable);

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return targetStatement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        targetStatement.setQueryTimeout(seconds);

    }

    @Override
    public void cancel() throws SQLException {
        targetStatement.cancel();

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return targetStatement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        targetStatement.clearWarnings();

    }

    @Override
    public void setCursorName(String name) throws SQLException {
        targetStatement.setCursorName(name);

    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return targetStatement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return targetStatement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return targetStatement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        targetStatement.setFetchDirection(direction);

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return targetStatement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        targetStatement.setFetchSize(rows);

    }

    @Override
    public int getFetchSize() throws SQLException {
        return targetStatement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return targetStatement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return targetStatement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        targetStatement.addBatch(sql);

    }

    @Override
    public void clearBatch() throws SQLException {
        targetStatement.clearBatch();
        targetSQL = null;
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return targetStatement.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return targetStatement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return targetStatement.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        ResultSet rs = targetStatement.getGeneratedKeys();
        if (null == scrollableGeneratedKeysCache || !rs.isAfterLast()) {
            //Conditions for flushing the cache:
            //1.originally not cached
            //2.the original ResultSet was not traversed, including executed repeatedly
            scrollableGeneratedKeysCache = RowSetProvider.newFactory().createCachedRowSet();
            scrollableGeneratedKeysCache.populate(rs);
        }
        return scrollableGeneratedKeysCache;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return targetStatement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return targetStatement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        targetStatement.setPoolable(poolable);

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return targetStatement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        targetStatement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return targetStatement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return targetStatement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return targetStatement.isWrapperFor(iface);
    }
}
