/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

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

/**
 *
 * @author Daniel
 */
class SnakeEnvironment extends Environment {

    private Grid grid;
    private Snake snake;
    private int defaultDelay = 0;
    private int delay = defaultDelay;
    private int score = 0;

    @Override
    public void initializeEnvironment() {
        grid = new Grid();
        grid.setColor(Color.GREEN);
        grid.getPosition().x = 50;
        grid.getPosition().y = 100;
        grid.setColumns(90);
        grid.setRows(45);

        snake = new Snake();
        snake.getBody().add(new Point(5, 5));
        snake.getBody().add(new Point(4, 5));
        snake.getBody().add(new Point(4, 4));

        this.setBackground(ResourceTools.loadImageFromResource("resources/autumn.jpg"));
    }

    @Override
    public void timerTaskHandler() {
        if (snake != null) {
            if (delay <= 0) {
                snake.move();
                delay = defaultDelay;
            } else {
                delay--;
            }
        }
          if (snake.getDirection() == Direction.RIGHT) {
                if (snake.getHead().x >= this.grid.getColumns()) {
                    snake.getHead().x = 0 ;
                }
            } else if (snake.getDirection() == Direction.LEFT) {
                if (snake.getHead().x <=-1) {
                    snake.getHead().x = this.grid.getColumns()-1;
                }
            } else if (snake.getDirection() == Direction.UP) {
                if (snake.getHead().y <= -1) {
                    snake.getHead().y = this.grid.getRows()-1;
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
            snake.setGrowthCount(2);
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            this.score += 10;


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
        
        GraphicsPalette.enterPortal(graphics, new Point(100, 100), new Point(this.grid.getCellSize()), Color.yellow);
        GraphicsPalette.leavePortal(graphics, new Point(500, 500), new Point(this.grid.getCellSize() ), Color.yellow);
        GraphicsPalette.drawPoisonBottle(graphics, new Point(300, 300),new Point(this.grid.getCellSize()), Color.yellow);
    }
}
