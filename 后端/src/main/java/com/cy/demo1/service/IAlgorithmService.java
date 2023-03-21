package com.cy.demo1.service;


import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;

import java.io.IOException;

public interface IAlgorithmService {
    //调用算法
    Result getResult(Data data) throws IOException;

}
