package custra.server.spring.core.SupportRequest;

import java.time.LocalDateTime;

public class SupportRequest {
    private Long id;
    private Long customerId;
    private Long supportId;
    private int customerRequestNumber;
    private String subject;
    private String description;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime doneAt;
    private String managerNotes;
    private String returnReason;


    /*/ ----- Constructor ----- /*/
    public SupportRequest(Long id, Long customerId, Long supportId, int customerRequestNumber, String subject,
                          String description, RequestStatus status, LocalDateTime createdAt,
                          LocalDateTime updatedAt, LocalDateTime doneAt,
                          String managerNotes, String returnReason) {
        this.id = id;
        this.customerId = customerId;
        this.supportId = supportId;
        this.customerRequestNumber = customerRequestNumber;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.doneAt = doneAt;
        this.managerNotes = managerNotes;
        this.returnReason = returnReason;

    }

    public SupportRequest() {
    }


    /*/ ----- Getter & Setter ----- /*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSupportId() {
        return supportId;
    }

    public void setSupportId(Long supportId) {
        this.supportId = supportId;
    }

    public int getCustomerRequestNumber() {
        return customerRequestNumber;
    }

    public void setCustomerRequestNumber(int customerRequestNumber) {
        this.customerRequestNumber = customerRequestNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(LocalDateTime doneAt) {
        this.doneAt = doneAt;
    }

    public String getManagerNotes() {
        return managerNotes;
    }

    public void setManagerNotes(String managerNotes) {
        this.managerNotes = managerNotes;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
}
