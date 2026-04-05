import models.characters.heroes.Hero;
import models.gridMap.GridMap;
import models.gridMap.MonsterGrid;
import services.GameTurn;
import services.GenerateMap;
import services.StartGame;
import utils.GameState;
import utils.PrintMap;

import static services.TurnCount.turnCount;

void main() {

    GenerateMap generator = new GenerateMap();
    StartGame starting = new StartGame();
    GameTurn turns = new GameTurn();
    MonsterGrid monsterGrid = new MonsterGrid();

    GameState gameState = starting.startGame();
    Hero hero = gameState.hero;
    turnCount = gameState.isLoaded ? gameState.turnCount : 1;

    do {
        GridMap map;
        if (gameState.isLoaded && gameState.map != null && turnCount == gameState.turnCount) {
            map = gameState.map;
        } else {
            map = new GridMap();
            map = generator.generateMap(map, hero);
            monsterGrid.setMonsterGrid(generator.getMonsterGrid());
            map.setMonsterGrid(monsterGrid);
        }

        gameState.map = map; // ← la map est maintenant accessible depuis GameTurn lors du save
        gameState.turnCount = turnCount;

        PrintMap.printMap(map.getGrid());

        do {
            turns.nextTurn(gameState); // adapte selon ta signature existante
        } while (hero.isAlive() && map.getBoss().isAlive());

        turnCount++;
    } while (turnCount <= 6 && hero.isAlive());

    if (turnCount > 6 && hero.isAlive()) {
        System.out.println("Congratulations, you escaped the forest! Thanks for playing!");
    }
    else {
        System.out.println("Game Over, you're dead!");
    }
}
