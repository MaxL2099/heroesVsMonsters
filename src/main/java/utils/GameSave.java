package utils;

import enums.Armor;
import interfaces.characters.IMonster;
import models.characters.blacksmith.BlackSmith;
import models.characters.heroes.Dwarf;
import models.characters.heroes.Hero;
import models.characters.heroes.Human;
import models.characters.monsters.*;
import models.gridMap.GridMap;
import models.gridMap.MonsterGrid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static services.TurnCount.turnCount;

public class GameSave {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE = "game_save.txt";

    public GameSave() {
        createSaveDirectory();
    }

    private void createSaveDirectory() {
        try {
            Path saveDir = Paths.get(SAVE_DIRECTORY);
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du dossier de sauvegarde: " + e.getMessage());
        }
    }

    private String getSaveFile(String playerName) {
        return playerName.toLowerCase().replaceAll("[^a-z0-9]", "_") + ".txt";
    }

    public void saveGame(GameState gameState) {
        Path savePath = Paths.get(SAVE_DIRECTORY, getSaveFile(gameState.playerName));
        List<String> lines = new ArrayList<>();

        // Timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lines.add("# Save from " + timestamp);
        lines.add("# Level: " + turnCount);
        lines.add("");

        // Hero information
        lines.add("[HERO]");
        lines.add("race=" + gameState.hero.getClass().getSimpleName());
        lines.add("hp=" + gameState.hero.getPV());
        lines.add("maxHp=" + gameState.hero.getMaxPV());
        lines.add("force=" + gameState.hero.getFor());
        lines.add("endurance=" + gameState.hero.getEnd());
        lines.add("posX=" + gameState.hero.getX());
        lines.add("posY=" + gameState.hero.getY());
        lines.add("gold=" + gameState.hero.getSotckGold());
        lines.add("leather=" + gameState.hero.getSotckLeather());
        lines.add("potions=" + gameState.hero.getSotckPotions());
        lines.add("boots=" + gameState.hero.getBoots().name());
        lines.add("pants=" + gameState.hero.getPants().name());
        lines.add("chestplate=" + gameState.hero.getChestplate().name());
        lines.add("");

        // Informations de la carte
        lines.add("[MAP]");
        lines.add("width=" + gameState.map.getGrid().length);
        lines.add("height=" + gameState.map.getGrid()[0].length);
        lines.add("");

        // Sauvegarde de la grille
        lines.add("[GRID]");
        for (int i = 0; i < gameState.map.getGrid().length; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < gameState.map.getGrid()[i].length; j++) {
                if (j > 0) row.append("|");
                row.append(gameState.map.getGrid()[i][j]);
            }
            lines.add(row.toString());
        }

        // Boss
        lines.add("");
        lines.add("[BOSS]");
        Boss boss = gameState.map.getBoss();
        lines.add(boss.getX() + "|" + boss.getY() + "|" + boss.getPV() + "|" +
                boss.getMaxPV() + "|" + boss.getFor() + "|" + boss.getEnd() + "|" +
                boss.getGold() + "|" + boss.getLeather());

// Blacksmith
        lines.add("");
        lines.add("[BLACKSMITH]");
        BlackSmith bs = gameState.map.getBlackSmith();
        lines.add(bs.getX() + "|" + bs.getY()+ "|" + turnCount + "|" + bs.armorType.name() + "|" + bs.getPriceBootsInGold() + "|" + bs.getPriceBootsInLeather() + "|" +
                bs.getPricePantsInGold() + "|" + bs.getPricePantsInLeather() + "|" +
                bs.getPriceChestplateInGold() + "|" + bs.getPriceChestplateInLeather());

// Monsters
        lines.add("");
        lines.add("[MONSTERS]");
        IMonster[][] mg = gameState.map.getMonsterGrid().getMonsterGrid();
        for (int i = 0; i < mg.length; i++) {
            for (int j = 0; j < mg[i].length; j++) {
                if (mg[i][j] != null) {
                    Monster m = (Monster) mg[i][j];
                    if (m.getPV() > 0) {
                        lines.add(m.getName() + "|" + m.getX() + "|" + m.getY() + "|" +
                                m.getPV() + "|" + m.getMaxPV() + "|" + m.getFor() + "|" +
                                m.getEnd() + "|" + m.getGold() + "|" + m.getLeather());
                    }
                }
            }
        }

