package ManagementSystem.fpt.Responses.auth;

import ManagementSystem.fpt.Models.Address;
import ManagementSystem.fpt.Models.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class AuthResponse {
    private String token;
    private String email;
    private Long id;
    private String username;
    private Set<Address> address;
    private List<Role> roles; // Thêm danh sách vai trò của người dùng

    // Constructors, getters, and setters
    // Constructors
}
