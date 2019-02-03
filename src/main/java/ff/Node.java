package ff;

import java.util.ArrayList;

public class Node {
    private double cur;
    private ArrayList<Double> weights;
    private ArrayList<Double> inputvalues;
    private ArrayList<Node> connections;
    private double bias;
    public Node(){
        cur = 0;
        bias = Math.random();
    }
    public void setConnections(ArrayList<Node> c){
        connections = c;
        weights = new ArrayList<>();
        for(Node n:c){
            weights.add((Math.random()<.5?-1:1)*Math.random());
        }
    }
    public void setCur(double d){
        cur = d;
    }
    public void prepare(){
        inputvalues = new ArrayList<Double>();
        for(Node d : connections){
            inputvalues.add(d.getCurrent());
        }
    }
    public void activate(){
        double v = 0;
        for(int i = 0; i<inputvalues.size(); i++){
            v+=inputvalues.get(i)*weights.get(i);
        }
        //v+=bias;
        cur = sigmoid(v);
    }
    public double getCurrent(){
        return cur;
    }
    private double sigmoid(double input){
        return input==0?0:1/(1+Math.exp(1-5*input));
    }
    public Node breed(Node o){
        Node ret = new Node();
        ret.weights = new ArrayList<>();
        for(int i=0; i<weights.size();i++){
            ret.weights.add(((weights.get(i)+o.weights.get(i))/2+(Math.random()<.01?Math.random():0))%1);
        }
        return ret;
    }

}