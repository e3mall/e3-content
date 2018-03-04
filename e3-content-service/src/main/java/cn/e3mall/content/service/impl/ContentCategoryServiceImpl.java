package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	/**
	 * 根据指定的parentId找出当前内容类别下的子类别
	 */
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		//转换成EasyUITreeNode的列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 创建一个tb_content_category对应的pojo
		TbContentCategory contentCategory = new TbContentCategory();
		// 设置属性
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setUpdated(new Date());
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		// 插入
		contentCategoryMapper.insert(contentCategory); 
		// 检查父节点的is_parent是否为，不是的话 改为true
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		// 返回E3Result
		return E3Result.ok(contentCategory);
	}

	@Override
	public void updateContentCategory(Long id, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
	}

}
