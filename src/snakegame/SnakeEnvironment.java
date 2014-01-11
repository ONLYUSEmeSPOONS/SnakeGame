/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import environment.Environment;
import environment.Grid;
import image.ResourceTools;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Daniel
 */
class SnakeEnvironment extends Environment {
    Grid grid;
  

    @Override
    public void initializeEnvironment() {
        grid = new Grid();
        grid.getPosition().x = 50;
        grid.getPosition().y = 100;
        grid.setColumns(35);
        grid.setRows(30);
        
        this.setBackground(ResourceTools.loadImageFromResource("resources/elephants.jpg"));
    }

    @Override
    public void timerTaskHandler() {

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {

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
    }
    
}
