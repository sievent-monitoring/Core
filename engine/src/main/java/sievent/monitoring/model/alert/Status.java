package sievent.monitoring.model.alert;

public enum Status {
    None(0),
    Open(1),
    Closed(2),
    Pending(3),
    Cancelled(4);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Status valueOf(int value) {
        for (Status status : Status.values()) {
            if (status.getValue() == value)
                return status;
        }
        return Status.Closed;
    }
}
