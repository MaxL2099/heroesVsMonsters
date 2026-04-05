package models.characters.heroes;

import services.GenerateCharacterStats;

public class Human extends Hero {
    public Human() {
        super();
        GenerateCharacterStats statGenerator = new GenerateCharacterStats();

        int base = statGenerator.generateCharacterForceEnd();
        int split = (int)(Math.random() * 3) - 1;

        this.setFor(base + 2 + split);
        this.setEnd(base - split);

        this.setPV(statGenerator.generateCharacterLife(this.getEnd()));
        setMaxPV(this.getPV());
    }

    @Override
    public String getSymbol() {
        return "👦";
    }
}
