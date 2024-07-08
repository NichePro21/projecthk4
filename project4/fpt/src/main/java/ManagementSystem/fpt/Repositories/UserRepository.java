package ManagementSystem.fpt.Repositories;

import ManagementSystem.fpt.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isVerified = TRUE, u.verificationCode = NULL WHERE u.email = :email AND u.verificationCode = :verificationCode")
    int verifyUserByEmail(@Param("email") String email, @Param("verificationCode") String verificationCode);


    @Modifying
    @Query("UPDATE User u SET u.verificationCode = :verificationCode WHERE u.email = :email")
    int updateVerificationCodeByEmail(@Param("verificationCode") String verificationCode, @Param("email") String email);
}




