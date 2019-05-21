package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.page.service.impl
 * @date 2019-4-25
 */
@Service(timeout = 5000, interfaceClass = ItemPageService.class)
@Transactional
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${PAGE_SERVICE_DIR}")
    private String PAGE_SERVICE_DIR;

    @Override
    public boolean genItemHtml(Long goodsId) {
        //1、查询商品基本与扩展信息
        TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if(goods != null){
            try {
                TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
                //2、使用Freemarker绑定相应的信息与输出模板
                Configuration cfg = freeMarkerConfigurer.getConfiguration();
                //获取模板
                Template template = cfg.getTemplate("item.ftl");
                Map map = new HashMap();
                map.put("goods", goods);
                map.put("goodsDesc", goodsDesc);

                //查询商品三级分类
                String itemCat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
                map.put("itemCat1Name", itemCat1Name);
                String itemCat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
                map.put("itemCat2Name", itemCat2Name);
                String itemCat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
                map.put("itemCat3Name", itemCat3Name);

                //查询商品sku列表
                Example example = new Example(TbItem.class);
                Example.Criteria criteria = example.createCriteria();
                //设置查询条件
                criteria.andEqualTo("goodsId", goodsId);
                criteria.andEqualTo("status", "1");//只查询启用的商品
                //以默认信息排序
                example.setOrderByClause("isDefault desc");
                List<TbItem> itemList = itemMapper.selectByExample(example);
                map.put("itemList", itemList);
                //输出对象
                String parentPath = new File("").getCanonicalPath().replace("\\", "/").split("pinyougou_page_service")[0];//读取项目路径并且转义（"\"替换为"/"）,去除多余"pinyougou_page_service"后缀
                Writer out = new OutputStreamWriter(new FileOutputStream(parentPath + PAGE_SERVICE_DIR + goodsId + ".html"), "UTF-8");
                //保存文档
                template.process(map,out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
