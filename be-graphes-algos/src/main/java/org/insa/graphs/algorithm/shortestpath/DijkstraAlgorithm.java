package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Arc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

/*    final boolean sommetsNonMarques(HashMap labels){

        while (labels.values().iterator().hasNext()) {
            Label l=(Label)labels.values().iterator().next();
            System.out.println(l.getSommetCourant().toString());
            if (!l.isMarque()){
                return true;
            }
        }
        return false;
    }*/

    public HashMap<Integer,Label> initHashMap(Graph graph,Node origine, Node dest){
        HashMap <Integer, Label> labels=new HashMap<>();
        labels.put(origine.getId(), new Label(origine));
        labels.get(origine.getId()).setCoutRealise(0);
  

        for (Node node: graph.getNodes()){
            if (!node.equals(origine)){

                labels.put(node.getId(), new Label(node));
                labels.get(node.getId()).setCoutRealise(Double.MAX_VALUE);

            }
        }
        return labels;
    }
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        Graph graph = data.getGraph();
        
        Node origine = data.getOrigin();



        HashMap<Integer, Label> labels=this.initHashMap(graph,origine, data.getDestination());

        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        tas.insert(labels.get(origine.getId()));

        
        while(!labels.get(data.getDestination().getId()).isMarque() && !tas.isEmpty()) {
            Label x = tas.deleteMin();
            x.setMarque(true);

            notifyNodeMarked(x.getSommetCourant());

            for (Arc arc : x.getSommetCourant().getSuccessors()){
                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                Node noeud = arc.getDestination();
                Label y = labels.get(noeud.getId());

                if (Double.isInfinite(y.getCoutRealise())) {
                    notifyNodeReached(noeud);
                }

                if (!y.isMarque()){
                    double cout = data.getCost(arc)+x.getCoutRealise();
                    if (y.getCoutRealise()>cout){
                        y.setCoutRealise(cout);
                        y.setPere(arc);
                    }
                    try {
                        tas.remove(y);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    tas.insert(y);
                    
                }
            }

            if (x.getSommetCourant().getId()==data.getOrigin().getId()){
                notifyOriginProcessed(origine);
            }

        }


        // Destination has no predecessor, the solution is infeasible...
        /*try {
            tas.remove(labels.get(data.getDestination().getId()));
            tas.insert(labels.get(data.getDestination().getId()));
        } catch (ElementNotFoundException e) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }*/
        

        if (labels.get(data.getDestination().getId()).getPere()==null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            notifyDestinationReached(data.getDestination());
            ArrayList<Arc> arcs = new ArrayList<>();
            Label x = labels.get(data.getDestination().getId());
            while(!x.getSommetCourant().equals(origine)){
                arcs.add(x.getPere());
                x = labels.get(x.getPere().getOrigin().getId());
            }

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());        

            Collections.reverse(arcs);        // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        
        }

        return solution;
    }

}
