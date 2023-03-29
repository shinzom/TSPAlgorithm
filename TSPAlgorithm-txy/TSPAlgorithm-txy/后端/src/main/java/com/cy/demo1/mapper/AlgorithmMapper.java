package com.cy.demo1.mapper;

import com.cy.demo1.data.Data;
import com.cy.demo1.entity.Result_;

public interface AlgorithmMapper {
    void insertResult(Data data);

    Integer findMaxId();

    void insertResult_tx(Result_ result);

    void insertResult_dp(Result_ result);

    void insertResult_tabu(Result_ result);

    void insertResult_sa(Result_ result);

    void insertResult_aco(Result_ result);
}