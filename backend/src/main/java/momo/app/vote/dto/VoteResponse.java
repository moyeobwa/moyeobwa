package momo.app.vote.dto;

import momo.app.vote.domain.Vote;

public record VoteResponse(
        String title,
        Long id
        // TODO: 참여자수, 최고 득표 옵션 등 화면 정해지면 추가하기.
) {

    public static VoteResponse from(Vote vote) {
        return new VoteResponse(vote.getTitle(), vote.getId());
    }
}
