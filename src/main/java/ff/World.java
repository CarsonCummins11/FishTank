package ff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class World extends JPanel implements MouseMotionListener {
    public static final int SIZE = 10;
    ArrayList<MessageHolder> messagequeue;
    volatile ArrayList<Person> people = new ArrayList<>();
    ArrayList<Food> foodlist = new ArrayList<>();
    int steps;
    int mx = 0;
    int my = 0;
    String dt = "";
    public void submitMessage(double d, Person p) {
        char message = (char) Math.round((d * 27) + 'a');
        messagequeue.add(new MessageHolder(message, p));
    }

    public World() {
        super();
        steps = 0;
        messagequeue = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            people.add(Person.random(this));
        }
    }

    public void distributeMessages() {
        while (messagequeue.size() > 0) {
            MessageHolder h = messagequeue.get(0);
            messagequeue.remove(0);
            getClosest(h.p).mes = (double) h.message;
        }
    }

    private Person getClosest(Person p) {
        Person ret = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Person d : people) {
            if(d!=p){
                double dd = Math.sqrt((p.x - d.x) * (p.x - d.x) + (p.y - d.y) * (p.y - d.y));
                if (dd < dist)
                    ret = d;
            }
        }
        return ret;
    }

    public void step() {
        steps++;
        if (steps % 5 == 0) {
            for (Food f : foodlist) {
                f.eaters = new ArrayList<>();
            }
        }
        // have some sexy times
        ArrayList<Person> pnew = new ArrayList<>();
        for (Person p : people) {
            Person d = getClosest(p);
            double dist = Math.sqrt((p.x - d.x) * (p.x - d.x) + (p.y - d.y) * (p.y - d.y));
            if (dist < 1 && d.food > 100 && p.food > 100 && Math.random() < .9) {
                pnew.add(p.breed(d));
                System.out.println(p.name+" and "+d.name+" had a baby, they were" + dist + "apart");
            }
        }
        people.addAll(pnew);
        // cook some food
        if (Math.random() <.8&&foodlist.size()<200) {
            Food f = new Food(this, (int) (Math.random() * 600), (int) (Math.random() * 600));
            foodlist.add(f);
        }
        distributeMessages();
        ArrayList<Person> tokill = new ArrayList<>();
        for (Person p : people) {
            p.step(buildInput(p));
            // check if they eating
            for (Food f : foodlist) {
                if (p.containspoint(f.x, f.y))
                    f.addEater(p);
            }
            if (p.food < 0)
                tokill.add(p);
            if(within_N(p,8).size()>5){
                tokill.add(within_N(p,8).get(0));
            }
        }
        for (Person p : tokill) {
            if(people.contains(p))people.remove(p);
        }
    }
    private ArrayList<Person> within_N(Person p,int distance) {
        ArrayList<Person> ret = new ArrayList<>();
        for (Person d : people) {
            if(d!=p){
                double dd = Math.sqrt((p.x - d.x) * (p.x - d.x) + (p.y - d.y) * (p.y - d.y));
                if (dd < distance)
                    ret.add(d);
            }
        }
        return ret;
    }

    private double[] buildInput(Person p) {
        final int rc = Person.VISION_WIDTH - 1;
        double[] ret = new double[Person.VISION_WIDTH];
        // raycast for whole fov
        for (int i = -rc / 2; i < rc / 2; i++) {
            int index = i + 1 + rc / 2;
            double angle = Math.toRadians((int) Math.round(p.direction) + i);
            double ycomp = Math.sin(angle);
            double xcomp = Math.cos(angle);
            double rx = p.x;
            double ry = p.y;
            for (int k = 0; k < 50; k++) {
                rx += xcomp;
                ry += ycomp;
                ret[index] = colliding(rx, ry, p);
                if (ret[index] != 0)
                    break;
                if (k == 49)
                    ret[index] = 0;
            }
        }
        ret[0] = p.mes;
        p.mes = -1;
        return ret;
    }

    private int colliding(double rx, double ry, Person p) {
        if (rx > 600 || rx < 0 || ry < 0 || ry > 600)
            return -1;
        for (Food f : foodlist) {
            if (f.containspoint(rx, ry))
                return 2;
        }
        for (Person c : people) {
            if (c != p) {
                if (c.containspoint(rx, ry))
                    return 1;
            }
        }
        return 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(33, 150, 60));
        g.fillRect(0, 0, 600, 600);
        Person ip = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Person p : people) {
            double dd = Math.sqrt((p.x - mx) * (p.x - mx) + (p.y - my) * (p.y - my));
            if(dd<dist){
                ip = p;
                dist = dd;
            }
            p.draw(g);
        }
        dt = ip.name+","+ip.food;
        ArrayList<Food> torot = new ArrayList<>();
        for (Food f : foodlist) {
            f.draw(g);
            if(f.req_hunters==0)torot.add(f);
        }
        for(Food f : torot){
            foodlist.remove(f);
        }
        g.drawString(dt, mx, my);
        dt = "";
    }

    class MessageHolder {
        char message;
        Person p;

        public MessageHolder(char m, Person e) {
            message = m;
            p = e;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }
}
