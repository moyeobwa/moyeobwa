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

    @Query("SELECT f FROM Friend f WHERE (f.fromUser = :user1 AND f.toUser = :user2) OR (f.fromUser = :user2 AND f.toUser = :user1)")
    List<Friend> findByTwoUser(User user1, User user2);

    List<Friend> findAllByFromUserAndState(User fromUser, FriendState friendState);

    List<Friend> findAllByToUserAndState(User toUser, FriendState friendState);

    @Query("DELETE FROM Friend f WHERE f.id IN :ids")
    void deleteAllByIds(List<Long> ids);
}