        try {
            Files.write(savePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("✅ Partie sauvegardée avec succès !");
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    public boolean saveExists() {
        Path savePath = Paths.get(SAVE_DIRECTORY, SAVE_FILE);
        return Files.exists(savePath);
    }

    public void deleteSave() {
        try {
            Path savePath = Paths.get(SAVE_DIRECTORY, SAVE_FILE);
            Files.deleteIfExists(savePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de la sauvegarde: " + e.getMessage());
        }
    }

    public GameState loadGame(String playerName) {
        MonsterGrid monsterGrid = new MonsterGrid();
        List<Monster> monsterList = new ArrayList<>();
        Boss boss = null;
        BlackSmith blackSmith = null;
        Path savePath = Paths.get(SAVE_DIRECTORY, getSaveFile(playerName));
        int gridRow = 0;
        try {
            List<String> lines = Files.readAllLines(savePath);

            Hero hero = null;
            int turnCount = 0;
            int width = 0, height = 0;
            String[][] grid = null;
            String section = "";

            for (String line : lines) {
                if (line.startsWith("#") || line.isBlank()) continue;

                if (line.startsWith("[")) {
                    section = line;
                    continue;
                }

                if (section.equals("[HERO]")) {
                    String[] parts = line.split("=", 2);
                    if (parts[0].equals("race")) {
                        hero = parts[1].equals("Human") ? new Human() : new Dwarf();
                    }
                    if (hero != null) {
                        String[] p = line.split("=", 2);
                        switch (p[0]) {
                            case "hp"         -> hero.setPV(Integer.parseInt(p[1]));
                            case "maxHp"      -> hero.setMaxPV(Integer.parseInt(p[1]));
                            case "force"      -> hero.setFor(Integer.parseInt(p[1]));
                            case "endurance"  -> hero.setEnd(Integer.parseInt(p[1]));
                            case "posX"       -> hero.setX(Integer.parseInt(p[1]));
                            case "posY"       -> hero.setY(Integer.parseInt(p[1]));
                            case "gold"       -> hero.setSotckGold(Integer.parseInt(p[1]));
                            case "leather"    -> hero.setSotckLeather(Integer.parseInt(p[1]));
                            case "potions"    -> hero.setSotckPotions(Integer.parseInt(p[1]));
                            case "boots"      -> hero.setBoots(Armor.valueOf(p[1]));
                            case "pants"      -> hero.setPants(Armor.valueOf(p[1]));
                            case "chestplate" -> hero.setChestplate(Armor.valueOf(p[1]));
                        }
                    }
                }

                if (section.equals("[MAP]")) {
                    String[] p = line.split("=", 2);
                    if (p[0].equals("width"))  width  = Integer.parseInt(p[1]);
                    if (p[0].equals("height")) height = Integer.parseInt(p[1]);
                    if (width > 0 && height > 0 && grid == null) grid = new String[width][height];
                }

                if (line.startsWith("# Level:")) {
                    turnCount = Integer.parseInt(line.split(":")[1].trim());
                    services.TurnCount.turnCount = turnCount;

                }

                if (section.equals("[GRID]") && grid != null) {
                    String[] parts = line.split("\\|", -1);
                    for (int j = 0; j < parts.length && j < grid[gridRow].length; j++) {
                        grid[gridRow][j] = parts[j];
                    }
                    gridRow++;
                }

                if (section.equals("[BOSS]")) {
                    String[] p = line.split("\\|");
                    boss = new Boss();
                    boss.setX(Integer.parseInt(p[0]));
                    boss.setY(Integer.parseInt(p[1]));
                    boss.setPV(Integer.parseInt(p[2]));
                    boss.setMaxPV(Integer.parseInt(p[3]));
                    boss.setFor(Integer.parseInt(p[4]));
                    boss.setEnd(Integer.parseInt(p[5]));
                    boss.setGold(Integer.parseInt(p[6]));
                    boss.setLeather(Integer.parseInt(p[7]));
                }

                if (section.equals("[BLACKSMITH]")) {
                    String[] p = line.split("\\|");
                    blackSmith = new BlackSmith(
                            Integer.parseInt(p[0]),
                            Integer.parseInt(p[1]),
                            Integer.parseInt(p[2]),
                            Integer.parseInt(p[4]),
                            Integer.parseInt(p[5]),
                            Integer.parseInt(p[6]),
                            Integer.parseInt(p[7]),
                            Integer.parseInt(p[8]),
                            Integer.parseInt(p[9])
                    );
                    blackSmith.armorType = Armor.valueOf(p[3]); // ← récupère LEATHER, IRON, etc.
                }

                if (section.equals("[MONSTERS]")) {
                    String[] p = line.split("\\|");
                    String type = p[0];
                    int mx = Integer.parseInt(p[1]);
                    int my = Integer.parseInt(p[2]);

                    Monster m = switch (type) {
                        case "Dragon" -> new Dragon();
                        case "Orc"    -> new Orc();
                        case "Wolf"   -> new Wolf();
                        default       -> null;
                    };

                    if (m != null) {
                        m.setPV(Integer.parseInt(p[3]));
                        m.setMaxPV(Integer.parseInt(p[4]));
                        m.setFor(Integer.parseInt(p[5]));
                        m.setEnd(Integer.parseInt(p[6]));
                        m.setGold(Integer.parseInt(p[7]));
                        m.setLeather(Integer.parseInt(p[8]));
                        m.setX(mx);
                        m.setY(my);
                        monsterList.add(m);
                    }
                }
            }

            GridMap map = new GridMap();
            map.setGrid(grid);
            if (!monsterList.isEmpty()) {
                IMonster[][] mgGrid = new IMonster[width][height];
                for (Monster m : monsterList) {
                    mgGrid[m.getX()][m.getY()] = m;
                }
                monsterGrid.setMonsterGrid(mgGrid);
                map.setMonsterGrid(monsterGrid);
            }

            if (boss != null) map.setBoss(boss);
            if (blackSmith != null) map.setBlackSmith(blackSmith);

            System.out.println("✅ Load successful !");

            return new GameState(hero, map, turnCount, true, playerName);

        } catch (IOException e) {
            System.err.println("❌ Error while loading: " + e.getMessage());
            return null;
        }
    }
    public boolean saveExists(String playerName) {
        return Files.exists(Paths.get(SAVE_DIRECTORY, getSaveFile(playerName)));
    }

    public List<String> listSaves() {
        List<String> names = new ArrayList<>();
        try {
            File dir = new File(SAVE_DIRECTORY);
            if (dir.exists()) {
                for (File f : dir.listFiles()) {
                    if (f.getName().endsWith(".txt")) {
                        names.add(f.getName().replace(".txt", ""));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lecture des sauvegardes: " + e.getMessage());
        }
        return names;
    }

    public String getSaveSummary(String playerName) {
        Path savePath = Paths.get(SAVE_DIRECTORY, getSaveFile(playerName));
        try {
            List<String> lines = Files.readAllLines(savePath);
            String race = "", hp = "", maxHp = "", level = "", date = "";
            for (String line : lines) {
                if (line.startsWith("# Save from")) date  = line.replace("# Save from ", "");
                if (line.startsWith("# Level:"))   level  = line.split(":")[1].trim();
                if (line.startsWith("race="))       race   = line.split("=")[1];
                if (line.startsWith("hp="))         hp     = line.split("=")[1];
                if (line.startsWith("maxHp="))      maxHp  = line.split("=")[1];
            }
            return String.format("%-10s | %s | ❤️ %s/%s | 🗺️  Level %s | 📅 %s", playerName, race, hp, maxHp, level, date);
        } catch (IOException e) {
            return playerName;
        }
    }
}