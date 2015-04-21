package HideAndSeek;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

public interface GraphTraverser extends Comparable<GraphTraverser>, Runnable {

	public GraphController<?, ?> getGraphController();
	/**
	 * @param responsibleAgent
	 */
	public abstract void setResponsibleAgent(GraphTraversingAgent responsibleAgent);
	
	/**
	 * @return
	 */
	public abstract HashSet<StringVertex> uniquelyVisitedNodes();

	/**
	 * @return
	 */
	public abstract HashSet<StringEdge> uniquelyVisitedEdges();

	/**
	 * @return
	 */
	public abstract boolean strategyOverRounds();

	/**
	 * @param strategyOverRounds
	 */
	public abstract void strategyOverRounds(boolean strategyOverRounds);

	/**
	 * @return
	 */
	public abstract boolean isPlaying();
	
	/**
	 * 
	 */
	public abstract void startPlaying();

	/**
	 * @param currentNode
	 * @return
	 */
	public abstract StringVertex nextNode(StringVertex currentNode);

	/**
	 * @return
	 */
	public abstract StringVertex startNode();

	/**
	 * 
	 */
	public abstract void endOfRound();

	/**
	 * 
	 */
	public abstract void endOfGame();

	/**
	 * @return
	 */
	public abstract String printGameStats();

	/**
	 * @return
	 */
	public abstract String printRoundStats();

	/**
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * @return
	 */
	public int roundsPassed();
	
	/**
	 * @return
	 */
	public StringVertex currentNode();
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> exploredNodes();
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> hideLocations();
	
	/**
	 * @param location
	 */
	public void addHideLocation(StringVertex location);
	
	/**
	 * 
	 */
	public void mergeOtherTraverser(GraphTraverser traverser);
	
}