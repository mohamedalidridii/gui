package entities;

public enum TypeEvent {
    NATIONAL(1),
    INTERNATIONAL(2);

    private final int value;

    TypeEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TypeEvent fromValue(int value) {
        for (TypeEvent type : TypeEvent.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
