package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	EasyUIDataGridResult getContentListByCategoryId(Long categoryId,Integer page, Integer rows);
	
	List<TbContent> getContentListByCid(Long cid);
	E3Result addContent(TbContent content);
	
}
