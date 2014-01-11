/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import environment.Environment;
import environment.Grid;
import image.ResourceTools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Daniel
 */
class SnakeEnvironment extends Environment {
    private Grid grid;
    private Snake snake;
    private int defaultDelay = 4;
    private int delay = defaultDelay;

    @Override
    public void initializeEnvironment() {
        grid = new Grid();
        grid.getPosition().x = 50;
        grid.getPosition().y = 100;
        grid.setColumns(35);
        grid.setRows(30);
        
        snake = new Snake();
        snake.getBody().add(new Point(5, 5));
        snake.getBody().add(new Point(4, 5));
        snake.getBody().add(new Point(4, 4));
        
        this.setBackground(ResourceTools.loadImageFromResource("resources/elephants.jpg"));
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
            graphics.setColor(Color.GREEN);
            for (int i = 0; i < snake.getBody().size(); i++) {
                graphics.fillRect(grid.getCellPosition(snake.getBody().get(i)).x, grid.getCellPosition(snake.getBody().get(i)).y, grid.getCellWidth(), grid.getCellHeight());
            }
        }
    }
    
}
