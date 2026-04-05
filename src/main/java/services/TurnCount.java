package services;

public class TurnCount {
    public static int turnCount = 1;

    public static int getTurnCount() {
        return turnCount;
    }

    public static void setTurnCount(int turnCount) {
        TurnCount.turnCount = turnCount;
    }
}
