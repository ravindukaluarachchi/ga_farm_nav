/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatest1;

import com.sun.javafx.css.FontFace;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author ravindu
 */
public class GAtest2 extends Application {

    final double CROSSOVER_RATE = 0.5d;
    final int CHROMOSOME_LENGTH = 500;
    final int POPULATION_SIZE = 12;
    final int MUTATION_PERCENTAGE = 10;
    final int NO_OF_ROUNDS = 50;

    final int INIT_X = 100;
    final int INIT_Y = 700;
    final int SLEEP = 20;

    Image craftImage;

    GraphicsContext gc;

    Image appleImage;
    Image treeImage;
    Image stoneImage;
    Image factoryImage;
    Image tileImage;

    double currentX = 200;
    double currentY = 400;
    double movementDelta = 20;
    Integer completion = 0;
    boolean end = false;
    int round = 0;
    List<int[]> genes = new ArrayList<int[]>();

    List<Individual> inidividuals = new ArrayList<>();
    List<Block> blocks = new ArrayList<>();
    Individual solution;
    final Goal goal = new Goal(330, 130);
    final Goal endGoal = new Goal(800, 550);

    List<Thread> movementThreads = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Canvas canvas = new Canvas(1024, 768);

        gc = canvas.getGraphicsContext2D();
        File imageFile = new File("robot3.png");
        File appleFile = new File("apple.png");
        File treeFile = new File("tree1.png");
        File stoneFile = new File("Coal-rock.png");
        File factoryFile = new File("center.png");
        File tileFile = new File("tile1.png");

        appleImage = new Image(new FileInputStream(appleFile));
        treeImage = new Image(new FileInputStream(treeFile));
        stoneImage = new Image(new FileInputStream(stoneFile));
        factoryImage = new Image(new FileInputStream(factoryFile));
        tileImage = new Image(new FileInputStream(tileFile));

        for (int i = 0; i < 10; i++) {
            if (i > 3 && i < 6) {
                continue;
            }
            blocks.add(new Block(50 + 50 * (i + 1), 250));

        }
        for (int i = 0; i < 10; i++) {
            if (i < 2 || i > 6) {
                continue;
            }
            blocks.add(new Block(50 + 50 * (i + 1), 400));

        }

        for (int i = 0; i < 14; i++) {
            if (i > 3 && i < 6) {
                continue;
            }
            blocks.add(new Block(300 + 50 * (i + 1), 400));

        }
        
        for (int i = 0; i < 7; i++) {
             if (i > 1 && i < 4) {
                continue;
            }
            blocks.add(new Block(700, 400 + 50 * (i + 1)));

        }

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println();
        // draw();
        craftImage = new Image(new FileInputStream(imageFile));
        /*for (int i = 0; i < POPULATION_SIZE; i++) {
            //genes.add(;
            inidividuals.add(new Individual(i * INIT_X, INIT_Y, getRandomlyInitializedChromosome()));
        }*/
        String s = "5 3 6 2 1 5 5 6 4 6 5 5 4 5 6 6 4 3 1 3 5 4 6 4 1 6 5 3 1 3 2 6 1 2 1 5 6 6 1 3 1 1 5 1 1 1 1 1 4 6 5 5 6 3 1 6 1 1 6 3 3 2 6 2 4 5 4 4 6 5 2 4 6 6 4 1 1 3 4 4 6 6 4 5 5 2 6 4 3 6 5 2 5 4 6 6 1 1 1 4 4 5 5 4 2 3 5 3 4 5 6 1 6 1 4 3 5 1 1 6 1 1 5 3 2 6 5 2 1 5 4 1 3 2 5 1 3 3 3 4 2 6 3 1 4 2 4 3 5 2 6 1 6 2 1 6 1 3 6 6 6 6 1 1 4 3 2 2 3 3 3 4 5 1 2 2 3 3 6 1 6 5 1 2 6 5 1 2 4 1 1 4 6 1 5 1 1 1 4 3 2 3 2 4 1 1 6 1 1 2 1 4 4 1 1 2 3 3 5 4 3 6 4 2 1 5 6 1 6 5 3 1 4 4 5 6 6 6 2 5 4 4 1 1 5 4 2 3 4 4 1 6 6 1 6 5 3 2 2 5 3 2 4 4 3 5 3 3 4 1 6 4 4 1 6 4 1 3 6 6 4 2 3 1 5 4 2 2 5 6 4 5 1 5 4 5 1 4 4 4 1 6 6 2 1 3 6 1 4 4 4 5 5 4 3 1 1 5 6 3 3 1 3 2 1 5 2 5 2 4 6 2 3 3 2 3 6 6 2 4 5 5 1 2 4 6 5 2 6 4 2 2 4 1 1 4 5 4 5 4 2 1 1 4 3 1 6 1 4 4 4 2 3 1 4 4 3 6 1 2 1 2 4 5 6 5 1 4 3 5 1 4 4 4 5 6 2 1 5 2 1 6 1 2 3 2 4 6 2 1 2 2 4 1 5 3 6 2 4 2 2 5 2 4 6 5 2 3 6 5 2 3 4 2 6 5 2 5 2 5 6 4 1 5 6 1 5 2 3 1 4 2 4 3 2 5 2 4 4 2 4 5 1 6 1 1 4 4 2 4 4 3 4 6 6 3 3 6 2 6 5 4 2 1 5 1 1 5 1 3 5 2 5 4 6 2 2 1 5 4";
        String[] ss =s.split(" ");
        int[] genes = new int[CHROMOSOME_LENGTH];
        for (int i = 0; i < ss.length; i++) {
            genes[i] = new Integer(ss[i]);
        }
        Individual i = new Individual( 0, INIT_Y, genes);
        inidividuals.add(i);
        solution = i;
        
