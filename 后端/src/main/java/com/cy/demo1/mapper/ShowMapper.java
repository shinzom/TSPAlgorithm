package com.cy.demo1.mapper;

import com.cy.demo1.data.Data;
import com.cy.demo1.entity.Data_;
import com.cy.demo1.entity.Result_;

public interface ShowMapper {
    Integer[] listId();

    Result_ getResult_tx(Integer id);

    Result_ getResult_dp(Integer id);

    Result_ getResult_tabu(Integer id);

    Result_ getResult_sa(Integer id);

    Result_ getResult_aco(Integer id);

    Data_ getData(Integer id);
}
