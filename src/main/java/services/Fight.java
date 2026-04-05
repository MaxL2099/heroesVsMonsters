package services;

import enums.Dice;
import models.characters.Charact;
import models.characters.heroes.Hero;
import models.characters.monsters.Monster;

import java.util.Objects;

import static utils.FightMenu.showFightMenu;
import static utils.PressAnyKeyToContinue.pressAnyKey;

public class Fight {
    private static int specialAttackCooldown = 0; // Tracks cooldown turns
    
    public static boolean fight(Hero hero, Monster monster) {
        boolean fightOngoing = true;
        specialAttackCooldown = 0;
        
        while(fightOngoing && hero.getPV() > 0 && monster.getPV() > 0) {
            // Clear screen and show fight display
            clearScreen();
            monster.showFight(hero);
            
            // Show fight menu and get choice
            int choice = showFightMenu(hero.getPV(), hero.getMaxPV(), hero.getFor(),
                    hero.getEnd(), hero.getSotckPotions(),
                    monster.getName(), monster.getPV(), monster.getMaxPV(),
                    hero.getBoots(), hero.getPants(), hero.getChestplate(),
                    hero.getTotalArmorValue());
            
            if(choice == 1){
                // Hero attacks
                heroAttack(hero, monster);
                
                // Check if monster is defeated
                if(monster.getPV() <= 0) {
                    System.out.println("\n💀 " + monster.getName() + " has been defeated!");
                    collectLoot(hero, monster);
                    fightOngoing = false;
                } else {
                    // Monster counter-attacks if still alive
                    monsterAttack(monster, hero);
                }
            } else if(choice == 2) {
                // Hero defends
                heroDefend(hero, monster);
            } else if(choice == 3) {
                // Use potion
                usePotion(hero);
                // Monster attacks while hero uses potion
                if(monster.getPV() > 0) {
                    monsterAttack(monster, hero);
                }
            } else if(choice == 4) {
                if(specialAttackCooldown > 0) {
                    System.out.println("\n⏳ Special attack is on cooldown! " + specialAttackCooldown + " turn(s) remaining.");
                    System.out.println("Press Enter to continue...");
                    pressAnyKey();
                    // ✅ Vérifie si le héros a une potion
                } else if(hero.getSotckPotions() <= 0) {
                    System.out.println("\n❌ You need a potion to use the special attack!");
                    System.out.println("Press Enter to continue...");
                    pressAnyKey();
                } else {
                    // ✅ Consomme la potion
                    hero.setSotckPotions(hero.getSotckPotions() - 1);
                    System.out.println("\n🧪 You consume a potion to fuel your special attack! (" + hero.getSotckPotions() + " remaining)");

                    heroSpecialAttack(hero, monster);

                    if(monster.getPV() <= 0) {
                        System.out.println("\n💀 " + monster.getName() + " has been defeated!");
                        collectLoot(hero, monster);
                        fightOngoing = false;
                    } else {
                        monsterAttack(monster, hero);
                    }

                    specialAttackCooldown = 3;
                }
                }else if(choice == 5) {
                // Flee attempt
                if(attemptFlee(hero)) {
                    System.out.println("\n🏃 You successfully fled from battle!");
                    fightOngoing = false;
                } else {
                    System.out.println("\n⚠️ Failed to flee!");
                    monsterAttack(monster, hero);
                }
                if(specialAttackCooldown > 0) specialAttackCooldown--;
            }

            // Check if hero is defeated
            if(hero.getPV() <= 0) {
                System.out.println("\n💀 You have been defeated...");
                fightOngoing = false;
            }

            pressAnyKey();

        }

        // Final message
        if(hero.getPV() > 0 && monster.getPV() <= 0) {
            System.out.println("\n🎉 Victory! You survived the battle!");
            if(Objects.equals(monster.getSymbol(), "👑")) {
                System.out.println(" ");
                System.out.println("👑 You have defeated the boss! All your stats are increased by 3 points and you have been healed!");
                System.out.println("You are now in level " + (TurnCount.turnCount + 2) + "! Be careful, all monsters are stronger, get a better stuff!");
            }
            pressAnyKey();
            return true;
        }

        return false;
    }

