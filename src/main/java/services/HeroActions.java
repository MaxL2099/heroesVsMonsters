package services;

import interfaces.characters.IMonster;
import models.characters.blacksmith.BlackSmith;
import models.characters.heroes.Hero;
import models.characters.monsters.Monster;
import models.gridMap.GridMap;
import java.util.Objects;

import static services.BlackSmithMenu.blackSmithMenu;
import static services.Fight.usePotion;
import static utils.ShowBlackSmith.showBlackSmith;

public class HeroActions {

    // Constants to avoid "Magic Strings" and typos
    private static final String TREE = "🍀";
    private static final String WATER = "🟦";
    private static final String POTION = "🧪";
    private static final String GROUND = "🟩";
    private static final String BLACKSMITH = "🔨";
    private static final String BOSS = "👑";

    public HeroActions(Hero hero) {
    }

    public void move(char direction, Hero hero, GridMap map) {
        int nextX = hero.getX();
        int nextY = hero.getY();
        //Determine target coordinates based on input
        switch (direction) {
            case 'z' -> nextX--;
            case 's' -> nextX++;
            case 'q' -> nextY--;
            case 'd' -> nextY++;
            case 'e' -> usePotion(hero);
            default -> { return; }
        }

        //Execute movement
        processMove(hero, map, nextX, nextY);
    }

    private void processMove(Hero hero, GridMap map, int nextX, int nextY) {
        String[][] grid = map.getGrid();
        boolean leaveOnMap = false;

        //Collision for the map border
        if (nextX < 0 || nextX >= grid.length || nextY < 0 || nextY >= grid[0].length) {
            return;
        }

        String targetTile = grid[nextX][nextY];

        //Obstacles (Trees or Water)
        if (Objects.equals(targetTile, TREE) || Objects.equals(targetTile, WATER)) {
            return; // Blocked
        }

        //Monsters (Combat Trigger)
        if (isMonsterTile(targetTile)) {
            Monster monster = findMonsterAt(nextX, nextY, map);
            boolean won = false;
            if (monster != null) {
                won = Fight.fight(hero, monster);
            }
            if (!won) {
                leaveOnMap = true;
            }
        }

        //Potions (Collection)
        else if (Objects.equals(targetTile, POTION)) {
            hero.setSotckPotions(hero.getSotckPotions() + 1);
            leaveOnMap = false;
        }

        else if (Objects.equals(targetTile, BLACKSMITH)){
            BlackSmith blackSmith = map.getBlackSmith();

            if (blackSmith != null) {
                showBlackSmith();
                blackSmithMenu(blackSmith, hero);
            }
            leaveOnMap = true;
        }

        else if (Objects.equals(targetTile, BOSS)){
            Monster boss = map.getBoss();
            boolean won = false;
            if (boss != null) {
                won = Fight.fight(hero, boss);
            }
            if (!won) {
                leaveOnMap = true;
            }
        }

        //Finalizing Movement
        if (!leaveOnMap) {
            grid[hero.getX()][hero.getY()] = GROUND;
            hero.setX(nextX);
            hero.setY(nextY);
        }
    }

    private boolean isMonsterTile(String tile) {
        return Objects.equals(tile, "🐺") || Objects.equals(tile, "🐉") || Objects.equals(tile, "👹");
    }

    private Monster findMonsterAt(int x, int y, GridMap map) {
        if (map.getMonsterGrid() == null || map.getMonsterGrid().getMonsterGrid() == null) {
            return null;
        }
        IMonster monster = map.getMonsterGrid().getMonsterGrid()[x][y];
        return (monster instanceof Monster) ? (Monster) monster : null;
    }
}