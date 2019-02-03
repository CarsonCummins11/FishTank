package ff;

import java.util.ArrayList;

public class FF{
    Node[][] net;
    public FF(int height, int width){
        net = new Node[height][width];
        //initialize net
        for(Node[] d : net){
            for(int i = 0; i<d.length;i++){
                d[i] = new Node();
            }
        }
        //connect net
        for(int i = 0; i<net.length;i++ ){
            for(int j = 0; j<net[i].length;j++){
                ArrayList<Node> cons = new ArrayList<>();
                if(i-1>=0)cons.add(net[i-1][j]);
                if(i+1<net.length)cons.add(net[i+1][j]);
                if(j-1>=0)cons.add(net[i][j-1]);
                if(j+1<net[0].length)cons.add(net[i][j+1]);
                net[i][j].setConnections(cons);
            }
        }
    }
    public FF(Node[][] nn){
        //connect net
        for(int i = 0; i<nn.length;i++ ){
            for(int j = 0; j<nn[i].length;j++){
                ArrayList<Node> cons = new ArrayList<>();
                if(i-1>=0)cons.add(nn[i-1][j]);
                if(i+1<nn.length)cons.add(nn[i+1][j]);
                if(j-1>=0)cons.add(nn[i][j-1]);
                if(j+1<nn[0].length)cons.add(nn[i][j+1]);
                nn[i][j].setConnections(cons);
            }
        }
        net = nn;
    }
    public double[][] values(){
        double[][] ret = new double[net.length][net[0].length];
        for(int i = 0; i<ret.length; i++){
            for(int j = 0; j<ret[0].length;j++){
                ret[i][j] = net[i][j].getCurrent();
            }
        }   
        return ret;
             
    }
    public void train(){

    }
    public void setInputs(double[] inputs){
        for(int i = 0; i< inputs.length; i++){
            net[0][i].setCur(inputs[i]);
        }
    }
    public double[] step(){
        for(Node[] d : net){
            for(int i = 0; i<d.length;i++){
                d[i].prepare();
            }
        }
        for(Node[] d : net){
            for(int i = 0; i<d.length;i++){
                d[i].activate();
            }
        }
        double[] ret = new double[net.length];
        for(int i = 0; i<ret.length; i++){
            ret[i] = net[i][net[0].length-1].getCurrent();
        }
        return ret;
    }
    public FF breed(FF o){
        Node[][] retnet = new Node[net.length][net[0].length];
        for(int i = 0; i<retnet.length;i++){
            for(int j=0;j<retnet[0].length;j++){
                retnet[i][j]=net[i][j].breed(o.net[i][j]);
            }
        }
        return new FF(retnet);
    }

}