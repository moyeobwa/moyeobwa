package momo.app.gathering.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.chat.domain.chatroom.ChatRoom;
import momo.app.chat.domain.chatroom.ChatRoomRepository;
import momo.app.chat.domain.chatroom.ChatRoomUser;
import momo.app.chat.domain.chatroom.ChatRoomUserRepository;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringInfo;
import momo.app.gathering.domain.GatheringMember;
import momo.app.gathering.domain.GatheringMemberRepository;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.domain.GatheringTag;
import momo.app.gathering.domain.GatheringTagRepository;
import momo.app.gathering.dto.GatheringUpdateRequest;
import momo.app.tag.domain.Tag;
import momo.app.tag.domain.TagRepository;
import momo.app.gathering.dto.GatheringCreateRequest;
import momo.app.image.S3Service;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringCommandService {

    @Value("${gathering.default.image}")
    private String defaultImage;

    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final TagRepository tagRepository;
    private final GatheringTagRepository gatheringTagRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public Long create(GatheringCreateRequest request, AuthUser authUser) {
        User user = findUser(authUser.getId());
        String uploadedImageUrl = uploadImage(request.image());
        GatheringInfo gatheringInfo = GatheringInfo.of(
                request.category(),
                request.name(),
                request.description(),
                uploadedImageUrl
        );

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .managerId(authUser.getId())
                .build());

        chatRoomUserRepository.save(ChatRoomUser.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build());

        Gathering gathering = Gathering.builder()
                .gatheringInfo(gatheringInfo)
                .managerId(user.getId())
                .chatRoomId(chatRoom.getId())
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

    private String uploadImage(MultipartFile image) {
        if (image != null) {
            return s3Service.upload(image);
        }
        return defaultImage;
    }

    public void update(Long id, GatheringUpdateRequest request, AuthUser authUser) {
        Gathering gathering = findGathering(id);
        gathering.validateManager(authUser);
        s3Service.deleteFile(gathering.getGatheringInfo().getImageUrl());
        String uploadedImage = s3Service.upload(request.image());
        gatheringTagRepository.deleteAllByGathering(gathering);

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
            tag.addGatheringTag(gatheringTag);
        }

        gathering = findGathering(id);
        GatheringInfo gatheringInfo = GatheringInfo.of(
                request.category(),
                request.name(),
                request.description(),
                uploadedImage
        );
        gathering.updateGatheringInfo(gatheringInfo);
    }

    public void delete(Long id, AuthUser authUser) {
        Gathering gathering = findGathering(id);
        gathering.validateManager(authUser);

        gatheringTagRepository.deleteAllByGathering(gathering);
        gatheringMemberRepository.deleteAllByGathering(gathering);
        gatheringRepository.deleteById(id);
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("gathering not found"));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }
}
