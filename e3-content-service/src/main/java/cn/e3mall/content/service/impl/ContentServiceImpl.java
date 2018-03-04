package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, Integer page, Integer rows) {
		// 设置分页信息

		PageHelper.startPage(page, rows);

		// 执行查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);

		// 取分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);

		// 创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public E3Result addContent(TbContent content) {
		content.setUpdated(new Date());
		content.setCreated(new Date());
		tbContentMapper.insert(content);
		//插入数据库后要做缓存同步
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId()+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}

	/**
	 * 根据内容ID查询内容列表
	 * 缓存加在service层的好处：service可以封装事务  其他表现层来调用service都可以使用缓存
	 * 
	 */
	@Override
	public List<TbContent> getContentListByCid(Long cid) {
		//查询缓存
		try {
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			//如果缓存中存在则返回缓存中的内容
			if(StringUtils.isNotBlank(json)){
				List<TbContent> jsonList = JsonUtils.jsonToList(json, TbContent.class);
				return jsonList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//不存在则查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		//写入缓存
		try {
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
