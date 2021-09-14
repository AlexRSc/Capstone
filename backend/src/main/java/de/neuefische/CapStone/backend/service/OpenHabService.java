package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.config.OpenhabClientConfigProperties;
import de.neuefische.CapStone.backend.model.Device;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabClient;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabLightsBrightnessDto;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabOnOffDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
public class OpenHabService {

    private final HubRepository hubRepository;
    private final OpenHabClient openHabClient;
    private final LightsService lightsService;
    private final OnOffService onOffService;

    @Autowired
    public OpenHabService(HubRepository hubRepository, OpenHabClient openHabClient, LightsService lightsService, OnOffService onOffService) {
        this.hubRepository = hubRepository;
        this.openHabClient = openHabClient;
        this.lightsService = lightsService;
        this.onOffService = onOffService;
    }

    //this is a workaround because I'm using only one turnOn/turnOff method for lights and onOff Devices, we might have to
    //separate them. This way we'll get, even after a while, the right data from our DB
    public void findDeviceToTurnOnOff(Device device, boolean onOff) {
        lightsService.findAndTurnOnOff(device,onOff);
        onOffService.findAndTurnOnOff(device,onOff);
    }


    public ResponseEntity<OpenHabOnOffDto> turnOn (Device device){
        OpenhabClientConfigProperties clientRawCredentials = setOpenHabAccess(device.getHubId());
        String httpHeaders = createHeaders(clientRawCredentials.getUsername(), clientRawCredentials.getPassword());
        OpenHabOnOffDto openHabOnOffDto = OpenHabOnOffDto.builder()
                .itemName(device.getItemName())
                .onOff("ON").build();
        ResponseEntity<OpenHabOnOffDto> openHabOnOffDtoResponseEntity = openHabClient.turnDeviceOn(httpHeaders, openHabOnOffDto);
        findDeviceToTurnOnOff(device, TRUE);
        return openHabOnOffDtoResponseEntity;
    }



    public ResponseEntity<OpenHabOnOffDto> turnOff(Device device) {
        OpenhabClientConfigProperties clientRawCredentials = setOpenHabAccess(device.getHubId());
        String httpHeaders = createHeaders(clientRawCredentials.getUsername(), clientRawCredentials.getPassword());
        OpenHabOnOffDto openHabOnOffDto = OpenHabOnOffDto.builder()
                .itemName(device.getItemName())
                .onOff("OFF").build();
        ResponseEntity<OpenHabOnOffDto> openHabOnOffDtoResponseEntity = openHabClient.turnDeviceOff(httpHeaders, openHabOnOffDto);
        findDeviceToTurnOnOff(device, FALSE);
        return openHabOnOffDtoResponseEntity;
    }

    public ResponseEntity<OpenHabLightsBrightnessDto> changeBrightness(Device device, String brightness) {

        OpenhabClientConfigProperties clientRawCredentials = setOpenHabAccess(device.getHubId());
        String httpHeaders = createHeaders(clientRawCredentials.getUsername(), clientRawCredentials.getPassword());
        OpenHabLightsBrightnessDto openHabLightsBrightnessDto = OpenHabLightsBrightnessDto.builder()
                .itemName(device.getItemName())
                .brightnessLevel(brightness)
                .build();

        ResponseEntity<OpenHabLightsBrightnessDto> responseEntity = openHabClient.changeDeviceBrightness(httpHeaders, openHabLightsBrightnessDto);
        lightsService.findAndChangeBrightness(device, brightness);
        return responseEntity;
    }

    public OpenhabClientConfigProperties setOpenHabAccess(long hubId) {
        Optional<HubEntity> optionalHubEntity = hubRepository.findHubEntitiesById(hubId);
        if(optionalHubEntity.isPresent()){
            HubEntity hubEntity = optionalHubEntity.get();
            return new OpenhabClientConfigProperties(hubEntity.getHubEmail(), hubEntity.getHubPassword());
        }
        else{
            throw new IllegalArgumentException("Hub not found");
        }
    }

    public String createHeaders(String username, String password){

            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII) );

        return "Basic " + new String( encodedAuth );
    }



}
