package sievent.monitoring.model.alert;

public enum Severity {
    Informational(0),
    Low(1000),
    Medium(2000),
    High(3000),
    Critical(4000);

    private final int value;

    private Severity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
