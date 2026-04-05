package services;

import utils.GameState;
import utils.PrintMap;
import utils.GameSave;

import java.io.IOException;
import java.util.Scanner;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import static utils.PressAnyKeyToContinue.pressAnyKey;

public class GameTurn {
    private GameSave gameSave;

    public GameTurn() {
        this.gameSave = new GameSave();
    }

    public void nextTurn(GameState gameState){
        int UI_WIDTH = 60;

        String stats = String.format(" ❤️ HP: %d/%d | 🧪 Potions: %d", gameState.hero.getPV(), gameState.hero.getMaxPV(), gameState.hero.getSotckPotions());
        String movement = "🕹️  MOVE: [Z]▲ [Q]◀ [S]▼ [D]▶ ";
        System.out.printf("%-30s %30s%n", stats, movement);

        String inventory = String.format(" 💰 Gold: %d | 🧵 Leather: %d", gameState.hero.getSotckGold(), gameState.hero.getSotckLeather());
        String armor = String.format("🛡️  %s | %s | %s", gameState.hero.getBoots(), gameState.hero.getPants(), gameState.hero.getChestplate());
        System.out.printf("%-30s %30s%n", inventory, armor);

        String action = " ⚡ ITEM: [E] Use Potion | [F] Save";
        System.out.println(action);

        Scanner scanner = new Scanner(System.in);
        HeroActions move = new HeroActions(gameState.hero);
        char moveChoice;
        while (true) {
            moveChoice = Character.toLowerCase(scanner.next().charAt(0));
            switch (moveChoice) {
                case 'z', 'q', 's', 'd', 'e' -> {
                    move.move(moveChoice, gameState.hero, gameState.map);
                }
                case 'f' -> {
                    gameSave.saveGame(gameState);
                    pressAnyKey();
                }
                default -> { continue; }
            }
            break;
        }

        gameState.map.getGrid()[gameState.hero.getX()][gameState.hero.getY()] = gameState.hero.getSymbol();
        for (int i = 0; i < 10; i++) {
            System.out.println(" ");
        }
        PrintMap.printMap(gameState.map.getGrid());
    }
}