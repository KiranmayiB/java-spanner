/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.connection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.TimestampBound;
import com.google.cloud.spanner.connection.AbstractStatementParser.ParsedStatement;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ConnectionStatementWithNoParametersTest {
  @Parameter public Dialect dialect;

  @Parameters(name = "dialect = {0}")
  public static Object[] data() {
    return Dialect.values();
  }

  private AbstractStatementParser parser;

  @Before
  public void setup() {
    parser = AbstractStatementParser.getInstance(dialect);
  }

  @Test
  public void testExecuteGetAutocommit() {
    ParsedStatement statement = parser.parse(Statement.of("show variable autocommit"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowAutocommit()).thenCallRealMethod();
    statement.getClientSideStatement().execute(executor, "show variable autocommit");
    verify(connection, times(1)).isAutocommit();
  }

  @Test
  public void testExecuteGetReadOnly() {
    ParsedStatement statement = parser.parse(Statement.of("show variable readonly"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowReadOnly()).thenCallRealMethod();
    statement.getClientSideStatement().execute(executor, "show variable readonly");
    verify(connection, times(1)).isReadOnly();
  }

  @Test
  public void testExecuteGetAutocommitDmlMode() {
    ParsedStatement statement = parser.parse(Statement.of("show variable autocommit_dml_mode"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowAutocommitDmlMode()).thenCallRealMethod();
    when(connection.getAutocommitDmlMode()).thenReturn(AutocommitDmlMode.TRANSACTIONAL);
    statement.getClientSideStatement().execute(executor, "show variable autocommit_dml_mode");
    verify(connection, times(1)).getAutocommitDmlMode();
  }

  @Test
  public void testExecuteGetStatementTimeout() {
    ParsedStatement statement = parser.parse(Statement.of("show variable statement_timeout"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowStatementTimeout()).thenCallRealMethod();
    when(connection.hasStatementTimeout()).thenReturn(true);
    when(connection.getStatementTimeout(TimeUnit.NANOSECONDS)).thenReturn(1L);
    statement.getClientSideStatement().execute(executor, "show variable statement_timeout");
    verify(connection, times(2)).getStatementTimeout(TimeUnit.NANOSECONDS);
  }

  @Test
  public void testExecuteGetReadTimestamp() {
    ParsedStatement statement = parser.parse(Statement.of("show variable read_timestamp"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowReadTimestamp()).thenCallRealMethod();
    when(connection.getReadTimestampOrNull()).thenReturn(Timestamp.now());
    statement.getClientSideStatement().execute(executor, "show variable read_timestamp");
    verify(connection, times(1)).getReadTimestampOrNull();
  }

  @Test
  public void testExecuteGetCommitTimestamp() {
    ParsedStatement statement = parser.parse(Statement.of("show variable commit_timestamp"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowCommitTimestamp()).thenCallRealMethod();
    when(connection.getCommitTimestampOrNull()).thenReturn(Timestamp.now());
    statement.getClientSideStatement().execute(executor, "show variable commit_timestamp");
    verify(connection, times(1)).getCommitTimestampOrNull();
  }

  @Test
  public void testExecuteGetReadOnlyStaleness() {
    ParsedStatement statement = parser.parse(Statement.of("show variable read_only_staleness"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowReadOnlyStaleness()).thenCallRealMethod();
    when(connection.getReadOnlyStaleness()).thenReturn(TimestampBound.strong());
    statement.getClientSideStatement().execute(executor, "show variable read_only_staleness");
    verify(connection, times(1)).getReadOnlyStaleness();
  }

  @Test
  public void testExecuteGetOptimizerVersion() {
    ParsedStatement statement = parser.parse(Statement.of("show variable optimizer_version"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowOptimizerVersion()).thenCallRealMethod();
    when(connection.getOptimizerVersion()).thenReturn("1");
    statement.getClientSideStatement().execute(executor, "show variable optimizer_version");
    verify(connection, times(1)).getOptimizerVersion();
  }

  @Test
  public void testExecuteGetOptimizerStatisticsPackage() {
    ParsedStatement statement =
        parser.parse(Statement.of("show variable optimizer_statistics_package"));
    ConnectionImpl connection = mock(ConnectionImpl.class);
    ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
    when(executor.getConnection()).thenReturn(connection);
    when(executor.statementShowOptimizerStatisticsPackage()).thenCallRealMethod();
    when(connection.getOptimizerStatisticsPackage()).thenReturn("custom-package");
    statement
        .getClientSideStatement()
        .execute(executor, "show variable optimizer_statistics_package");
    verify(connection, times(1)).getOptimizerStatisticsPackage();
  }

  @Test
  public void testExecuteBegin() {
    ParsedStatement subject = parser.parse(Statement.of("begin"));
    for (String statement : subject.getClientSideStatement().getExampleStatements()) {
      ConnectionImpl connection = mock(ConnectionImpl.class);
      ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
      when(executor.getConnection()).thenReturn(connection);
      when(executor.statementBeginTransaction()).thenCallRealMethod();
      subject.getClientSideStatement().execute(executor, statement);
      verify(connection, times(1)).beginTransaction();
    }
  }

  @Test
  public void testExecuteCommit() {
    ParsedStatement subject = parser.parse(Statement.of("commit"));
    for (String statement : subject.getClientSideStatement().getExampleStatements()) {
      ConnectionImpl connection = mock(ConnectionImpl.class);
      ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
      when(executor.getConnection()).thenReturn(connection);
      when(executor.statementCommit()).thenCallRealMethod();
      subject.getClientSideStatement().execute(executor, statement);
      verify(connection, times(1)).commit();
    }
  }

  @Test
  public void testExecuteRollback() {
    ParsedStatement subject = parser.parse(Statement.of("rollback"));
    for (String statement : subject.getClientSideStatement().getExampleStatements()) {
      ConnectionImpl connection = mock(ConnectionImpl.class);
      ConnectionStatementExecutorImpl executor = mock(ConnectionStatementExecutorImpl.class);
      when(executor.getConnection()).thenReturn(connection);
      when(executor.statementRollback()).thenCallRealMethod();
      subject.getClientSideStatement().execute(executor, statement);
      verify(connection, times(1)).rollback();
    }
  }
}
