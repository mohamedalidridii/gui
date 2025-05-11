package entities;

public enum StatusEvent {
    AVAILABLE(1),
    NOT_AVAILABLE(2);

    private final int value;

    StatusEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusEvent fromValue(int value) {
        for (StatusEvent status : StatusEvent.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
