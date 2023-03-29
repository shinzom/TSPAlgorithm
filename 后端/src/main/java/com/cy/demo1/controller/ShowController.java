package com.cy.demo1.controller;

import com.cy.demo1.data.Data;
import com.cy.demo1.data.Data2;
import com.cy.demo1.entity.Data_;
import com.cy.demo1.entity.Result_;
import com.cy.demo1.service.IShowService;
import com.cy.demo1.util.JsonResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin
public class ShowController extends BaseController{
    @Resource
    IShowService iShowService;

    @RequestMapping("show")
    public JsonResult<Result_[][]> show() throws IOException {
        Result_[][] data = iShowService.listResult();
        return new JsonResult<Result_[][]>(OK, data);
    }

    @RequestMapping("get_data")
    public JsonResult<Data2> get_data(int id, int al) throws IOException {
        Data2 data = new Data2();
        Data_ data_ = iShowService.getData(id);
        Result_ result_ = iShowService.getResult(id, al);
        String strx = data_.getX();
        String stry = data_.getY();
        String strPath = result_.getPath();

//        String str_x = strx.substring(1, strx.length() - 1);
//        String str_y = stry.substring(1, stry.length() - 1);
//        String str_path = stry.substring(1, strPath.length() - 1);
//        String numArray_x[] = str_x.split(",");
//        String numArray_y[] = str_y.split(",");
//        String numArray_path[] = str_path.split(",");
        String numArray_x[] = strx.split(",");
        String numArray_y[] = stry.split(",");
        String numArray_path[] = strPath.split(",");
        double[]x = new double[numArray_x.length];
        double[]y = new double[numArray_y.length];
        int[]path = new int[numArray_path.length];
        double d;
        int t;
        for (int i = 0; i <numArray_x.length; i++) {
            d = Double.parseDouble(numArray_x[i]);  //string 转 double
            x[i] = d;
            d = Double.parseDouble(numArray_y[i]);  //string 转 double
            y[i] = d;
//            t = Integer.parseInt(numArray_path[i]);  //string 转 int
//            path[i] = t;
        }
        for (int i = 0; i <numArray_path.length; i++) {
            t = Integer.valueOf(numArray_path[i]).intValue();;  //string 转 int
            path[i] = t;
        }
//        t = Integer.parseInt(numArray_path[numArray_path.length-1]);  //string 转 int
//        y[numArray_path.length-1] = t;
        data.x = x;
        data.y = y;
        data.path = path;
        return new JsonResult<Data2>(OK, data);
    }

    @RequestMapping("get_kml")
    public JsonResult<Integer> kml(int id, int al, String filename) throws IOException {
        Data2 data = new Data2();
        Data_ data_ = iShowService.getData(id);
        Result_ result_ = iShowService.getResult(id, al);
        String strx = data_.getX();
        String stry = data_.getY();
        String strPath = result_.getPath();

        String numArray_x[] = strx.split(",");
        String numArray_y[] = stry.split(",");
        String numArray_path[] = strPath.split(",");
        double[]x = new double[numArray_x.length];
        double[]y = new double[numArray_y.length];
        int[]path = new int[numArray_path.length];
        double d;
        int t;
        for (int i = 0; i <numArray_x.length; i++) {
            d = Double.parseDouble(numArray_x[i]);  //string 转 double
            x[i] = d;
            d = Double.parseDouble(numArray_y[i]);  //string 转 double
            y[i] = d;
        }
        for (int i = 0; i <numArray_path.length; i++) {
            t = Integer.valueOf(numArray_path[i]).intValue();;  //string 转 int
            path[i] = t;
        }
        iShowService.getKml(x, y, path, filename);
        return new JsonResult<Integer>(OK,0);
    }
}
