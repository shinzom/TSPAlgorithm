package com.cy.demo1.mapper;

import com.cy.demo1.data.Data;
import com.cy.demo1.entity.Result_;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
//@RunWith：表示启动这个单元测试类
@RunWith(SpringRunner.class)
public class AlgorithmMapperTests {
    @Resource
    private AlgorithmMapper algorithmMapper;

    @Test
    public void insertResult() {
        Data data = new Data();
        data.num = 2;
        data.strx = "[1,2]";
        data.stry = "[1,2]";
        algorithmMapper.insertResult(data);
    }

    @Test
    public void findMaxId() {
        int max = algorithmMapper.findMaxId();
        System.out.println(max);
    }

    @Test
    public void insertResult_tx() {
        Result_ result = new Result_();
        result.setResult_id(4);
        result.setTime(100);
        result.setDistance(222.22);
        result.setPath("[1,2]");
        algorithmMapper.insertResult_tx(result);
    }
}
