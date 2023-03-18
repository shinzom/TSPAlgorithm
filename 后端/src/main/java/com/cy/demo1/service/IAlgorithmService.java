package com.cy.demo1.service;


import com.cy.demo1.data.Data;
import java.io.IOException;

public interface IAlgorithmService {
    //调用算法
    int[] getResult(Data data) throws IOException;

}
