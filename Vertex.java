import java.util.LinkedList;

/**
 class to represent a  vertex in a graph
*/
public class Vertex {
   
    private LinkedList<AdjListNode> adjList ; // the adjacency list 
    private int index; // the index of this vertex in the graph
    
    
	boolean visited; // whether vertex has been visited in a traversal
    int predecessor; // index of predecessor vertex in a traversal

     
    /**
	 creates a new instance of Vertex
	 */
    public Vertex(int n) {
    	adjList = new LinkedList<AdjListNode>();
    	index = n;
    	visited = false;
    }
    
    /**
	 copy constructor
	*/
    public Vertex(Vertex v){
    	adjList = v.getAdjList();
    	index = v.getIndex();
    	visited = v.getVisited();
    }
     
    
    public LinkedList<AdjListNode> getAdjList(){
        return adjList;
    }
    
    public int getIndex(){
    	return index;
    }
    
    public void setIndex(int n){
    	index = n;
    }
    
    public boolean getVisited(){
    	return visited;
    }
    
    public void setVisited(boolean b){
    	visited = b;
    }
    
    public int getPredecessor(){
    	return predecessor;
    }
    
    public void setPredecessor(int n){
    	predecessor = n;
    }
    
    public void addToAdjList(int n, int w){
        adjList.addLast(new AdjListNode(n, w));
    }
    
    public int vertexDegree(){
        return adjList.size();
    }
}
