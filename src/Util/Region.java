/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author James
 */
public class Region {
    private Point topLeftBound;
    private Point bottomRightBound;
    private int area;
    private List<Point> points;

    public Region(){
        topLeftBound = new Point(0,0);
        bottomRightBound = new Point(0,0);
        area = 0;
        points = new ArrayList<>();
    }

    public void addPixel(int x, int y){
        points.add(new Point(x,y));
        area = area++;
        if (x < topLeftBound.getX()){
            topLeftBound.x = x;
        }else if(x > bottomRightBound.getX()){
            bottomRightBound.x = x;
        }
        if (y < topLeftBound.getY()){
            topLeftBound.y = y;
        }else if(y > bottomRightBound.getY()){
            bottomRightBound.y = y;
        }
    }

    public int getTrueArea(){
        return points.size();
    }
    
    public int getArea(){
        return area;
    }

    public List<Point> getPointList(){
        return this.points;
    }
}
