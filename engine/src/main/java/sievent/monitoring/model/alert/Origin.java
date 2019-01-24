package sievent.monitoring.model.alert;

public enum Origin {
    Unknown(-1),
    Alarm(0),
    Sla(1),
    Logs(2),
    Agent(3),
    Probe(4),
    External(5);

    private final int value;

    private Origin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Origin valueOf(int value) {
        for (Origin origin : Origin.values()) {
            if (origin.getValue() == value)
                return origin;
        }
        return Origin.Unknown;
    }
}
