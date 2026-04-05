package utils;

import models.characters.heroes.Hero;
import models.gridMap.GridMap;

public class GameState {
    public final Hero hero;
    public GridMap map;
    public int turnCount;
    public final boolean isLoaded;
    public String playerName;

    public GameState(Hero hero, GridMap map, int turnCount, boolean isLoaded, String playerName) {
        this.hero = hero;
        this.map = map;
        this.turnCount = turnCount;
        this.isLoaded = isLoaded;
        this.playerName = playerName;
    }
}