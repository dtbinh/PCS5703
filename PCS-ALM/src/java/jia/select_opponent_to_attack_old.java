package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import env.Percept;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns the best opponent to attack.
 * </p>
 *  Use: jia.select_opponent_to_attack(-Pos); </br>
 *  Where: Pos is the opponent's position.
 * 
 * @author mafranko
 */
public class select_opponent_to_attack_old extends DefaultInternalAction {

	private static final long serialVersionUID = 6306830130922763011L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		List<Vertex> zone = model.getBestOpponentZone();
		if (null != zone && !zone.isEmpty()) {
			Map<String, Entity> opponents = model.getOpponents();
			List<Entity> opponentsOnZone = new ArrayList<Entity>();
			for (Entity opponent : opponents.values()) {
				if (!opponent.getStatus().equals(Percept.STATUS_DISABLED)
						&& !opponent.getRole().equals(Percept.ROLE_SABOTEUR)) {
					Vertex v = opponent.getVertex();
					if (zone.contains(v)) {
						opponentsOnZone.add(opponent);
					}
				}
			}
			if (!opponentsOnZone.isEmpty()) {
				// attack the nearest opponent on the zone
				Entity closerOpponent = model.getCloserAgent(opponentsOnZone);
				if (null != closerOpponent) {
					Vertex closerVertex = closerOpponent.getVertex(); 
					if (null != closerVertex) {
						String vertex = Percept.VERTEX_PREFIX + closerOpponent.getVertex().getId();
						return un.unifies(terms[0], ASSyntax.createString(vertex));
					}
				}
			}
		}
		// else go attack the nearest opponent
		Entity closerOpponent = model.getCloserActiveOpponent();
		if (null != closerOpponent) {
			String vertex = Percept.VERTEX_PREFIX + closerOpponent.getVertex().getId();
			return un.unifies(terms[0], ASSyntax.createString(vertex));
		} else {
			// go to the least visited vertex
			int nextMove = graph.returnLeastVisitedNeighbor(model.getMyVertex().getId());
			if (nextMove == -1) {
				return un.unifies(terms[0], ASSyntax.createString("none"));
			}
			String vertex = Percept.VERTEX_PREFIX + nextMove;
			return un.unifies(terms[0], ASSyntax.createString(vertex));
		}
	}

}
