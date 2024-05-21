package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {

    public double coutEstime;
    public Node destination;
    private int speed;

    public LabelStar(Node sommetCourant, Node destination, Mode mode, int speed) {
        super(sommetCourant);
        this.destination = destination;   
        this.speed = speed;
        if (mode == Mode.LENGTH){
            this.coutEstime = Point.distance(this.getSommetCourant().getPoint(), this.destination.getPoint());
        } else {
            if (speed<0){this.speed = 130;} 
            this.coutEstime = Point.distance(this.getSommetCourant().getPoint(), this.destination.getPoint()) / this.speed;
            }    
    }

    @Override
    public double getTotalCost(){
        return this.coutEstime + getCoutRealise();
    }
}
