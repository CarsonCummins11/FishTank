package ff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfigTemplate;
import java.awt.Point;

public class Person{
    public static final String[] names = {"Emma","Olivia","Ava","Isabella","Sophia","Mia","Charlotte","Amelia","Evelyn","Abigail","Harper","Emily","Elizabeth","Avery","Sofia","Ella","Madison","Scarlett","Victoria","Aria","Grace","Chloe","Camila","Penelope","Riley","Layla","Lillian","Nora","Zoey","Mila","Aubrey","Hannah","Lily","Addison","Eleanor","Natalie","Luna","Savannah","Brooklyn","Leah","Zoe","Stella","Hazel","Ellie","Paisley","Audrey","Skylar","Violet","Claire","Bella","Aurora","Lucy","Anna","Samantha","Caroline","Genesis","Aaliyah","Kennedy","Kinsley","Allison","Maya","Sarah","Madelyn","Adeline","Alexa","Ariana","Elena","Gabriella","Naomi","Alice","Sadie","Hailey","Eva","Emilia","Autumn","Quinn","Nevaeh","Piper","Ruby","Serenity","Willow","Everly","Cora","Kaylee","Lydia","Aubree","Arianna","Eliana","Peyton","Melanie","Gianna","Isabelle","Julia","Valentina","Nova","Clara","Vivian","Reagan","Mackenzie","Madeline"};
    public static final int VISION_WIDTH = 51;
    public static final int COMPLEXITY = 51; 
    public static final int MAX_ROTATION = 5;
    int food;
    double direction;
    double velocity;
    int x;
    int y;
    FF brain;
    double mes;
    String name;
    World mine;
    public Person(World w){
        name = names[(int)(Math.random()*names.length)];
        direction=velocity=0;
        x= (int)(Math.random()*600);
        y= (int)(Math.random()*600);
        food = 200;
        mes=-1;
        mine = w;
    }
    public static Person random(World w){
        Person p = new Person(w);
        p.brain = new FF(VISION_WIDTH,COMPLEXITY);
        return p;
    }
    public void step(double[] inp){
        food-=1;
        brain.setInputs(inp);
        double[] move = brain.step();
        mine.submitMessage(move[0],this);
        double rotation = 0;
        for(int i = 1; i<1+((VISION_WIDTH-1)/2); i++){
            rotation+=move[i];
        }
        rotation/=(VISION_WIDTH-1)/2;
        rotation*=MAX_ROTATION;
        double speed = 0;
        for(int i = 1+((VISION_WIDTH-1)/2); i<VISION_WIDTH; i++){
            speed+=move[i];
        }
        speed/=(VISION_WIDTH-1)/2;
        direction+=rotation;
        direction = direction%360;
        velocity=speed;
        x+=velocity*Math.cos(Math.toRadians(direction));
        y+=velocity*Math.sin(Math.toRadians(direction));
        //little bit of gravity
        final double GRAVITY_POWER = .004;
        int xvec = 300-x;
        int yvec = 300-y;
        y+=GRAVITY_POWER*yvec;
        x+=GRAVITY_POWER*xvec;
    }
    public void draw(Graphics g){
        Point ur = rotate_point((float)x, (float)y, (float)Math.toRadians(direction), new Point(x+5,y));
        Point br = rotate_point((float)x, (float)y, (float)Math.toRadians(direction), new Point(x+5,y+5));
        Point bl = rotate_point((float)x, (float)y, (float)Math.toRadians(direction), new Point(x,y+5));
        g.setColor(Color.ORANGE);
        g.drawPolyline(new int[]{
            x,
            ur.x,
            br.x,
            bl.x,
            x
        }, new int[]{
            y,
            ur.y,
            br.y,
            bl.y,
            y
        }, 4);
    }
    //rotate point p around cx,cy
    Point rotate_point(float cx, float cy, float angle, Point p){
        return new Point((int)Math.round(Math.cos(angle) * (p.x - cx) - Math.sin(angle) * (p.y - cy) + cx),(int)Math.round(Math.sin(angle) * (p.x - cx) + Math.cos(angle) * (p.y - cy) + cy));
   }
   public boolean rectContainsPoint(int rx,int ry,int px,int py){
    if(px>=rx&&px<=rx+5&&py>=ry&&py<=py+5)return true;
    return false;
   }
	public boolean containspoint(double rx, double ry) {
        //if(rectContainsPoint(x,y,(int)rx,(int)ry))System.out.println(x+","+y+","+rx+","+ry);
        Point rb = rotate_point((float)x, (float)y, (float)-direction, new Point((int)Math.round(rx),(int)Math.round(ry)));
        if(rectContainsPoint(x, y, rb.x, rb.y))return true;
        return false;
	}
    public Person breed(Person d){
        Person ret = new Person(mine);
        ret.brain=brain.breed(d.brain);
        return ret;
    }


}