package utils;

public class HasAdjecentGround {
    public boolean hasAdjacentGround(int x, int y, String[][] grid) {
        String ground = "🟩";
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int d = 0; d < 4; d++) {
            int nx = x + dx[d];
            int ny = y + dy[d];

            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length
                    && ground.equals(grid[nx][ny])) {
                return true;
            }
        }
        return false;
    }
}
