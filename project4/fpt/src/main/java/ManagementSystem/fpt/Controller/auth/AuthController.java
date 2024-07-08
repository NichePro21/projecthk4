package ManagementSystem.fpt.Controller.auth;

import ManagementSystem.fpt.Controller.ApiController;
import ManagementSystem.fpt.Models.User;
import ManagementSystem.fpt.Requests.auth.LoginRequest;
import ManagementSystem.fpt.Requests.auth.SignUpRequest;
import ManagementSystem.fpt.Responses.ApiResponse;
import ManagementSystem.fpt.Responses.auth.AuthResponse;
import ManagementSystem.fpt.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"api/"})
public class AuthController extends ApiController {
    @Autowired
    ApiResponse apiResponse;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginReq, BindingResult rs) {
        try {
            if (rs.hasErrors()) {
                return apiResponse.failure("Invalid Email", parseFieldErrors(rs), HttpStatus.UNPROCESSABLE_ENTITY.value());
            } else {
                AuthResponse resq = userService.login(loginReq);
                if (resq != null) {
                    return apiResponse.ok("login success", resq);
                } else {
                    return apiResponse.failure("Invalid email or password", null, HttpStatus.UNAUTHORIZED.value());
                }
            }
        } catch (Exception e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signupRequest, BindingResult rs) {
        try {
            if (rs.hasErrors()) {
                return this.apiResponse.failure("invalid", this.parseFieldErrors(rs), HttpStatus.UNPROCESSABLE_ENTITY.value());
            }

            // Kiểm tra xem email đã được đăng ký hay chưa
            if (userService.existsByEmail(signupRequest.getEmail())) {
                return this.apiResponse.failure("Email đã được đăng ký", HttpStatus.BAD_REQUEST.value());
            }

            User user = this.userService.createUser(signupRequest);
            return this.apiResponse.ok("created", user);
        } catch (Exception var4) {
            var4.printStackTrace();
            return this.apiResponse.failure("Error");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = userService.verifyUser(email, code);
        if (isVerified) {
            return apiResponse.ok("User verified successfully");
        } else {
            return apiResponse.failure("Invalid verification code or email", null, HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        boolean emailExists = userService.forgotPassword(email);
        if (emailExists) {
            return apiResponse.ok("Check your mail and get Verification Code");
        } else {
            return apiResponse.failure("Email not found", null, HttpStatus.NOT_FOUND.value());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
        boolean isReset = userService.resetPassword(email, code, newPassword);
        if (isReset) {
            return apiResponse.ok("Password reset successfully");
        } else {
            return apiResponse.failure("Invalid email or verification code", null, HttpStatus.BAD_REQUEST.value());
        }
    }

}
