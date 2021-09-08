package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.Hub;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.HubService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasText;

@CrossOrigin
@RestController
@Getter
@Setter
@RequestMapping("/hub")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }


    @PostMapping("/newHub")
    public ResponseEntity<Hub> connectANewHub(@AuthenticationPrincipal UserEntity authUser, @RequestBody Hub hub) {
        if(!hasText(hub.getHubEmail())){
            throw new IllegalArgumentException("HubEmail can't be blank!");
        }
        if(!hasText(hub.getHubPassword())){
            throw new IllegalArgumentException("HubPassword can't be blank!");
        }
        HubEntity hubEntity = map(authUser.getUserName(), hub);
        HubEntity newHubEntity = hubService.createHub(hubEntity);
        Hub newHub = map(newHubEntity);
        return ok(newHub);
    }

    private Hub map(HubEntity newHubEntity) {
        return Hub.builder()
                .hubEmail(newHubEntity.getHubEmail()).build();
    }

    private HubEntity map(String userName, Hub hub) {
        return HubEntity.builder()
                .userName(userName)
                .hubEmail(hub.getHubEmail())
                .hubPassword(hub.getHubPassword()).build();
    }
}
