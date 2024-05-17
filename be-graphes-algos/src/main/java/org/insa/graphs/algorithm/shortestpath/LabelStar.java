package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {

    public double coutEstime;
    public Node destination;

    public LabelStar(Node sommetCourant, Node destination) {
        super(sommetCourant);
        this.destination = destination;
        this.coutEstime = Point.distance(this.getSommetCourant().getPoint(), this.destination.getPoint());
        
    }

    @Override
    public double getTotalCost(){
        return coutEstime + getCoutRealise();
    }

    public void setCoutEstime(double coutEstime) {
        this.coutEstime = coutEstime;
    }



}
