package com.cy.demo1.service;


import com.cy.demo1.algorithm.MTSP.DataForMultipleDrones;
import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
import com.cy.demo1.data.Result2;
import com.cy.demo1.data.ResultForMultipleDrones;

import java.io.IOException;

public interface IAlgorithmService {
    int getId(Data data);

    //调用算法
    Result getResult_tx(Data data, int id) throws IOException;
    Result getResult_dp(Data data, int id) throws IOException;

    Result getResult_sa(Data data, int id) throws IOException;

    Result getResult_tabu(Data data, int id) throws IOException;

    Result getResult_aco(Data data, int id) throws IOException;

    ResultForMultipleDrones getResult_mtsp(DataForMultipleDrones data, int num, double distance, boolean radioVal) throws IOException;
}
