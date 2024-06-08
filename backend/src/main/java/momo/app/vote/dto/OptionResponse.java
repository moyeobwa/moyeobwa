package momo.app.vote.dto;

import momo.app.vote.domain.Option;

public record OptionResponse(
        Long optionId,
        String content,
        int voteCount
) {

    public static OptionResponse from(Option option) {
        return new OptionResponse(
                option.getId(),
                option.getContent(),
                option.getCount());
    }
}
