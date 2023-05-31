package e1.i3.e1i3.repository.user;

import e1.i3.e1i3.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>,UserRepositoryCustom {
    Optional<User> findByAddress(String address);

}
