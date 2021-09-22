package de.neuefische.CapStone.backend.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEvent {

    private long id;
    private boolean isEvent;
    private boolean isDaily;
    private Date date;
}
