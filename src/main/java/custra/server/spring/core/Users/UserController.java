package custra.server.spring.core.Users;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/custra/v1/")
public class UserController {

    private final UserStorage users;

    public UserController(UserStorage users) {
        this.users = users;
    }

    /*/ ----- Register ----- /*/
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            HttpSession session
    ) {
        if (users.findByPhone(phone).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("این شماره تلفن قبلاً ثبت شده است.");
        }

        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setPassword(password);
        u.setRole(UserRole.CUSTOMER);
        users.add(u);

        session.setAttribute("userId", u.getId());
        session.setAttribute("role", u.getRole().name());

        return ResponseEntity.ok("ثبت نام با موفقیت انجام شد.");
    }


    /*/ ----- Login ----- /*/
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String phone,
            @RequestParam String password,
            HttpSession session
    ) {
        var userOpt = users.findByPhone(phone);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("کاربر یافت نشد.");
        }

        var user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("رمز عبور نادرست است.");
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());

        return ResponseEntity.ok("ورود موفقیت آمیز بود.");
    }

    /*/ ----- Logout ----- /*/
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("خروج انجام شد.");
    }

    /*/ ----- Get User Information ----- /*/
    @GetMapping("/get-user-info")
    public ResponseEntity<?> me(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("ابتدا وارد شوید.");
        }

        var opt = users.findById(userId);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body("کاربر یافت نشد.");
        }
        return ResponseEntity.ok(opt.get());
    }
}