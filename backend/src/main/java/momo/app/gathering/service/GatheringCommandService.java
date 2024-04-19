package momo.app.gathering.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringMember;
import momo.app.gathering.domain.GatheringMemberRepository;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.domain.GatheringTag;
import momo.app.gathering.domain.GatheringTagRepository;
import momo.app.tag.domain.Tag;
import momo.app.tag.domain.TagRepository;
import momo.app.gathering.dto.GatheringCreateRequest;
import momo.app.image.S3Service;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatheringCommandService {

    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final TagRepository tagRepository;
    private final GatheringTagRepository gatheringTagRepository;

    @Transactional
    public Long createGathering(GatheringCreateRequest request, AuthUser authUser) {
        User user = findUser(authUser.getId());
        String uploadedImageUrl = s3Service.upload(request.image());
        Gathering gathering = Gathering.builder()
                .category(request.category())
                .description(request.description())
                .imageUrl(uploadedImageUrl)
                .name(request.name())
                .build();

        gatheringRepository.save(gathering);

        for (String tagName : request.tagNames()) {
            Tag tag = tagRepository.findByName(tagName).orElse(null);
            if (tag == null) {
                tag = Tag.builder()
                        .name(tagName)
                        .build();

                tagRepository.save(tag);
            }

            GatheringTag gatheringTag = GatheringTag.builder()
                    .tag(tag)
                    .gathering(gathering)
                    .build();

            gatheringTagRepository.save(gatheringTag);
            gathering.addGatheringTag(gatheringTag);
            tag.addGatheringTag(gatheringTag);
        }

        gatheringMemberRepository.save(GatheringMember.builder()
                .gathering(gathering)
                .user(user)
                .build());

        return gathering.getId();
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }
}
