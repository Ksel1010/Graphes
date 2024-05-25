package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
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
    public static final String BLUE = "\033[0;34m";
    public static final String GREEN = "\033[0;32m";
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
        /**  Code pour des cartes aléatoires */
        
        /*final String folderMapsPath = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/";
        File folder = new File(folderMapsPath);
        File[] listMaps = folder.listFiles();
        String map;
        int i=0;
        while(i<5){ //a voir nombre de cartes voulu
            map = listMaps[new Random().nextInt(listMaps.length)].getName();
            if(map.endsWith("mapgr")){
                System.out.println(BLUE+map+RESET);
                test(folderMapsPath+map);
                i++;
            }
        }
        */

        /*Tester les durées d'éxecution de Dij et A star */
        
        //String carteFrance="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/france.mapgr";
        //TestDuree(carteFrance);
       // String carteCarreDense="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
       // TestDuree(carteCarreDense);
       String carteToulouse="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
       // TestDuree(carteToulouse);
       // String carteBordeaux="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/bordeaux.mapgr";
       // TestDuree(carteBordeaux);

       String carteFrance="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/france.mapgr";
       cheminLong(carteToulouse);
         /** Tests rapides sur petites cartes */
        /*
        final String folderMapsPath = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/";
        String[] liste={"toulouse.mapgr","bordeaux.mapgr","paris.mapgr"};
        String map;
        for(int i=0;i<liste.length;i++){
            map=liste[i];
            System.out.println(BLUE+map+RESET);
            test(folderMapsPath+map);        
        }*/
        
        
    }

    /**Test pour les durées de Dij vs Astart */

    public static void TestDuree(String map) throws FileNotFoundException, IOException{
        System.out.println("La carte: "+map);
        final GraphReader reader = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        final Graph graph = reader.read();

        Node origin ;
        Node destination ;
        origin = getRandomNode(graph);
        destination = getRandomNode(graph);
        ShortestPathData data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm djikstra = new DijkstraAlgorithm(data);
        AStarAlgorithm aStar = new AStarAlgorithm(data);

        long timeDij=new Date().getTime();
        ShortestPathSolution pathDij = djikstra.run();
        timeDij=new Date().getTime()-timeDij;
    
        long timeAStar= new Date().getTime();
        ShortestPathSolution pathAstar = aStar.run();
        timeAStar = new Date().getTime()-timeAStar;

        System.out.println(RED+"Les temps d'éxecution sont:");
        System.out.println("    Dij: "+timeDij/1000+"s, "+(int)(timeDij%1000)+" ms");
        System.out.println("    A start: "+timeAStar/1000+"s, "+(int)(timeAStar%1000)+" ms"+RESET);
    }


    public static void cheminLong(String map)throws FileNotFoundException, IOException{
        final GraphReader reader = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        final Graph graph = reader.read();

        Node origin ;
        Node destination ;
        origin = getRandomNode(graph);
        destination = getRandomNode(graph);
        System.out.println(GREEN+"origine: "+ origin.getId());
        System.out.println("destination: "+ destination.getId()+RESET);
        for (ArcInspector inspector : ArcInspectorFactory.getAllFilters()){
        //    ArcInspector inspector = ArcInspectorFactory.getAllFilters().get(0);
            System.out.println("inspector: "+inspector.toString());
            ShortestPathData data = new ShortestPathData(graph, origin, destination, inspector);
            DijkstraAlgorithm djikstra = new DijkstraAlgorithm(data);
            AStarAlgorithm aStar = new AStarAlgorithm(data);
            ShortestPathSolution pathDij = djikstra.run();
            ShortestPathSolution pathAstar = aStar.run();
            if ( pathDij.getPath()==null && pathAstar.getPath()==null){
                System.out.println("pas de chemin pour les 2 algos");   
            } else {
                if(inspector.getMode()==Mode.LENGTH){
                    double lengthPortionDij=0, lengthPortionAstar=0;
                    Node debutPortion=origin;
                    Node finPortion=origin;
                    for(int i=0; i<pathAstar.getPath().size();i+=pathAstar.getPath().size()/10){
                        debutPortion=finPortion;
                        if((i+pathAstar.getPath().size()/10)>=(pathAstar.getPath().size()-1)){
                            i=pathAstar.getPath().size();
                            finPortion=destination;
                        }else{
                            finPortion=pathAstar.getPath().getArcs().get(i+pathAstar.getPath().size()/10).getDestination();
                        }
                        ShortestPathData dataPotrtion = new ShortestPathData(graph, debutPortion, finPortion, inspector);
                        ShortestPathSolution pathPortionAstar=new AStarAlgorithm(dataPotrtion).run();
                        if(pathPortionAstar.getPath()==null){
                            System.out.println("srs"+debutPortion.getId()+" dest "+finPortion.getId());
                        }
                        else{
                            lengthPortionAstar+=pathPortionAstar.getPath().getLength();
                        }
                    }
                    System.out.println("La distance en:\n"+RED+"A star est:\t"+pathAstar.getPath().getLength());
                    System.out.println("A star portion est:\t"+lengthPortionAstar+RESET);
                    double diff=Math.abs(lengthPortionAstar-pathAstar.getPath().getLength());
                    System.out.println("La différence est "+diff+" représente: "+diff*100/pathAstar.getPath().getLength()+"%");

                    debutPortion=origin;
                    finPortion=origin;
                    for(int i=0; i<pathDij.getPath().size();i+=pathDij.getPath().size()/10){
                        debutPortion=finPortion;
                        if((i+pathDij.getPath().size()/10)>=(pathDij.getPath().size()-1)){
                            i=pathDij.getPath().size();
                            finPortion=destination;
                        }else{
                            finPortion=pathDij.getPath().getArcs().get(i+pathDij.getPath().size()/10).getDestination();
                        }
                        ShortestPathData dataPotrtion = new ShortestPathData(graph, debutPortion, finPortion, inspector);
                        ShortestPathSolution pathPorionDij=new DijkstraAlgorithm(dataPotrtion).run();
                        if(pathPorionDij.getPath()==null){
                            System.out.println("srs"+debutPortion.getId()+" dest "+finPortion.getId());
                        }
                        else{
                            lengthPortionDij+=pathPorionDij.getPath().getLength();
                        }

                    }
                    System.out.println("La distance en:\n"+RED+"Dij est:\t"+pathDij.getPath().getLength());
                    System.out.println("Dij portion est:\t"+lengthPortionDij+RESET);
                    diff=Math.abs(lengthPortionDij-pathDij.getPath().getLength());
                    System.out.println("La différence est "+diff+" représente: "+diff*100/pathDij.getPath().getLength()+"%");
                }
                if(inspector.getMode()==Mode.TIME){
                    double timePortionDij=0, timePortionAstar=0;
                    Node debutPortion=origin;
                    Node finPortion=origin;
                    for(int i=0; i<pathAstar.getPath().size();i+=pathAstar.getPath().size()/10){
                        debutPortion=finPortion;
                        if((i+pathAstar.getPath().size()/10)>=(pathAstar.getPath().size()-1)){
                            i=pathAstar.getPath().size();
                            finPortion=destination;
                        }else{
                            finPortion=pathAstar.getPath().getArcs().get(i+pathAstar.getPath().size()/10).getDestination();
                        }
                        ShortestPathData dataPotrtion = new ShortestPathData(graph, debutPortion, finPortion, inspector);
                        ShortestPathSolution pathPortionAstar=new AStarAlgorithm(dataPotrtion).run();
                        if(pathPortionAstar.getPath()==null){
                            System.out.println("srs"+debutPortion.getId()+" dest "+finPortion.getId());
                        }
                        else{
                            timePortionAstar+=pathPortionAstar.getPath().getMinimumTravelTime();
                        }
                    
                    }
                    System.out.println("La distance en:\n"+RED+"A star est\t:"+pathAstar.getPath().getMinimumTravelTime());
                    System.out.println("A star portion est:\t"+timePortionAstar+RESET);
                    double diff=Math.abs(timePortionAstar-pathAstar.getPath().getMinimumTravelTime());
                    System.out.println("La différence est "+diff+" représente: "+diff*100/pathAstar.getPath().getMinimumTravelTime()+"%");
                
                    debutPortion=origin;
                    finPortion=origin;
                    for(int i=0; i<pathDij.getPath().size();i+=pathDij.getPath().size()/10){
                        debutPortion=finPortion;
                        if((i+pathDij.getPath().size()/10)>=(pathDij.getPath().size()-1)){
                            i=pathDij.getPath().size();
                            finPortion=destination;
                        }else{
                            finPortion=pathDij.getPath().getArcs().get(i+pathDij.getPath().size()/10).getDestination();
                        }
                        ShortestPathData dataPotrtion = new ShortestPathData(graph, debutPortion, finPortion, inspector);
                        ShortestPathSolution pathPorionDij=new DijkstraAlgorithm(dataPotrtion).run();
                        if(debutPortion.getId() ==finPortion.getId()){
                            System.out.println("srs"+debutPortion.getId()+" dest "+finPortion.getId());
                        }
                        else{
                            timePortionDij+=pathPorionDij.getPath().getMinimumTravelTime();
                        }
                    }
                    System.out.println("Le temps en :\n"+RED+"Dij est:\t"+pathDij.getPath().getMinimumTravelTime());
                    System.out.println("Dij portion est:"+timePortionDij+RESET);
                    diff=Math.abs(timePortionAstar-pathDij.getPath().getMinimumTravelTime());
                    System.out.println("La différence est "+diff+" représente: "+diff*100/pathDij.getPath().getMinimumTravelTime()+"%");
                }
                
            }
        }
        
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
            System.out.println(GREEN+"origine: "+ origin.getId());
            System.out.println("destination: "+ destination.getId()+RESET);
            for (ArcInspector inspector: ArcInspectorFactory.getAllFilters()){
                ShortestPathData data = new ShortestPathData(graph, origin, destination, inspector);
                DijkstraAlgorithm djikstra = new DijkstraAlgorithm(data);
                BellmanFordAlgorithm bellman= new BellmanFordAlgorithm(data);
                AStarAlgorithm aStar = new AStarAlgorithm(data);

                ShortestPathSolution pathDij = djikstra.run();
                ShortestPathSolution pathBel = bellman.run();
                ShortestPathSolution pathAstar = aStar.run();


                
                
                System.out.println("arc incpector est : "+BLUE+inspector.toString()+RESET);

                if (pathBel.getPath()==null && pathDij.getPath()==null && pathAstar.getPath()==null){
                    System.out.println("pas de chemin pour les trois algos");   
                } else {
                    if (pathBel.getPath()==null || pathDij.getPath()==null || pathAstar.getPath()==null){
                        System.out.println("pas de chemin pour au moins un algo"+RED+" ALERTE"+RESET);   
                    }
                
                if (inspector.getMode()==Mode.LENGTH){
                    if(Math.abs(pathBel.getPath().getLength()-pathDij.getPath().getLength())<0.001 && Math.abs(pathAstar.getPath().getLength()-pathDij.getPath().getLength())<0.001){
                        System.out.println("Même longueur");         
                    }
                    else{
                        System.out.println(RED+"Les coûts sont différents:"+RESET+ "dij:"+pathDij.getPath().getLength()+" \nalors que bel: "+pathBel.getPath().getLength()+ " \net cout AStar est"+ pathAstar.getPath().getLength());
                        nbEchecCouts++;
                    }
                }
                else {
                        
                        if (Math.abs(pathAstar.getPath().getMinimumTravelTime()-pathDij.getPath().getMinimumTravelTime())<0.01 && Math.abs(pathAstar.getPath().getMinimumTravelTime()-pathBel.getPath().getMinimumTravelTime())<0.001){
                            System.out.println("même durée pour les 3 algos");
                        }
                        else{
                            System.out.println(RED+"durées différentes:"+RESET+ "dij="+pathDij.getPath().getMinimumTravelTime()+"\nbel:"+pathBel.getPath().getMinimumTravelTime()+"\nAstar:"+pathAstar.getPath().getMinimumTravelTime());
                            nbEchecDurées++;
                        }
                    }
                }
            }
            
        }
        System.out.println(RED+ "nombre Echecs couts: "+nbEchecCouts+RESET);
        System.out.println(RED+ "nombre Echecs durees: "+nbEchecDurées+RESET);
    }

    static Node getRandomNode(Graph graph) {
        return graph.getNodes().get(new Random().nextInt(graph.size()));
    }
}
