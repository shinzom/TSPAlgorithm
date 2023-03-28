package com.cy.demo1.service.impl;

import com.cy.demo1.entity.Result_;
import com.cy.demo1.mapper.ShowMapper;
import com.cy.demo1.service.IShowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShowServiceImpl implements IShowService {

    @Resource
    private ShowMapper showMapper;

    @Override
    public Result_[][] listResult() {
        Integer[] listId = showMapper.listId();
        Result_[][] result = new Result_[listId.length][5];
        for(int i = 0; i < listId.length; i++) {
            result[i][0] = showMapper.getResult_tx(listId[i]);
            result[i][1] = showMapper.getResult_dp(listId[i]);
            result[i][2] = showMapper.getResult_sa(listId[i]);
            result[i][3] = showMapper.getResult_tabu(listId[i]);
            result[i][4] = showMapper.getResult_aco(listId[i]);
        }
        return result;
    }
}
