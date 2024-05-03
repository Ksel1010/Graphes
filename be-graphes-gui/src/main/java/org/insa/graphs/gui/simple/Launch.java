package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {
/*
        // Visit these directory to see the list of available files on Commetud.
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
        final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // TODO: Read the graph.
        final Graph graph = reader.read();

        // Create the drawing:
        final Drawing drawing = createDrawing();

        // TODO: Draw the graph on the drawing.
        drawing.drawGraph(graph);

        // TODO: Create a PathReader.
        final PathReader pathReader = new BinaryPathReader( 
                new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));

        // TODO: Read the path.
        final Path path = pathReader.readPath(graph);

        // TODO: Draw the path.
        drawing.drawPath(path);

*/

        /**
         * Scénario : on part de l'INSA pour bikini
         */
        final String mapToulouse = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        test(mapToulouse);
    }

    public static void test(String map) throws FileNotFoundException, IOException{
        final GraphReader reader = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        final Graph graph = reader.read();

        Node origin = getRandomNode(graph);
        Node destination = getRandomNode(graph);
        
        for (ArcInspector inspector: ArcInspectorFactory.getAllFilters()){
            ShortestPathData data = new ShortestPathData(graph, origin, destination, inspector);
            DijkstraAlgorithm djikstra = new DijkstraAlgorithm(data);
            BellmanFordAlgorithm bellman= new BellmanFordAlgorithm(data);

            ShortestPathSolution pathDij = djikstra.run();
            ShortestPathSolution pathBel = bellman.run();

            
            System.out.println("origine: "+ origin.getId());
            System.out.println("destination: "+ destination.getId());


            if (pathBel.getPath()==null && pathDij.getPath()==null){
                System.out.println("pas de chemin pour les deux");   
            } else {
                if (pathBel.getPath()==null || pathDij.getPath()==null){
                    System.out.println("pas de chemin pour un seul ALERTE");   
                } else {
                    if(Math.abs(pathBel.getPath().getLength()-pathDij.getPath().getLength())<0.001){
                        System.out.println("Même coût");         
                    }
                    else{
                        System.out.println("Les coûts sont différents : dij"+pathDij.getPath().getLength()+" alors que bel: "+pathBel.getPath().getLength());
                    }
                }
            }    
        }
    }

    static Node getRandomNode(Graph graph){
        return graph.getNodes().get(new Random().nextInt(graph.size()));
    }
}
