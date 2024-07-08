package ManagementSystem.fpt.Services;


import ManagementSystem.fpt.Models.Address;
import ManagementSystem.fpt.Models.Role;
import ManagementSystem.fpt.Models.User;
import ManagementSystem.fpt.Repositories.AddressRepository;
import ManagementSystem.fpt.Repositories.RoleRepository;
import ManagementSystem.fpt.Repositories.UserRepository;
import ManagementSystem.fpt.Requests.auth.LoginRequest;
import ManagementSystem.fpt.Requests.auth.SignUpRequest;
import ManagementSystem.fpt.Responses.auth.AuthResponse;
import ManagementSystem.fpt.Util.JwtUtil;
import ManagementSystem.fpt.Util.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerificationUtil verificationUtil;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private AddressRepository addressRepository;

//    @Autowired
//    MailUserVerificationCode mailUserVerificationCode;


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Create user
     *
     * @param signupRequest
     * @return
     */

    public User createUser(SignUpRequest signupRequest) throws Exception {
        // Check if email is already registered
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký trước đó. Vui lòng sử dụng email khác.");
        }

        // Create user entity
        User newUser = new User();
        newUser.setFirstname(signupRequest.getFirstname());
        newUser.setLastname(signupRequest.getLastname());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setUsername(generateUsername(signupRequest.getEmail())); // Generate username from email
        newUser.setPassword(BCrypt.hashpw(signupRequest.getPassword(), BCrypt.gensalt()));
        newUser.setIsVerified(false); // Optionally set user as inactive until verified
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_CUSTOMER"); // Assuming you have a role named "ROLE_USER"
        roles.add(userRole);
        newUser.setRoles(roles);
        // Generate and send verification code
        String verificationCode = verificationUtil.generateVerificationCode();
        newUser.setVerificationCode(verificationCode);

        // Save user to database
        User savedUser = userRepository.save(newUser);
        sendVerificationCode(savedUser.getEmail(), verificationCode);
        // Create address entity with default null values
        Address newAddress = new Address();
        newAddress.setUser(savedUser);
        addressRepository.save(newAddress);


        return savedUser;
    }

    private String generateUsername(String email) {
        // Example: if email is email@gmail.com, generate something like email27
        Random random = new Random();
        int suffix = random.nextInt(50) + 1; // Random number between 1 and 50
        return email.split("@")[0] + suffix;
    }

    //send first
    private void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Xác nhận đăng ký");
        message.setText("Mã xác nhận của bạn là: " + code);
        emailSender.send(message);
    }

    //forgot-send code
    private void sendVerificationCodeForgot(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Hi" + email);
        message.setText("Your Verification Code Forgot Password is: " + code);
        emailSender.send(message);
    }

    //    veri email
    @Transactional
    public boolean verifyUser(String email, String verificationCode) {
        int updatedRows = userRepository.verifyUserByEmail(email, verificationCode);
        return updatedRows > 0;
    }

    @Transactional
    public boolean forgotPassword(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String newVerificationCode = verificationUtil.generateVerificationCode();
            userRepository.updateVerificationCodeByEmail(newVerificationCode, email);
            sendResetPasswordLink(email, newVerificationCode); // gui link forgot
            return true;
        } else {
            return false;
        }
    }

    public void sendResetPasswordLink(String toEmail, String verificationCode) {
        String resetLink = "http://localhost:4200/reset-password?email=" + toEmail + "&code=" + verificationCode;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Reset Password");
        mailMessage.setText("Click the following link to reset your password: " + resetLink);
        emailSender.send(mailMessage);
    }

    @Transactional
    public boolean resetPassword(String email, String verificationCode, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode().equals(verificationCode)) {
                user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt())); // Hash mật khẩu trước khi lưu
                user.setVerificationCode(null); // Xóa mã xác minh sau khi đặt lại mật khẩu
                user.setIsVerified(true); // Xác minh người dùng
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }


    /**
     * Login
     *
     * @param loginReq
     * @return
     * @throws Exception
     */
    public AuthResponse login(LoginRequest loginReq) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginReq.getEmailOrUsername(),
                            loginReq.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String email = authentication.getName();
            Optional<User> userOptional = userRepository.findByEmail(email);
            User user = userOptional.orElseThrow(() -> new Exception("User not found with email: " + email));

            List<Role> roles = new ArrayList<>(user.getRoles());

            return AuthResponse.builder()
                    .token(jwtUtil.generateToken(user))
                    .email(user.getEmail())
                    .id(user.getId())
                    .username(user.getUsername())
                    .address(user.getAddresses()) // Giả sử Address là một List<Address>
                    .roles(roles)
                    .build();
        } catch (Exception e) {
            throw new Exception("Email or password incorrect", e);
        }
    }


    /**
     * Find user by email
     *
     * @param email
     * @return
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


//    public User verifyUser(String email) {
//        User user = userRepository.findByEmail(email);
//        if (user != null && !user.isVerified()) {
//            user.setIsVerified(true);
//            user.prePersist();
//
//            return user;
//        }
//        return null;
//    }
//    public ResponseEntity<User> getSingleUser(Integer userId) {
//        Optional<User> userData = userRepository.findById(userId);
//        if (userData.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<User>(userData.get(), HttpStatus.OK);
//    }

//
//    //update user [service]
//    public ResponseEntity<User> updateUser(Integer userId, UserRequest request) {
//        Optional<User> userData = userRepository.findById(userId);
//        if (userData.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        if (request.password() != null)
//            userData.get().setPassword(request.password());
//        if (request.firstName() != null)
//            userData.get().setFirstName(request.firstName());
//        if (request.lastName() != null)
//            userData.get().setLastName(request.lastName());
//        if (request.avatar() != null)
//            userData.get().setAvatar(request.avatar());
//
//
//        return new ResponseEntity<User>(
//                userRepository.save(userData.get()),
//                HttpStatus.OK);
//    }

//delete user [service]
//    public ResponseEntity deleteUser(Integer userId) {
//        Optional<User> userData = userRepository.findById(userId);
//        if (userData.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        userRepository.deleteById(userId);
//
//        return new ResponseEntity(HttpStatus.OK);
//    }

    //get all user [service - admin]
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }


}

