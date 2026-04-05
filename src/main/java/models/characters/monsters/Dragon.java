package models.characters.monsters;

import models.characters.heroes.Hero;

import static utils.ShowDragonFight.showDragonFight;

public class Dragon extends Monster {


    public Dragon() {}

    public Dragon(int x, int y, int turnCount, int statBonus) {
        super(x, y, turnCount, statBonus);
    }

    @Override
    public String getSymbol() { return "🐉"; }

    @Override
    public void showFight(Hero hero) {
        showDragonFight(hero);
    }

    @Override
    public String getName() {
        return "Dragon";
    }

}
