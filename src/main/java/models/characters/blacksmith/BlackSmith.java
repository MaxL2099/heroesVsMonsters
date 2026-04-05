package models.characters.blacksmith;


import enums.Armor;
import enums.Dice;
import interfaces.characters.ICharacters;
import interfaces.characters.IHeroes;
import models.characters.Charact;
import models.characters.heroes.Hero;

import javax.xml.stream.events.Characters;

import static services.TurnCount.turnCount;

public class BlackSmith {
    public Armor armorType;
    private final int priceBootsInGold;
    private final int pricePantsInGold;
    private final int priceChestplateInGold;
    private final int priceBootsInLeather;
    private final int pricePantsInLeather;
    private final int priceChestplateInLeather;
    protected int x;
    protected int y;


    public BlackSmith(int x, int y, int turnCount) {
        priceBootsInGold = 10 + Dice.D4.roll() - 2 + (turnCount-1)*5;
        priceBootsInLeather = 8 + Dice.D4.roll() - 2 + (turnCount-1)*5;

        pricePantsInGold = 12 + Dice.D4.roll() - 2 + (turnCount-1)*5;
        pricePantsInLeather = 10 + Dice.D4.roll() - 2 + (turnCount-1)*5;

        priceChestplateInGold = 15 + Dice.D4.roll() - 2 + (turnCount-1)*5;
        priceChestplateInLeather = 12 + Dice.D4.roll() - 2 + (turnCount-1)*5;

        switch(turnCount) {
            case 1:
                armorType = Armor.LEATHER;
                break;
            case 2:
                armorType = Armor.CHAIN;
                break;
            case 3:
                armorType = Armor.IRON;
                break;
            case 4:
                armorType = Armor.STEEL;
                break;
            case 5:
                armorType = Armor.GOLD;
                break;
            case 6:
                armorType = Armor.DIAMOND;
                break;
            default:
        }

        this.x = x;
        this.y = y;
    }

    public BlackSmith(int x, int y, int turnCount, int priceBootsInGold, int priceBootsInLeather, int pricePantsInGold, int pricePantsInLeather, int priceChestplateInGold, int priceChestplateInLeather) {
        this.x = x;
        this.y = y;
        switch(turnCount) {
            case 1:
                armorType = Armor.LEATHER;
                break;
            case 2:
                armorType = Armor.CHAIN;
                break;
            case 3:
                armorType = Armor.IRON;
                break;
            case 4:
                armorType = Armor.STEEL;
                break;
            case 5:
                armorType = Armor.GOLD;
                break;
            case 6:
                armorType = Armor.DIAMOND;
                break;
            default:
        }
        this.priceBootsInGold = priceBootsInGold;
        this.priceBootsInLeather = priceBootsInLeather;
        this.pricePantsInGold = pricePantsInGold;
        this.pricePantsInLeather = pricePantsInLeather;
        this.priceChestplateInGold = priceChestplateInGold;
        this.priceChestplateInLeather = priceChestplateInLeather;
    }

    public int getPriceBootsInGold() {
        return priceBootsInGold;
    }

    public int getPricePantsInGold() {
        return pricePantsInGold;
    }

    public int getPriceChestplateInGold() {
        return priceChestplateInGold;
    }

    public int getPriceBootsInLeather() {
        return priceBootsInLeather;
    }

    public int getPricePantsInLeather() {
        return pricePantsInLeather;
    }

    public int getPriceChestplateInLeather() {
        return priceChestplateInLeather;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public String getSymbol() {
        return "🔨";
    }
}
