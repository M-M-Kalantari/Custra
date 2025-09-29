package custra.server.spring.core.MainController;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Custra")
public class PageController {

    // http://localhost:8080/Custra/login
    @GetMapping("/login")
    public String redirectToLoginPage() {
        return "redirect:/pages/login/login.html";
    }

    // http://localhost:8080/Custra/register
    @GetMapping("/register")
    public String redirectToRegisterPage() {
        return "redirect:/pages/register/register.html";
    }

    // http://localhost:8080/Custra/dashboard
    @GetMapping("/dashboard")
    public String redirectToDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return "redirect:/pages/login/login.html";
        }

        switch (role) {
            case "CUSTOMER":
                return "redirect:/pages/customer_dashboard/customer_dashboard.html";
            case "SUPPORT":
                return "redirect:/pages/support_panel/support_panel.html";
            default:
                return "redirect:/pages/login/login.html";
        }
    }
}