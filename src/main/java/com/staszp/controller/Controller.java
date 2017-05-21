package com.staszp.controller;

import com.staszp.dto.response.ResponseModel;
import com.staszp.exception.TestAppException;
import com.staszp.service.RateService;
import com.staszp.dto.RateResultModelDto;
import com.staszp.model.Rate;
import com.staszp.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private static Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private RateService rateService;

    @RequestMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public List<Rate> getAllUsers() {
        return rateService.getAll();
    }

    @RequestMapping(value = "/rates/{start}/{end}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public List<RateResultModelDto> getRates(
            @PathVariable String start, @PathVariable String end) {
        logger.info("Entering getRates() start = {}, end = {}", start, end);
        if (!Validator.isValidDate(start)){
            logger.warn("Not valid data: start = {}", start);
            throw new TestAppException("Start time is not valid", 1001);
        }

        if (!Validator.isValidDate(end)){
            logger.warn("Not valid data: end = {}", end);
            throw new TestAppException("End time is not valid", 1002);
        }
        return rateService.getRates(start, end);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("rates",  rateService.getAll());
        return "page";
    }

    @RequestMapping(value = "/file", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    @ResponseBody
    public String saveFile(@RequestBody List<Rate> rates, HttpServletResponse response ) {
        logger.info("Entering getFile() with size rates = {}", rates.size());
        String fileName = rateService.createExcelFile(rates);
        logger.info("Leaving saveFile, file name = {}", fileName);
        return fileName;
    }

    @RequestMapping(value = "/download/{file_name:.+}", method = RequestMethod.GET)
    public void getFile
            (@PathVariable("file_name") String fileName, HttpServletResponse response) throws Exception {
        logger.info("Entering download()");
        rateService.getFile(fileName,response);
    }

    @ExceptionHandler(value = {TestAppException.class})
    public ResponseModel handle(TestAppException e) {
        ResponseModel errorData = new ResponseModel();
        errorData.setCode(e.getErrorCode());
        errorData.setMessage(e.getMessage());
        return errorData;
    }
}
