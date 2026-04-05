package models.characters.heroes;

import services.GenerateCharacterStats;

public class Dwarf extends Hero {

    public Dwarf() {
        super();
        GenerateCharacterStats statGenerator = new GenerateCharacterStats();

        int base = statGenerator.generateCharacterForceEnd();
        int split = (int)(Math.random() * 3) - 1;

        this.setFor(base + 1 + split);
        this.setEnd(base + 1 - split);

        this.setPV(statGenerator.generateCharacterLife(this.getEnd()));
        setMaxPV(this.getPV());
    }

    @Override
    public String getSymbol() {
        return "🧔";
    }
}
