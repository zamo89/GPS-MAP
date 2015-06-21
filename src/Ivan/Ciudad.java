/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ivan;


public class Ciudad {
    
    private int x,y,nodo;
    
    Ciudad(){

    }
    
    Ciudad(int x,int y,int nodo){
        this.x=x;
        this.y=y;
        this.nodo=nodo;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getNodo() {
        return nodo;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setNodo(int nodo) {
        this.nodo = nodo;
    }
    
    
}