    private static void heroAttack(Hero hero, Monster monster) {
        System.out.println("\n⚔️ You attack " + monster.getName() + "!");

        // Attack roll using D20
        int attackRoll = Dice.D20.roll();
        System.out.println("🎲 Attack roll: " + attackRoll);

        // Harder to hit - need higher roll
        int hitThreshold = 9 + (monster.getEnd() / 3);
        if(attackRoll + (hero.getFor() / 3) > hitThreshold) {
            // Calculate damage using hero's strength-based dice
            int baseDamage = rollDamageForStrength(hero.getFor());
            int totalDamage = Math.max(1, baseDamage + (hero.getFor() / 2));

            monster.setPV(monster.getPV() - totalDamage);
            System.out.println("✅ Hit! Dealt " + totalDamage + " damage!");
            System.out.println(monster.getName() + " HP: " + Math.max(0, monster.getPV()) + "/" + monster.getMaxPV());
        } else {
            System.out.println("❌ Miss!");
        }
    }

    private static void monsterAttack(Monster monster, Hero hero) {
        System.out.println("\n👹 " + monster.getName() + " attacks!");

        // Monster attack roll
        int attackRoll = Dice.D20.roll();
        System.out.println("🎲 Monster attack roll: " + attackRoll);

        int armorBonus = hero.getTotalArmorValue();
        int hitThreshold = 8 + (hero.getEnd() / 3) + armorBonus;

        if(attackRoll + (monster.getFor() / 3) > hitThreshold) {
            // Calculate monster damage - now uses armor reduction
            int baseDamage = Dice.D10.roll();
            int armorReduction = armorBonus / 2;
            int totalDamage = Math.max(1, baseDamage + monster.getFor() - (hero.getEnd() / 4) - armorReduction);
            totalDamage = totalDamage / 2;

            hero.setPV(hero.getPV() - totalDamage);
            System.out.println("💥 Monster hits! You take " + totalDamage + " damage!");
            if(armorBonus > 0) {
                System.out.println("🛡️ Your armor reduced " + armorReduction + " damage!");
            }
            System.out.println("Your HP: " + Math.max(0, hero.getPV()) + "/" + hero.getMaxPV());
        } else {
            if(armorBonus > 0) {
                System.out.println("🛡️ Your armor deflected the attack!");
            } else {
                System.out.println("🛡️ You dodged the attack!");
            }
        }
    }

    private static void heroDefend(Hero hero, Monster monster) {
        System.out.println("\n🛡️ You take a defensive stance!");

        int defenseRoll = Dice.D20.roll();
        System.out.println("🎲 Defense roll: " + defenseRoll);

        // Monster attacks but hero has advantage
        int monsterAttackRoll = Dice.D20.roll();
        System.out.println("👹 " + monster.getName() + " attacks!");
        System.out.println("🎲 Monster attack roll: " + monsterAttackRoll);

        int armorBonus = hero.getTotalArmorValue();
        int hitThreshold = 12 + (hero.getEnd() / 2) + (defenseRoll / 3) + armorBonus;

        if(monsterAttackRoll + (monster.getFor() / 3) > hitThreshold) {
            int baseDamage = Dice.D10.roll();
            int armorReduction = armorBonus;
            int reducedDamage = Math.max(1, baseDamage + (monster.getFor() / 2) - hero.getEnd() - armorReduction);

            hero.setPV(hero.getPV() - reducedDamage);
            System.out.println("💥 Hit through defense! You take " + reducedDamage + " damage (reduced)!");
            if(armorBonus > 0) {
                System.out.println("🛡️ Your armor absorbed " + armorReduction + " damage!");
            }
        } else {
            System.out.println("✅ Blocked! No damage taken!");
        }

        // ✅ Récupération de PV basée sur l'Endurance
        int recoveredHP = Math.max(1, hero.getEnd() / 4);
        hero.setPV(Math.min(hero.getMaxPV(), hero.getPV() + recoveredHP));
        System.out.println("💚 Defensive stance recovered " + recoveredHP + " HP!");
        System.out.println("Your HP: " + Math.max(0, hero.getPV()) + "/" + hero.getMaxPV());
    }

