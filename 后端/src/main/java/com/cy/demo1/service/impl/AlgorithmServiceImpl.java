package com.cy.demo1.service.impl;

import com.cy.demo1.algorithm.TxTsp;
import com.cy.demo1.data.Data;
import com.cy.demo1.mapper.AlgorithmMapper;
import com.cy.demo1.service.IAlgorithmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class AlgorithmServiceImpl implements IAlgorithmService{

    @Resource
    private AlgorithmMapper algorithmMapper;

    @Override
    public int[] getResult(Data data) throws IOException {
//       data = new Data();

        //读入数据
//        data.num = 3;
//        data.x = new double[]{1,3,2};
//        data.y = new double[]{1,3,2};

        //调用算法，返回Data对象
        TxTsp txTsp = new TxTsp();
        int[] result = txTsp.main(data);
//        Data result = data;
//
//        String str = result.x[0] + "," + result.y[0];
//        for(int i = 1; i < result.num; i++) {
//            str = str + " " + result.x[i] + "," + result.y[i];
//        }
//        System.out.println(str);
        return result;
    }
}
