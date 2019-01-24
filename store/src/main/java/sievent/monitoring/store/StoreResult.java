package sievent.monitoring.store;

import java.util.UUID;

public class StoreResult {
    private UUID id;
    private boolean status;
    private String message;

    public StoreResult(UUID id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public StoreResult(UUID id, boolean status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
