package services;

import models.characters.heroes.Dwarf;
import models.characters.heroes.Hero;
import models.characters.heroes.Human;
import models.gridMap.GridMap;
import utils.GameSave;
import utils.GameState;

import java.util.List;
import java.util.Scanner;

public class StartGame {
    public GameState startGame() {
        System.out.println("                v .   ._, |_  .,");
        System.out.println("           `-._\\/  .  \\ /    |/_");
        System.out.println("               \\\\  _\\, y | \\//");
        System.out.println("         _\\_.___\\\\, \\\\/ -.\\||");
        System.out.println("           `7-,--.`._||  / / ,");
        System.out.println("           /'     `-. `./ / |/_.");
        System.out.println("                     |    |//");
        System.out.println("                     |_    /");
        System.out.println("                     |-   |");
        System.out.println("                     |   =|");
        System.out.println("                     |    |");

        // Message de bienvenue
        System.out.println("\n==================================================================");
        System.out.println("    Welcome to the forest of \"SHOREWOOD\"");
        System.out.println("    Enchanted forest of the land of \"STORMWALL\"");
        System.out.println("==================================================================");
        System.out.println(" ");

        GameSave gameSave = new GameSave();
        Scanner sc = new Scanner(System.in);

        List<String> saves = gameSave.listSaves();


        if (!saves.isEmpty()) {
            System.out.println("\n--- Existing saves ---");
            for (int i = 0; i < saves.size(); i++) {
                System.out.println(" [" + (i + 1) + "] " + gameSave.getSaveSummary(saves.get(i)));
            }
            System.out.println("\n [n] New game");
            System.out.println("\nType your choice and press Enter > ");

            String input = sc.next();


            if (!input.equalsIgnoreCase("n")) {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < saves.size()) {
                        String playerName = saves.get(index);
                        return gameSave.loadGame(playerName);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Choix invalide, nouvelle partie.");
                }
            }
        }

        System.out.println("\n--- Please enter your name ---");
        System.out.print(" > ");
        String playerName = sc.next();

        System.out.println("\n--- Please choose a character ---");
        System.out.println(" [h] Human : Arthur the legend");
        System.out.println(" [d] Dwarf : Nanito the savage");
        System.out.println("\nType your choice and press Enter > ");

        char characterChoice;
        do {
            characterChoice = Character.toLowerCase(sc.next().charAt(0));
            if (characterChoice == 'h') {
                return new GameState(new Human(), null, 0, false, playerName);
            } else if (characterChoice == 'd') {
                return new GameState(new Dwarf(), null, 0, false, playerName);
            }
        } while (true);
    }
}
