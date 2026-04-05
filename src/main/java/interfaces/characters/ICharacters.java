package interfaces.characters;

import models.characters.heroes.Hero;

public interface ICharacters {
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
    String getSymbol();
}
