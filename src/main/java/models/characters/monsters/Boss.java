package models.characters.monsters;

import enums.Dice;
import models.characters.Charact;
import models.characters.heroes.Hero;
import services.GenerateCharacterStats;

import static services.TurnCount.turnCount;
import static utils.ShowBossFight.showBossFight;
import static utils.ShowDragonFight.showDragonFight;

public class Boss extends Monster {
    private int gold;
    private int leather;
    protected int x;
    protected int y;

    public Boss() {}

    public Boss(int x, int y) {
        GenerateCharacterStats statGenerator = new GenerateCharacterStats();
        this.x = x;
        this.y = y;
        this.setFor(statGenerator.generateCharacterForceEnd() + 4 + (turnCount-1)*4);
        this.setEnd(statGenerator.generateCharacterForceEnd() + 4 + (turnCount-1)*4);
        this.setPV(statGenerator.generateCharacterLife(this.getEnd()) + 4 + (turnCount-1)*4);
        setMaxPV(this.getPV());
        this.gold = (Dice.D12.roll())*turnCount;
        this.leather = (Dice.D10.roll())*turnCount;
    }

    @Override
    public String getName() {
        return "Boss";
    }

    @Override
    public void showFight(Hero hero) {
        showBossFight(hero);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getLeather() {
        return leather;
    }

    public void setLeather(int leather) {
        this.leather = leather;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String getSymbol() {
        return "👑";
    }

    public boolean isAlive() {
        return this.getPV() > 0;
    }

}
