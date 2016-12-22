package morgan.learn;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by morgan on 2016/12/22.
 */
public class RedisDataTrains {

    public static int timeout = 60 * 60 * 24;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(16);

        final CountDownLatch cd = new CountDownLatch(16);

        final AtomicInteger num = new AtomicInteger(0);
        for (int j = 0; j < 16; j++) {
            executorService.submit(new Runnable() {
                public void run() {
                    Jedis jedis = new Jedis("192.168.103.87", 6379);
                    jedis.connect();

                    Jedis jedis2 = new Jedis("m.redis.sohuno.com", 22513);
                    jedis2.connect();
                    try {

                        for (;;) {
                            String key = jedis.randomKey();
                            if (key == null || "".equals(key)) {
                                break;
                            }
                            String val = jedis.get(key);
                            Long ttl = jedis.ttl(key);
                            if (-1 == ttl) {
                                jedis2.setex(key, timeout ,val);
                            } else {
                                jedis2.setex(key, ttl.intValue(), val);
                            }
                            jedis.del(key);
                            System.out.println(num.incrementAndGet() + ":" + ttl+ " --> " + key + ":" + val);
                        }

                        cd.countDown();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
                        jedis.close();
                        jedis2.close();
                    }
                }
            });
        }

        cd.await();

        System.out.println("over ............");
    }



}
