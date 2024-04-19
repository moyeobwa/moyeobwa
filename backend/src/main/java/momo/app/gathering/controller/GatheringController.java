package momo.app.gathering.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.gathering.dto.GatheringCreateJsonRequest;
import momo.app.gathering.dto.GatheringCreateRequest;
import momo.app.gathering.dto.GatheringUpdateJsonRequest;
import momo.app.gathering.dto.GatheringUpdateRequest;
import momo.app.gathering.service.GatheringCommandService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gatherings")
public class GatheringController {

    private final GatheringCommandService gatheringCommandService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> create(
            @RequestPart MultipartFile image,
            @RequestPart GatheringCreateJsonRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        GatheringCreateRequest gatheringCreateRequest = GatheringCreateRequest.of(request, image);
        Long id = gatheringCommandService.createGathering(gatheringCreateRequest, authUser);

        return ResponseEntity.created(URI.create("/api/v1/gatherings/" + id))
                .build();
    }

    @PutMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> update(
            @RequestPart MultipartFile image,
            @RequestPart GatheringUpdateJsonRequest request,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id
    ) {

        GatheringUpdateRequest gatheringUpdateRequest = GatheringUpdateRequest.of(request, image);
        gatheringCommandService.update(id, gatheringUpdateRequest, authUser);

        return ResponseEntity.ok()
                .build();
    }

}
