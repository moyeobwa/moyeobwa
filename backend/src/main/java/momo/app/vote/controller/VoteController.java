package momo.app.vote.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.vote.dto.VoteCreateRequest;
import momo.app.vote.dto.VoteRequest;
import momo.app.vote.dto.VoteResponse;
import momo.app.vote.service.VoteCommandService;
import momo.app.vote.service.VoteQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/votes")
public class VoteController {

    private final VoteCommandService voteCommandService;
    private final VoteQueryService voteQueryService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody VoteCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long id = voteCommandService.create(request, authUser);

        return ResponseEntity.created(URI.create("/api/v1/votes/" + id))
                .build();
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> vote(
            @RequestBody VoteRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        voteCommandService.vote(id, request, authUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<List<VoteResponse>> findAll(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long gatheringId
    ) {
        return ResponseEntity.ok(voteQueryService.findAll(authUser, gatheringId));
    }
}
