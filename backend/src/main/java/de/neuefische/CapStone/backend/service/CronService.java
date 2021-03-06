package de.neuefische.CapStone.backend.service;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CronService {


    public String convertDateToCron (Instant date) {
        String dateFormat="ss mm HH dd MM ? ";
        return formatDateByPattern(date, dateFormat);
    }

    private String formatDateByPattern(Instant date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if(date!=null) {
            formatTimeStr=sdf.format(Date.from(date));
        }
        return formatTimeStr;
    }

    public String convertToDailyCron(Instant date) {

        return date.atZone(ZoneId.systemDefault()).getSecond() + " " + date.atZone(ZoneId.systemDefault()).getMinute()
                + " " + date.atZone(ZoneId.systemDefault()).getHour() +" * * *";
    }
}
