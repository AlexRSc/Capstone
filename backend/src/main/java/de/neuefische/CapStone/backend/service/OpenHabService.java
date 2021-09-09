package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.config.OpenhabClientConfigProperties;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabClient;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabOnOffDto;
import feign.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class OpenHabService {

    private final HubRepository hubRepository;
    private final OpenHabClient openHabClient;

    @Autowired
    public OpenHabService(HubRepository hubRepository, OpenHabClient openHabClient) {
        this.hubRepository = hubRepository;
        this.openHabClient = openHabClient;
    }


    public ResponseEntity<OpenHabOnOffDto> turnLightOn (LightsDeviceEntity lightsDeviceEntity){
        OpenhabClientConfigProperties clientRawCredentials = setOpenHabAccess(lightsDeviceEntity.getDevice().getHubId());
        String httpHeaders = createHeaders(clientRawCredentials.getUsername(), clientRawCredentials.getPassword());
        OpenHabOnOffDto openHabOnOffDto = OpenHabOnOffDto.builder()
                .itemName(lightsDeviceEntity.getDevice().getItemName())
                .onOff("ON").build();
        return openHabClient.turnLightsOn(httpHeaders, openHabOnOffDto);
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
