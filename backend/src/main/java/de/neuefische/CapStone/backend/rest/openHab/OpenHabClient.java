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



    public ResponseEntity<OpenHabOnOffDto> turnDeviceOn(String httpHeaders, OpenHabOnOffDto openHabOnOffDto) {
        return openHabAPI.turnLightOn(httpHeaders, openHabOnOffDto.getItemName(), openHabOnOffDto.getOnOff());
    }

    public ResponseEntity<OpenHabOnOffDto> turnDeviceOff(String httpHeaders, OpenHabOnOffDto openHabOnOffDto) {
        return openHabAPI.turnLightsOff(httpHeaders, openHabOnOffDto.getItemName(), openHabOnOffDto.getOnOff());
    }

    public ResponseEntity<OpenHabLightsBrightnessDto> changeDeviceBrightness(String httpHeaders, OpenHabLightsBrightnessDto openHabLightsBrightnessDto) {
        return openHabAPI.changeBrightness(httpHeaders, openHabLightsBrightnessDto.getItemName(), openHabLightsBrightnessDto.getBrightnessLevel());
    }

    public ResponseEntity<OpenHabAlarmDto> handleCommands(String httpHeaders, OpenHabAlarmDto openHabAlarmDto) {
        return openHabAPI.handleItems(httpHeaders, openHabAlarmDto.getItemName(), openHabAlarmDto.getCommand());
    }
}
