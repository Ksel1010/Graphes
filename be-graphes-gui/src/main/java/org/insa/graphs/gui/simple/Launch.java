package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;

public class Launch {
    public static final String RED = "\033[0;31m";
    public static final String RESET = "\033[0m";
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
         * // Visit these directory to see the list of available files on Commetud.
         * final String mapName =
         * "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
         * final String pathName =
         * "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path"
         * ;
         * 
         * // Create a graph reader.
         * final GraphReader reader = new BinaryGraphReader(
         * new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
         * 
         * // TODO: Read the graph.
         * final Graph graph = reader.read();
         * 
         * // Create the drawing:
         * final Drawing drawing = createDrawing();
         * 
         * // TODO: Draw the graph on the drawing.
         * drawing.drawGraph(graph);
         * 
         * // TODO: Create a PathReader.
         * final PathReader pathReader = new BinaryPathReader(
         * new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));
         * 
         * // TODO: Read the path.
         * final Path path = pathReader.readPath(graph);
         * 
         * // TODO: Draw the path.
         * drawing.drawPath(path);
         * 
         */

        /**
         * Scénario : on part de l'INSA pour bikini
         */
        final String folderMapsPath = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps";
        File folder = new File(folderMapsPath);
        File[] listMaps = folder.listFiles();
        String map;
        int i=0;
        /*while(i<5){
            map = listMaps[new Random().nextInt(listMaps.length)].getName();
            if(map.endsWith("mapgr")){
                System.out.println(map);
                test(folderMapsPath+"/"+map);
                i++;
            }
        }*/
        final String mapToulouse = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        test(mapToulouse);
    }

    public static void test(String map) throws FileNotFoundException, IOException{
        final GraphReader reader = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        final Graph graph = reader.read();

        Node origin ;
        Node destination ;
        
        
        int nbEchecCouts=0;
        int nbEchecDurées=0;

        for (int i=0;i<5;i++){
            origin = getRandomNode(graph);
            destination = getRandomNode(graph);
            System.out.println("origine: "+ origin.getId());
             System.out.println("destination: "+ destination.getId());
            for (ArcInspector inspector: ArcInspectorFactory.getAllFilters()){
                ShortestPathData data = new ShortestPathData(graph, origin, destination, inspector);
                DijkstraAlgorithm djikstra = new DijkstraAlgorithm(data);
                BellmanFordAlgorithm bellman= new BellmanFordAlgorithm(data);
                AStarAlgorithm aStar = new AStarAlgorithm(data);

                ShortestPathSolution pathDij = djikstra.run();
                ShortestPathSolution pathBel = bellman.run();
                ShortestPathSolution pathAstar = aStar.run();

                
                
                System.out.println("arc incpector est : "+inspector.toString());

                if (pathBel.getPath()==null && pathDij.getPath()==null && pathAstar.getPath()==null){
                    System.out.println("pas de chemin pour les trois algos");   
                } else {
                    if (pathBel.getPath()==null || pathDij.getPath()==null || pathAstar.getPath()==null){
                        System.out.println("pas de chemin pour au moins un algo ALERTE");   
                    }
                
                if (inspector.getMode()==Mode.LENGTH){
                    if(Math.abs(pathBel.getPath().getLength()-pathDij.getPath().getLength())<0.001 && Math.abs(pathAstar.getPath().getLength()-pathDij.getPath().getLength())<0.001){
                        System.out.println("Même longueur");         
                    }
                    else{
                        System.out.println("Les coûts sont différents : dij:"+pathDij.getPath().getLength()+" \nalors que bel: "+pathBel.getPath().getLength()+ " \net cout AStar est"+ pathAstar.getPath().getLength());
                        nbEchecCouts++;
                    }
                }
                else {
                        
                        if (Math.abs(pathAstar.getPath().getMinimumTravelTime()-pathDij.getPath().getMinimumTravelTime())<0.01 && Math.abs(pathAstar.getPath().getMinimumTravelTime()-pathBel.getPath().getMinimumTravelTime())<0.001){
                            System.out.println("même durée pour les 3 algos");
                        }
                        else{
                            System.out.println("durées différentes: dij="+pathDij.getPath().getMinimumTravelTime()+"\nbel:"+pathBel.getPath().getMinimumTravelTime()+"\nAstar:"+pathAstar.getPath().getMinimumTravelTime());
                            nbEchecDurées++;
                        }
                    }
                }
            }
            System.out.println(RED+ "nombre Echecs couts: "+nbEchecCouts+RESET);
            System.out.println(RED+ "nombre Echecs durees: "+nbEchecDurées+RESET);
        }
    }

    static Node getRandomNode(Graph graph) {
        return graph.getNodes().get(new Random().nextInt(graph.size()));
    }
}
