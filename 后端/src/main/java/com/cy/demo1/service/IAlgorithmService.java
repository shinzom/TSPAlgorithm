package com.cy.demo1.service;


import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
import com.cy.demo1.data.Result2;

import java.io.IOException;

public interface IAlgorithmService {
    //调用算法
    Result getResult_tx(Data data) throws IOException;
    Result getResult_dp(Data data) throws IOException;

    Result getResult_sa(Data data) throws IOException;

    Result getResult_tabu(Data data) throws IOException;

    Result getResult_aco(Data data) throws IOException;

    Result2 getResult_mtsp(Data data, int num, double distance) throws IOException;
}
