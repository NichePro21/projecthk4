package ManagementSystem.fpt.Requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginRequest {
    @NotEmpty(message = "Email is not empty")
    @Email(message = "Invalid email format")
    private String emailOrUsername;

    @NotEmpty(message = "Password is not empty")
    private String password;

  
}
