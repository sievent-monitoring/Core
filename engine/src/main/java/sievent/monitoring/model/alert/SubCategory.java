package sievent.monitoring.model.alert;

public enum SubCategory {
    Undefined(0),
    Cpu(100),
    Ram(101),
    Disk(102),
    Network(103),
    Component(200),
    Api(201),
    Web(202),
    Chain(203),
    Process(300),
    Function(301);

    private final int value;

    private SubCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    public static SubCategory valueOf(int value) {
        for (SubCategory subCategory : SubCategory.values()) {
            if (subCategory.getValue() == value)
                return subCategory;
        }
        return SubCategory.Undefined;
    }
}
