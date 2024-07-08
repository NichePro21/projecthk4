package ManagementSystem.fpt.Requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotEmpty(message = "first name is not empty")
    private String firstname;
    @NotEmpty(message = "last name is not empty")
    private String lastname;

    @NotEmpty(message = "email is not empty")
    @Email(message = "email is a@gmail.com ....")
    private String email;


    @NotEmpty(message = "password is not empty")
    private String password;
}
