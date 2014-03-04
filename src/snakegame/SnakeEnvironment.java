/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.Grid;
import image.ResourceTools;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
class SnakeEnvironment extends Environment {

    private GameState gameState = GameState.PAUSED;
    private Grid grid;
    private Snake snake;
    private int end;
    private int defaultDelay = 3 / 4;
    private int delay = defaultDelay;
    private int score = 0;
    private ArrayList<Point> apples;
    private ArrayList<Point> bomb;
    private ArrayList<Point> poison;
    private ArrayList<Point> diamond;
    private ArrayList<Point> lollipop;
    private ArrayList<Point> smileyFace;

    @Override
    public void initializeEnvironment() {
        grid = new Grid();
        grid.setColor(Color.BLACK);
        grid.getPosition().x = 50;
        grid.getPosition().y = 100;
        grid.setColumns(77);
        grid.setRows(40);

        this.apples = new ArrayList<Point>();
        this.apples.add(getRandomGridLocation());
        this.apples.add(getRandomGridLocation());

        this.diamond = new ArrayList<Point>();
        this.diamond.add(getRandomGridLocation());
        this.diamond.add(getRandomGridLocation());

        this.bomb = new ArrayList<Point>();
        this.bomb.add(getRandomGridLocation());
        this.bomb.add(getRandomGridLocation());

        this.poison = new ArrayList<Point>();
        this.poison.add(getRandomGridLocation());
        this.poison.add(getRandomGridLocation());

        this.lollipop = new ArrayList<Point>();
        this.lollipop.add(getRandomGridLocation());
        this.lollipop.add(getRandomGridLocation());

        this.smileyFace = new ArrayList<Point>();
        this.smileyFace.add(getRandomGridLocation());
        this.smileyFace.add(getRandomGridLocation());


        snake = new Snake();
        snake.getBody().add(new Point(5, 5));
        snake.getBody().add(new Point(4, 5));
        snake.getBody().add(new Point(4, 4));
        snake.getBody().add(new Point(3, 4));


        this.setBackground(ResourceTools.loadImageFromResource("resources/Silioutte.jpg"));
    }

    private Point getRandomGridLocation() {
        return new Point((int) (Math.random() * this.grid.getColumns()), (int) (Math.random() * this.grid.getRows()));

    }

