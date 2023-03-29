package com.cy.demo1.service;

import com.cy.demo1.entity.Data_;
import com.cy.demo1.entity.Result_;

public interface IShowService {
    Result_[][] listResult();

    Result_ getResult(int id, int al);

    Data_ getData(int id);
}
