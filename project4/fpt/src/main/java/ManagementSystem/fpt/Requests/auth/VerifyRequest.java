package ManagementSystem.fpt.Requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyRequest {
    @NotEmpty(message = "email is not empty")
    @Email(message = "email is a@gmail.com ....")
    private String email;

    @NotEmpty(message = "code is not empty")
    private String code;
}
