package com.cy.demo1.controller;

import com.cy.demo1.data.Data;
import com.cy.demo1.service.IAlgorithmService;
import com.cy.demo1.util.JsonResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin
public class AlgorithmController extends BaseController{

    @Resource
    private IAlgorithmService algorithmService;

    @RequestMapping("callAlgorithm")
    public JsonResult<int[]> callAlgorithm(int num, double[][] xy) throws IOException {
        Data data = new Data();
        data.num = num;
        data.x = new double[num];
        data.y = new double[num];
        for(int i = 0; i < num; i++) {
            data.x[i] = xy[i][0];
            data.y[i] = xy[i][1];
        }
        int[] result = algorithmService.getResult(data);

        return new JsonResult<int[]>(OK, result);
    }

}
