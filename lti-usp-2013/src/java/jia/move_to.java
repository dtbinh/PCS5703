package jia;

import env.Percept;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * Retrieves the next move to achieve a given target.
 * </p>
 * Use: jia.move_to_target(+X,+Y,-N); </br>
 * Where: X is the actual location, Y is the target location and N is the next move (location).
 * 
 * @author mafranko 
 */
public class move_to extends DefaultInternalAction {

	private static final long serialVersionUID = -6710390609224328605L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int v1 = (int)((NumberTerm) terms[0]).solve();

		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		
		Vertex myPosition = model.getMyVertex();
		int myVertexId = myPosition.getId();

		if (v1 == myVertexId) {
			return un.unifies(terms[1], ASSyntax.createString("none"));
		}

		if (graph.existsPath(myVertexId, v1)) {
			int nextMove = graph.returnNextMove(myVertexId, v1);
			if (nextMove != -1) {
				String vertex = Percept.VERTEX_PREFIX + nextMove;
				return un.unifies(terms[1], ASSyntax.createString(vertex));
			}
		}
		
//		System.out.println("Could not find a path from vertex" + myVertexId + " to vertex" + v1);

		return un.unifies(terms[1], ASSyntax.createString("none"));
	}
	
}
