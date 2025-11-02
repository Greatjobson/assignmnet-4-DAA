package graph.dagsp;

import java.util.List;
import java.util.ArrayList;

public class PathResult {
    private final int[] dist;
    private final int[] prev;

    public PathResult(int[] dist, int[] prev) {
        this.dist = dist;
        this.prev = prev;
    }

    public int[] getDist() {
        return dist;
    }

    public int[] getPrev() {
        return prev;
    }

    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.add(0, at); // добавляем в начало
        }
        return path;
    }
}
