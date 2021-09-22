package de.neuefische.CapStone.backend.rest.openHab;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.ResponseEntity;

public interface OpenHabAPI {

    String ACCESS_PARAM = "access";

    @RequestLine(value = "POST /rest/items/{item}")
    @Headers({"Content-Type: text/plain", "Authorization: {"+ACCESS_PARAM+"}"})
    ResponseEntity<OpenHabOnOffDto> turnLightOn(@Param(ACCESS_PARAM) String httpHeaders, @Param("item") String item, String onOff);

    @RequestLine(value = "POST /rest/items/{item}")
    @Headers({"Content-Type: text/plain", "Authorization: {"+ACCESS_PARAM+"}"})
    ResponseEntity<OpenHabOnOffDto> turnLightsOff(@Param(ACCESS_PARAM) String httpHeaders, @Param("item") String item, String onOff);

    @RequestLine(value = "POST /rest/items/{item}")
    @Headers({"Content-Type: text/plain", "Authorization: {"+ACCESS_PARAM+"}"})
    ResponseEntity<OpenHabLightsBrightnessDto> changeBrightness(@Param(ACCESS_PARAM) String httpHeaders, @Param("item") String item, String brightnessLevel);

    @RequestLine(value = "POST /rest/items/{item}")
    @Headers({"Content-Type: text/plain", "Authorization: {"+ACCESS_PARAM+"}"})
    ResponseEntity<OpenHabAlarmDto> handleItems(@Param(ACCESS_PARAM) String httpHeaders, @Param("item") String item, String command);

}
