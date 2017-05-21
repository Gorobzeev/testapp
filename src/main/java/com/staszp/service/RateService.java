package com.staszp.service;

import com.staszp.dto.RateResultModelDto;
import com.staszp.model.Rate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface RateService {

    List<Rate> getAll();

    List<RateResultModelDto> getRates(String start, String end);

    void getFile(String file , HttpServletResponse response);

    String createExcelFile(List<Rate> list);
}
