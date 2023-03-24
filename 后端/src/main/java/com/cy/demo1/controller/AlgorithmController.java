package com.cy.demo1.controller;

import com.cy.demo1.data.Data;
import com.cy.demo1.data.Result;
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

    @RequestMapping("callAlgorithm_tx")
    public JsonResult<Result> callAlgorithm_tx(int num, String x, String y) throws IOException {
//    public JsonResult<int[]> callAlgorithm(int num) throws IOException {
        Data data = new Data();
        data.num = num;
        String strx = x.substring(1, x.length() - 1);
        String stry = y.substring(1, y.length() - 1);

        String numArray1[] = strx.split(",");
        String numArray2[] = stry.split(",");

        double doubleArray1[] = new double[numArray1.length];
        double doubleArray2[] = new double[numArray2.length];

        double d;
        for (int i = 0; i < num; i++) {
            d = Double.parseDouble(numArray1[i]);  //string 转 double
            doubleArray1[i] = d;
            d = Double.parseDouble(numArray2[i]);  //string 转 double
            doubleArray2[i] = d;
        }
        data.x = doubleArray1;
        data.y = doubleArray2;
//        data.num = 3;
//        data.x = new double[]{1,3,2};
//        data.y = new double[]{1,3,2};
        Result result = algorithmService.getResult_tx(data);
        return new JsonResult<Result>(OK, result);
    }

    @RequestMapping("callAlgorithm_dp")
    public JsonResult<Result> callAlgorithm_dp(int num, String x, String y) throws IOException {
        Data data = new Data();
        data.num = num;
        String strx = x.substring(1, x.length() - 1);
        String stry = y.substring(1, y.length() - 1);

        String numArray1[] = strx.split(",");
        String numArray2[] = stry.split(",");

        double doubleArray1[] = new double[numArray1.length];
        double doubleArray2[] = new double[numArray2.length];

        double d;
        for (int i = 0; i < num; i++) {
            d = Double.parseDouble(numArray1[i]);  //string 转 double
            doubleArray1[i] = d;
            d = Double.parseDouble(numArray2[i]);  //string 转 double
            doubleArray2[i] = d;
        }
        data.x = doubleArray1;
        data.y = doubleArray2;
        Result result = algorithmService.getResult_dp(data);
        return new JsonResult<Result>(OK, result);
    }

    @RequestMapping("callAlgorithm_sa")
    public JsonResult<Result> callAlgorithm_sa(int num, String x, String y) throws IOException {
        Data data = new Data();
        data.num = num;
        String strx = x.substring(1, x.length() - 1);
        String stry = y.substring(1, y.length() - 1);

        String numArray1[] = strx.split(",");
        String numArray2[] = stry.split(",");

        double doubleArray1[] = new double[numArray1.length];
        double doubleArray2[] = new double[numArray2.length];

        double d;
        for (int i = 0; i < num; i++) {
            d = Double.parseDouble(numArray1[i]);  //string 转 double
            doubleArray1[i] = d;
            d = Double.parseDouble(numArray2[i]);  //string 转 double
            doubleArray2[i] = d;
        }
        data.x = doubleArray1;
        data.y = doubleArray2;
        Result result = algorithmService.getResult_sa(data);
        return new JsonResult<Result>(OK, result);
    }
}
