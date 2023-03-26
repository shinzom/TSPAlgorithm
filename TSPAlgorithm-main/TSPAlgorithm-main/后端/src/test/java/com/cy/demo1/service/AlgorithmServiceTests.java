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

        data.num = 3;
//        data.x = new double[]{1,2,3,4,5,6,7,8,9,10};
//        data.y = new double[]{1,2,3,4,5,6,7,8,9,10};
        data.x = new double[]{1,2,3};
        data.y = new double[]{1,2,3};
        Result2 result = algorithmService.getResult_mtsp(data,1,2000, false);
        for(int i = 0; i < 1; i++) {
            System.out.println("第" + i+1 +"架飞机：" + result.path[i]);
//            for(int j = 0; j <= data.num; j++) {
//                System.out.print(result.path[i][j] + " ");
//            }
        }

//        Result result = algorithmService.getResult_aco(data);
//        System.out.println("time:" + result.time);
//        for(int i = 0; i <= data.num; i++) {
//            System.out.print(result.path[i] + " ");
//        }
    }
}
