package custra.server.spring.core.Users;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserStorage {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(0);

    public UserStorage() {
        SampleUsers.getAllSampleUsers().forEach(this::add);
    }

    public User add(User user) {
        user.setId(idSeq.incrementAndGet());
        users.add(user);
        return user;
    }

    public Optional<User> findByPhone(String phone) {
        return users.stream().filter(u -> u.getPhone().equals(phone)).findFirst();
    }

    public Optional<User> findById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public User findUserById(Long id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                return users.get(i);
            }
        }
        return null;
    }
}
