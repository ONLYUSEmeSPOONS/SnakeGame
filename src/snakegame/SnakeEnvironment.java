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
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
class SnakeEnvironment extends Environment {

    private GameState gameState = GameState.PAUSED;
    private Grid grid;
    private Snake snake;
    private int defaultDelay = 0;
    private int delay = defaultDelay;
    private int score = 0;
    private ArrayList<Point> apples;
    private ArrayList<Point> bomb;
    private ArrayList<Point> poison;
    private ArrayList<Point> diamond;
    private ArrayList<Point> lolipop;

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

        this.lolipop = new ArrayList<Point>();
        this.lolipop.add(getRandomGridLocation());
        this.lolipop.add(getRandomGridLocation());






        snake = new Snake();
        snake.getBody().add(new Point(5, 5));
        snake.getBody().add(new Point(4, 5));
        snake.getBody().add(new Point(4, 4));

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
                    delay = defaultDelay;
                    checkSnakeAppleIntersection();
                    checkSnakeDiamondIntersection();
                    checkSnakeBombIntersection();
                    checkSnakePoisonIntersection();
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
            snake.setDirection(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            snake.setDirection(Direction.DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            snake.setDirection(Direction.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            snake.setDirection(Direction.RIGHT);
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

        for (int i = 0; i < this.diamond.size(); i++) {
            GraphicsPalette.drawDiamond(graphics, this.grid.getCellPosition(this.diamond.get(i)), new Point(this.grid.getCellSize()), Color.CYAN);
        }

        if (gameState == GameState.ENDED) {
            graphics.setFont(new Font("Calibri ", Font.ITALIC, 150));
            graphics.drawString("Game Over!!!", 420, 500);
        }

    }

    private void checkSnakeAppleIntersection() {
//        If the snake head location is the same as any apple location
//        then grow body of snake and make apple dissapear
//        later move the apple and make a sound and invrease score
        for (int i = 0; i < this.apples.size(); i++) {
            if (snake.getHead().equals(this.apples.get(i))) {
                System.out.println("Apple Chompped");
                this.snake.grow(2);
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
                this.snake.grow(2);
                this.score += 45;
                AudioPlayer.play("/resources/DiamondTheme.wav");
                this.diamond.get(i).setLocation(getRandomGridLocation());
                this.snake.grow(2);

            }

        }
    }

    private void checkSnakeBombIntersection() {
        for (int j = 0; j < this.bomb.size(); j++) {
            if (snake.getHead().equals(this.bomb.get(j))) {
                System.out.println("Ouch!!");
                this.snake.grow(2);
                this.score += -15;
                AudioPlayer.play("/resources/Explode1.wav");
                this.bomb.get(j).setLocation(getRandomGridLocation());
            }

        }
    }

    private void checkSnakePoisonIntersection() {
        for (int j = 0; j < this.poison.size(); j++) {
            if (snake.getHead().equals(this.poison.get(j))) {
                System.out.println("Shatter");
                this.snake.grow(2);
                this.score += -20;
                AudioPlayer.play("/resources/GlassBreak1.wav");
                this.poison.get(j).setLocation(getRandomGridLocation());
            }

        }
    }
}
