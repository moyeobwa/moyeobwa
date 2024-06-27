package momo.app.schedule.controller;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.schedule.dto.request.ScheduleCreateRequest;
import momo.app.schedule.dto.request.ScheduleUpdateRequest;
import momo.app.schedule.dto.response.ScheduleResponse;
import momo.app.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody ScheduleCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long scheduleId = scheduleService.create(request, authUser);

        return ResponseEntity.created(URI.create("/api/v1/schedules/" + scheduleId))
                .build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        scheduleService.update(id, request, authUser);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<List<ScheduleResponse>> get(
            @PathVariable Long gatheringId
    ) {
        return ResponseEntity.ok(scheduleService.get(gatheringId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        scheduleService.delete(id, authUser);
        return ResponseEntity.ok()
                .build();
    }

}
