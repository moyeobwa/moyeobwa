package momo.app.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialId(String socialId);
    Optional<User> findAllByNickname(String nickname);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByEmail(String email);
    List<User> findByNicknameContaining(String nickname);
}
