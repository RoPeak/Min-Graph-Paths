import java.io.*;
import java.util.*;

/**
 program to find shortest path using Dijkstra's algorithm
 */
public class Main {

	public static void main(String[] args) throws IOException {
 
		long start = System.currentTimeMillis();

		String inputFileName = args[0]; // input file name
  
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);
		
		// read in the data here and create graph here
		int numVertices = Integer.parseInt(in.nextLine());
		Graph graph = new Graph(numVertices);

		// Construct the graph using the values of each line in file
		for (int i = 0; i < numVertices; i++) {
			String vertexLine = in.nextLine();
			String[] values = vertexLine.split(" ");
			for (int j = 0; j < numVertices; j++) {
				int edgeValue = Integer.parseInt(values[j]);
				if (edgeValue != 0) {
					graph.getVertex(i).addToAdjList(j, edgeValue);
				}
			}
		}

		String[] finalLine = in.nextLine().split(" ");
		int sourceVertex = Integer.parseInt(finalLine[0]);
		int destVertex = Integer.parseInt(finalLine[1]);
		
		in.close();
		reader.close();		
		
		// do the work here
		DijkstraResult result = dijkstra(graph, sourceVertex, destVertex);
		
		// If no path found
		if (result.getShortestPathLength() == -1) {
			System.out.print("No path exists");
		} else {
			// Otherwise, print the result
			System.out.println("Shortest distance from vertex " + sourceVertex + " to vertex " + destVertex + " is " + result.getShortestPathLength());
			System.out.print("Shortest path: ");
			for (int vertex : result.getShortestPath()) {
				System.out.print(vertex + " ");
			}
		}

		// end timer and print total time
		long end = System.currentTimeMillis();
		System.out.println("\nElapsed time: " + (end - start) + " milliseconds");
	
		// Uncomment the following lines to calculate and print memory used during execution
		// These values are used in my report
		/*
		Runtime runtimeInstance = Runtime.getRuntime();
		long memoryUsed = (runtimeInstance.totalMemory() - runtimeInstance.freeMemory()) / (1024*1024);
		System.out.println("Memory used: " + memoryUsed + " Mebibytes");
		*/
	}

	public static DijkstraResult dijkstra(Graph graph, int source, int dest) {
		// Declare variables, including required queue and arrays
		int numVertices = graph.size();
		PriorityQueue<VertexDistance> pq = new PriorityQueue<>();
		int[] distances = new int[numVertices];
		int[] predecessors = new int[numVertices];
		boolean[] visited = new boolean[numVertices];

		// Initialise arrays
		for (int i = 0; i < numVertices; i++) {
			distances[i] = Integer.MAX_VALUE;
			predecessors[i] = -1;
			visited[i] = false;
		}

		// Distance for source -> source is always 0
		distances[source] = 0;
		
		// Source vertex is the only node we currently know, add this to queue
		pq.add(new VertexDistance(source, 0));

		// Main loop
		while (!pq.isEmpty()) {
			// Get (and pop) the first vertex of the queue, then mark this as visited before processing
			int currentVertex = pq.poll().getVertexIndex();
			visited[currentVertex] = true;

			// Loop through nodes adjacent to the current vertex
			for (AdjListNode neighbour: graph.getVertex(currentVertex).getAdjList()) {
				int neighbourIndex = neighbour.getVertexIndex();
				// If the neighbour has not been visited before, calculate the distance to it
				if (!visited[neighbourIndex]) {
					int newDistance = distances[currentVertex] + neighbour.getEdgeWeight();
					// If this new distance is better than what we currently have, update the distance and predecessor then add node to queue
					if (newDistance < distances[neighbourIndex]) {
						distances[neighbourIndex] = newDistance;
						predecessors[neighbourIndex] = currentVertex;
						pq.add(new VertexDistance(neighbourIndex, newDistance));
					}
				}
			}
		}

		// Check if there is no path from source to destination
		if (distances[dest] == Integer.MAX_VALUE) {
			// In this case, return -1 and null to indicate no path exists
			return new DijkstraResult(-1, null);
		}

		// Otherwise, construct the shortest path by going back through the predecessors
		LinkedList<Integer> shortestPath = new LinkedList<>();
		int current = dest;
		while (current != -1) {
			shortestPath.addFirst(current);
			current = predecessors[current];
		}

		// Return the result
		return new DijkstraResult(distances[dest], shortestPath);
	}
}

// Class for storing results of Dijkstra's algorithm
class DijkstraResult {
	private int shortestPathLength;
	private LinkedList<Integer> shortestPath;

	public DijkstraResult(int shortestPathLength, LinkedList<Integer> shortestPath) {
		this.shortestPathLength = shortestPathLength;
		this.shortestPath = shortestPath;
	}

	public int getShortestPathLength() {
		return shortestPathLength;
	}

	public LinkedList<Integer> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPathLength(int length) {
		shortestPathLength = length;
	}

	public void setShortestPath(LinkedList<Integer> path) {
		shortestPath = path;
	}
}

// Class for storing distance to a given vertex
class VertexDistance implements Comparable<VertexDistance> {
	// Note that this does not do anything new, other than
	// implementing a compareTo.
	// The class is needed for the priority queue so that
	// vertices can be stored (with their distances) and
	// ordered based off their distance value
	private int vertexIndex;
	private int distance;

	public VertexDistance(int vertexIndex, int distance) {
		this.vertexIndex = vertexIndex;
		this.distance = distance;
	}

	public int getVertexIndex() {
		return vertexIndex;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public int compareTo(VertexDistance other) {
		return Integer.compare(this.distance, other.distance);
	}
}