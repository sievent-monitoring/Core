package sievent.monitoring.store;

import java.util.UUID;

public class StoreResult {
    private UUID id;
    private boolean status;
    private String message;
    private int count;

    public StoreResult(UUID id, boolean status, int count) {
        this.id = id;
        this.status = status;
        this.count = count;
    }

    public StoreResult(UUID id, boolean status, int count, String message) {
        this(id, status, count);
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
