import java.io.*;
import java.util.*;

/**
 program to find shortest path using the backtrack search algorithm
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

		// Construct the graph using the values of each line
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
		BacktrackResult result = beginBacktrack(graph, sourceVertex, destVertex);

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

	public static BacktrackResult beginBacktrack(Graph graph, int sourceVertex, int destVertex) {
		// Declare variables
		int numVertices = graph.size();
		LinkedList<Integer> currentPath = new LinkedList<Integer>();
		int currentPathLength;
		boolean[] visited = new boolean[numVertices];

		// Initialise variables
		currentPath.add(sourceVertex);
		currentPathLength = 0;
		for (int i = 0; i < numVertices; i++) {
			visited[i] = false;
		}

		// Create result object with maximum path length and an empty path
		BacktrackResult result = new BacktrackResult(Integer.MAX_VALUE, new LinkedList<Integer>());
		
		// Begin backtrack search
		result = tryBacktrack(graph, sourceVertex, destVertex, currentPath, currentPathLength, visited, result);
		
		return result;
	}

	public static BacktrackResult tryBacktrack(Graph graph, int currentVertex, int destVertex, LinkedList<Integer> currentPath, int currentPathLength, boolean[] visited, BacktrackResult currentResult) {
		// Grab the current best path value stored in the result object
		int shortestPathLength = currentResult.getShortestPathLength();

		// Base case: if the current path length is already longer than the shortest path found so far, return the current result
		if (currentPathLength >= shortestPathLength) {
			return currentResult;
		}

		// Iterate through neighbours of the current vertex
		for (AdjListNode neighbour : graph.getVertex(currentVertex).getAdjList()) {
			int neighbourIndex = neighbour.getVertexIndex();

			if (!visited[neighbourIndex]) {
				// Add the neighbour to the current path and mark it as visited
				currentPath.add(neighbourIndex);
				visited[neighbourIndex] = true;

				// Calculate the new path length including this neighbour
				currentPathLength += neighbour.getEdgeWeight();

				// Compare current path length and the stored shortest path length
				if (currentPathLength < shortestPathLength) {
					if (neighbourIndex == destVertex) {
						// If we've reached the destination, update the shortest path
						currentResult.setShortestPathLength(currentPathLength);
						currentResult.setShortestPath(new LinkedList<>(currentPath)); // Add a copy of the path, instead of the path itself
					} else {
						// Otherwise, continue the search from this neighbour
						currentResult = tryBacktrack(graph, neighbourIndex, destVertex, currentPath, currentPathLength, visited, currentResult);
					}
				}

				// Revert changes to the current path and visited array before trying the next neighbour
				currentPath.removeLast();
				currentPathLength -= neighbour.getEdgeWeight();
				visited[neighbourIndex] = false;
			}
		}

		// All candidates have been tried so return the result

		// If no path was found, return -1 and null to indicate this
		if (currentResult.getShortestPathLength() == Integer.MAX_VALUE) {
			return new BacktrackResult(-1, null);
		}

		// Otherwise, return the calculated result
		return currentResult;
	}
}

// Class for storing the result of the backtrack search
class BacktrackResult {
	private int shortestPathLength;
	private LinkedList<Integer> shortestPath;
	
	public BacktrackResult(int shortestPathLength, LinkedList<Integer> shortestPath) {
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