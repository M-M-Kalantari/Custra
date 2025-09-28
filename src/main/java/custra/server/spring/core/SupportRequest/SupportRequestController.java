package custra.server.spring.core.SupportRequest;

import custra.server.spring.core.Users.User;
import custra.server.spring.core.Users.UserStorage;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/custra/v1/requests")
public class SupportRequestController {

    private final SupportRequestStorage storage;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");

    public SupportRequestController(SupportRequestStorage storage) {
        this.storage = storage;
    }

    /*/ ----- Get Requests for Customer or Support ----- /*/
    @GetMapping
    public ResponseEntity<?> getRequests(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        UserStorage userStorage = new UserStorage();
        User user = userStorage.findUserById(userId);
        String name = user.getFullName();
        String username = user.getPhone();

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body("ابتدا وارد شوید.");
        }

        List<SupportRequest> requestsList;

        if (role.equals("CUSTOMER")) {
            requestsList = storage.findByCustomer(userId);
        } else if (role.equals("SUPPORT")) {
            requestsList = storage.findBySupportId(userId);
        } else {
            return ResponseEntity.status(403).body("دسترسی نامعتبر.");
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (SupportRequest r : requestsList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getCustomerRequestNumber());
            map.put("requestTime", r.getCreatedAt().format(formatter));
            map.put("status", r.getStatus().name());
            map.put("subject", r.getSubject());
            map.put("completionTime", r.getDoneAt() != null ? r.getDoneAt().format(formatter) : "-");
            map.put("supportNote", r.getManagerNotes() != null ? r.getManagerNotes() : "-");
            map.put("returnReason", r.getReturnReason() != null ? r.getReturnReason() : "-");
            map.put("customerName", name);
            map.put("username", username);
            response.add(map);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/support")
    public ResponseEntity<?> getRequestsForCurrentSupport(HttpSession session) {
        Long supportId = (Long) session.getAttribute("userId");
        if (supportId == null) {
            return ResponseEntity.status(401).body("ابتدا وارد شوید.");
        }

        UserStorage userStorage = new UserStorage();
        String name;
        String username;

        List<SupportRequest> requestsList = storage.findBySupportId(supportId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (SupportRequest r : requestsList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getCustomerRequestNumber());
            map.put("requestTime", r.getCreatedAt().format(formatter));

            User supportUser = userStorage.findUserById(r.getSupportId());
            name = supportUser.getFullName();
            username = supportUser.getPhone();
            map.put("supportName" , name);

            map.put("status", r.getStatus().name());
            map.put("subject", r.getSubject());
            map.put("completionTime", r.getDoneAt() != null ? r.getDoneAt().format(formatter) : "-");
            map.put("supportNote", r.getManagerNotes() != null ? r.getManagerNotes() : "-");
            map.put("returnReason", r.getReturnReason() != null ? r.getReturnReason() : "-");

            User customerUser = userStorage.findUserById(r.getCustomerId());
            name = customerUser.getFullName();
            username = customerUser.getPhone();
            map.put("customerName", name);

            response.add(map);
        }

        return ResponseEntity.ok(response);
    }

    /*/ ----- Add New Request for Logged-in Customer ----- /*/
    @PostMapping("new-request")
    public ResponseEntity<?> addRequest(
            @RequestParam String subject,
            @RequestParam(required = false) String description,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"CUSTOMER".equals(role)) {
            return ResponseEntity.status(401).body("ابتدا وارد شوید.");
        }

        SupportRequest req = new SupportRequest();
        req.setCustomerId(userId);
        req.setSubject(subject);
        req.setDescription(description);

        SupportRequest saved = storage.add(req);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getCustomerRequestNumber());
        response.put("requestTime", saved.getCreatedAt().format(formatter));
        response.put("status", saved.getStatus().name());
        response.put("subject", saved.getSubject());
        response.put("completionTime", saved.getDoneAt() != null ? saved.getDoneAt().format(formatter) : "-");
        response.put("supportNote", saved.getManagerNotes() != null ? saved.getManagerNotes() : "-");
        response.put("returnReason", saved.getReturnReason() != null ? saved.getReturnReason() : "-");

        return ResponseEntity.ok(response);
    }

    /*/ ----- Update Request by Support ----- /*/
    @PostMapping("update")
    public ResponseEntity<?> updateRequest(
            @RequestParam Long id,
            @RequestParam String status,
            @RequestParam String review,
            HttpSession session
    ) {
        String role = (String) session.getAttribute("role");

        if (role == null || !"SUPPORT".equals(role)) {
            return ResponseEntity.status(403).body("فقط کارشناسان می‌توانند تغییر دهند.");
        }

        Optional<SupportRequest> optional = storage.findById(id);
        if (optional.isPresent()) {
            SupportRequest r = optional.get();
            r.setStatus(RequestStatus.valueOf(status));
            r.setManagerNotes(review);
            return ResponseEntity.ok(r);
        }

        return ResponseEntity.status(404).body("درخواست یافت نشد.");
    }

}