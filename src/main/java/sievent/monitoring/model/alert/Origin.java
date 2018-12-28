package sievent.monitoring.model.alert;

public enum Origin {
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
}
