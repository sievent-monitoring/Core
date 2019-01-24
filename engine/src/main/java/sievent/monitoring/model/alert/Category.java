package sievent.monitoring.model.alert;

public enum Category {
    Undefined(0),
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

    public static Category valueOf(int value) {
        for (Category category : Category.values()) {
            if (category.getValue() == value)
                return category;
        }
        return Category.Undefined;
    }

}
