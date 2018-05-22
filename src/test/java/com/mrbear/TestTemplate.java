package com.mrbear;


import com.mrbear.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestTemplate {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd(){
        TbItem item=new TbItem();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为 2 号专卖店");
        item.setTitle("华为 Mate9");
        item.setPrice(new BigDecimal(2000));
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public  void findById(){
        TbItem item = solrTemplate.getById(1L, TbItem.class);
        System.out.println(item.getTitle());
    }

    @Test
    public void deletById(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void testAddList(){
        List<TbItem> list=new ArrayList();
        for(int i=0;i<100;i++){
            TbItem item=new TbItem();
            item.setId(i+1L);
            item.setBrand("华为2");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为 2 号专卖店");
            item.setTitle("华为 Mate"+i);
            item.setPrice(new BigDecimal(2000+i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();



    }



    @Test
    public void testPage(){
        SimpleQuery query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_category")
                //类似于like 支持通配符
                .contains("手机");
        criteria=criteria.and("item_brand").contains("2");

        query.addCriteria(criteria);

        //query.setOffset(20);//开始索引默认是0
        //query.setRows(20);//每页记录数默认是10

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        //当前页结果
        for(TbItem item:page.getContent()){
            System.out.println(item.getTitle()+" "+item.getPrice());
        }

        System.out.println("总记录数"+page.getTotalElements());
        System.out.println("每页的大小"+page.getSize());
        System.out.println("总页数"+page.getTotalPages());
    }

    @Test
    public void deletAll(){
        //根据id删除
//        solrTemplate.deleteById("1");

        SimpleQuery query = new SimpleQuery("*:*");



        solrTemplate.delete(query);
        solrTemplate.commit();
    }





}
