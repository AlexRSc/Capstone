package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.Hub;
import de.neuefische.CapStone.backend.api.LightDevice;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.LightsService;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.event.internal.DefaultEvictEventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Getter
@Setter
@RequestMapping("/lights")
public class LightsController {

    private final LightsService lightsService;

    public LightsController(LightsService lightsService) {
        this.lightsService = lightsService;
    }

    @PostMapping("/add")
    public ResponseEntity<LightDevice> addLightsDevice(@AuthenticationPrincipal UserEntity authUser,
                                                       @RequestBody LightDevice lightDevice) {

        return null;

    }
}
