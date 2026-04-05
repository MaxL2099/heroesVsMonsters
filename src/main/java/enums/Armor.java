package enums;

public enum Armor {
    NONE(0),
    LEATHER(1),
    CHAIN(2),
    IRON(3),
    STEEL(4),
    GOLD(5),
    DIAMOND(6);

    private final int value;

    Armor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}