        start();
    }

    private void start() {
        for (int i = 0; i < this.inidividuals.size(); i++) {
            inidividuals.get(i).x = INIT_X;
            inidividuals.get(i).y = INIT_Y;
            inidividuals.get(i).payload.clear();
        }
        completion = 0;
        round++;
        if (round == NO_OF_ROUNDS) {
            System.exit(0);
        }
        System.out.println(round + " ==========================");
        for (Individual inidividual : inidividuals) {
            System.out.println(inidividual);
        }
        for (Individual i : inidividuals) {
            new Thread(() -> {
                move(i);
            }).start();
        }
    }

    private int[] getRandomlyInitializedChromosome() {
        int[] chromosome = new int[CHROMOSOME_LENGTH];
        Random random = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = random.nextInt(6) + 1;
            //  System.out.print(chromosome[i] + " ");
        }
        //System.out.println("");
        return chromosome;
    }

    private void move(Individual individual) {
        int[] gene = individual.genes;
        if (solution != null && individual != solution) {
            System.out.println(solution);
            return;
        }
        Thread t = new Thread(() -> {
            for (int i : gene) {
                int originalX = individual.x;
                int originalY = individual.y;
                switch (i) {
                    case 1:
                        individual.y -= movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 2:
                        individual.y += movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 3:
                        individual.x -= movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 4:
                        individual.x += movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 5:
                        if (individual.pick(new Goal[]{goal})) {
                            System.out.println("************************************found goal*******************");
                            //  System.exit(0);
                        }
                        break;
                    case 6:
                        if (individual.payload.size() > 0 && individual.drop(endGoal)) {
                            System.out.println("************************************drop complete*******************");
                            //System.exit(0);
                            if (solution == null) {                                
                                solution = individual;
                                individual.x = INIT_X;
                                individual.y = INIT_Y;
                                move(individual);
                            }else{
                                return;
                            }

                        }
                        break;
                }
                Platform.runLater(() -> {
                    draw(individual);
                });
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GAtest2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            synchronized (goal) {
                completion++;
                //  System.out.println("completion" + completion);
                if (completion / inidividuals.size() == 1) {
                    calculateFitness();
                }
            }
        });
        t.start();
    }

    private void draw(Individual ind) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                gc.drawImage(tileImage, 256 * (j), 256 * i);
            }
        }

        for (int i = 0; i < 5; i++) {
            gc.drawImage(treeImage, 100 * (i + 1), 80, 100, 100);
        }

        /*for (int i = 0; i < 10; i++) {
            gc.drawImage(stoneImage, 50 + 50 * (i + 1), 300, 50, 50);
        }

        for (int i = 0; i < 10; i++) {
            gc.drawImage(stoneImage, 700, 50 + 50 * (i + 1), 50, 50);
         }*/
        for (Block block : blocks) {
            gc.drawImage(stoneImage, block.x, block.y, block.w, block.h);
        }

        gc.drawImage(appleImage, goal.x, goal.y, 20, 20);
        gc.drawImage(factoryImage, endGoal.x, endGoal.y, 160, 100);
        if (solution == null) {

            for (Individual inidividual : inidividuals) {
                gc.drawImage(craftImage, inidividual.x, inidividual.y,inidividual.w, inidividual.h);
            }

            //draw status
            double consoleX = gc.getCanvas().getWidth() - 250;
            double consoleY = 0;
            double consoleXText = consoleX + 10;
            double consoleYText = consoleY + 50;

            gc.setFill(Color.BLACK);
            gc.fillRect(consoleX, consoleY, 250, 300);
            gc.setStroke(Color.GREEN);
            gc.strokeText("[Console] round : " + round, consoleXText, consoleYText);
            for (int i = 0; i < inidividuals.size(); i++) {
                //  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>" );
                //System.out.println( inidividuals.get(i).fitness);
                String s = String.format("%d | %.8f  %d", i + 1, inidividuals.get(i).fitness, inidividuals.get(i).payload.size());
                // gc.strokeText((i + 1) + " | " + new DecimalFormat("#.0000").format(inidividuals.get(i).fitness) + "  " + inidividuals.get(i).payload.size(), consoleXText, consoleYText + (20 * (i + 1)));
                gc.strokeText(s, consoleXText, consoleYText + (20 * (i + 1)));
            }

        } else {
            gc.drawImage(craftImage, ind.x, ind.y, 50, 50);
        }
    }

    private void calculateFitness() {
        for (Individual inidividual : inidividuals) {
            if (inidividual.payload.size() == 0) {
                inidividual.fitness = Math.sqrt(Math.pow(goal.x - inidividual.x, 2) + Math.pow(goal.y - inidividual.y, 2));
                inidividual.fitness = 1 / inidividual.fitness;
            } else {
                inidividual.fitness = Math.sqrt(Math.pow(endGoal.x - inidividual.x, 2) + Math.pow(endGoal.y - inidividual.y, 2));
                inidividual.fitness = 1 / inidividual.fitness;
                inidividual.fitness += inidividual.payload.size();
            }
            System.out.println(inidividual.fitness);
        }
       // crossOver();
    }

    private void crossOver() {
        List<Individual> unSelectedParents = new ArrayList<>();
        List<Individual> newIndividuals = new ArrayList<>();
        unSelectedParents.addAll(inidividuals);
        int crossOverAmount = new Double(inidividuals.size() * CROSSOVER_RATE).intValue() / 2;
        Random random = new Random();
        for (int i = 0; i < crossOverAmount; i++) {
            System.out.println("un selected parents size : " + unSelectedParents.size());
            System.out.println("cross over i : " + i + " start ---");
            int parent1Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 1 index " + parent1Index);
            int parent2Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 2 index " + parent2Index);
            while (parent1Index == parent2Index) {
                parent2Index = random.nextInt(unSelectedParents.size());
                System.out.println("@@parent 2 index " + parent2Index);
            }

            int parent3Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 3 index " + parent3Index);
            while (parent1Index == parent3Index
                    || parent2Index == parent3Index) {
                parent3Index = random.nextInt(unSelectedParents.size());
                System.out.println("@p@arent 3 index " + parent3Index);
            }

            int parent4Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 4 index " + parent4Index);
            while (parent1Index == parent4Index
                    || parent2Index == parent4Index
                    || parent3Index == parent4Index) {
                parent4Index = random.nextInt(unSelectedParents.size());
                System.out.println("@@parent 4 index " + parent4Index);
            }

            Individual candidateParent1 = unSelectedParents.get(parent1Index);
            Individual candidateParent2 = unSelectedParents.get(parent2Index);
            Individual candidateParent3 = unSelectedParents.get(parent3Index);
            Individual candidateParent4 = unSelectedParents.get(parent4Index);

            List<Individual> candidateList = new ArrayList<>();
            candidateList.add(candidateParent1);
            candidateList.add(candidateParent2);
            candidateList.add(candidateParent3);
            candidateList.add(candidateParent4);

            Individual parent1 = null;
            Individual parent2 = null;
            for (Individual individual : candidateList) {
                if (parent1 == null) {
                    parent1 = individual;
                } else if (parent1.fitness < individual.fitness) {
                    parent1 = individual;
                }
            }
            candidateList.remove(parent1);

            for (Individual individual : candidateList) {
                if (parent2 == null) {
                    parent2 = individual;
                } else if (parent2.fitness < individual.fitness) {
                    parent2 = individual;
                }
            }

            System.out.println("parent1 >> " + parent1);
            System.out.println("parent2 >> " + parent2);

            List<Individual> removeList = new ArrayList<>();
            removeList.add(candidateParent1);
            removeList.add(candidateParent2);
            removeList.add(candidateParent3);
            removeList.add(candidateParent4);

            unSelectedParents.removeAll(removeList);
            System.out.println("1");
            //2point cross over
            int point1 = random.nextInt(CHROMOSOME_LENGTH - 1);
            int point2;
            do {
                point2 = random.nextInt(CHROMOSOME_LENGTH);
                System.out.println("2 >> " + point2 + " >= " + point1);
            } while (point2 < point1);
            System.out.println("3");
            //child1
            int[] newChromosome = new int[CHROMOSOME_LENGTH];
            for (int j = 0; j < point1; j++) {
                newChromosome[j] = parent1.genes[j];
            }
            System.out.println("4");
            for (int j = point1; j < point2; j++) {
                newChromosome[j] = parent2.genes[j];
            }
            System.out.println("5");
            for (int j = point2; j < newChromosome.length; j++) {
                newChromosome[j] = parent1.genes[j];
            }
            System.out.println("6");
            System.out.println("7");
            Individual child1 = new Individual(0, 0, newChromosome);
            newIndividuals.add(child1);
            //child2
            newChromosome = new int[CHROMOSOME_LENGTH];
            for (int j = 0; j < point1; j++) {
                newChromosome[j] = parent2.genes[j];
            }
            System.out.println("8");
            for (int j = point1; j < point2; j++) {
                newChromosome[j] = parent1.genes[j];
            }
            System.out.println("9");
            for (int j = point2; j < newChromosome.length; j++) {
                newChromosome[j] = parent2.genes[j];
            }
            System.out.println("10");
            Individual child2 = new Individual(0, 0, newChromosome);
            newIndividuals.add(child2);
            System.out.println("cross over i : " + i + " end ---");
        }
        System.out.println("new individual size " + newIndividuals.size());
        for (Individual newIndividual : newIndividuals) {
            System.out.println(newIndividual);
        }
        List<Individual> newPopulation = new ArrayList<>();
        newPopulation.addAll(newIndividuals);

        Individual mostFitted = null;
        for (Individual inidividual : inidividuals) {
            if (mostFitted == null) {
                mostFitted = inidividual;
            }
            if (mostFitted.fitness < inidividual.fitness) {
                mostFitted = inidividual;
            }
        }
        newPopulation.add(mostFitted);
        inidividuals.remove(mostFitted);

        int remainingPopulationSize = POPULATION_SIZE - newIndividuals.size();
        Collections.shuffle(inidividuals);
        for (int i = 0; i < remainingPopulationSize; i++) {
            inidividuals.get(i).x = 0;
            inidividuals.get(i).y = 0;
            //inidividuals.get(i).fitness = 0;
            newPopulation.add(inidividuals.get(i));
        }
        inidividuals = newPopulation;
        mutate(mostFitted);
    }

    private void mutate(Individual mostFitted) {
        int mutationCount = CHROMOSOME_LENGTH * MUTATION_PERCENTAGE / 100;
        Random random = new Random();
        Collections.shuffle(inidividuals);
        for (int j = 0; j < inidividuals.size() / 2; j++) {
            Individual individual = inidividuals.get(j);
            if (individual == mostFitted) {
                continue;
            }
            for (int i = 0; i < mutationCount; i++) {
                int index = random.nextInt(CHROMOSOME_LENGTH);
                individual.genes[index] = random.nextInt(6) + 1;
            }
        }
        start();
    }

    private void runSolution() {
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    private boolean validateMove(Individual individual) {
        for (Block block : blocks) {
            if ((individual.x >= block.x && individual.x <= block.x + block.w && individual.y >= block.y && individual.y <= block.y + block.h)
                    || (individual.x  + individual.w>= block.x && individual.x + individual.w <= block.x + block.w && individual.y + individual.h>= block.y && individual.y + individual.h <= block.y + block.h)){
                return false;
}
        }
        return true;
    }
}
