package ff;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Food {
int req_hunters;
int x;
int y;
World m;
ArrayList<Person> eaters;
    public Food(World k,int xx, int yy){
        x=xx;
        //System.out.println("food grown");
        m=k;
        y=yy;
        eaters = new ArrayList<>();
        req_hunters =1+ (int)Math.round(Math.random()*3);
    }
    public void feed(){
        //System.out.println("food eaten");
        for(Person p: eaters){
            p.food+=100*req_hunters;
        }
        req_hunters = 0;
    }
    public boolean containspoint(double xx, double yy){
        if(xx>=x&&xx<=x+3&&yy>=y&&yy<=y+3)return true;
        return false;
    }
    public void addEater(Person p){
        eaters.add(p);
        if(eaters.size()>req_hunters)feed();
    }
    public void draw(Graphics g){
        g.setColor(Color.black);
        g.drawRect(x, y, 3, 3);
    }

}