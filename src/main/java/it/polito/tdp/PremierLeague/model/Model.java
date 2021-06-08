package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Map<Integer, Team> idMap;
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
		dao.listAllTeams(idMap);
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo() {
		Graphs.addAllVertices(grafo, idMap.values());
		
		dao.classifica(idMap);
		for(Team t1 : idMap.values()) {
			for(Team t2 : idMap.values()) {
				if(!grafo.containsEdge(t1, t2) && !grafo.containsEdge(t2, t1)) {
					if(t1.getPunti() > t2.getPunti())
						Graphs.addEdgeWithVertices(grafo, t1, t2, t1.getPunti()-t2.getPunti());
					else if(t2.getPunti() > t1.getPunti())
						Graphs.addEdgeWithVertices(grafo, t2, t1, t2.getPunti()-t1.getPunti());
				}
			}
		}
		
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
	}
	
	public List<Team> squadreBattute(Team s) {
		List<Team> r = new ArrayList<>();
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(s))
			r.add(grafo.getEdgeTarget(e));
		
		r.sort((Team t1, Team t2) -> t2.getPunti()-t1.getPunti());
		return r;
	}
	
	public List<Team> squadreCheHannoBattuto(Team s) {
		List<Team> r = new ArrayList<>();
		for(DefaultWeightedEdge e : grafo.incomingEdgesOf(s))
			r.add(grafo.getEdgeSource(e));
		
		r.sort((Team t1, Team t2) -> t1.getPunti()-t2.getPunti());
		return r;
	}
	
	public List<Team> getSquadre() {
		List<Team> res = new ArrayList<>(idMap.values());
		res.sort((Team t1, Team t2) -> t1.getName().compareTo(t2.getName()));
		return res;
	}
	
	
}
