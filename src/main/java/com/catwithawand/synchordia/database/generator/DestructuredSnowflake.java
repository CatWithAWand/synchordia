package com.catwithawand.synchordia.database.generator;

public class DestructuredSnowflake {

  private final long timestamp;
  private final long threadId;
  private final long processId;
  private final long sequence;

  public DestructuredSnowflake(long timestamp, long threadId, long processId, long sequence) {
    this.timestamp = timestamp;
    this.threadId = threadId;
    this.processId = processId;
    this.sequence = sequence;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getThreadId() {
    return threadId;
  }

  public long getProcessId() {
    return processId;
  }

  public long getSequence() {
    return sequence;
  }

}
