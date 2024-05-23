package com.xy7.shortlink.project.test;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataBloomTest {

    @Autowired
    private RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Test
    public void testRBloomFilter() {
//        String fullShortUrl = "43.139.204.197:8001/3EPqGD";
        String fullShortUrl = "43.139.204.197:8001/3EPqGD";
        boolean contains = shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl);
        if(contains){
            log.info("存在");
        }
    }
}