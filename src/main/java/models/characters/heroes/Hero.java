package models.characters.heroes;

import enums.Armor;
import interfaces.characters.IHeroes;
import models.characters.Charact;
import models.gridMap.GridMap;
import utils.HasAdjecentGround;

import java.util.Random;

public abstract class Hero extends Charact implements IHeroes {
    private int sotckGold;
    private int sotckLeather;
    private int sotckPotions;
    private Armor boots;
    private Armor pants;
    private Armor chestplate;
    protected int x;
    protected int y;


    Hero() {
        sotckGold = 0;
        sotckLeather = 0;
        sotckPotions = 0;
        boots = Armor.NONE;
        pants = Armor.NONE;
        chestplate = Armor.NONE;
    }

    public int getSotckGold() { return sotckGold; }
    public int getSotckLeather() { return sotckLeather; }
    public int getSotckPotions() { return sotckPotions; }

    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public void setX(int x) { this.x = x; }
    @Override public void setY(int y) { this.y = y; }
    @Override
    public String toString() {
        return "Hero{" +
                "sotckGold=" + sotckGold +
                ", sotckLeather=" + sotckLeather +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void setSotckGold(int sotckGold) {
        this.sotckGold = sotckGold;
    }

    public void setSotckLeather(int sotckLeather) {
        this.sotckLeather = sotckLeather;
    }

    public void setSotckPotions(int sotckPotions) {
        this.sotckPotions = sotckPotions;
    }
    
    public Armor getBoots() { return boots; }
    public Armor getPants() { return pants; }
    public Armor getChestplate() { return chestplate; }

    public void setBoots(Armor boots) { this.boots = boots; }
    public void setPants(Armor pants) { this.pants = pants; }
    public void setChestplate(Armor chestplate) { this.chestplate = chestplate; }

    public int getTotalArmorValue() {
        return boots.getValue() + pants.getValue() + chestplate.getValue();
    }

    public boolean isAlive() {
        return this.getPV() > 0;
    }
}
