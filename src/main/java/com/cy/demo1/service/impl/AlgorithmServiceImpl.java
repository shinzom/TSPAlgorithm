package com.cy.demo1.service.impl;

import com.cy.demo1.algorithm.*;
import com.cy.demo1.algorithm.Aco.ACO;
import com.cy.demo1.algorithm.MTSP.DataForMultipleDrones;
import com.cy.demo1.algorithm.MTSP.MTSPWithLimits;
import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
import com.cy.demo1.data.Result2;
import com.cy.demo1.data.ResultForMultipleDrones;
import com.cy.demo1.entity.Result_;
import com.cy.demo1.mapper.AlgorithmMapper;
import com.cy.demo1.service.IAlgorithmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

import static com.cy.demo1.algorithm.MTSP.MTSPWithLimits.solveMTSP;

@Service
public class AlgorithmServiceImpl implements IAlgorithmService{

    @Resource
    private AlgorithmMapper algorithmMapper;

    @Override
    public int getId(Data data){
        algorithmMapper.insertResult(data);
        int id = algorithmMapper.findMaxId();
        return id;
    }
    @Override
    public Result getResult_tx(Data data, int id) throws IOException {
        TxTsp txTsp = new TxTsp();
        Result result = new Result();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = txTsp.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        Result_ result_ = new Result_();
        result_.setResult_id(id);
        result_.setTime(time);
        result_.setDistance(txTsp.getDistance());
        String str = "";
        for(int i = 0; i < data.num; i++) {
            str += path[i] + ",";
        }
        str += path[data.num];
        result_.setPath(str);
        algorithmMapper.insertResult_tx(result_);

        return result;
    }

    @Override
    public Result getResult_dp(Data data, int id) throws IOException {
        Result result = new Result();
        TSp tsp= new TSp();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = tsp.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        Result_ result_ = new Result_();
        result_.setResult_id(id);
        result_.setTime(time);
        result_.setDistance(tsp.getDistance());
        String str = "";
        for(int i = 0; i < data.num; i++) {
            str += path[i] + ",";
        }
        str += path[data.num];
        result_.setPath(str);
        algorithmMapper.insertResult_dp(result_);

        return result;
    }

    @Override
    public Result getResult_sa(Data data, int id) throws IOException {
        Result result = new Result();
        SA_TSP sa_tsp= new SA_TSP();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = sa_tsp.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        Result_ result_ = new Result_();
        result_.setResult_id(id);
        result_.setTime(time);
        result_.setDistance(sa_tsp.getDistance());
        String str = "";
        for(int i = 0; i < data.num; i++) {
            str += path[i] + ",";
        }
        str += path[data.num];
        result_.setPath(str);
        algorithmMapper.insertResult_sa(result_);

        return result;
    }

    public Result getResult_tabu(Data data, int id) throws IOException {
        Result result = new Result();
        Tabu tabu = new Tabu();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = tabu.main(data);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        Result_ result_ = new Result_();
        result_.setResult_id(id);
        result_.setTime(time);
        result_.setDistance(tabu.getDistance());
        String str = "";
        for(int i = 0; i < data.num; i++) {
            str += path[i] + ",";
        }
        str += path[data.num];
        result_.setPath(str);
        algorithmMapper.insertResult_tabu(result_);

        return result;
    }

    public Result getResult_aco(Data data, int id) throws IOException {
        Result result = new Result();
        ACO aco = new ACO();
        long startTime=System.currentTimeMillis();   //获取开始时间
        int[] path = aco.main(data);;
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;

        Result_ result_ = new Result_();
        result_.setResult_id(id);
        result_.setTime(time);
        result_.setDistance(aco.getDistance());
        String str = "";
        for(int i = 0; i < data.num; i++) {
            str += path[i] + ",";
        }
        str += path[data.num];
        result_.setPath(str);
        algorithmMapper.insertResult_aco(result_);

        return result;
    }


    public ResultForMultipleDrones getResult_mtsp(DataForMultipleDrones data, int num, double distance, boolean radioVal) throws IOException {
        ResultForMultipleDrones result = new ResultForMultipleDrones();

        long startTime=System.currentTimeMillis();   //获取开始时间

        int path[][] = solveMTSP(data, num, distance, radioVal);
        long endTime=System.currentTimeMillis(); //获取结束时间
        long time = endTime-startTime;
        result.time = time;
        System.out.println("程序运行时间： " + time + "ms");
        result.path = path;
        result.x = MTSPWithLimits.dataForMultipleDronesWithForbiddenZones.x;
        result.y = MTSPWithLimits.dataForMultipleDronesWithForbiddenZones.y;
        result.num = MTSPWithLimits.dataForMultipleDronesWithForbiddenZones.num;

        return result;
    }
}
