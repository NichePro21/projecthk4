package ManagementSystem.fpt.Util;

import ManagementSystem.fpt.Models.Role;
import ManagementSystem.fpt.Models.User;
import ManagementSystem.fpt.Repositories.RoleRepository;
import ManagementSystem.fpt.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Tạo các vai trò mặc định nếu chưa tồn tại trong cơ sở dữ liệu
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_EMPLOYEE");
        createRoleIfNotExists("ROLE_CUSTOMER");

        // Kiểm tra xem có người dùng admin trong cơ sở dữ liệu chưa
        if (!userRepository.existsByEmail("admin@example.com")) {
            // Tạo người dùng admin mặc định
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("Manager");
            admin.setEmail("admin@example.com");
            admin.setIsVerified(true);
            admin.setVerificationCode("");
            admin.setPassword(passwordEncoder.encode("adminpassword"));
            admin.setUsername("admin");

            // Lấy vai trò admin từ cơ sở dữ liệu (đảm bảo đã tồn tại từ createRoleIfNotExists)
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName("ROLE_USER"); // Assuming you have a role named "ROLE_USER"
            roles.add(userRole);
            admin.setRoles(roles);
            // Lưu vào cơ sở dữ liệu
            userRepository.save(admin);
        }
    }

    public void createRoleIfNotExists(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
