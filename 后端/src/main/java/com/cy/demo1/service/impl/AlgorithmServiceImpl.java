package com.cy.demo1.service.impl;

import com.cy.demo1.algorithm.SA_TSP;
import com.cy.demo1.algorithm.TSp;
import com.cy.demo1.algorithm.Tabu;
import com.cy.demo1.algorithm.TxTsp;
import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
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
    public Result getResult_tx(Data data) throws IOException {
//       data = new Data();

        //读入数据
//        data.num = 3;
//        data.x = new double[]{1,3,2};
//        data.y = new double[]{1,3,2};

        //调用算法，返回Data对象
        TxTsp txTsp = new TxTsp();
        Result result = new Result();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = txTsp.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;
        return result;
    }

    @Override
    public Result getResult_dp(Data data) throws IOException {
        //调用算法，返回Data对象
        Result result = new Result();
        TSp tsp= new TSp();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = tsp.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        return result;
    }

    @Override
    public Result getResult_sa(Data data) throws IOException {
        //调用算法，返回Data对象
        Result result = new Result();
        SA_TSP sa_tsp= new SA_TSP();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = sa_tsp.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;
        return result;
    }

    public Result getResult_tabu(Data data) throws IOException {
        //调用算法，返回Data对象
        Result result = new Result();
        Tabu tabu = new Tabu();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = tabu.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        return result;
    }
}
