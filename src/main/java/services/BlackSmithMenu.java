package services;

import enums.Armor;
import models.characters.Charact;
import models.characters.blacksmith.BlackSmith;
import models.characters.heroes.Hero;
import utils.ShowBlackSmith;

import java.util.Scanner;

import static utils.PressAnyKeyToContinue.pressAnyKey;

public class BlackSmithMenu {

    private static final int MENU_WIDTH = 128;

    public static void blackSmithMenu(BlackSmith blackSmith, Hero hero) {
        Armor leatherName = Armor.LEATHER;

        Scanner scanner = new Scanner(System.in);
        char blackSmithChoice;
        while(true) {
            ShowBlackSmith.showBlackSmith();
            showBlackSmithMenu(blackSmith, hero, leatherName);
            blackSmithChoice = scanner.next().charAt(0);
            if (blackSmithChoice == '1'){
                if (hero.getSotckGold() >= blackSmith.getPriceBootsInGold() && hero.getSotckLeather() >= blackSmith.getPriceBootsInLeather()) {
                    if (hero.getBoots().getValue() >= blackSmith.armorType.getValue()) {
                        System.out.println("You already have a same or higher-level armor equipped!");
                        pressAnyKey();
                        continue;
                    }
                    hero.setSotckGold(hero.getSotckGold() - blackSmith.getPriceBootsInGold());
                    hero.setSotckLeather(hero.getSotckLeather() - blackSmith.getPriceBootsInLeather());
                    hero.setBoots(leatherName);
                    System.out.println("You have successfully bought boots!");
                    pressAnyKey();
                }
                else {
                    System.out.println("You don't have enough gold or leather!");
                    pressAnyKey();
                }
            }
            else if (blackSmithChoice == '2'){
                if (hero.getSotckGold() >= blackSmith.getPricePantsInGold() && hero.getSotckLeather() >= blackSmith.getPricePantsInLeather()) {
                    if (hero.getPants().getValue() >= blackSmith.armorType.getValue()) {
                        System.out.println("You already have a same or higher-level armor equipped!");
                        pressAnyKey();
                        continue;
                    }
                    hero.setSotckGold(hero.getSotckGold() - blackSmith.getPricePantsInGold());
                    hero.setSotckLeather(hero.getSotckLeather() - blackSmith.getPricePantsInLeather());
                    hero.setPants(leatherName);
                    System.out.println("You have successfully bought a pants!");
                    pressAnyKey();
                }
                else if (hero.getPants().getValue() >= blackSmith.armorType.getValue()) {
                    System.out.println("You already have a same or higher-level armor equipped!");
                    pressAnyKey();
                }
                else {
                    System.out.println("You don't have enough gold or leather!");
                    pressAnyKey();
                }
            }
            else if (blackSmithChoice == '3'){
                if (hero.getSotckGold() >= blackSmith.getPriceChestplateInGold() && hero.getSotckLeather() >= blackSmith.getPriceChestplateInLeather()) {
                    if (hero.getChestplate().getValue() >= blackSmith.armorType.getValue()) {
                        System.out.println("You already have a same or higher-level armor equipped!");
                        pressAnyKey();
                        continue;
                    }
                    hero.setSotckGold(hero.getSotckGold() - blackSmith.getPriceChestplateInGold());
                    hero.setSotckLeather(hero.getSotckLeather() - blackSmith.getPriceChestplateInLeather());
                    hero.setChestplate(leatherName);
                    System.out.println("You have successfully bought a chestplate!");
                    pressAnyKey();
                }
                else if (hero.getChestplate().getValue() >= blackSmith.armorType.getValue()) {
                    System.out.println("You already have a same or higher-level armor equipped!");
                    pressAnyKey();
                }
                else {
                    System.out.println("You don't have enough gold or leather!");
                    pressAnyKey();
                }
            }
            else if (blackSmithChoice == '4'){
                System.out.println("Goodbye!");
                pressAnyKey();
                break;
            }
        }
    }
    public static void showBlackSmithMenu(BlackSmith blackSmith, Hero hero, Armor leatherName){
        String border = "=".repeat(MENU_WIDTH);
        System.out.println(border);

        // Line 1: Hero's resources
        String line1 = String.format("== YOUR RESOURCES: 💰 Gold: %d | 🧵 Leather: %d",
                hero.getSotckGold(),
                hero.getSotckLeather());
        int padding1 = MENU_WIDTH - line1.length() - 2; // -2 for the closing "=="
        System.out.println(line1 + " ".repeat(Math.max(0, padding1)) + "==");

        // Line 2: Item names and prices
        String item1 = String.format("1. 👢 Boots (%dG + %dL)",
                blackSmith.getPriceBootsInGold(),
                blackSmith.getPriceBootsInLeather(),
                leatherName.getValue());
        String item2 = String.format("2. 👖 Pants (%dG + %dL)",
                blackSmith.getPricePantsInGold(),
                blackSmith.getPricePantsInLeather(),
                leatherName.getValue());
        String item3 = String.format("3. 🦺 Chestplate (%dG + %dL)",
                blackSmith.getPriceChestplateInGold(),
                blackSmith.getPriceChestplateInLeather(),
                leatherName.getValue());
        String item4 = "4. ❌ Quit";

        String line2 = String.format("== %s | %s | %s | %s", item1, item2, item3, item4);
        int padding2 = MENU_WIDTH - line2.length() - 3;
        System.out.println(line2 + " ".repeat(Math.max(0, padding2)) + "==");

        System.out.println(border);
    }
}