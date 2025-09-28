package custra.server.spring.core.SupportRequest;

import custra.server.spring.core.Users.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class SupportRequestStorage {
    private final List<SupportRequest> requests = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Map<Long, AtomicInteger> perCustomerCounters = new ConcurrentHashMap<>();

    public SupportRequestStorage() {
        try {
            SampleRequests.getAll().forEach(this::add);
        } catch (Throwable ignored) {
        }
    }

    public synchronized SupportRequest add(SupportRequest r) {
        long id = idSeq.incrementAndGet();
        r.setId(id);

        LocalDateTime now = LocalDateTime.now();
        if (r.getCreatedAt() == null) r.setCreatedAt(now);
        if (r.getUpdatedAt() == null) r.setUpdatedAt(r.getCreatedAt());

        long customerId = r.getCustomerId();
        int num = perCustomerCounters
                .computeIfAbsent(customerId, k -> new AtomicInteger(0))
                .incrementAndGet();
        r.setCustomerRequestNumber(num);

        if (r.getStatus() == null) r.setStatus(RequestStatus.SENT);

        requests.add(r);
        return r;
    }

    public synchronized SupportRequest update(SupportRequest updated) {
        Optional<SupportRequest> opt = findById(updated.getId());
        if (opt.isEmpty()) return null;
        SupportRequest existing = opt.get();

        existing.setSubject(updated.getSubject());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setManagerNotes(updated.getManagerNotes());
        existing.setReturnReason(updated.getReturnReason());
        existing.setSupportId(updated.getSupportId());

        existing.setUpdatedAt(LocalDateTime.now());
        if (existing.getStatus() == RequestStatus.DONE) existing.setDoneAt(LocalDateTime.now());

        return existing;
    }

    public synchronized SupportRequest addPreserveCustomerNumber(SupportRequest r) {
        long id = idSeq.incrementAndGet();
        r.setId(id);

        if (r.getCreatedAt() == null) r.setCreatedAt(LocalDateTime.now());
        if (r.getUpdatedAt() == null) r.setUpdatedAt(r.getCreatedAt());

        long customerId = r.getCustomerId();
        int given = r.getCustomerRequestNumber();
        perCustomerCounters.compute(customerId, (k, v) -> {
            if (v == null) return new AtomicInteger(Math.max(0, given));
            v.updateAndGet(prev -> Math.max(prev, given));
            return v;
        });

        if (r.getStatus() == null) r.setStatus(RequestStatus.SENT);

        requests.add(r);
        return r;
    }

    public List<SupportRequest> findAll() {
        return new ArrayList<>(requests);
    }

    public long count() {
        return requests.size();
    }

    /*/ ----- Find by ----- /*/
    public List<SupportRequest> findByCustomer(Long customerId) {
        return requests.stream()
                .filter(r -> Objects.equals(r.getCustomerId(), customerId))
                .sorted(Comparator.comparing(SupportRequest::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<SupportRequest> findByRequestStatusSorted(RequestStatus status, boolean ascByCreatedAt) {
        Comparator<SupportRequest> cmp = Comparator.comparing(SupportRequest::getCreatedAt);
        if (!ascByCreatedAt) cmp = cmp.reversed();
        return requests.stream()
                .filter(r -> r.getStatus() == status)
                .sorted(cmp)
                .collect(Collectors.toList());
    }

    public Optional<SupportRequest> findById(Long id) {
        return requests.stream().filter(r -> Objects.equals(r.getId(), id)).findFirst();
    }

    public List<SupportRequest> findBySupportId(Long supportId) {
        List<SupportRequest> result = new ArrayList<>();
        for (SupportRequest r : requests) {
            if (r.getSupportId() != null && r.getSupportId().equals(supportId)) {
                result.add(r);
            }
        }
        return result;
    }

    public SupportRequest findRequestById(Long id) {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getId().equals(id)) {
                return requests.get(i);
            }
        }
        return null;
    }
}
