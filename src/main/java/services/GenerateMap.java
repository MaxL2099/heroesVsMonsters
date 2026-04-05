package services;

import interfaces.characters.IMonster;
import models.characters.blacksmith.BlackSmith;
import models.characters.heroes.Hero;
import models.characters.monsters.Boss;
import models.gridMap.GridMap;
import utils.HasAdjecentGround;

import java.util.*;

import static services.TurnCount.turnCount;

public class GenerateMap {
    private final String arbre = "🍀";
    private final String ground = "🟩";
    private final String water = "🟦";
    private final String potion = "🧪";
    private final String blacksmith = "🔨";
    private final String boss = "👑";
    private final int HAUTEUR = 32;
    private final int LARGEUR = 60;

    private IMonster[][] monsterGrid;
    private boolean[][] potionGrid;
    private int[] blacksmithPosition;
    private int[] bossPosition;

    public GridMap generateMap(GridMap map, Hero hero) {
        Random rand = new Random();
        map.setGrid(new String[HAUTEUR][LARGEUR]);

        // 1. Initialisation
        for (int i = 0; i < HAUTEUR; i++) {
            Arrays.fill(map.getGrid()[i], ground);
        }

        // 2. Génération décors
        for (int c = 0; c < 28; c++)
            growCluster(rand.nextInt(HAUTEUR), rand.nextInt(LARGEUR), arbre, 40, rand, map);

        for (int c = 0; c < 10; c++)
            growCluster(rand.nextInt(HAUTEUR), rand.nextInt(LARGEUR), water, 25, rand, map);

        // 3. Connexion des zones de marche
        connectGroundTiles(map);

        // 4. Monstres (Après le décor pour savoir où ils peuvent marcher)
        GenerateMonsters monsterGenerator = new GenerateMonsters();
        monsterGrid = monsterGenerator.generateMonsterGrid(HAUTEUR, LARGEUR, map.getGrid()).getMonsterGrid();

        // 5. Potions
        generatePotions(rand, map);

        // 6. Blacksmith and Boss
        generateBlacksmithAndBoss(rand, map);

        // 7. Hero placement
        placeHero(map, hero);
        hero.setMaxPV(hero.getMaxPV()+(turnCount-1)*3);
        hero.setEnd(hero.getEnd()+(turnCount-1)*3);
        hero.setFor(hero.getFor()+(turnCount-1)*3);
        hero.setPV(hero.getMaxPV());

        return map;
    }

    private void generatePotions(Random rand, GridMap map) {
        potionGrid = new boolean[HAUTEUR][LARGEUR];
        HasAdjecentGround adjacent = new HasAdjecentGround();
        int potionCount = 5;
        int attempts = 0;
        int placed = 0;

        while (placed < potionCount && attempts < 500) {
            int x = rand.nextInt(HAUTEUR);
            int y = rand.nextInt(LARGEUR);

            if (!map.getGrid()[x][y].equals(ground) &&
                    adjacent.hasAdjacentGround(x, y, map.getGrid()) &&
                    (monsterGrid == null || monsterGrid[x][y] == null)) {
                potionGrid[x][y] = true;
                map.getGrid()[x][y] = potion;
                placed++;
            }
            attempts++;
        }
    }

    private void generateBlacksmithAndBoss(Random rand, GridMap map) {
        HasAdjecentGround adjacent = new HasAdjecentGround();
        boolean generated = false;

        // Generate Blacksmith
        while (!generated) {
            int x = rand.nextInt(HAUTEUR);
            int y = rand.nextInt(LARGEUR);

            if (!map.getGrid()[x][y].equals(ground) &&
                    adjacent.hasAdjacentGround(x, y, map.getGrid()) &&
                    (monsterGrid == null || monsterGrid[x][y] == null) &&
                    !map.getGrid()[x][y].equals(potion)) {
                BlackSmith blackSmithInstance = new BlackSmith(x, y, turnCount);
                map.setBlackSmith(blackSmithInstance);
                blacksmithPosition = new int[]{x, y};
                map.getGrid()[x][y] = blackSmithInstance.getSymbol();
                generated = true;
            }
        }

        // Generate Boss
        generated = false;
        while (!generated) {
            int x = rand.nextInt(HAUTEUR);
            int y = rand.nextInt(LARGEUR);

            if (!map.getGrid()[x][y].equals(ground) &&
                    adjacent.hasAdjacentGround(x, y, map.getGrid()) &&
                    (monsterGrid == null || monsterGrid[x][y] == null) &&
                    !map.getGrid()[x][y].equals(potion) &&
                    !map.getGrid()[x][y].equals(blacksmith)) {
                Boss bossInstance = new Boss(x, y);
                map.setBoss(bossInstance);
                bossPosition = new int[]{x, y};
                map.getGrid()[x][y] = boss;
                generated = true;
            }
        }
    }

