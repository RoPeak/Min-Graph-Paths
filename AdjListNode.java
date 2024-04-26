/**
 class to represent an entry in the adjacency list of a vertex
 in a graph
*/
public class AdjListNode implements Comparable<AdjListNode> {

	private int vertexIndex;
	private int weight;
	// could be other fields, for example representing
	// properties of the edge - weight, capacity, . . .
	
    /* creates a new instance */
	public AdjListNode(int n, int w){
		vertexIndex = n;
		weight = w;
	}
	
	public int getVertexIndex(){
		return vertexIndex;
	}
	
	public void setVertexIndex(int n){
		vertexIndex = n;
	}

	public int getEdgeWeight() {
		return weight;
	}

	public void setEdgeWeight(int w) {
		weight = w;
	}
	
	@Override
	public int compareTo(AdjListNode other) {
		return Integer.compare(this.weight, other.weight);
	}
}
