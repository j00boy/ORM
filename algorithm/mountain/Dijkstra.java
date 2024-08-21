package mountain;

import java.util.*;

class Dijkstra {
    public static Map<Integer, Double> computeShortestPaths(Graph graph, int start, Map<Integer, Integer> previousNodes,
                                                            Map<Integer, Integer> previousEdges) {
        Map<Integer, Double> distances = new HashMap<>();
        for (int node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
            previousNodes.put(node, null);
            previousEdges.put(node, null);
        }
        distances.put(start, 0.0);

        PriorityQueue<Map.Entry<Integer, Double>> priorityQueue = new PriorityQueue<>(Map.Entry.comparingByValue());
        priorityQueue.add(new AbstractMap.SimpleEntry<>(start, 0.0));

        while (!priorityQueue.isEmpty()) {
            Map.Entry<Integer, Double> current = priorityQueue.poll();
            int currentNode = current.getKey();
            double currentDistance = current.getValue();

            if (currentDistance > distances.get(currentNode)) {
                continue;
            }

            for (Map.Entry<Integer, Double> neighborEntry : graph.getNeighbors(currentNode).entrySet()) {
                int neighbor = neighborEntry.getKey();
                double weight = neighborEntry.getValue();
                double distance = currentDistance + weight;

                if (distance < distances.get(neighbor)) {
                    distances.put(neighbor, distance);
                    previousNodes.put(neighbor, currentNode);
                    previousEdges.put(neighbor, graph.getEdgeId(currentNode, neighbor));
                    priorityQueue.add(new AbstractMap.SimpleEntry<>(neighbor, distance));
                }
            }
        }

        return distances;
    }

    public static List<Integer> getPath(Map<Integer, Integer> previousNodes, Map<Integer, Integer> previousEdges,
                                        int start, int end) {
        List<Integer> path = new LinkedList<>();
        List<Integer> edges = new LinkedList<>();
        for (Integer at = end; at != null; at = previousNodes.get(at)) {
            if (previousNodes.get(at) != null) {
                edges.add(previousEdges.get(at));
            }
            path.add(at);
        }
        Collections.reverse(edges);
        Collections.reverse(path);
//        System.out.println("경로에 포함된 간선 ID들: " + edges);
        return edges;
    }
}