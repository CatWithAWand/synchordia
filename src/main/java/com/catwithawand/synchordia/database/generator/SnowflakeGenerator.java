package com.catwithawand.synchordia.database.generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;


public class SnowflakeGenerator extends SequenceStyleGenerator {

  private static final ReentrantLock mutex = new ReentrantLock();

  private static final long timestampMaxValue = (long) Math.pow(2, 42) - 1;
  private static final int timestampBitShift = 22;
  private static final long timestampBitMask = timestampMaxValue << timestampBitShift;

  private static final long threadIdMaxValue = (long) Math.pow(2, 5) - 1;
  private static final int threadIdBitShift = 17;
  private static final long threadIdBitMask = threadIdMaxValue << threadIdBitShift;

  private static final long processIdMaxValue = (long) Math.pow(2, 5) - 1;
  private static final int processIdBitShift = 12;
  private static final long processIdBitMask = processIdMaxValue << processIdBitShift;

  private static final long sequenceMaxValue = (long) Math.pow(2, 12) - 1;

  private static long epoch = 0;
  private static long lastTimestamp = 0;
  private static long sequence = 0;

  public static DestructuredSnowflake destructure(long snowflakeId) {
    long timestamp = ((snowflakeId & timestampBitMask) >> timestampBitShift) + epoch;
    long threadId = (snowflakeId & threadIdBitMask) >> threadIdBitShift;
    long processId = (snowflakeId & processIdBitMask) >> processIdBitShift;
    long sequence = snowflakeId & sequenceMaxValue;

    return new DestructuredSnowflake(timestamp, threadId, processId, sequence);
  }

  @Override
  public Long generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    mutex.lock();

    long currentTimestamp = System.currentTimeMillis();

    if (currentTimestamp != lastTimestamp) {
      sequence = 0;
    } else {
      sequence = (sequence + 1) & sequenceMaxValue;
      if (sequence == 0) {
        while (currentTimestamp == lastTimestamp) {
          currentTimestamp = System.currentTimeMillis();
        }
      }
    }

    if (currentTimestamp < lastTimestamp) {
      mutex.unlock();
      throw new RuntimeException("Clock drift detected");
    }

    long epochTimestamp = currentTimestamp - epoch;
    long threadId = Thread.currentThread()
                          .getId() & threadIdMaxValue;
    long processId = ProcessHandle.current()
                                  .pid() & processIdMaxValue;

    long snowflake = epochTimestamp << timestampBitShift | threadId << threadIdBitShift
        | processId << processIdBitShift | sequence;

    lastTimestamp = currentTimestamp;

    mutex.unlock();

    return snowflake;
  }

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)
      throws MappingException {
    super.configure(type, params, serviceRegistry);
    String epochParam = ConfigurationHelper.getString("epoch", params);
    epoch = Long.parseLong(epochParam);
  }

}
