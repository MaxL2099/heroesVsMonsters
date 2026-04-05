package services;

import enums.Dice;

public class GenerateCharacterStats {
    public int generateCharacterForceEnd() {
        return Dice.D6.roll(4, 3);
    }
    public int generateCharacterLife(int end) {
        if (end < 5) return end - 1;
        else if (end < 10) return end;
        else if (end < 15) return end + 1;
        else return end + 1;
    }
}
