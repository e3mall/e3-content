package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

public class JedisClientTest {
	@Test
	public void testJedisClient(){
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = context.getBean(JedisClient.class);
		jedisClient.set("mytest", "jedisClient");
		String string = jedisClient.get("mytest");
		System.out.println(string);
		
	}
}
