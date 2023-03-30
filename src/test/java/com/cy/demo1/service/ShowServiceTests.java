package com.cy.demo1.service;

import com.cy.demo1.data.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
//@RunWith：表示启动这个单元测试类
@RunWith(SpringRunner.class)
public class ShowServiceTests {
    @Resource
    private IShowService iShowService;

    @Test
    public void getKml() throws IOException {
        iShowService.getKml(new double[]{1,2}, new double[]{1,2}, new int[]{0,1},"test");
    }
}
