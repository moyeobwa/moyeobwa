package momo.app.vote.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.vote.dto.OptionResponse;
import momo.app.vote.service.OptionQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/options")
public class OptionController {

    private final OptionQueryService optionQueryService;

    @GetMapping("/{voteId}")
    public ResponseEntity<List<OptionResponse>> findAll(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long voteId
    ) {
        return ResponseEntity.ok(optionQueryService.findAll(authUser, voteId));
    }
}
