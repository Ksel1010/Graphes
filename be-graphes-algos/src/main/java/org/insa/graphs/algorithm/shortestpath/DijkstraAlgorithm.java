package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Arc;

import java.util.ArrayList;
import java.util.Collections;

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

    public Label[] initArray(ShortestPathData data){

        Node origine = data.getOrigin();
        Graph graph = data.getGraph();
        int size = graph.getNodes().size();

        Label[] labels=new Label[size];
        labels[origine.getId()] = new Label(origine);
        labels[origine.getId()].setCoutRealise(0);
  

        for (Node node: graph.getNodes()){
            if (!node.equals(origine)){

                labels[node.getId()] = new Label(node);
                labels[node.getId()].setCoutRealise(Double.POSITIVE_INFINITY);

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

        Label[] labels=this.initArray(data);

        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        tas.insert(labels[origine.getId()]);

        notifyOriginProcessed(origine);

        while(!labels[data.getDestination().getId()].isMarque() && !tas.isEmpty()) {
            Label x = tas.deleteMin();
            x.setMarque(true);

            notifyNodeMarked(x.getSommetCourant());

            for (Arc arc : x.getSommetCourant().getSuccessors()){
                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                Node noeud = arc.getDestination();
                Label y = labels[noeud.getId()];

                if (!y.isMarque()){
                    double cout = data.getCost(arc)+x.getCoutRealise();

                    if (Double.isInfinite(y.getCoutRealise()) && Double.isFinite(cout)) {
                        notifyNodeReached(arc.getDestination());
                    }

                    if ((y.getCoutRealise()-cout)>0){
                        y.setCoutRealise(cout);
                        y.setPere(arc);
                    
                        try {
                            tas.remove(y);
                        } catch (Exception e) {/*System.out.println("catch catch");*/}
                        tas.insert(y);
                    }
                }
            }

        }


        // Destination has no predecessor, the solution is infeasible...
        /*try {
            tas.remove(labels.get(data.getDestination().getId()));
            tas.insert(labels.get(data.getDestination().getId()));
        } catch (ElementNotFoundException e) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }*/
        

        if (labels[data.getDestination().getId()].getPere()==null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            notifyDestinationReached(data.getDestination());
            ArrayList<Arc> arcs = new ArrayList<>();
            Label x = labels[data.getDestination().getId()];
            while(!x.getSommetCourant().equals(origine)){
                arcs.add(x.getPere());
                x = labels[x.getPere().getOrigin().getId()] ;
            }

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());        

            Collections.reverse(arcs);        // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        
        }

        return solution;
    }

}
