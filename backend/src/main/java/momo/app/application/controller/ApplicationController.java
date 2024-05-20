package momo.app.application.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import momo.app.application.dto.ApplicationCreateRequest;
import momo.app.application.service.ApplicationCommandService;
import momo.app.auth.dto.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationCommandService applicationCommandService;

    @PostMapping
    public ResponseEntity<Void> create(AuthUser authUser, @RequestBody ApplicationCreateRequest request) {
        Long applicationId = applicationCommandService.createApplication(authUser, request);
        return ResponseEntity.created(URI.create("/api/v1/applications/" + applicationId))
                .build();
    }
}
