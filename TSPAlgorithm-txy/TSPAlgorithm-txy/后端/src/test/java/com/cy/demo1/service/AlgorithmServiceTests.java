package com.cy.demo1.service;

import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
import com.cy.demo1.data.Result2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
//@RunWith：表示启动这个单元测试类
@RunWith(SpringRunner.class)
public class AlgorithmServiceTests {
    @Resource
    private IAlgorithmService algorithmService;

    @Test
    public void getResult() throws IOException {
        Data data = new Data();

        data.num = 10;
        data.x = new double[]{1,2,3,4,5,6,7,8,9,10};
        data.y = new double[]{1,2,3,4,5,6,7,8,9,10};
//        Result2 result = algorithmService.getResult_mtsp(data,3,250, false);
        Result result = algorithmService.getResult_tx(data, 4);
        System.out.println("time:" + result.time);
        for(int i = 0; i <= data.num; i++) {
            System.out.print(result.path[i] + " ");
        }
    }
}
