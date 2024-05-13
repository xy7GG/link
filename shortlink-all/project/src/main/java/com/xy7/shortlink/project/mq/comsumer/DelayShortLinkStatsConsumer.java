package com.xy7.shortlink.project.mq.comsumer;

import com.xy7.shortlink.project.common.convention.exception.ServiceException;
import com.xy7.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.xy7.shortlink.project.idempotent.MessageQueueIdempotentHandler;
import com.xy7.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static com.xy7.shortlink.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/**
 * 延迟记录短链接统计组件
 */
@Slf4j
@Component
@Deprecated
@RequiredArgsConstructor
public class DelayShortLinkStatsConsumer implements InitializingBean {

    private final RedissonClient redissonClient;
    private final ShortLinkService shortLinkService;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    public void onMessage() {
        Executors.newSingleThreadExecutor(
                        runnable -> {
                            Thread thread = new Thread(runnable);
                            thread.setName("delay_short-link_stats_consumer");
                            thread.setDaemon(Boolean.TRUE);
                            return thread;
                        })
                .execute(() -> {
                    RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
                    RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                    for (; ; ) {
                        try {
                            ShortLinkStatsRecordDTO statsRecord = delayedQueue.poll();
                            if (!messageQueueIdempotentHandler.isMessageProcessed(statsRecord.getKeys())) {
                                // 判断当前的这个消息流程是否执行完成
                                if (messageQueueIdempotentHandler.isAccomplish(statsRecord.getKeys())) {
                                    return;
                                }
                                throw new ServiceException("消息未完成流程，需要消息队列重试");
                            }
                            try {
                                shortLinkService.shortLinkStats(statsRecord);
                            } catch (Throwable ex) {
                                messageQueueIdempotentHandler.delMessageProcessed(statsRecord.getKeys());
                                log.error("延迟记录短链接监控消费异常", ex);
                            }
                            messageQueueIdempotentHandler.setAccomplish(statsRecord.getKeys());
                            if (statsRecord != null) {
                                shortLinkService.shortLinkStats(statsRecord);
                                continue;
                            }
                            LockSupport.parkUntil(500);
                        } catch (Throwable ignored) {
                        }
                    }
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        onMessage();
    }
}