    public static void usePotion(Hero hero) {
        if(hero.getSotckPotions() > 0) {
            int healAmount = Dice.D12.roll() + Dice.D6.roll();
            hero.setPV(Math.min(hero.getMaxPV(), hero.getPV() + healAmount));
            hero.setSotckPotions(hero.getSotckPotions() - 1);
            System.out.println("\n🧪 Used potion! Healed " + healAmount + " HP!");
            System.out.println("Your HP: " + hero.getPV() + "/" + hero.getMaxPV());
            System.out.println("Potions remaining: " + hero.getSotckPotions());
        } else {
            System.out.println("\n❌ No potions left!");
        }
    }

    private static void heroSpecialAttack(Hero hero, Monster monster) {
        System.out.println("\n⚡ ===== SPECIAL ATTACK ===== ⚡");
        System.out.println("✨ You unleash a devastating power strike!");

        // Special attack has advantage (roll twice, take higher)
        int attackRoll1 = Dice.D20.roll();
        int attackRoll2 = Dice.D20.roll();
        int attackRoll = Math.max(attackRoll1, attackRoll2);

        System.out.println("🎲 Attack rolls: " + attackRoll1 + ", " + attackRoll2 + " → Using: " + attackRoll);

        int hitThreshold = 8 + (monster.getEnd() / 3); // Easier to hit than normal
        if(attackRoll + (hero.getFor() / 2) > hitThreshold) {
            // Special attack deals significantly more damage
            int baseDamage1 = rollDamageForStrength(hero.getFor());
            int baseDamage2 = rollDamageForStrength(hero.getFor());
            int totalDamage = Math.max(1, baseDamage1 + baseDamage2 + hero.getFor());

            monster.setPV(monster.getPV() - totalDamage);
            System.out.println("💥 CRITICAL HIT! Dealt " + totalDamage + " massive damage!");
            System.out.println(monster.getName() + " HP: " + Math.max(0, monster.getPV()) + "/" + monster.getMaxPV());
        } else {
            // Even on miss, deal some damage
            int minDamage = hero.getFor() / 4;
            monster.setPV(monster.getPV() - minDamage);
            System.out.println("⚠️ Glancing blow! Dealt " + minDamage + " damage!");
            System.out.println(monster.getName() + " HP: " + Math.max(0, monster.getPV()) + "/" + monster.getMaxPV());
        }
    }

    private static boolean attemptFlee(Hero hero) {
        int fleeRoll = Dice.D20.roll();
        System.out.println("🎲 Flee attempt roll: " + fleeRoll);
        return fleeRoll > 5; // Harder to flee - only 30% chance
    }

    private static int rollDamageForStrength(int strength) {
        if(strength >= 15) {
            return Dice.D10.roll();
        } else if(strength >= 10) {
            return Dice.D8.roll();
        } else if(strength >= 5) {
            return Dice.D6.roll();
        } else {
            return Dice.D4.roll();
        }
    }

    private static void collectLoot(Hero hero, Monster monster) {
        int goldLooted = monster.getGold();
        int leatherLooted = monster.getLeather();

        System.out.println("\n💰 ===== LOOT COLLECTED ===== 💰");
        System.out.println("🪙 Gold: +" + goldLooted);
        System.out.println("🧵 Leather: +" + leatherLooted);

        hero.setSotckGold(hero.getSotckGold() + goldLooted);
        hero.setSotckLeather(hero.getSotckLeather() + leatherLooted);

        System.out.println("\n📦 Your Inventory:");
        System.out.println("🪙 Total Gold: " + hero.getSotckGold());
        System.out.println("🧵 Total Leather: " + hero.getSotckLeather());
        System.out.println("🧪 Potions: " + hero.getSotckPotions());
    }

    private static void clearScreen() {
        for(int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
