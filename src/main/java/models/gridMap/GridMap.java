package models.gridMap;

import models.characters.blacksmith.BlackSmith;
import models.characters.monsters.Boss;
import models.characters.monsters.Monster;

public class GridMap {
    private String[][] grid;
    private MonsterGrid monsterGrid;
    private boolean[][] potionGrid;
    private BlackSmith blackSmith;
    private Boss boss;

    public GridMap() {
    }

    public String[][] getGrid() {
        return grid;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    public MonsterGrid getMonsterGrid() {
        return monsterGrid;
    }

    public void setMonsterGrid(MonsterGrid monsterGrid) {
        this.monsterGrid = monsterGrid;
    }

    public boolean[][] getPotionGrid() {
        return potionGrid;
    }

    public void setPotionGrid(boolean[][] potionGrid) {
        this.potionGrid = potionGrid;
    }

    public BlackSmith getBlackSmith() {
        return blackSmith;
    }

    public void setBlackSmith(BlackSmith blackSmith) {
        this.blackSmith = blackSmith;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public Boss getBoss() {
        return boss;
    }
}