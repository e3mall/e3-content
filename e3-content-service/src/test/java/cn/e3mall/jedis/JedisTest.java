package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	@Test
	public void testJedis(){
		//创建一个jedis对象 参数 host port
		Jedis jedis = new Jedis("119.23.52.45",6379);
		//向redis服务器发送数据
		jedis.set("test123", "first");
		//取数据
		String value = jedis.get("test123");
		System.out.println(value);
		//关闭连接
		jedis.close();
	}
	
	@Test
	public void testJedisPool(){
		JedisPool pool = new JedisPool("119.23.52.45",6379);
		Jedis jedis = pool.getResource();
		String value = jedis.get("test123");
		System.out.println(value);
		jedis.close();
		pool.close();
	}
	
	/**
	 * 前面两个是连接单机版redis
	 * 这个是连接集群
	 */
	/*@Test
	public void testJedisCluster(){
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.130", 7001));
		nodes.add(new HostAndPort("192.168.25.130", 7002));
		nodes.add(new HostAndPort("192.168.25.130", 7003));
		nodes.add(new HostAndPort("192.168.25.130", 7004));
		nodes.add(new HostAndPort("192.168.25.130", 7005));
		nodes.add(new HostAndPort("192.168.25.130", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用jedisCluster操作redis
		jedisCluster.set("test", "123");
		String value = jedisCluster.get("test");
		System.out.println(value);
		
		jedisCluster.close();
	}*/
}
