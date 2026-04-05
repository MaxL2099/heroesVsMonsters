package models.characters;

import interfaces.characters.ICharacters;
import interfaces.characters.IHeroes;

public abstract class Charact implements ICharacters {
    private int End; //endurance
    private int For; //force
    private int PV;
    private int maxPV;//health

    public Charact() {
        maxPV = PV;
    }

    public Charact(int end, int forc, int pv) {
        End = end;
        For = forc;
    }

    public int getEnd() {
        return End;
    }
    public int getFor() {
        return For;
    }
    public int getPV() {
        return PV;
    }
    public int getMaxPV() {
        return maxPV;
    }

    public void setEnd(int end) {
        End = end;
    }
    public void setFor(int forc) {
        For = forc;
    }
    public void setPV(int pv) {
        PV = pv;
    }

    public void setMaxPV(int maxPV) {
        this.maxPV = maxPV;
    }

}
