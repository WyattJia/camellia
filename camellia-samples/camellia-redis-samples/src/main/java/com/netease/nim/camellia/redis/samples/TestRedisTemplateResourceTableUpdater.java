package com.netease.nim.camellia.redis.samples;

import com.netease.nim.camellia.core.model.Resource;
import com.netease.nim.camellia.core.model.ResourceTable;
import com.netease.nim.camellia.core.util.ResourceTableUtil;
import com.netease.nim.camellia.redis.CamelliaRedisEnv;
import com.netease.nim.camellia.redis.CamelliaRedisTemplate;
import com.netease.nim.camellia.redis.resource.RedisTemplateResourceTableUpdater;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by caojiajun on 2021/7/30
 */
public class TestRedisTemplateResourceTableUpdater {

    private static class CustomRedisTemplateResourceTableUpdater extends RedisTemplateResourceTableUpdater {

        public CustomRedisTemplateResourceTableUpdater() {
            Executors.newSingleThreadScheduledExecutor()
                    .scheduleAtFixedRate(this::checkUpdate, 1, 1, TimeUnit.SECONDS);
        }

        @Override
        public ResourceTable getResourceTable() {
            //用于初始化ResourceTable
            return ResourceTableUtil.simpleTable(new Resource("redis://@127.0.0.1:6379"));
        }

        private void checkUpdate() {
            //从你的配置中心获取配置，或者监听配置的变更
            ResourceTable resourceTable = ResourceTableUtil.simpleTable(new Resource("redis://@127.0.0.1:6380"));
            //如果配置发生了变更，则回调告诉CamelliaRedisTemplate有更新
            invokeUpdateResourceTable(resourceTable);
        }
    }

    public static void main(String[] args) {
        CamelliaRedisTemplate template = new CamelliaRedisTemplate(CamelliaRedisEnv.defaultRedisEnv(), new CustomRedisTemplateResourceTableUpdater());
        System.out.println(template.get("k1"));
    }
}
