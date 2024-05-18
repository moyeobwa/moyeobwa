package momo.app.friend.domain;

import momo.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromUser(User fromUser);

    Optional<Friend> findByFromUserAndToUser(User fromUser, User toUser);

    @Query("SELECT f FROM Friend f JOIN FETCH f.fromUser WHERE f.fromUser = :fromUser")
    List<Friend> findAllByFromUser(User fromUser);

    @Query("SELECT f From Friend f JOIN FETCH f.fromUser WHERE f.toUser = :toUser AND f.friendState = :friendState")
    List<Friend> findByToUserAndState(User toUser, FriendState friendState);


}
