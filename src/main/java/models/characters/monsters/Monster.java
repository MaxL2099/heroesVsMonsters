package models.characters.monsters;

import enums.Dice;
import interfaces.characters.IMonster;
import models.characters.Charact;
import services.GenerateCharacterStats;

public abstract class Monster extends Charact implements IMonster {
    private int gold;
    private int leather;
    protected int x;
    protected int y;

    public Monster() {}

    public Monster(int x, int y, int turnCount, int statBonus) {
        GenerateCharacterStats statGenerator = new GenerateCharacterStats();
        this.x = x;
        this.y = y;
        this.setFor(statGenerator.generateCharacterForceEnd() + (turnCount-1)*4 + statBonus - 4);
        this.setEnd(statGenerator.generateCharacterForceEnd() + (turnCount-1)*4 + statBonus);
        this.setPV(statGenerator.generateCharacterLife(this.getEnd()) + (turnCount-1)*4 + statBonus);
        setMaxPV(this.getPV());
        this.gold = Dice.D6.roll();
        this.leather = Dice.D4.roll();
    }

    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public void setX(int x) { this.x = x; }
    @Override public void setY(int y) { this.y = y; }

    public abstract String getSymbol();

    public int getGold() { return gold; }
    public int getLeather() { return leather; }

    @Override
    public String toString() {
        return "Monster{" +
                "x=" + x +
                ", y=" + y +
                ", force=" + this.getFor() +
                ", end=" + this.getEnd() +
                ", HP=" + this.getPV() +
                ", leather=" + this.leather +
                ", gold=" + this.gold +
                ", monsterType=" + getSymbol() +
                '}';
    }

    public void setGold(int gold) { this.gold = gold; }
    public void setLeather(int leather) { this.leather = leather; }
}