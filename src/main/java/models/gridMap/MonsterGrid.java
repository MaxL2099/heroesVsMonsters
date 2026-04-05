package models.gridMap;

import interfaces.characters.IMonster;

public class MonsterGrid {
    private IMonster[][] monsterGrid;

    public MonsterGrid() {
    }

    public IMonster[][] getMonsterGrid() {
        return monsterGrid;
    }

    public void setMonsterGrid(IMonster[][] monsterGrid) {
        this.monsterGrid = monsterGrid;
    }
}