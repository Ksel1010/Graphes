package org.insa.graphs.algorithm.shortestpath;

import java.util.HashMap;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    @Override
    public HashMap<Integer,Label> initHashMap(Graph graph,Node origine,Node dest){

        HashMap <Integer, Label> labels=new HashMap<>();
        labels.put(origine.getId(), new LabelStar(origine, dest));
        labels.get(origine.getId()).setCoutRealise(0);
  

        for (Node node: graph.getNodes()){
            if (!node.equals(origine)){

                labels.put(node.getId(), new LabelStar(node,dest));
                labels.get(node.getId()).setCoutRealise(Double.MAX_VALUE);
                /// Set cout estime en fonction du mode de data (LENGTH ou TIME)
            }
        }
        return labels;
    }

}
