package com.cy.demo1.service.impl;

import com.cy.demo1.entity.Data_;
import com.cy.demo1.entity.Result_;
import com.cy.demo1.kml.KmlUtils;
import com.cy.demo1.mapper.ShowMapper;
import com.cy.demo1.service.IShowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

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

    @Override
    public Result_ getResult(int id, int al) {
        Result_ result;
        switch (al){
            case 0:
                result = showMapper.getResult_tx(id);
                break;
            case 1:
                result = showMapper.getResult_dp(id);
                break;
            case 2:
                result = showMapper.getResult_sa(id);
                break;
            case 3:
                result = showMapper.getResult_tabu(id);
                break;
            case 4:
                result = showMapper.getResult_aco(id);
                break;
            default:
                result = null;
        }
        return result;
    }

    @Override
    public Data_ getData(int id) {
        Data_ data = showMapper.getData(id);
        return data;
    }

    @Override
    public void getKml(double[] x, double[] y, int[] path, String filename) throws IOException {
        String str = "";
        for(int i = 0; i < path.length - 1; i++) {
            str = str + x[path[i]] + "," + y[path[i]] + "," + "100" + " ";
        }
        str = str + x[path[path.length - 1]] + "," + y[path[path.length - 1]] + "," + "100";
        KmlUtils.setTravelsKml(str, filename);
    }
}