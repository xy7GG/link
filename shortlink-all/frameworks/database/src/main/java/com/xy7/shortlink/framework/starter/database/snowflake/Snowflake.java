package com.xy7.shortlink.framework.starter.database.snowflake;

import java.util.concurrent.atomic.AtomicLong;

public class Snowflake {
    private final long workerId;
    private final long epoch = 1623168000000L; // 起始时间戳，2021-06-09 00:00:00
    private final AtomicLong sequence = new AtomicLong(0L);
    private final long workerIdBits = 5L;
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private volatile long lastTimestamp = -1L;

    public Snowflake(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID must be between 0 and %d", maxWorkerId));
        }
        this.workerId = workerId;
    }

    public long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        if (lastTimestamp == timestamp) {
            long currentSequence = sequence.incrementAndGet() & sequenceMask;
            if (currentSequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence.set(0L);
        }

        lastTimestamp = timestamp;

        return ((timestamp - epoch) << timestampLeftShift) |
                (workerId << workerIdShift) |
                sequence.get();
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
