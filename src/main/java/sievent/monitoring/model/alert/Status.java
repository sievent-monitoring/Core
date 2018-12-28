package sievent.monitoring.model.alert;

public enum Status {
    Open(1),
    Closed(0),
    Pending(2),
    Cancelled(-1);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
