/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Start;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Start extends JApplet implements ActionListener, KeyListener, MouseListener, MouseMotionListener, Runnable {

    Graphics2D myPic;
    Image dbImage, master, pic;
    private Graphics dbg;
    Timer timer;
    int[][] grid = new int[27][27];
    int[][] map = new int[27][27];
    int[][] buy = new int[5][5];
    int[] cost = new int[24];
    int[] wx = new int[10];
    int[] wy = new int[10];
    int[] res = new int[13];
    int[] type = new int[60];
    boolean[][] vis = new boolean[27][27];
    int[][] build = new int[5][2];
    int[][][] pRes = new int[27][27][3];
    int mX, mY, lX, lY, mapCurrent = 21, mode = 0, time = 0, closest, p1, p2, sec, upgrades, save, go = 0, bt, wi = 1;
    boolean press = false, pause = true, placed = false, dis = false;

    public Start() {
        timer = new Timer(60, this);
        timer.setInitialDelay(100);     //starts timer
        timer.start();
        Timer timer;
        timer = new Timer(2000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pause == false) {
                    time++;

                    for (int i = 0; i < 10; i++) {
                        if (wx[i] < p2 * 31) {
                            wx[i] += 31;
                        }

                        if (wy[i] < p1 * 31) {
                            wy[i] += 31;
                        }

                        if (wx[i] > p2 * 31) {
                            wx[i] -= 31;
                        }

                        if (wy[i] > p1 * 31) {
                            wy[i] -= 31;
                        }

                        if (wx[i] == p2 * 31 && wy[i] == p1 * 31) {
                            sec += 1;
                            if (grid[p1][p2] < 46 && sec < 30) {
                                grid[p1][p2] -= 20;
                                grid[p1 - 1][p2] -= 20;
                                grid[p1][p2 - 1] -= 20;
                                grid[p1 - 1][p2 - 1] -= 20;
                            }
                            if (grid[p1][p2] < 23 && sec == 30) {
                                grid[p1][p2] -= 20;
                                grid[p1 - 1][p2] -= 20;
                                grid[p1][p2 - 1] -= 20;
                                grid[p1 - 1][p2 - 1] -= 20;

                            }
                        }
                    }
                }

            }
        });
        timer.setRepeats(true); // Only execute once
        timer.start(); // Go go go!
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        /**
         * @param args the command line arguments
         */

        try {//COST
            FileReader fr = new FileReader("cost.txt");
            BufferedReader br = new BufferedReader(fr);
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    buy[x][y] = Integer.parseInt(br.readLine());
                }
            }
            for (int i = 0; i < 24; i++) {
                cost[i] = Integer.parseInt(br.readLine());
            }

        } catch (IOException a) {
            System.out.println("Couldn't Load");
        }
        try {//MAP
            FileReader fr = new FileReader("map.txt");
            BufferedReader br = new BufferedReader(fr);
            for (int x = 0; x < 27; x++) {
                for (int y = 0; y < 27; y++) {
                    map[x][y] = Integer.parseInt(br.readLine());
                }
            }

        } catch (IOException a) {
            System.out.println("Couldn't Load");
        }

        for (int r = 0; r < 27; r++) {//RESOURCES
            for (int c = 0; c < 27; c++) {
                for (int l = 0; l < 3; l++) {
                    //DO PERCENAGES
                    int rnd = (int) Math.ceil(Math.random() * 11);
                    if (map[r][c] == 0) {
                        //MOST
                        pRes[r][c][l] = rnd;
                    } else if (map[r][c] == 1) {
                        //WOOD
                        pRes[r][c][l] = 1;
                    } else if (map[r][c] == 2) {
                        //STONE
                        pRes[r][c][l] = 2;
                    } else if (map[r][c] == 3) {
                        //MOST
                        pRes[r][c][l] = rnd;
                    } else if (map[r][c] == 4) {
                        //SOME
                        pRes[r][c][l] = rnd;
                    } else if (map[r][c] == 5) {
                        //SOME
                        pRes[r][c][l] = rnd;

                    }
                }
            }
        }

        try {//SAVE
            FileReader fr = new FileReader("save.txt");
            BufferedReader br = new BufferedReader(fr);
            for (int z = 0; z < 12; z++) {
                res[z] = Integer.parseInt(br.readLine());
            }
            for (int z = 0; z < 23; z++) {
                type[z] = Integer.parseInt(br.readLine());
            }
            upgrades = Integer.parseInt(br.readLine());
            time = Integer.parseInt(br.readLine());

        } catch (IOException a) {
            System.out.println("Couldn't Load");
        }
    }

    public void run() {

    }

    public static void main(String[] args) {
        JFrame f = new JFrame("");
        JApplet applet = new Start();        //sets up the window
        f.getContentPane().add("Center", applet);
        applet.init();
        f.setType(Window.Type.UTILITY);
        f.pack();
        f.setLocation(400, -6);
        f.setVisible(true);
        //f.setResizable(false);
        f.setSize(910, 862);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //stops program if you x out the window    
    }

    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());      //creats and image the size of the screen
        dbg = dbImage.getGraphics();        //double buffers the panel
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);

    }

    public void paintComponent(Graphics g) {
        myPic = (Graphics2D) g;
        myPic.drawRect(0, 0, getWidth(), getHeight());

        if (mode == 0) {//MAIN MENU
            myPic.setFont(new Font("Dialog", Font.PLAIN, 30));
            myPic.setColor(new Color(0, 0, 0));
            myPic.drawString("Play", getWidth() / 2 - 29, 115);
            myPic.drawString("Continue", getWidth() / 2 - 60, 215);
            myPic.drawString("Make Map", getWidth() / 2 - 69, 315);
            myPic.drawString("Exit", getWidth() / 2 - 25, 415);

            myPic.drawRect(getWidth() / 2 - 31, 90, 62, 33);
            myPic.drawRect(getWidth() / 2 - 62, 189, 124, 30);
            myPic.drawRect(getWidth() / 2 - 71, 289, 142, 33);
            myPic.drawRect(getWidth() / 2 - 27, 389, 54, 30);
        } else if (mode == 1 || mode == 4 || mode == 2 || mode == 6 || mode == 7) {//GAME
            for (int x = 0; x < 27; x++) {
                for (int y = 0; y < 27; y++) {
                    myPic.setColor(Color.pink);
                    myPic.fillRect(mX - 3, mY, 15, 15);
                    if (vis[x][y] == true) {
                        myPic.setColor(Color.PINK);
                        myPic.fillRect(x * 31 + 1, y * 31 + 1, 31, 31);
                    }

                    myPic.setColor(Color.GREEN);
                    myPic.fillRect(x * 31 + 1, y * 31 + 1, 31, 31);

                    Image pic = null;
                    if (map[y][x] == 0) {
                        myPic.setColor(new Color(0, 153, 0));
                    } else if (map[y][x] == 1) {
                        pic = new ImageIcon("src\\Start\\pine.png").getImage();
                    } else if (map[y][x] == 2) {
                        myPic.setColor(Color.LIGHT_GRAY);
                    } else if (map[y][x] == 3) {
                        myPic.setColor(Color.DARK_GRAY);
                    } else if (map[y][x] == 4) {
                        myPic.setColor(new Color(0, 0, 153));
                    } else if (map[y][x] == 5) {
                        myPic.setColor(new Color(255, 255, 153));
                    }

                    /*if (grid[y][x] == 1) {
                     myPic.setColor(Color.red);
                     } else if (grid[y][x] == 2) {
                     myPic.setColor(Color.yellow);
                     } else if (grid[y][x] == 3) {
                     myPic.setColor(Color.lightGray);
                     } else if (grid[y][x] == 4) {
                     myPic.setColor(Color.gray);
                     } else */ if (grid[y][x] == 5) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 6) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 7) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 8) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 9) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 10) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 11) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 12) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 13) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 14) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 15) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 16) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 17) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 18) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 19) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 20) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 21) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 22) {
                        myPic.setColor(Color.orange);
                    } else if (grid[y][x] == 23) {
                        myPic.setColor(Color.orange);
                    } else if ((grid[y][x] < 46) && (grid[y][x] > 23)) {
                        myPic.setColor(Color.cyan);

                    }

                    //Squares
                    myPic.fillRect(x * 31 + 1, y * 31 + 1, 31, 31);

                    //Worker
                    for (int i = 0; i < 10; i++) {
                        myPic.setColor(Color.red);
                        myPic.fillRect(wx[i], wy[i], 31, 31);

                        myPic.setColor(Color.yellow);
                        // myPic.fillRect(wx[i], wy[i], 31, 31);

                        myPic.setColor(Color.lightGray);
                        // myPic.fillRect(wx[i], wy[i], 31, 31);

                        myPic.setColor(Color.gray);
                        // myPic.fillRect(wx[i], wy[i], 31, 31);
                    }
                    //Grid
                    myPic.setColor(new Color(25, 25, 25));

                    myPic.drawImage(pic, x * 31, y * 31, 31, 31, null);
                    myPic.drawRect(x * 31, y * 31, 31, 31);

                }
            }
            if (mode == 1) {
                myPic.drawString("Time: ", getWidth() - 52, 15);
                myPic.drawString("" + time, getWidth() - 50, 35);

                //CHECK SAVE FILES  
                //Money
                myPic.setColor(new Color(50, 205, 50));
                myPic.fillRect(840, 50, 16, 16);
                //Wood
                myPic.setColor(new Color(139, 69, 19));
                myPic.fillRect(840, 86, 16, 16);
                //Stone
                myPic.setColor(new Color(128, 128, 128));
                myPic.fillRect(840, 122, 16, 16);
                //Water
                myPic.setColor(new Color(0, 0, 255));
                myPic.fillRect(840, 158, 16, 16);
                //Copper
                myPic.setColor(new Color(217, 144, 88));
                myPic.fillRect(840, 194, 16, 16);
                //Iron
                myPic.setColor(new Color(192, 192, 192));
                myPic.fillRect(840, 230, 16, 16);
                //Aluminium
                myPic.setColor(new Color(211, 211, 211));
                myPic.fillRect(840, 266, 16, 16);
                //Oil
                myPic.setColor(new Color(0, 0, 0));
                myPic.fillRect(840, 302, 16, 16);
                //Titanium
                myPic.setColor(new Color(182, 175, 169));
                myPic.fillRect(840, 338, 16, 16);
                //Silicon
                myPic.setColor(new Color(222, 184, 135));
                myPic.fillRect(840, 374, 16, 16);
                //Gold
                myPic.setColor(new Color(218, 165, 32));
                myPic.fillRect(840, 410, 16, 16);
                //Coal
                myPic.setColor(new Color(50, 50, 50));
                myPic.fillRect(840, 446, 16, 16);
                //Uranium
                myPic.setColor(new Color(0, 255, 0));
                myPic.fillRect(840, 482, 16, 16);

                myPic.setColor(new Color(0, 0, 0));
                myPic.drawString(res[0] + "", 858, 63);
                myPic.drawString(res[1] + "", 858, 99);
                myPic.drawString(res[2] + "", 858, 135);
                myPic.drawString(res[3] + "", 858, 171);
                myPic.drawString(res[4] + "", 858, 207);
                myPic.drawString(res[5] + "", 858, 243);
                myPic.drawString(res[6] + "", 858, 279);
                myPic.drawString(res[7] + "", 858, 315);
                myPic.drawString(res[8] + "", 858, 351);
                myPic.drawString(res[9] + "", 858, 387);
                myPic.drawString(res[10] + "", 858, 423);
                myPic.drawString(res[11] + "", 858, 459);
                myPic.drawString(res[12] + "", 858, 495);
                if (upgrades < 1) {

                }

                while (placed == false) {
                    p1 = (int) Math.ceil(Math.random() * 27);
                    p2 = (int) Math.ceil(Math.random() * 27);

                    if (map[p1][p2] == 0 && map[p1 - 1][p2] == 0 && map[p1][p2 - 1] == 0 && map[p1 - 1][p2 - 1] == 0) {
                        placed = true;
                        grid[p1][p2] = 48;
                        grid[p1 - 1][p2] = 48;
                        grid[p1][p2 - 1] = 48;
                        grid[p1 - 1][p2 - 1] = 48;
                    }
                }

                if (dis == true) {
                    int num = 81, tranX = 77, tranY = 77;
                    String name = "gh";
                    for (int r = 0; r < 27; r++) {
                        for (int c = -1; c < 27; c++) {
                            Rectangle box = new Rectangle(r * 31 + 1, c * 31 + 1, 31, 31), m = new Rectangle(lX, lY, 1, 1);
                            if (m.intersects(box)) {
                                if (grid[c][r] != 0) {
                                    num = grid[c][r] + 10;
                                } else {
                                    num = map[c][r];
                                    tranX = c;
                                    tranY = r;
                                }
                            }
                        }
                        if (num == 0) {
                            name = "Plains";
                        } else if (num == 1) {
                            name = "Forest";
                        } else if (num == 2) {
                            name = "Rock";
                        } else if (num == 3) {
                            name = "Hill";
                        } else if (num == 4) {
                            name = "Water";
                        } else if (num == 5) {
                            name = "Sand";
                        } else if (num == 11) {
                            name = "Mine";
                        } else if (num == 12) {
                            name = "Quarry";
                        } else if (num == 13) {
                            name = "Forester";
                        } else if (num == 14) {
                            name = "House";
                        } else if (num == 15) {
                            name = "Sand";
                        } else if (num == 16) {
                            name = "Sand";
                        } else if (num == 17) {
                            name = "Sand";
                        } else if (num == 18) {
                            name = "Sand";
                        } else if (num == 19) {
                            name = "Sand";
                        } else if (num == 20) {
                            name = "Sand";
                        } else if (num == 21) {
                            name = "Sand";
                        } else if (num == 22) {
                            name = "Sand";
                        } else if (num == 23) {
                            name = "Sand";
                        } else if (num == 24) {
                            name = "Sand";
                        } else if (num == 25) {
                            name = "Sand";
                        } else if (num == 26) {
                            name = "Sand";
                        } else if (num == 27) {
                            name = "Sand";
                        } else if (num == 28) {
                            name = "Sand";
                        } else if (num == 29) {
                            name = "Sand";
                        } else if (num == 30) {
                            name = "Sand";
                        } else {
                            name = "F:" + num;
                        }
                    }

                    num = 64;
                    if (lX > getWidth() / 2 && lY > getHeight() / 2) {
                        myPic.setColor(Color.white);
                        myPic.fillRect(lX - 220, lY - 180, 199, 199);//DownRight
                        myPic.setColor(Color.black);
                        myPic.drawRect(lX - 221, lY - 181, 200, 200);

                        if (vis[tranX][tranY] == true) {
                            myPic.drawString("Res 1:" + pRes[tranX][tranY][0] + "  Res 2:" + pRes[tranX][tranY][1] + "  Res 3:" + pRes[tranX][tranY][2], lX - 210, lY - 140);
                        }
                        myPic.drawString("Sell", lX - 50, lY + 10);
                        myPic.drawString("Upgrade", lX - 208, lY + 10);
                        myPic.drawString(name, lX - 210, lY - 160);
                    } else if (lX < getWidth() / 2 && lY > getHeight() / 2) {//DownLeft
                        myPic.setColor(Color.white);
                        myPic.fillRect(lX + 20, lY - 180, 199, 199);
                        myPic.setColor(Color.black);
                        myPic.drawRect(lX + 19, lY - 181, 200, 200);

                        myPic.drawString(name, lX + 28, lY - 160);
                    } else if (lX > getWidth() / 2 && lY < getHeight() / 2) {//UpRight
                        myPic.setColor(Color.white);
                        myPic.fillRect(lX - 220, lY + 20, 199, 199);
                        myPic.setColor(Color.black);
                        myPic.drawRect(lX - 221, lY + 19, 200, 200);

                        myPic.drawString(name, lX - 210, lY + 41);
                    } else if (lX < getWidth() / 2 && lY < getHeight() / 2) {//UpLeft
                        myPic.setColor(Color.white);
                        myPic.fillRect(lX + 20, lY + 20, 199, 199);
                        myPic.setColor(Color.black);
                        myPic.drawRect(lX + 19, lY + 19, 200, 200);

                        myPic.drawString(name, lX + 28, lY + 41);
                    }
                }
            }
        }

        if (mode == 2) {//PAUSE MENU
            myPic.setColor(new Color(0, 0, 0));
            myPic.fillRect(getWidth() / 2 - 139, getHeight() / 2 - 364, 278, 408);
            myPic.setColor(new Color(255, 255, 255));
            myPic.fillRect(getWidth() / 2 - 135, getHeight() / 2 - 360, 270, 400);
            myPic.setFont(new Font("Dialog", Font.PLAIN, 30));
            myPic.setColor(new Color(0, 0, 0));
            myPic.drawString("Resume", getWidth() / 2 - 55, 115);
            myPic.drawString("Options", getWidth() / 2 - 51, 215);
            myPic.drawString("Save", getWidth() / 2 - 33, 315);
            myPic.drawString("Exit", getWidth() / 2 - 25, 415);

            myPic.drawRect(getWidth() / 2 - 57, 90, 113, 30);
            myPic.drawRect(getWidth() / 2 - 53, 190, 106, 33);
            myPic.drawRect(getWidth() / 2 - 35, 290, 71, 30);
            myPic.drawRect(getWidth() / 2 - 27, 390, 54, 30);

        } else if (mode == 3) {//SHOP
            //BOXES
            for (int sx = 0; sx < 5; sx++) {
                for (int sy = 0; sy < 5; sy++) {
                    myPic.drawRect(sx * 181 + 1, sy * 172 + 1, 150, 150);
                    myPic.drawLine(sx * 181 + 1, sy * 172 + 20, sx * 181 + 150, sy * 172 + 20);
                    myPic.drawLine(sx * 181 + 1, sy * 172 + 42, sx * 181 + 150, sy * 172 + 42);
                    myPic.drawLine(sx * 181 + 1, sy * 172 + 86, sx * 181 + 150, sy * 172 + 86);
                    myPic.drawString("Buy  : " + buy[sx][sy] + "$", sx * 181 + 4, sy * 172 + 37);
                    myPic.drawRect(sx * 181 + 3, sy * 172 + 22, 22, 18);
                    myPic.drawString("Produce  :", sx * 181 + 4, sy * 172 + 57);
                    myPic.drawRect(sx * 181 + 3, sy * 172 + 44, 48, 16);
                    myPic.drawString("Description  :", sx * 181 + 4, sy * 172 + 102);
                }
            }
            myPic.setColor(new Color(255, 255, 255));
            myPic.fillRect(173, 1, 344, 171);
            myPic.setFont(new Font("Dialog", Font.PLAIN, 30));
            myPic.setColor(new Color(0, 0, 0));
            myPic.drawString("Shop", getWidth() / 2 - 280, 30);

            myPic.setFont(new Font("Dialog", Font.PLAIN, 12));

            myPic.drawString("Money: " + res[0], getWidth() / 2 - 280, 60);

            for (int i1 = 0; i1 < 2; i1++) {
                for (int i2 = 0; i2 < 5; i2++) {
                    myPic.drawRect(305 + i1 * 90, 15 + i2 * 30, 10, 11);
                    myPic.drawRect(331 + i1 * 90, 15 + i2 * 30, 10, 11);
                }
            }
            myPic.drawRect(485, 15, 10, 11);
            myPic.drawRect(511, 15, 10, 11);
            myPic.drawRect(485, 45, 10, 11);
            myPic.drawRect(511, 45, 10, 11);

            myPic.drawString(cost[0] + "$ + " + res[1], 290, 25);
            myPic.drawString(cost[1] + "$ + " + res[2], 290, 55);
            myPic.drawString(cost[2] + "$ + " + res[3], 290, 85);
            myPic.drawString(cost[3] + "$ + " + res[4], 290, 115);
            myPic.drawString(cost[4] + "$ + " + res[5], 290, 145);
            myPic.drawString(cost[5] + "$ + " + res[6], 380, 25);
            myPic.drawString(cost[6] + "$ + " + res[7], 380, 55);
            myPic.drawString(cost[7] + "$ + " + res[8], 380, 85);
            myPic.drawString(cost[8] + "$ + " + res[9], 380, 115);
            myPic.drawString(cost[9] + "$ + " + res[10], 380, 145);
            myPic.drawString(cost[10] + "$ + " + res[11], 470, 25);
            myPic.drawString(cost[11] + "$ + " + res[12], 470, 55);
            myPic.drawString("-  " + cost[12] + "$", 335, 25 + 0 * 30);
            myPic.drawString("-  " + cost[13] + "$", 335, 25 + 1 * 30);
            myPic.drawString("-  " + cost[14] + "$", 335, 25 + 2 * 30);
            myPic.drawString("-  " + cost[15] + "$", 335, 25 + 3 * 30);
            myPic.drawString("-  " + cost[16] + "$", 335, 25 + 4 * 30);
            myPic.drawString("-  " + cost[17] + "$", 335 + 1 * 90, 25 + 0 * 30);
            myPic.drawString("-  " + cost[18] + "$", 335 + 1 * 90, 25 + 1 * 30);
            myPic.drawString("-  " + cost[19] + "$", 335 + 1 * 90, 25 + 2 * 30);
            myPic.drawString("-  " + cost[20] + "$", 335 + 1 * 90, 25 + 3 * 30);
            myPic.drawString("-  " + cost[21] + "$", 335 + 1 * 90, 25 + 4 * 30);
            myPic.drawString("-  " + cost[22] + "$", 515, 25);
            myPic.drawString("-  " + cost[23] + "$", 515, 55);
            //Wood
            myPic.setColor(new Color(139, 69, 19));
            myPic.fillRect(270, 13, 16, 16);
            //Stone
            myPic.setColor(new Color(128, 128, 128));
            myPic.fillRect(270, 43, 16, 16);
            //Water
            myPic.setColor(new Color(0, 0, 255));
            myPic.fillRect(270, 73, 16, 16);
            //Copper
            myPic.setColor(new Color(217, 144, 88));
            myPic.fillRect(270, 103, 16, 16);
            //Iron
            myPic.setColor(new Color(192, 192, 192));
            myPic.fillRect(270, 133, 16, 16);
            //Aluminium
            myPic.setColor(new Color(211, 211, 211));
            myPic.fillRect(360, 13, 16, 16);
            //Oil
            myPic.setColor(new Color(0, 0, 0));
            myPic.fillRect(360, 43, 16, 16);
            //Titanium
            myPic.setColor(new Color(182, 175, 169));
            myPic.fillRect(360, 73, 16, 16);
            //Silicon
            myPic.setColor(new Color(222, 184, 135));
            myPic.fillRect(360, 103, 16, 16);
            //Gold
            myPic.setColor(new Color(218, 165, 32));
            myPic.fillRect(360, 133, 16, 16);
            //Coal
            myPic.setColor(new Color(50, 50, 50));
            myPic.fillRect(450, 13, 16, 16);
            //Uranium
            myPic.setColor(new Color(0, 255, 0));
            myPic.fillRect(450, 43, 16, 16);

            myPic.setColor(new Color(50, 50, 50));
            //BOX 1
            myPic.drawLine(2, 42, 150, 42);
            myPic.drawString("Worker                         No." + (type[0] + type[30]), 5, 17);
            myPic.drawString("  + 100$ M", 60, 208);
            myPic.drawString("Can't Produce", 60, 229);
            myPic.drawString("Workers are ", 70, 102);
            myPic.drawString("used to build basic things.", 4, 122);

            myPic.drawString("Engineer                      No." + (type[1] + type[31]), 5, 189);
            myPic.drawString("  + 150$ M", 60, 37);
            myPic.drawString("Can't Produce", 60, 58);
            myPic.drawString("Engineers are ", 70, 274);
            myPic.drawString("used to operate machines.", 4, 294);

            myPic.drawString("Worker Drone             No." + (type[2] + type[32]), 5, 361);
            myPic.drawString("Worker Drones", 70, 446);
            myPic.drawString("can build everything.", 4, 466);

            myPic.drawString("Survey Drone             No." + (type[3] + type[33]), 5, 533);
            myPic.drawString("Engineers are ", 70, 618);
            myPic.drawString("used to operate machines.", 4, 638);
            myPic.drawString("Buy", 127, 663);
            myPic.drawRect(123, 650, 28, 17);

            myPic.drawString("Mine                            No." + (type[4] + type[34]), 5, 705);
            myPic.drawString("Quarry                          No." + (type[5] + type[35]), 186, 189);
            myPic.drawString("Forest                          No." + (type[6] + type[36]), 186, 361);
            myPic.drawString("House                          No." + (type[7] + type[37]), 186, 533);
            myPic.drawString("Pump                           No." + (type[8] + type[38]), 186, 705);
            myPic.drawString("Chrusher                     No." + (type[9] + type[39]), 367, 189);
            myPic.drawString("Smelter                        No." + (type[10] + type[40]), 367, 361);
            myPic.drawString("Sifter                           No." + (type[11] + type[41]), 367, 533);
            myPic.drawString("Briquets                       No." + (type[12] + type[42]), 367, 705);
            myPic.drawString("Factory                         No." + (type[13] + type[43]), 548, 17);
            myPic.drawString("School                         No." + (type[14] + type[44]), 548, 189);
            myPic.drawString("Chemical Plant           No." + (type[15] + type[45]), 548, 361);
            myPic.drawString("Research Facility       No." + (type[16] + type[46]), 548, 533);
            myPic.drawString("Solar                            No." + (type[17] + type[47]), 548, 705);
            myPic.drawString("Wind                             No." + (type[18] + type[48]), 729, 17);
            myPic.drawString("Hydro                           No." + (type[19] + type[49]), 729, 189);
            myPic.drawString("Nuclear                        No." + (type[20] + type[50]), 729, 361);
            myPic.drawString("Coal                             No." + (type[21] + type[51]), 729, 533);
            myPic.drawString("Teleporter                   No." + (type[22] + type[52]), 729, 705);

        }
        if (mode == 4) {//MAP           
            //Plains
            myPic.setColor(new Color(25, 25, 25));
            myPic.drawRect(844, 1, 31, 31);
            myPic.setColor(Color.GREEN);
            myPic.fillRect(845, 2, 30, 30);
            //Forest
            myPic.setColor(new Color(25, 25, 25));
            myPic.drawRect(844, 62, 31, 31);
            pic = new ImageIcon("src\\Start\\pine.png").getImage();
            myPic.drawImage(pic, 845, 63, 30, 30, null);
            //Rock
            myPic.setColor(new Color(25, 25, 25));
            myPic.drawRect(844, 124, 31, 31);
            myPic.setColor(Color.LIGHT_GRAY);
            myPic.fillRect(845, 125, 30, 30);
            //Hill
            myPic.setColor(new Color(25, 25, 25));
            myPic.drawRect(844, 186, 31, 31);
            myPic.setColor(Color.darkGray);
            myPic.fillRect(845, 187, 30, 30);
            //Water
            myPic.setColor(new Color(25, 25, 25));
            myPic.drawRect(844, 248, 31, 31);
            myPic.setColor(Color.blue);
            myPic.fillRect(845, 249, 30, 30);
            //Bank
            myPic.setColor(new Color(25, 25, 25));
            myPic.drawRect(844, 310, 31, 31);
            myPic.setColor(Color.yellow);
            myPic.fillRect(845, 311, 30, 30);

            myPic.setColor(Color.black);
            myPic.drawRect(843, 618, 33, 15);
            myPic.drawString("Done", 845, 630);

            myPic.drawRect(843, 518, 33, 15);
            myPic.drawString("Wipe", 845, 530);
            if (mapCurrent == 0) {
                myPic.setColor(Color.GREEN);
                myPic.fillRect(mX - 3, mY, 15, 15);
            } else if (mapCurrent == 1) {
                pic = new ImageIcon("src\\Start\\pine.png").getImage();
                myPic.drawImage(pic, mX - 3, mY, 15, 15, null);
            } else if (mapCurrent == 2) {
                myPic.setColor(Color.LIGHT_GRAY);
                myPic.fillRect(mX - 3, mY, 15, 15);
            } else if (mapCurrent == 3) {
                myPic.setColor(Color.DARK_GRAY);
                myPic.fillRect(mX - 3, mY, 15, 15);
            } else if (mapCurrent == 4) {
                myPic.setColor(Color.BLUE);
                myPic.fillRect(mX - 3, mY, 15, 15);
            } else if (mapCurrent == 5) {
                myPic.setColor(Color.YELLOW);
                myPic.fillRect(mX - 3, mY, 15, 15);
            }
        } else if (mode == 5) {//START OPTIONS
            myPic.setFont(new Font("Dialog", Font.PLAIN, 30));
            myPic.drawString("Defult Map", getWidth() / 2 - 71, 116);
            myPic.drawString("Custom Map", getWidth() / 2 - 84, 215);
            myPic.drawString("Save Slot 1", getWidth() / 2 - 73, 315);
            myPic.drawString("Save Slot 2", getWidth() / 2 - 75, 415);

            myPic.drawRect(getWidth() / 2 - 73, 90, 147, 33);
            myPic.drawRect(getWidth() / 2 - 86, 189, 173, 33);
            myPic.drawRect(getWidth() / 2 - 75, 289, 151, 33);
            myPic.drawRect(getWidth() / 2 - 77, 389, 155, 33);

        }
        if (mode == 6) {//OPTIONS
            myPic.setColor(new Color(0, 0, 0));
            myPic.fillRect(getWidth() / 2 - 250, 97, 501, 501);
            myPic.setColor(new Color(255, 255, 255));
            myPic.fillRect(getWidth() / 2 - 247, 100, 495, 495);
            myPic.setColor(new Color(0, 0, 0));
            myPic.setFont(new Font("Dialog", Font.PLAIN, 30));
            myPic.drawString("Options", getWidth() / 2 - 51, 130);
            myPic.drawRect(getWidth() / 2 - 53, 105, 106, 33);

            myPic.drawString("Shop: Tab", 225, 184);

        }
        if (mode == 7) {//Placer

        }

    }

    @Override
    public void actionPerformed(ActionEvent e
    ) {
        repaint();
        requestFocus();
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void keyTyped(KeyEvent e
    ) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e
    ) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (mode == 2) {
                mode = 1;
                pause = false;
            } else if (mode == 1) {
                mode = 2;
                pause = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
            if (mode == 3) {
                mode = 1;
                pause = false;
            } else if (mode == 1) {
                mode = 3;
                pause = true;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

    }

    @Override
    public void mouseClicked(MouseEvent e
    ) {
        if (press == false) {
            Rectangle m = new Rectangle(mX, mY, 1, 1);

            //MAIN MENU
            if (mode == 0 && press == false) {
                Rectangle m1 = new Rectangle(getWidth() / 2 - 31, 90, 62, 33);
                Rectangle m2 = new Rectangle(getWidth() / 2 - 62, 189, 124, 30);
                Rectangle m3 = new Rectangle(getWidth() / 2 - 71, 289, 142, 33);
                Rectangle m4 = new Rectangle(getWidth() / 2 - 27, 389, 54, 30);

                if (m.intersects(m1)) {
                    mode = 5;
                    pause = true;
                } else if (m.intersects(m2)) {
                    mode = 1;
                    pause = false;
                } else if (m.intersects(m3)) {
                    mode = 4;
                    pause = true;
                } else if (m.intersects(m4)) {
                    System.exit(1);
                }
                press = true;
            }
            //GAME
            if (mode == 1 && press == false) {
                if (e.isMetaDown()) {//View Settings
                    if (dis == true) {
                        dis = false;
                    } else {
                        dis = true;
                        lX = mX;
                        lY = mY;
                    }
                }

                for (int i = 0; i < 23; i++) {
                    for (int r = 0; r < 27; r++) {
                        for (int c = -1; c < 27; c++) {
                            Rectangle box = new Rectangle(r * 31 + 1, c * 31 + 1, 31, 31);
                            if (m.intersects(box)) {
                                if (type[i] == 33) {
                                    type[i + 30] -= 1;
                                    type[i] += 1;
                                    vis[c][r] = true;
                                    vis[c + 1][r] = true;
                                    vis[c + 1][r] = true;
                                    vis[c][r - 1] = true;
                                    vis[c + 1][r - 1] = true;
                                    vis[c + 1][r - 1] = true;
                                    vis[c][r + 1] = true;
                                    vis[c + 1][r + 1] = true;
                                    vis[c + 1][r + 1] = true;
                                }
                            }
                        }
                    }

                }
                press = true;
            }
            //PAUSE MENU
            if (mode == 2 && press == false) {
                Rectangle m1 = new Rectangle(getWidth() / 2 - 57, 90, 113, 30);
                Rectangle m2 = new Rectangle(getWidth() / 2 - 53, 189, 106, 33);
                Rectangle m3 = new Rectangle(getWidth() / 2 - 35, 289, 71, 30);
                Rectangle m4 = new Rectangle(getWidth() / 2 - 27, 389, 54, 30);

                if (m.intersects(m1)) {
                    mode = 1;
                    pause = false;
                } else if (m.intersects(m2)) {
                    mode = 6;
                } else if (m.intersects(m3)) {
                    if (save == 1) {
                        try {
                            FileWriter fw = new FileWriter("save1.txt");
                            PrintWriter pw = new PrintWriter(fw);
                            for (int z = 0; z < 12; z++) {
                                pw.println(res[z]);
                            }
                            for (int z = 0; z < 23; z++) {
                                pw.println(type[z]);
                            }
                            pw.println(upgrades);
                            for (int x = 0; x < 5; x++) {
                                for (int y = 0; y < 5; y++) {
                                    pw.println(buy[x][y]);
                                }
                            }
                            pw.println(time);
                            System.out.println("Saved");
                            pw.close();
                        } catch (IOException a) {
                            System.out.println("ERROR");
                        }
                    } else {

                    }
                } else if (m.intersects(m4)) {
                    System.exit(1);
                }
                press = true;
            }
            //SHOP
            if (mode == 3 && press == false) {
                Rectangle b1 = new Rectangle(3, 22, 22, 18);
                Rectangle b2 = new Rectangle(3, 172 + 22, 22, 18);
                Rectangle b3 = new Rectangle(3, 344 + 22, 22, 18);
                Rectangle b4 = new Rectangle(3, 516 + 22, 22, 18);
                Rectangle b5 = new Rectangle(3, 688 + 22, 22, 18);
                Rectangle b6 = new Rectangle(181 + 3, 172 + 22, 22, 18);
                Rectangle b7 = new Rectangle(181 + 3, 344 + 22, 22, 18);
                Rectangle b8 = new Rectangle(181 + 3, 516 + 22, 22, 18);
                Rectangle b9 = new Rectangle(181 + 3, 688 + 22, 22, 18);
                Rectangle b10 = new Rectangle(362 + 3, 172 + 22, 22, 18);
                Rectangle b11 = new Rectangle(362 + 3, 344 + 22, 22, 18);
                Rectangle b12 = new Rectangle(362 + 3, 516 + 22, 22, 18);
                Rectangle b13 = new Rectangle(362 + 3, 688 + 22, 22, 18);
                Rectangle b14 = new Rectangle(543 + 3, 22, 22, 18);
                Rectangle b15 = new Rectangle(543 + 3, 172 + 22, 22, 18);
                Rectangle b16 = new Rectangle(543 + 3, 344 + 22, 22, 18);
                Rectangle b17 = new Rectangle(543 + 3, 516 + 22, 22, 18);
                Rectangle b18 = new Rectangle(543 + 3, 688 + 22, 22, 18);
                Rectangle b19 = new Rectangle(724 + 3, 22, 22, 18);
                Rectangle b20 = new Rectangle(724 + 3, 172 + 22, 22, 18);
                Rectangle b21 = new Rectangle(724 + 3, 344 + 22, 22, 18);
                Rectangle b22 = new Rectangle(724 + 3, 516 + 22, 22, 18);
                Rectangle b23 = new Rectangle(724 + 3, 688 + 22, 22, 18);
                Rectangle p1 = new Rectangle(3, 44, 48, 16);
                Rectangle p2 = new Rectangle(3, 172 + 44, 48, 16);
                Rectangle p3 = new Rectangle(3, 344 + 44, 48, 16);
                Rectangle p4 = new Rectangle(3, 516 + 44, 48, 16);
                Rectangle p5 = new Rectangle(3, 688 + 44, 48, 16);
                Rectangle p6 = new Rectangle(181 + 3, 172 + 44, 48, 16);
                Rectangle p7 = new Rectangle(181 + 3, 344 + 44, 48, 16);
                Rectangle p8 = new Rectangle(181 + 3, 516 + 44, 48, 16);
                Rectangle p9 = new Rectangle(181 + 3, 688 + 44, 48, 16);
                Rectangle p10 = new Rectangle(362 + 3, 172 + 44, 48, 16);
                Rectangle p11 = new Rectangle(362 + 3, 344 + 44, 48, 16);
                Rectangle p12 = new Rectangle(362 + 3, 516 + 44, 48, 16);
                Rectangle p13 = new Rectangle(362 + 3, 688 + 44, 48, 16);
                Rectangle p14 = new Rectangle(543 + 3, 44, 48, 16);
                Rectangle p15 = new Rectangle(543 + 3, 172 + 44, 48, 16);
                Rectangle p16 = new Rectangle(543 + 3, 344 + 44, 48, 16);
                Rectangle p17 = new Rectangle(543 + 3, 516 + 44, 48, 16);
                Rectangle p18 = new Rectangle(543 + 3, 688 + 44, 48, 16);
                Rectangle p19 = new Rectangle(724 + 3, 44, 48, 16);
                Rectangle p20 = new Rectangle(724 + 3, 172 + 44, 48, 16);
                Rectangle p21 = new Rectangle(724 + 3, 344 + 44, 48, 16);
                Rectangle p22 = new Rectangle(724 + 3, 516 + 44, 48, 16);
                Rectangle p23 = new Rectangle(724 + 3, 688 + 44, 48, 16);

                if (m.intersects(b1)) {
                    if (res[0] > 300) {
                        bt = 1;
                        res[0] -= 300;
                        mode = 7;
                        type[0] += 1;
                    }
                } else if (m.intersects(b2)) {
                    if (res[0] > 300) {
                        bt = 2;
                        res[0] -= 300;
                        mode = 7;
                        type[1] += 1;
                    }
                } else if (m.intersects(b3)) {
                    if (res[0] > 300) {
                        bt = 3;
                        res[0] -= 300;
                        mode = 7;
                        type[2] += 1;
                    }
                } else if (m.intersects(b4)) {
                    if (res[0] > 300) {
                        bt = 4;
                        res[0] -= 300;
                        mode = 7;
                        type[3] += 1;
                    }
                } else if (m.intersects(b5)) {
                    if (res[0] > 300) {
                        bt = 5;
                        res[0] -= 300;
                        mode = 7;
                        type[4] += 1;
                    }
                } else if (m.intersects(b6)) {
                    if (res[0] > 300) {
                        bt = 6;
                        res[0] -= 300;
                        mode = 7;
                        type[5] += 1;
                    }
                } else if (m.intersects(b7)) {
                    if (res[0] > 300) {
                        bt = 7;
                        res[0] -= 300;
                        mode = 7;
                        type[6] += 1;
                    }
                } else if (m.intersects(b8)) {
                    if (res[0] > 300) {
                        bt = 8;
                        res[0] -= 300;
                        mode = 7;
                        type[7] += 1;
                    }
                } else if (m.intersects(b9)) {
                    if (res[0] > 300) {
                        bt = 9;
                        res[0] -= 300;
                        mode = 7;
                        type[8] += 1;
                    }
                } else if (m.intersects(b10)) {
                    if (res[0] > 300) {
                        bt = 10;
                        res[0] -= 300;
                        mode = 7;
                        type[9] += 1;
                    }
                } else if (m.intersects(b11)) {
                    if (res[0] > 300) {
                        bt = 11;
                        res[0] -= 300;
                        mode = 7;
                        type[10] += 1;
                    }
                } else if (m.intersects(b12)) {
                    if (res[0] > 300) {
                        bt = 12;
                        res[0] -= 300;
                        mode = 7;
                        type[11] += 1;
                    }
                } else if (m.intersects(b13)) {
                    if (res[0] > 300) {
                        bt = 13;
                        res[0] -= 300;
                        mode = 7;
                        type[12] += 1;
                    }
                } else if (m.intersects(b14)) {
                    if (res[0] > 300) {
                        bt = 14;
                        res[0] -= 300;
                        mode = 7;
                        type[13] += 1;
                    }
                } else if (m.intersects(b15)) {
                    if (res[0] > 300) {
                        bt = 15;
                        res[0] -= 300;
                        mode = 7;
                        type[14] += 1;
                    }
                } else if (m.intersects(b16)) {
                    if (res[0] > 300) {
                        bt = 16;
                        res[0] -= 300;
                        mode = 7;
                        type[15] += 1;
                    }
                } else if (m.intersects(b17)) {
                    if (res[0] > 300) {
                        bt = 17;
                        res[0] -= 300;
                        mode = 7;
                        type[16] += 1;
                    }
                } else if (m.intersects(b18)) {
                    if (res[0] > 300) {
                        bt = 18;
                        res[0] -= 300;
                        mode = 7;
                        type[17] += 1;
                    }
                } else if (m.intersects(b19)) {
                    if (res[0] > 300) {
                        bt = 19;
                        res[0] -= 300;
                        mode = 7;
                        type[18] += 1;
                    }
                } else if (m.intersects(b20)) {
                    if (res[0] > 300) {
                        bt = 20;
                        res[0] -= 300;
                        mode = 7;
                        type[19] += 1;
                    }
                } else if (m.intersects(b21)) {
                    if (res[0] > 300) {
                        bt = 21;
                        res[0] -= 300;
                        mode = 7;
                        type[20] += 1;
                    }
                } else if (m.intersects(b22)) {
                    if (res[0] > 300) {
                        bt = 22;
                        res[0] -= 300;
                        mode = 7;
                        type[21] += 1;
                    }
                } else if (m.intersects(b23)) {
                    if (res[0] > 300) {
                        bt = 23;
                        res[0] -= 300;
                        mode = 7;
                        type[22] += 1;
                    }
                } else if (m.intersects(p1)) {
                    System.out.println("Cannot Produce");
                } else if (m.intersects(p2)) {
                    System.out.println("Cannot Produce");
                } else if (m.intersects(p3)) {
                    bt = 3;
                } else if (m.intersects(p4)) {
                    bt = 4;
                } else if (m.intersects(p5)) {
                    bt = 5;
                } else if (m.intersects(p6)) {
                    bt = 6;
                } else if (m.intersects(p7)) {
                    bt = 7;
                } else if (m.intersects(p8)) {
                    bt = 8;
                } else if (m.intersects(p9)) {
                    bt = 9;
                } else if (m.intersects(p10)) {
                    bt = 10;
                } else if (m.intersects(p11)) {
                    bt = 11;
                } else if (m.intersects(p12)) {
                    bt = 12;
                } else if (m.intersects(p13)) {
                    bt = 13;
                } else if (m.intersects(p14)) {
                    bt = 14;
                } else if (m.intersects(p15)) {
                    bt = 15;
                } else if (m.intersects(p16)) {
                    bt = 16;
                } else if (m.intersects(p17)) {
                    bt = 17;
                } else if (m.intersects(p18)) {
                    bt = 18;
                } else if (m.intersects(p19)) {
                    bt = 19;
                } else if (m.intersects(p20)) {
                    bt = 20;
                } else if (m.intersects(p21)) {
                    bt = 21;
                } else if (m.intersects(p22)) {
                    bt = 22;
                } else if (m.intersects(p23)) {
                    bt = 23;
                }

                Rectangle g1 = new Rectangle(305, 15, 10, 11);
                Rectangle g2 = new Rectangle(331, 15, 10, 11);

                Rectangle g3 = new Rectangle(305, 45, 10, 11);
                Rectangle g4 = new Rectangle(331, 45, 10, 11);

                Rectangle g5 = new Rectangle(305, 75, 10, 11);
                Rectangle g6 = new Rectangle(331, 75, 10, 11);

                Rectangle g7 = new Rectangle(305, 105, 10, 11);
                Rectangle g8 = new Rectangle(331, 105, 10, 11);

                Rectangle g9 = new Rectangle(305, 135, 10, 11);
                Rectangle g10 = new Rectangle(328, 135, 10, 11);

                Rectangle g11 = new Rectangle(395, 15, 10, 11);
                Rectangle g12 = new Rectangle(421, 15, 10, 11);

                Rectangle g13 = new Rectangle(395, 45, 10, 11);
                Rectangle g14 = new Rectangle(421, 45, 10, 11);

                Rectangle g15 = new Rectangle(395, 75, 10, 11);
                Rectangle g16 = new Rectangle(421, 75, 10, 11);

                Rectangle g17 = new Rectangle(395, 105, 10, 11);
                Rectangle g18 = new Rectangle(421, 105, 10, 11);

                Rectangle g19 = new Rectangle(395, 135, 10, 11);
                Rectangle g20 = new Rectangle(421, 135, 10, 11);

                Rectangle g21 = new Rectangle(485, 15, 10, 11);
                Rectangle g22 = new Rectangle(511, 15, 10, 11);

                Rectangle g23 = new Rectangle(485, 45, 10, 11);
                Rectangle g24 = new Rectangle(511, 45, 10, 11);
                if (m.intersects(g1)) {
                    res[1] += 1;
                    res[0] -= cost[0];
                } else if (m.intersects(g2)) {
                    if (res[1] > 0) {
                        res[1] -= 1;
                    }
                } else if (m.intersects(g3)) {
                    res[2] += 1;
                    res[0] -= cost[1];
                } else if (m.intersects(g4)) {
                    if (res[2] > 0) {
                        res[2] -= 1;
                    }
                } else if (m.intersects(g5)) {
                    res[3] += 1;
                    res[0] -= cost[2];
                } else if (m.intersects(g6)) {
                    if (res[3] > 0) {
                        res[3] -= 1;
                    }
                } else if (m.intersects(g7)) {
                    res[4] += 1;
                    res[0] -= cost[3];
                } else if (m.intersects(g8)) {
                    if (res[4] > 0) {
                        res[4] -= 1;
                    }
                } else if (m.intersects(g9)) {
                    res[5] += 1;
                    res[0] -= cost[4];
                } else if (m.intersects(g10)) {
                    if (res[5] > 0) {
                        res[5] -= 1;
                    }
                } else if (m.intersects(g11)) {
                    res[6] += 1;
                    res[0] -= cost[5];
                } else if (m.intersects(g12)) {
                    if (res[6] > 0) {
                        res[6] -= 1;
                    }
                } else if (m.intersects(g13)) {
                    res[7] += 1;
                    res[0] -= cost[6];
                } else if (m.intersects(g14)) {
                    if (res[7] > 0) {
                        res[7] -= 1;
                    }
                } else if (m.intersects(g15)) {
                    res[8] += 1;
                    res[0] -= cost[7];
                } else if (m.intersects(g16)) {
                    if (res[8] > 0) {
                        res[8] -= 1;
                    }
                } else if (m.intersects(g17)) {
                    res[9] += 1;
                    res[0] -= cost[8];
                } else if (m.intersects(g18)) {
                    if (res[9] > 0) {
                        res[9] -= 1;
                    }
                } else if (m.intersects(g19)) {
                    res[10] += 1;
                    res[0] -= cost[9];
                } else if (m.intersects(g20)) {
                    if (res[10] > 0) {
                        res[10] -= 1;
                    }
                } else if (m.intersects(g21)) {
                    res[11] += 1;
                    res[0] -= cost[10];
                } else if (m.intersects(g22)) {
                    if (res[11] > 0) {
                        res[11] -= 1;
                    }
                } else if (m.intersects(g23)) {
                    res[12] += 1;
                    res[0] -= cost[11];
                } else if (m.intersects(g24)) {
                    if (res[12] > 0) {
                        res[12] -= 1;
                    }
                }

                press = true;
            }
            //MAP
            if (mode == 4) {
                Rectangle t0 = new Rectangle(844, 1, 31, 31);
                Rectangle t1 = new Rectangle(844, 62, 31, 31);
                Rectangle t2 = new Rectangle(844, 124, 31, 31);
                Rectangle t3 = new Rectangle(844, 186, 31, 31);
                Rectangle t4 = new Rectangle(844, 248, 31, 31);
                Rectangle t5 = new Rectangle(844, 310, 31, 31);
                Rectangle dn = new Rectangle(843, 618, 33, 15);
                Rectangle cl = new Rectangle(843, 518, 33, 15);
                if (m.intersects(t0)) {
                    mapCurrent = 0;
                } else if (m.intersects(t1)) {
                    mapCurrent = 1;
                } else if (m.intersects(t2)) {
                    mapCurrent = 2;
                } else if (m.intersects(t3)) {
                    mapCurrent = 3;
                } else if (m.intersects(t4)) {
                    mapCurrent = 4;
                } else if (m.intersects(t5)) {
                    mapCurrent = 5;
                }

                if (mapCurrent != 21) {
                    for (int r = 0; r < 27; r++) {
                        for (int c = -1; c < 27; c++) {
                            Rectangle box = new Rectangle(r * 31 + 1, c * 31 + 1, 31, 31);
                            if (m.intersects(box)) {
                                map[c][r] = mapCurrent;
                            }
                        }
                    }
                }

                if (m.intersects(dn)) {
                    try {
                        FileWriter fw = new FileWriter("map1.txt");
                        PrintWriter pw = new PrintWriter(fw);
                        System.out.println("Saved");
                        for (int x = 0; x < 27; x++) {
                            for (int y = 0; y < 27; y++) {
                                pw.println(map[x][y]);
                            }
                        }

                        pw.close();
                    } catch (IOException a) {
                        System.out.println("ERROR");
                    }
                    mode = 0;
                    pause = false;
                } else if (m.intersects(cl)) {
                    for (int r = 0; r < 27; r++) {
                        for (int c = 0; c < 27; c++) {
                            map[c][r] = 0;
                        }
                    }
                    
                }
                press = true;
            }

            //PLAY OPTION
            if (mode == 5) {
                Rectangle m1 = new Rectangle(getWidth() / 2 - 73, 90, 147, 33);
                Rectangle m2 = new Rectangle(getWidth() / 2 - 86, 189, 173, 33);
                Rectangle m3 = new Rectangle(getWidth() / 2 - 75, 289, 151, 33);
                Rectangle m4 = new Rectangle(getWidth() / 2 - 77, 389, 155, 33);

                if (m.intersects(m1)) {
                    go = 1;
                } else if (m.intersects(m2)) {
                    try {
                        FileReader fr = new FileReader("map1.txt");
                        BufferedReader br = new BufferedReader(fr);
                        for (int x = 0; x < 27; x++) {
                            for (int y = 0; y < 27; y++) {
                                map[x][y] = Integer.parseInt(br.readLine());
                            }
                        }

                    } catch (IOException a) {
                        System.out.println("Couldn't Load");
                    }
                    go = 1;
                } else if (m.intersects(m3)) {
                    try {
                        FileReader fr = new FileReader("save1.txt");
                        BufferedReader br = new BufferedReader(fr);
                        for (int z = 0; z < 12; z++) {
                            res[z] = Integer.parseInt(br.readLine());
                        }
                        for (int z = 0; z < 23; z++) {
                            type[z] = Integer.parseInt(br.readLine());
                        }
                        upgrades = Integer.parseInt(br.readLine());
                        time = Integer.parseInt(br.readLine());

                    } catch (IOException a) {
                        System.out.println("Couldn't Load");
                    }
                    save = 1;
                    if (go == 1) {
                        go = 2;
                        pause = false;
                    }
                } else if (m.intersects(m4)) {
                    try {
                        FileReader fr = new FileReader("save2.txt");
                        BufferedReader br = new BufferedReader(fr);
                        for (int z = 0; z < 12; z++) {
                            res[z] = Integer.parseInt(br.readLine());
                        }
                        for (int z = 0; z < 23; z++) {
                            type[z] = Integer.parseInt(br.readLine());
                        }
                        upgrades = Integer.parseInt(br.readLine());
                        time = Integer.parseInt(br.readLine());

                    } catch (IOException a) {
                        System.out.println("Couldn't Load");
                    }
                    save = 1;
                    if (go == 1) {
                        go = 2;
                    }
                }
                press = true;
                if (go == 2) {
                    mode = 1;
                }
            }
            if (mode == 6 && press == false) {//OPTIONS MENU
                System.out.println("ff");
            }
            if (mode == 7 && press == false) {//Placer               
                for (int r = 0; r < 27; r++) {
                    for (int c = -1; c < 27; c++) {
                        Rectangle box = new Rectangle(r * 31 + 1, c * 31 + 1, 31, 31);
                        if (m.intersects(box)) {
                            grid[c][r] = bt;
                            mode = 1;
                            pause = false;
                            if (bt == 1) {
                                wi++;
                                wx[wi + 40] = r * 31 + 1;
                                wy[wi + 40] = c * 31 + 1;
                            }
                        }
                    }
                }
            }
        }
        press = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
    }
}
