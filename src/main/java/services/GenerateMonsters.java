package services;

import interfaces.characters.IMonster;
import models.characters.monsters.Dragon;
import models.characters.monsters.Orc;
import models.characters.monsters.Wolf;
import models.gridMap.MonsterGrid;
import utils.HasAdjecentGround;

import java.util.Random;

import static services.TurnCount.turnCount;

public class GenerateMonsters {

    public IMonster generateRandom(int x, int y, int randNum) {
        if (randNum < 2) return new Dragon(x, y, turnCount, 2);
        if (randNum < 5) return new Orc(x, y, turnCount, 0);
        if (randNum < 12) return new Wolf(x, y, turnCount, -2);
        return null;
    }

    public MonsterGrid generateMonsterGrid(int height, int width, String[][] grid) {
        HasAdjecentGround adjecent = new HasAdjecentGround();
        IMonster[][] monsterGrid = new IMonster[height][width];
        Random rand = new Random();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int randNum = rand.nextInt(1000);
                IMonster monster = generateRandom(i, j, randNum);

                if (monster != null
                        && grid[i][j] != null
                        && grid[i][j].equals("🟩")
                        && adjecent.hasAdjacentGround(i, j, grid)) {
                    monsterGrid[i][j] = monster;
                    grid[i][j] = monster.getSymbol();
                }
            }
        }

        MonsterGrid monsterGridMap = new MonsterGrid();
        monsterGridMap.setMonsterGrid(monsterGrid);
        return monsterGridMap;
    }
}