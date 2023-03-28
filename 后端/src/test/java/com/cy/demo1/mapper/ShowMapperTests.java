package com.cy.demo1.mapper;

import com.cy.demo1.entity.Data_;
import com.cy.demo1.entity.Result_;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
//@RunWith：表示启动这个单元测试类
@RunWith(SpringRunner.class)
public class ShowMapperTests {
    @Resource
    private ShowMapper showMapper;

    @Test
    public void show() {
        Integer[] list = showMapper.listId();
        for(int i = 0; i < list.length; i++) {
            System.out.print(list[i] + " ");
        }
    }

    @Test
    public void getResult_tx() {
        Result_ result = showMapper.getResult_tx(4);
        System.out.println("resultId: " + result.getResult_id());
        System.out.println("time: " + result.getTime());
        System.out.println("distance: " + result.getDistance());
        System.out.println("path: " + result.getPath());
    }

    @Test
    public void getData() {
        Data_ data = showMapper.getData(4);
//        System.out.println("resultId: " + data.getResult_id());
//        System.out.println("number: " + data.getNumber());
//        System.out.println("x: " + data.getX());
//        System.out.println("y: " + data.getY());
        System.out.println(data);
    }

}
