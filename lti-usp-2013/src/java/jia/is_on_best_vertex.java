package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if the agents is probably part of the teams zone.
 * </p>
 * Use: jia.is_on_team_zone(Z);</br>
 * 
 * @author mafranko
 */
public class is_on_best_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 1168992507523306792L;
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		
//		Term step = terms[1];
//		System.out.println("is_on_best_vertex - Agent: " + ts.getUserAgArch().getAgName() + ", Step: " + step.toString());
		
//		System.out.println("ENTROU!");
		
		Vertex myPosition = model.getMyVertex();
		List<Term> zone = ((ListTerm) terms[0]).getAsList();

		List<Integer> zoneIds = new ArrayList<Integer>();
		for (Term term : zone) {
			int value = (int) ((NumberTerm) term).solve();
			zoneIds.add(value);
		}
		
//		System.out.println("zone: " + zoneIds.toString() + ", mypos: " + myPosition.getId());
		
		if (!zoneIds.isEmpty()) {
			int bestVertex = zoneIds.get(0);
			if (bestVertex == myPosition.getId()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
