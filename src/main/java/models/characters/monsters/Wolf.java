package models.characters.monsters;

import models.characters.heroes.Hero;

import static utils.ShowWolfFight.showWolfFight;

public class Wolf extends Monster {


    public Wolf () {}

    public Wolf(int x, int y, int turnCount, int statBonus) {
        super(x, y, turnCount, statBonus);
    }

    @Override
    public String getSymbol() { return "🐺"; }

    @Override
    public void showFight(Hero hero) {
        showWolfFight(hero);
    }

    @Override
    public String getName() {
        return "Wolf";
    }
}