    @Override
    public void timerTaskHandler() {
        if (this.gameState == GameState.RUNNING) {
            if (snake != null) {
                if (delay <= 0) {
                    snake.move();
                    moveSmileyFace();
                    delay = defaultDelay;
                    if (snake.selfHitTest()) {
                        this.gameState = GameState.ENDED;
                    }
                    checkSnakeAppleIntersection();
                    checkSnakeDiamondIntersection();
                    checkSnakeBombIntersection();
                    checkSnakePoisonIntersection();
                    checkSnakeLollipopIntersection();
                    checkSnakeSmileyFaceIntersection();
                    moveSmileyFace();
                } else {
                    delay--;
                }
            }
        }

        if (snake.getDirection() == Direction.RIGHT) {
            if (snake.getHead().x >= this.grid.getColumns()) {
                snake.getHead().x = 0;
            }
        } else if (snake.getDirection() == Direction.LEFT) {
            if (snake.getHead().x <= -1) {
                snake.getHead().x = this.grid.getColumns() - 1;
            }
        } else if (snake.getDirection() == Direction.UP) {
            if (snake.getHead().y <= -1) {
                snake.getHead().y = this.grid.getRows() - 1;
            }
        } else if (snake.getDirection() == Direction.DOWN) {
            if (snake.getHead().y >= this.grid.getRows()) {
                snake.getHead().y = 0;
            }
        }

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (snake.getDirection() != Direction.DOWN) {
                snake.setDirection(Direction.UP);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (snake.getDirection() != Direction.UP) {
                snake.setDirection(Direction.DOWN);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (snake.getDirection() != Direction.RIGHT) {
                snake.setDirection(Direction.LEFT);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (snake.getDirection() != Direction.LEFT) {
                 snake.setDirection(Direction.RIGHT);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.RUNNING;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            this.score += 10;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameState = GameState.ENDED;
        }

    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {
    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {
    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        if (grid != null) {
            grid.paintComponent(graphics);
        }

        if (snake != null) {
            graphics.setColor(Color.WHITE);
            for (int i = 0; i < snake.getBody().size(); i++) {
                graphics.fillOval(grid.getCellPosition(snake.getBody().get(i)).x, grid.getCellPosition(snake.getBody().get(i)).y, grid.getCellWidth(), grid.getCellHeight());
            }
        }

        graphics.setFont(new Font("Calibri ", Font.ITALIC, 50));
        graphics.drawString("Score: " + this.score, 50, 50);

        for (int i = 0; i < this.apples.size(); i++) {
            GraphicsPalette.drawApple(graphics, this.grid.getCellPosition(this.apples.get(i)), new Point(this.grid.getCellSize()));
        }


        for (int i = 0; i < this.bomb.size(); i++) {
            GraphicsPalette.drawBomb(graphics, this.grid.getCellPosition(this.bomb.get(i)), new Point(this.grid.getCellSize()), Color.yellow);
        }

        for (int i = 0; i < this.poison.size(); i++) {
            GraphicsPalette.drawPoisonBottle(graphics, this.grid.getCellPosition(this.poison.get(i)), new Point(this.grid.getCellSize()), Color.yellow);
        }

        for (int i = 0; i < this.smileyFace.size(); i++) {
            GraphicsPalette.drawHappyFaceSunGlasses(graphics, this.grid.getCellPosition(this.smileyFace.get(i)), new Point(this.grid.getCellSize()), Color.GREEN);
        }


        for (int i = 0; i < this.diamond.size(); i++) {
            GraphicsPalette.drawDiamond(graphics, this.grid.getCellPosition(this.diamond.get(i)), new Point(this.grid.getCellSize()), Color.CYAN);
        }

        for (int i = 0; i < this.lollipop.size(); i++) {
            GraphicsPalette.drawLollipop(graphics, this.grid.getCellPosition(this.lollipop.get(i)), new Point(this.grid.getCellSize()));
        }

//        GraphicsPalette.drawLollipop(graphics, new Point(100, 10), new Point(new Point(100, 100));            



        if (gameState == GameState.ENDED) {
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Calibri ", Font.ITALIC, 150));
            graphics.drawString("Game Over!!!", 420, 500);
        }

        if (gameState == GameState.PAUSED) {
            graphics.setColor(Color.GREEN);
            graphics.setFont(new Font("Calibri ", Font.ITALIC, 90));
            graphics.drawString("Press Spacebar To Start/Stop The Game!!!", 90, 500);
        }
    }

    private void checkSnakeAppleIntersection() {
//        If the snake head location is the same as any apple location
//        then grow body of snake and make apple dissapear
//        later move the apple and make a sound and invrease score
        for (int i = 0; i < this.apples.size(); i++) {
            if (snake.getHead().equals(this.apples.get(i))) {
                System.out.println("Apple Chompped");
                AudioPlayer.play("/resources/AppleBite.wav");
                this.score += 15;
                this.apples.get(i).setLocation(getRandomGridLocation());
                this.snake.grow(1);
            }

        }
    }

    private void checkSnakeDiamondIntersection() {
        for (int i = 0; i < this.diamond.size(); i++) {
            if (snake.getHead().equals(this.diamond.get(i))) {
                System.out.println("WhooHoo");
                this.score += 25;
                AudioPlayer.play("/resources/DiamondTheme.wav");
                this.diamond.get(i).setLocation(getRandomGridLocation());
                this.snake.grow(2);

            }

        }
    }

    private void checkSnakeBombIntersection() {
        for (int j = 0; j < this.bomb.size(); j++) {
            if (snake.getHead().equals(this.bomb.get(j))) {
                System.out.println("Game Over!!!");
                this.snake.grow(-3);
                this.score += -15;
                AudioPlayer.play("/resources/Explode1.wav");
                this.bomb.get(j).setLocation(getRandomGridLocation());
                this.end += 1;
                if (this.end == 3) {
                    gameState = GameState.ENDED;
                }
            }

        }
    }

    private void checkSnakePoisonIntersection() {
        for (int j = 0; j < this.poison.size(); j++) {
            if (snake.getHead().equals(this.poison.get(j))) {
                System.out.println("Shatter");
                this.score += -20;
                AudioPlayer.play("/resources/GlassBreak1.wav");
                this.poison.get(j).setLocation(getRandomGridLocation());
            }
        }
    }

    private void checkSnakeLollipopIntersection() {
        for (int i = 0; i < this.lollipop.size(); i++) {
            if (snake.getHead().equals(this.lollipop.get(i))) {
                System.out.println("Shlluup");
                AudioPlayer.play("/resources/spit.WAV");
                this.score += 10;
                this.lollipop.get(i).setLocation(getRandomGridLocation());
            }
        }
    }

    private void checkSnakeSmileyFaceIntersection() {
        for (int i = 0; i < this.smileyFace.size(); i++) {
            if (snake.getHead().equals(this.smileyFace.get(i))) {
                System.out.println("Shlluup");
                this.smileyFace.get(i).setLocation(getRandomGridLocation());
            }
        }
    }

    private void moveSmileyFace() {
        for (Point location : smileyFace) {
            if (snake.getHead().equals(location)) {
                score += 100;
            }

            if (Math.random() > .5) {
                location.x += 1;
                if (location.x >= this.grid.getColumns()) {
                    location.x = 0;
                }
            } else {
                location.x -= 1;
                if (location.x <= -1) {
                    location.x = this.grid.getColumns() - 1;
                }
            }

            if (Math.random() > .5) {
                location.y += 1;
                if (location.y >= this.grid.getRows()) {
                    location.y = 0;
                }
            } else {
                location.y -= 1;
                if (location.y <= -1) {
                    location.y = this.grid.getRows() - 1;
                }
            }
        }
    }
}
