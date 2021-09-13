package de.neuefische.CapStone.backend.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CronService {


    public String convertDateToCron (Date date) {
        String dateFormat="ss mm HH dd MM ? ";
        return formatDateByPattern(date, dateFormat);
    }

    private String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if(date!=null) {
            formatTimeStr=sdf.format(date);
        }
        return formatTimeStr;
    }
}
