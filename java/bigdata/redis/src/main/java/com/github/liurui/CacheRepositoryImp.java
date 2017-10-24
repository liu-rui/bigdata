package com.github.liurui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

@Component
class CacheRepositoryImp implements CacheRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheRepositoryImp.class);
    @Autowired
    StringRedisTemplate template;

    @Override
    public void test(String key) {

        try {
            ValueOperations<String, String> opsForValue = template.opsForValue();
            opsForValue.set(key, key);

            if (!opsForValue.get(key).equals(key)) {
                LOGGER.error(String.format("ERROR %s %s", Thread.currentThread().getName(), key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                template.delete(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
