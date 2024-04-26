package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{

    private Node sommetCourant;
    private boolean marque;
    private double coutRealise;
    private Arc pere;

    public Label(Node sommetCourant) {
        this.sommetCourant = sommetCourant;
        this.marque = false;
        this.coutRealise = 0;
        this.pere = null;
    }
    public Node getSommetCourant() {
        return sommetCourant;
    }
    public boolean isMarque() {
        return marque;
    }
    public double getCoutRealise() {
        return coutRealise;
    }
    public Arc getPere() {
        return pere;
    }

    public void setSommetCourant(Node sommetCourant) {
        this.sommetCourant = sommetCourant;
    }
    public void setMarque(boolean marque) {
        this.marque = marque;
    }
    public void setCoutRealise(double coutRealise) {
        this.coutRealise = coutRealise;
    }
    public void setPere(Arc pere) {
        this.pere = pere;
    }

    @Override
    public int compareTo(Label arg0) {
        return (int)this.coutRealise - (int)arg0.getCoutRealise();
    }

    
}
