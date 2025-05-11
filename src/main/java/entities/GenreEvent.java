package entities;

public enum GenreEvent {
    SEMINAR(1),
    CRUISE(2),
    TRAVEL(3),
    AFTER_WORK(4);

    private final int value;

    GenreEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GenreEvent fromValue(int value) {
        for (GenreEvent genre : GenreEvent.values()) {
            if (genre.value == value) {
                return genre;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
