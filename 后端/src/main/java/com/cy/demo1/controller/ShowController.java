package com.cy.demo1.controller;

import com.cy.demo1.data.Data;
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
}