    private void connectGroundTiles(GridMap map) {
        boolean[][] visited = new boolean[HAUTEUR][LARGEUR];
        List<List<int[]>> regions = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < HAUTEUR; i++) {
            for (int j = 0; j < LARGEUR; j++) {
                if (!visited[i][j] && isGround(i, j,map)) {
                    List<int[]> region = new ArrayList<>();
                    Queue<int[]> queue = new LinkedList<>();
                    queue.add(new int[]{i, j});
                    visited[i][j] = true;

                    while (!queue.isEmpty()) {
                        int[] cur = queue.poll();
                        region.add(cur);
                        for (int d = 0; d < 4; d++) {
                            int nx = cur[0] + dx[d], ny = cur[1] + dy[d];
                            if (nx >= 0 && nx < HAUTEUR && ny >= 0 && ny < LARGEUR && !visited[nx][ny] && isGround(nx, ny, map)) {
                                visited[nx][ny] = true;
                                queue.add(new int[]{nx, ny});
                            }
                        }
                    }
                    regions.add(region);
                }
            }
        }

        if (regions.size() <= 1) return;
        regions.sort((a, b) -> b.size() - a.size());
        List<int[]> mainRegion = regions.get(0);

        for (int r = 1; r < regions.size(); r++) {
            connectRegions(mainRegion, regions.get(r), map);
            mainRegion.addAll(regions.get(r));
        }
    }

    private void connectRegions(List<int[]> regionA, List<int[]> regionB, GridMap map) {
        int[] bestA = regionA.get(0), bestB = regionB.get(0);
        int bestDist = Integer.MAX_VALUE;

        // Note: Pour optimiser, on pourrait ne vérifier qu'un échantillon de points
        for (int[] a : regionA) {
            for (int[] b : regionB) {
                int dist = Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestA = a;
                    bestB = b;
                }
            }
        }

        int x = bestA[0], tx = bestB[0];
        int y = bestA[1], ty = bestB[1];

        while (x != tx) {
            map.getGrid()[x][y] = ground;
            x += (tx > x) ? 1 : -1;
        }
        while (y != ty) {
            map.getGrid()[x][y] = ground;
            y += (ty > y) ? 1 : -1;
        }
    }

    private void growCluster(int startX, int startY, String type, int maxSize, Random rand, GridMap map) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        int filled = 0;
        int[] dx = {-1, 1, 0, 0}, dy = {0, 0, -1, 1};

        while (!queue.isEmpty() && filled < maxSize) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1];

            if (x < 0 || x >= HAUTEUR || y < 0 || y >= LARGEUR) continue;
            if (map.getGrid()[x][y].equals(type) || map.getGrid()[x][y].equals(water) || map.getGrid()[x][y].equals(arbre)) continue;

            map.getGrid()[x][y] = type;
            filled++;

            for (int d = 0; d < 4; d++) {
                if (rand.nextInt(100) < 60) queue.add(new int[]{x + dx[d], y + dy[d]});
            }
        }
    }

    private boolean isGround(int x, int y, GridMap map) {
        return map.getGrid()[x][y].equals(ground);
    }

    public void placeHero(GridMap map, Hero hero) {
        Random random = new Random();
        HasAdjecentGround adjacent = new HasAdjecentGround();
        boolean heroPlaced = false;
        while (!heroPlaced) {
            int x = random.nextInt(HAUTEUR);
            int y = random.nextInt(LARGEUR);

            if (!map.getGrid()[x][y].equals(ground) &&
                    adjacent.hasAdjacentGround(x, y, map.getGrid()) &&
                    (monsterGrid == null || monsterGrid[x][y] == null) &&
                    !map.getGrid()[x][y].equals(potion) &&
                    !map.getGrid()[x][y].equals(blacksmith) &&
                    !map.getGrid()[x][y].equals(boss)) {
                hero.setX(x);
                hero.setY(y);
                map.getGrid()[x][y] = hero.getSymbol();
                heroPlaced = true;
            }
        }
    }

    public IMonster[][] getMonsterGrid() { return monsterGrid; }
    public boolean[][] getPotionGrid() { return potionGrid; }
}