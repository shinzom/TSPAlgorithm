package com.cy.demo1.service;


import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
import com.cy.demo1.data.Result2;

import java.io.IOException;

public interface IAlgorithmService {
    int getId(Data data);

    //调用算法
    Result getResult_tx(Data data, int id) throws IOException;
    Result getResult_dp(Data data) throws IOException;

    Result getResult_sa(Data data) throws IOException;

    Result getResult_tabu(Data data) throws IOException;

    Result getResult_aco(Data data) throws IOException;

    Result2 getResult_mtsp(Data data, int num, double distance, boolean radioVal) throws IOException;
}
