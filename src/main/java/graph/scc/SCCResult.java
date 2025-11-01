package graph.scc;

import java.util.*;

public class SCCResult {
    private final List<List<Integer>> components;
    private final int[] compOfVertex;

    public SCCResult(List<List<Integer>> components, int[] compOfVertex) {
        this.components = components;
        this.compOfVertex = compOfVertex;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int[] getCompOfVertex() {
        return compOfVertex;
    }

    public int size() {
        return components.size();
    }

    public List<Integer> sizes() {
        List<Integer> res = new ArrayList<>();
        for (List<Integer> comp : components) res.add(comp.size());
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SCCs found: " + components.size() + "\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append("Component ").append(i)
                    .append(" (size ").append(components.get(i).size()).append("): ")
                    .append(components.get(i)).append("\n");
        }
        return sb.toString();
    }
}
