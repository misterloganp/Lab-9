package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dijkstra {

    static class Edge {
        String destination;
        double weight;

        Edge(String destination, double weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        String name;
        double distance;

        Node(String name, double distance) {
            this.name = name;
            this.distance = distance;
        }

        public int compareTo(Node other) {
            return Double.compare(distance, other.distance);
        }
    }

    static Map<String, List<Edge>> graph = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);

        System.out.print("Graph file to read from: ");
        String filename = input.nextLine();

        readGraph(filename);

        System.out.println("\nList of nodes: "
                + String.join(", ", graph.keySet()));

        while (true) {
            System.out.print("\nNode to start at: ");
            String start = input.nextLine().trim().toUpperCase();

            if (start.isEmpty()) {
                break;
            }

            if (!graph.containsKey(start)) {
                System.out.println("Node not found.");
                continue;
            }

            dijkstra(start);
        }

        input.close();
    }

    static void readGraph(String filename)
            throws FileNotFoundException {

        Scanner file = new Scanner(new File(filename));

        while (file.hasNext()) {
            String a = file.next();
            String b = file.next();
            double weight = file.nextDouble();

            graph.putIfAbsent(a, new ArrayList<>());
            graph.putIfAbsent(b, new ArrayList<>());

            graph.get(a).add(new Edge(b, weight));
            graph.get(b).add(new Edge(a, weight));
        }

        file.close();
    }

    static void dijkstra(String start) {

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        PriorityQueue<Node> queue = new PriorityQueue<>();

        for (String node : graph.keySet()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }

        distances.put(start, 0.0);
        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            if (current.distance > distances.get(current.name)) {
                continue;
            }

            for (Edge edge : graph.get(current.name)) {

                double newDistance =
                        current.distance + edge.weight;

                if (newDistance < distances.get(edge.destination)) {

                    distances.put(edge.destination, newDistance);

                    previous.put(edge.destination, current.name);

                    queue.add(new Node(edge.destination, newDistance));
                }
            }
        }

        List<String> nodes = new ArrayList<>(graph.keySet());

        nodes.sort(Comparator.comparingDouble(distances::get));

        for (String node : nodes) {

            if (distances.get(node) == Double.POSITIVE_INFINITY) {
                System.out.println(start + " -> " + node
                        + " unreachable");
                continue;
            }

            List<String> path = new ArrayList<>();
            String current = node;

            while (current != null) {
                path.add(current);
                current = previous.get(current);
            }

            Collections.reverse(path);

            System.out.println(String.join(" -> ", path)
                    + "  dist: " + distances.get(node));
        }
    }
}

