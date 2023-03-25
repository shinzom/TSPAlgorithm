package com.cy.demo1.service;


import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;

import java.io.IOException;

public interface IAlgorithmService {
    //调用算法
    Result getResult_tx(Data data) throws IOException;
    Result getResult_dp(Data data) throws IOException;

    Result getResult_sa(Data data) throws IOException;

    Result getResult_tabu(Data data) throws IOException;
}
