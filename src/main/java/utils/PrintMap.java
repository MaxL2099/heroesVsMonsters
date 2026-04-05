package utils;

public class PrintMap {

    public PrintMap() {
    }

    public static void printMap(String[][] map) {
        for(int i=0; i < 10; i++){
            System.out.println(" ");
        }
        if (map == null) {
            throw new IllegalStateException("Map has not been generated yet. Call generateMap() first.");
        }

        for (String[] row : map) {
            for (String cell : row) {
                System.out.printf("%-2s", cell);
            }
            System.out.println();
        }
    }
}
