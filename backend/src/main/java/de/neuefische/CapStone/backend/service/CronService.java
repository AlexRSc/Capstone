package de.neuefische.CapStone.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
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

    //this method is not active yet, it will translate the date into cron-formate, while days being daily, and not just once
    //this method needs to get tested
    public String convertToDailyCron(Instant date) {

        String formatTimeStr= date.atZone(ZoneId.systemDefault()).getSecond() + " " + date.atZone(ZoneId.systemDefault()).getMinute()
                + " " + date.atZone(ZoneId.systemDefault()).getHour() +" * * *";
        return formatTimeStr;
    }
}
