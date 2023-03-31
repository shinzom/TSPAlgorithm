package com.cy.demo1.controller;

import com.cy.demo1.algorithm.MTSP.ForbiddenZone;
import com.cy.demo1.data.Data;
import com.cy.demo1.data.Data2;
import com.cy.demo1.entity.Data_;
import com.cy.demo1.entity.Result_;
import com.cy.demo1.service.IShowService;
import com.cy.demo1.util.JsonResult;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.cy.demo1.kml.MultipleKMLGenerator.generateMultipleKML;


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

    @CrossOrigin
    @PostMapping("/downloadKML")
    public ResponseEntity<ByteArrayResource> downloadKML(HttpServletResponse response,
                                                         @RequestBody Map<String, Object> requestData) throws IOException {

        // Get new points and path from request data
        List<Map<String, Double>> newPointsList = (List<Map<String, Double>>) requestData.get("newPoints");
        List<List<Integer>> pathList = (List<List<Integer>>) requestData.get("path");
        int[][] path = pathList.stream().map(u -> u.stream().mapToInt(i -> i).toArray()).toArray(int[][]::new);


        // Convert new points list to double arrays
        double[] x = new double[newPointsList.size()];
        double[] y = new double[newPointsList.size()];
        for (int i = 0; i < newPointsList.size(); i++) {
            x[i] = newPointsList.get(i).get("lng");
            y[i] = newPointsList.get(i).get("lat");
        }

//        // 生成KML字符串
//        double[] x = {39.984702, 39.984683, 39.984686, 39.984688};
//        double[] y = {116.318417, 116.318450, 116.318417, 116.318385};
//        int[][] path = {{0, 1, 2}, {1, 2, 3}, {2, 3, 0}};

        System.out.println("hello");
        // Generate KML files
        generateMultipleKML(x, y, path, "paths.zip");

        // Send zip file to frontend
        Path pathToFile = Paths.get("paths.zip");
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(pathToFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=paths.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}
