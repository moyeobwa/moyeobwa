package momo.app.friend.domain;

import momo.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findById(Long id);

    Optional<Friend> findByUser(User user);

    Optional<Friend> findByUserAndFriend(User fromUser, User toUser);

    Optional<Friend> findByUserAndFriendAndState(User fromUser, User toUser, State state);

    @Query("SELECT f FROM Friend f JOIN FETCH f.user WHERE f.user = :user")
    List<Friend> findAllByUser(User user);

    @Query("SELECT f From Friend f JOIN FETCH f.user WHERE f.friend = :user AND f.state = :state")
    List<Friend> findByFriendAndState(User user, State state);


}
