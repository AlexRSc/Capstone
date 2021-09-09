package de.neuefische.CapStone.backend.rest.openHab;

import de.neuefische.CapStone.backend.config.OpenhabClientConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OpenHabClient {

    private final OpenHabAPI openHabAPI;

    @Autowired
    public OpenHabClient(OpenHabAPI openHabAPI) {
        this.openHabAPI = openHabAPI;
    }



    public ResponseEntity<OpenHabOnOffDto> turnLightsOn(String httpHeaders, OpenHabOnOffDto openHabOnOffDto) {
        return openHabAPI.turnLightOn(httpHeaders, openHabOnOffDto.getItemName(), openHabOnOffDto.getOnOff());
    }
}
