package ManagementSystem.fpt.Util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VerificationUtil {

    // Generate a random verification code
    public String generateVerificationCode() {
        // Generate a 6-digit random number
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(code);
    }
}