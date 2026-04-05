package interfaces.characters;

import models.characters.heroes.Hero;
import models.characters.monsters.Dragon;
import models.characters.monsters.Orc;
import models.characters.monsters.Wolf;

public interface IMonster extends ICharacters{
    String getName();
    void showFight(Hero hero);

    int getGold();
    int getLeather();
}