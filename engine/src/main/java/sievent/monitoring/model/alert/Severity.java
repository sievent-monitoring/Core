package sievent.monitoring.model.alert;

public enum Severity {
    Undefined(-1),
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

    public static Severity valueOf(int value) {
        for (Severity severity : Severity.values()) {
            if (severity.getValue() == value)
                return severity;
        }
        return Severity.Undefined;
    }

}
