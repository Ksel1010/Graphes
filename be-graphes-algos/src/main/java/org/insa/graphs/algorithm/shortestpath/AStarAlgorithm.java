package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    public Label[] initArray(ShortestPathData data){

        Node origine = data.getOrigin();
        Node dest = data.getDestination();
        Graph graph = data.getGraph();
        Mode mode = data.getMode();
        int speed = graph.getGraphInformation().getMaximumSpeed();
        int size = graph.getNodes().size();

        Label[]  labels=new Label[size] ;
        labels[origine.getId()] = new LabelStar(origine, dest,mode,speed);
        labels[origine.getId()].setCoutRealise(0);
  

        for (Node node: graph.getNodes()){
            if (!node.equals(origine)){

                labels[node.getId()] = new LabelStar(node,dest,mode,speed);
                labels[node.getId()].setCoutRealise(Double.POSITIVE_INFINITY);
            }
        }
        return labels;
    }

}
