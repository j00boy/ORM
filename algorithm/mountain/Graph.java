package mountain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Graph {
    private final Map<Integer, Map<Integer, Double>> nodes = new HashMap<>();
    private final Map<Integer, Map<Integer, Integer>> edgeIds = new HashMap<>();

    public void addNode(int node) {
        nodes.putIfAbsent(node, new HashMap<>());
        edgeIds.putIfAbsent(node, new HashMap<>());
    }

    public void addEdge(int source, int destination, double weight, int edgeId) {
        if (!nodes.containsKey(source))
            return;
        if (!nodes.containsKey(destination))
            return;
        nodes.get(source).put(destination, weight);
        edgeIds.get(source).put(destination, edgeId);
    }

    public void addBidirectionalEdge(int node1, int node2, double weight, int edgeId) {
        addEdge(node1, node2, weight, edgeId);
        addEdge(node2, node1, weight, edgeId);
    }

    public void addBidirectionalEdge(Edge edge) {
        int node1 = edge.start_id;
        int node2 = edge.end_id;
        double weight = edge.huri;
        int edgeId = edge.id;

        addEdge(node1, node2, weight, edgeId);
        addEdge(node2, node1, weight, edgeId);
    }

    public Map<Integer, Double> getNeighbors(int node) {
        return nodes.getOrDefault(node, new HashMap<>());
    }

    public Integer getEdgeId(int source, int destination) {
        return edgeIds.get(source).get(destination);
    }

    public Set<Integer> getNodes() {
        return nodes.keySet();
    }
}