package models.characters.monsters;

import models.characters.heroes.Hero;

import static utils.ShowOrcFight.showOrcFight;

public class Orc extends Monster {


    public Orc() {}

    public Orc(int x, int y, int turnCount, int statBonus) {
        super(x, y, turnCount, statBonus);
    }



    @Override
    public String getSymbol() { return "👹"; }

    @Override
    public void showFight(Hero hero) {
        showOrcFight(hero);
    }

    @Override
    public String getName() {
        return "Orc";
    }
}
