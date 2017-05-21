package com.staszp.service;


import com.staszp.dto.RateResultModelDto;
import com.staszp.dto.incommingmodel.IncomeRateModelDto;
import com.staszp.httpclient.HttpClient;
import com.staszp.model.Rate;
import com.staszp.reposotiry.RateRepository;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class RateServiceImpl implements RateService {

    private static final String CONTENT_TYPE = "application/force-download";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT = "attachment; filename=";
    private static final String FILE_NAME = "RateFile.xlsx";

    private static Logger logger = LoggerFactory.getLogger(RateServiceImpl.class);

    private final String USD = "USD";

    @Autowired
    private RateRepository someEntityRepository;

    @Autowired
    private HttpClient httpClient;

    @Override
    public List<Rate> getAll() {
        return someEntityRepository.findAll();
    }

    @Override
    public List<RateResultModelDto> getRates(String start, String end) {

        List<IncomeRateModelDto> temp = httpClient.getRates(start, end);
        List<RateResultModelDto> result = new ArrayList<>();
        temp.forEach(x ->
                x.getRates().forEach(rate -> {
                    if (rate.getCode().equals(USD)) {
                        result.add(new RateResultModelDto(x.getEffectiveDate(), rate.getMid()));
                    }
                }));
        if (result.size() != 0) {
            saveRatesToDataBase(result);
        }
        return result;
    }

    @Override
    public void getFile(String file, HttpServletResponse response) {
        logger.info("Entering getFile()");
        File fileToDownload = null;
        try {
            fileToDownload = new File(FILE_NAME);
            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType(CONTENT_TYPE);
            response.setHeader(CONTENT_DISPOSITION, ATTACHMENT + FILE_NAME);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Deleting file {}", FILE_NAME);
        boolean result = false;
        try {
            result = Files.deleteIfExists(fileToDownload.toPath());
        } catch (IOException e) {
            logger.warn("Error deleting file - {}", FILE_NAME);
        }
        logger.info("Leaving getFile(), delete file = {}", result);
    }


    private void saveRatesToDataBase(List<RateResultModelDto> modelDtoList) {
        for (RateResultModelDto rateResultModelDto : modelDtoList) {
            if (!someEntityRepository.existByTime(rateResultModelDto.getDate())) {
                someEntityRepository.save(
                        new Rate(rateResultModelDto.getDate(), String.valueOf(rateResultModelDto.getRate())));
            }
        }
    }

    @Override
    public String createExcelFile(List<Rate> list) {
        logger.info("Entering createExcelFile() with list size = {}", list.size());
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Rates");
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Date");
        header.createCell(1).setCellValue("Rate");
        // create data rows
        int rowCount = 1;

        for (Rate rate : list) {
            XSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(rate.getTime());
            aRow.createCell(1).setCellValue(rate.getRate());
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            logger.warn("Error saving file, {}", e.getLocalizedMessage());
        } catch (IOException e) {
            logger.warn("Error during saving excel file, {}", e.getLocalizedMessage());
        }
        logger.info("Leaving createExcelFile()");
        return FILE_NAME;
    }
}
