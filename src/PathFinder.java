import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PathFinder {
    private int[][] grid;
    private int n;
    private int[] start, end;
    private boolean isPathFound;
    private boolean isComplete;
    private boolean[][] isVisited;
    private Stack<int[]> dijkstraPath;
    private List<List<int[]>> exploredNodes;
    List<int[]> currExploringNodes;
    private Grid world;

    public PathFinder(Grid world, int n) {
        this.n = n;
        this.world = world;
        start = new int[]{-1, -1};
        end = new int[]{-1, -1};
        grid = new int[n][n];
        isPathFound = false;
        isVisited = new boolean[n][n];
        dijkstraPath = new Stack<>();
        exploredNodes = new ArrayList<>();
        currExploringNodes = new ArrayList<>();
    }

    public int size() {
        return n;
    }

    public void setObstacle(int x,int y) {
        grid[x][y] = 1;
    }

    public void setStart(int x, int y) {
        if (start[0] != -1) {
            grid[start[0]][start[1]] = 0;
        }
        grid[x][y] = 2;
        start[0] = x;
        start[1] = y;
    }

    public void setEnd(int x, int y) {
        if (end[0] != -1) {
            grid[end[0]][end[1]] = 0;
        }
        grid[x][y] = 3;
        end[0] = x;
        end[1] = y;
    }

    public void removeCell(int x, int y) {
        if (grid[x][y] == 1) removeObstacle(x, y);
        else if (grid[x][y] == 2) removeStart();
        else if (grid[x][y] == 3) removeEnd();
    }

    public void removeStart() {
        grid[start[0]][start[1]] = 0;
        start[0] = -1;
        start[1] = -1;
    }

    public void removeEnd() {
        grid[end[0]][end[1]] = 0;
        end[0] = -1;
        end[1] = -1;
    }

    public void removeObstacle(int x, int y) {
        grid[x][y] = 0;
    }

    public int[][] getGrid() {
        return grid;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n && grid[row][col] != 1;
    }

    private boolean isEnd(int x, int y) {
        return x == end[0] && y == end[1];
    }

    public int bfs() {
        isPathFound = false;
        int[][][] parent = getParent();
        if (start[0] == -1 || end[0] == -1) return -1;
        isVisited = new boolean[n][n];
        Queue<int[]> q = new LinkedList<>();
        q.offer(start);
        isVisited[start[0]][start[1]] = true;
        int[] dr = new int[]{1, 0, -1, 0};
        int[] dc = new int[]{0, 1, 0, -1};
        List<int[]> temp;
        while(!q.isEmpty()) {
            int[] curr = q.poll();
            int r = curr[0];
            int c = curr[1];
            temp = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int new_r = r + dr[i];
                int new_c = c + dc[i];
                if (isValid(new_r, new_c) && !isVisited[new_r][new_c]) {
                    isVisited[new_r][new_c] = true;
                    parent[new_r][new_c][0] = r;
                    parent[new_r][new_c][1] = c;
                    temp.add(new int[]{new_r, new_c});
                    if (isEnd(new_r, new_c)) {
                        isPathFound = true;
                        isComplete = true;
                        return formPath(parent);
                    } else {
                        q.offer(new int[]{new_r, new_c});
                    }
                }
            }
            exploredNodes.add(temp);
        }
        isComplete = true;
        return 0;
    }

    public int dfs() {
        if (start[0] == -1 || end[0] == -1) return -1;
        Stack<int[]> stack = new Stack<>();
        isVisited = new boolean[n][n];
        stack.push(start);
        int[][][] parent = getParent();
        isVisited[start[0]][start[1]] = true;
        int[] dr = new int[]{1, 0, -1, 0};
        int[] dc = new int[]{0, 1, 0, -1};
        List<int[]> temp;
        while (!stack.empty()) {
            int[] curr = stack.pop();
            int r = curr[0];
            int c = curr[1];
            temp = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int new_r = r + dr[i];
                int new_c = c + dc[i];
                if (isValid(new_r, new_c) && !isVisited[new_r][new_c]) {
                    isVisited[new_r][new_c] = true;
                    parent[new_r][new_c][0] = r;
                    parent[new_r][new_c][1] = c;
                    temp.add(new int[]{new_r, new_c});
                    if (isEnd(new_r, new_c)) {
                        isPathFound = true;
                        isComplete = true;
                        formPath(parent);
                        return 1;
                    } else {
                        stack.push(new int[]{new_r, new_c});
                    }
                }
            }
            exploredNodes.add(temp);
        }
        isComplete = true;
        return 0;
    }

    private int[][][] getParent() {
        int[][][] parent = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                parent[i][j][0] = -1;
                parent[i][j][1] = -1;
            }
        }
        return parent;
    }

    public int[] getStart() {
        return start;
    }

    public int[] getEnd() {
        return end;
    }

    public boolean isPathFound() {
        return isPathFound;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void resetIsComplete() {
        isComplete = false;
    }

    public void resetPathFound() {
        isPathFound = false;
    }

    public boolean[][] getVisited() {
        return isVisited;
    }

    public int dijkstra() {
        if (start[0] == -1 || end[0] == -1) return -1;
        isVisited = new boolean[n][n];
        int[][][] parent = getParent();
        int[][] dist = new int[n][n];
        exploredNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }
        PriorityQueue<int[]> queue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] t1, int[] t2) {
                return dist[t1[0]][t1[1]] - dist[t2[0]][t2[1]];
            }
        });
        List<int[]> temp;
        dist[start[0]][start[1]] = 0;
        queue.offer(start);
        int[] dr = new int[]{1, 0, -1, 0};
        int[] dc = new int[]{0, 1, 0, -1};
        while(!queue.isEmpty()) {
            int[] currMin = queue.poll();
            isVisited[currMin[0]][currMin[1]] = true;
            temp = new ArrayList<>();
            if (currMin[0] == end[0] && currMin[1] == end[1]) {
                isPathFound = true;
                isComplete = true;
                return formPath(parent);
            }
            for (int k = 0; k < 4; k++) {
                int new_r = currMin[0] + dr[k];
                int new_c = currMin[1] + dc[k];
                if (isValid(new_r, new_c) && !isVisited[new_r][new_c] &&
                        grid[new_r][new_c] != 1 && dist[currMin[0]][currMin[1]] + 1 < dist[new_r][new_c]) {
                    temp.add(new int[]{new_r, new_c});
                    parent[new_r][new_c][0] = currMin[0];
                    parent[new_r][new_c][1] = currMin[1];
                    dist[new_r][new_c] = dist[currMin[0]][currMin[1]] + 1;
                    queue.offer(new int[]{new_r, new_c});
                }
            }
            exploredNodes.add(temp);
        }
        isComplete = true;
        return 0;
    }

    public int aStarSearch() {
        if (start[0] == -1 || end[0] == -1) return -1;
        isVisited = new boolean[n][n];
        int[][][] parent = getParent();
        exploredNodes = new ArrayList<>();
        int[][] g_val = new int[n][n];
        int[][] f_val = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                f_val[i][j] = Integer.MAX_VALUE;
            }
        }
        g_val[start[0]][start[1]] = 0;
        f_val[start[0]][start[1]] = 0;
        PriorityQueue<int[]> queue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] t1, int[] t2) {
                return f_val[t1[0]][t1[1]] - f_val[t2[0]][t2[1]];
            }
        });
        queue.offer(start);
        List<int[]> temp;
        int[] dr = new int[]{1, 0, -1, 0};
        int[] dc = new int[]{0, 1, 0, -1};
        while(!queue.isEmpty()) {
            int[] curr = queue.poll();
            isVisited[curr[0]][curr[1]] = true;
            if (isEnd(curr[0], curr[1])) {
                isPathFound = true;
                isComplete = true;
                return formPath(parent);
            }
            temp = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int new_r = curr[0] + dr[i];
                int new_c = curr[1] + dc[i];
                if (isValid(new_r, new_c) && grid[new_r][new_c] != 1 && !isVisited[new_r][new_c]) {
                    int g = g_val[curr[0]][curr[1]] + 1;
                    int h = Math.abs(new_r - end[0]) + Math.abs(new_c - end[1]);
                    int f = g + h;
                    if (f < f_val[new_r][new_c]) {
                        f_val[new_r][new_c] = f;
                        g_val[new_r][new_c] = g;
                        parent[new_r][new_c][0] = curr[0];
                        parent[new_r][new_c][1] = curr[1];
                        queue.offer(new int[]{new_r, new_c});
                        temp.add(new int[]{new_r, new_c});
                    }
                }
            }
            exploredNodes.add(temp);
        }
        isComplete = true;
        return 0;
    }

    private int formPath(int[][][] parent) {
        dijkstraPath = new Stack<>();
        int[] curr = new int[2];
        curr[0] = end[0];
        curr[1] = end[1];
        while (true) {
            if (curr[0] == start[0] && curr[1] == start[1]) break;
            if (curr[0] == -1 || curr[1] == -1) {
                isPathFound = false;
                return -1;
            }
            dijkstraPath.push(new int[]{curr[0], curr[1]});
            int r = parent[curr[0]][curr[1]][0];
            int c = parent[curr[0]][curr[1]][1];
            curr[0] = r;
            curr[1] = c;
        }
        world.repaint();
        return 1;
    }

    public List<List<int[]>> getExploredNodes() {
        return exploredNodes;
    }

    public Stack<int[]> getPath() {
        return dijkstraPath;
    }

    public void resetGrid() {
        isPathFound = false;
        isComplete = false;
        isVisited = new boolean[n][n];
        exploredNodes = new ArrayList<>();
        currExploringNodes = new ArrayList<>();
        world.repaint();
    }

    public void reset() {
        start = new int[]{-1, -1};
        end = new int[]{-1, -1};
        grid = new int[n][n];
        isPathFound = false;
        isComplete = false;
        isVisited = new boolean[n][n];
        exploredNodes = new ArrayList<>();
        currExploringNodes = new ArrayList<>();
        world.repaint();
    }
}
