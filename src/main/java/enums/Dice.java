package enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public enum Dice {

    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D12(12),
    D20(20),
    D100(100);

    private int value;

    Dice(int value) {
        this.value = value;
    }

    public int roll(){
        Random rand = new Random();
        return rand.nextInt(this.value) + 1;
    }

    public int roll(int nbDices, int takenDices){
        Integer[] dices = new Integer[nbDices];
        Integer result = 0;
        for(int i=0; i < nbDices; i++) {
            dices[i] = roll();
        }
        Arrays.sort(dices, Collections.reverseOrder());
        for(int i=0; i < 3; i++) {
            result += dices[i];
        }
        return result;
    }
}