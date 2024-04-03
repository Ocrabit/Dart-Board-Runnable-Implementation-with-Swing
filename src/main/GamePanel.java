package main;

import UIComponents.*;
import Variables.MarkShot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;

public class GamePanel extends JFrame implements Runnable{
    GridBagLayout gbl = new GridBagLayout();
    ForceBar forceBar = new ForceBar();
    WindForce wf = new WindForce();

    final int FPS = 60;
    MarkShot[] markShotsP1 = {new MarkShot(), new MarkShot(), new MarkShot()};
    MarkShot[] markShotsP2 = {new MarkShot(), new MarkShot(), new MarkShot()};
    int shotCounter = 0;
    float dropFactor = 0.7f;
    boolean player1Turn = true;

    KeyHandler keyHandler = new KeyHandler();
    AimDart aimDart = new AimDart(keyHandler);
    DartBoard dartBoard = new DartBoard(aimDart, markShotsP1, markShotsP2);
    Thread gameThread;

    JPanel shootingArrow = new JPanel();

    JLabel windLabel;

    public GamePanel(){
        setLayout(gbl);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(keyHandler);
        getLayeredPane().setLayout(new GridLayout(1,1));
        addKeyListener(new KeyAdapter() {
           @Override
           public void keyPressed(KeyEvent e) {
               super.keyPressed(e);
               if(e.getKeyCode() == KeyEvent.VK_SPACE){
                   if(gameThread != null) {
                       forceBar.spaceBarPressed = true;
                       shotCounter++;
                       markShot(aimDart.x, aimDart.y, shotCounter);

                       try {
                           Thread.sleep(300);
                       } catch (InterruptedException ex) {
                           throw new RuntimeException(ex);
                       }

                       wf.generateWF();
                       windLabel.setText(String.format("%.2f", wf.getWindForce()) + " wf");
                       forceBar.spaceBarPressed = false;

                       //System.out.println(shotCounter);
                       if(shotCounter >= 3){
                           if(player1Turn) {
                               JOptionPane.showMessageDialog(GamePanel.this, "Player 2s Turn");
                               player1Turn = false;
                               shotCounter = 0;
                               aimDart.x = 0;
                               aimDart.y = 0;
                           } else {
                               int choice = JOptionPane.showConfirmDialog(GamePanel.this,"Would you like to play another round?", "", JOptionPane.YES_NO_OPTION);
                               if(choice==JOptionPane.YES_OPTION){
                                   saveToFile();
                                   for (int i = 0; i < markShotsP1.length; i++) {
                                       markShotsP1[i].empty();
                                   }
                                   for (int i = 0; i < markShotsP2.length; i++) {
                                       markShotsP2[i].empty();
                                   }
                                   shotCounter = 0;
                                   player1Turn = true;
                               } else {
                                   saveToFile();
                                   GamePanel.this.dispose();
                               }
                           }
                       }

                   }
               }
           }
        });


        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.25;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.gridx = 2;
        c.gridy = 1;
        add(forceBar, c);

        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.25;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.gridx = 2;
        c.gridy = 4;
        JLabel forceLabel = new JLabel("Force");
        add(forceLabel, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 5;
        c.gridx = 1;
        c.gridy = 0;
        add(dartBoard, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTH;
        c.weightx = 0.25;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        windLabel = new JLabel(String.format("%.2f", wf.getWindForce()) + " wf");
        windLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        windLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(windLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 0.25;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        add(wf.getDirection(), c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.25;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 4;
        JButton historyButton = new JButton("History");
        historyButton.setFocusable(false);
        add(historyButton, c);
        historyButton.addActionListener(e -> {
            displayHistory();
        });

        setVisible(true);
        pack();
    }

    public void startGameThread(){
        int decideStart = JOptionPane.showConfirmDialog(GamePanel.this,"Start Game?", "", JOptionPane.YES_NO_OPTION);
        if(decideStart == 0) {
            JOptionPane.showMessageDialog(GamePanel.this,"Press space bar to shoot arrow. WASD controls aim.");
            JOptionPane.showMessageDialog(GamePanel.this,"Player 1s Turn");
            gameThread = new Thread(this);
            gameThread.start();
        }
        else {
            dispose();
        }
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000/FPS;  // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                //System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    private void update() {
        forceBar.updateProgressbar();
        aimDart.update();
        dartBoard.repaint();
    }


    private int[] calculateShot(int x, int y){
        int[] point = new int[2];
        if(wf.getDirectionDir() == 0){          //UP
            point[0] = x;
            point[1] = (int)((dropFactor+forceBar.getForce())*(y - (int) (((wf.getWindForce()/5f)*0.5)*75)));
            //System.out.println(y - (int) (((wf.getWindForce()/5f)*0.5)*75));
        } else if(wf.getDirectionDir() == 1){   //LEFT
            point[0] = x - (int)(((wf.getWindForce()/5f)*0.5)*75);
            point[1] = (int)((dropFactor+forceBar.getForce())*(y));
        } else if (wf.getDirectionDir() == 2) { //DOWN
            point[0] = x;
            point[1] = (int)((dropFactor+forceBar.getForce())*(y + (int)(((wf.getWindForce()/5f)*0.5)*75)));
        } else {                                //RIGHT
            point[0] = x + (int) (((wf.getWindForce()/5f)*0.5)*75);
            point[1] = (int)((dropFactor+forceBar.getForce())*(y));
        }

        return point;
    }

    public void markShot(int x, int y, int numShot){
        int[] point;
        point = calculateShot(x,y);
        if(player1Turn) {
            markShotsP1[numShot - 1].setPoint(point[0], point[1]);
        } else {
            markShotsP2[numShot - 1].setPoint(point[0], point[1]);
        }
    }

    public void saveToFile(){
        try{
            FileWriter fw = new FileWriter("storeGames.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            out.println();
            //Convert pixels of image where x and y are to distance
            for (int i = 0; i < markShotsP1.length; i++) {
                double distX = (double) (30 * markShotsP1[i].x / 150) - 15;
                double distY = (double) (30 * markShotsP1[i].y / 150) - 15;

                out.print(distX + " " + distY + " ");
            }

            for (int i = 0; i < markShotsP2.length; i++) {
                double distX = (double) (30 * markShotsP2[i].x / 150) - 15;
                double distY = (double) (30 * markShotsP2[i].y / 150) - 15;

                out.print(distX + " " + distY + " ");
            }
            out.close();
        } catch (IOException ignored) {
        }
    }

    public void displayHistory(){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 20;
        c.ipadx = 5;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        int ySpot = 0;

        try{
            Scanner scan = new Scanner(new File("storeGames.txt"));
            while(scan.hasNextLine()) {
                int roundScoreP1 = 0;
                int roundScoreP2 = 0;
                //Determine score sum for p1
                for (int i = 0; i < 3; i++) {
                    double[] point = new double[2];
                    point[0] = scan.nextDouble();
                    point[1] = scan.nextDouble();
                    roundScoreP1 += score(point);
                }

                //Determine score sum for p2
                for (int i = 0; i < 3; i++) {
                    double[] point = new double[2];
                    point[0] = scan.nextDouble();
                    point[1] = scan.nextDouble();
                    roundScoreP2 += score(point);
                }

                c.gridy = ySpot;
                JLabel roundLabel = new JLabel("Round " + (ySpot + 1) + ": \tP1: " + roundScoreP1 + "\t\tP2: " + roundScoreP2);
                roundLabel.setHorizontalAlignment(SwingConstants.LEFT);
                panel.add(roundLabel, c);
                ySpot++;
                scan.nextLine();
            }

        }  catch (Exception e) {
            throw new RuntimeException(e);
        }

        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.add(panel);
        f.setVisible(true);
        f.pack();

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                f.dispose();
            }
        });
    }

    //Why put in x and y
    public int score(double[] point) {
        double magnitude = Math.sqrt(Math.pow(point[0],2) + Math.pow(point[1],2));
        if(magnitude <= 3) {
            //System.out.println("Bullseye");
            return 100;
        } else if(magnitude <= 6){
            //System.out.println("1st Ring");
            return 80;
        } else if(magnitude <= 9) {
            //System.out.println("2nd Ring");
            return 60;
        } else if(magnitude <= 12) {
            //System.out.println("3rd Ring");
            return 40;
        } else if(magnitude <= 15) {
            //System.out.println("4th Ring");
            return 20;
        } else {
            //System.out.println("Missed Entirely");
            return 0;
        }

    }
    public static void main(String[] args) {
        GamePanel gp = new GamePanel();
        gp.startGameThread();
    }
}
