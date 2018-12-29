package sievent.monitoring.model.alert;

public enum Category {

    Infrastructure(100),
    Applicative(200),
    Business(300);

    private final int value;

    private Category(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
