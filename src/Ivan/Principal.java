
package Ivan;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Principal extends Applet {
    String archivoEntrada="entrada2.txt";
    int nodos,aristas;
    Ciudad ciudades[];
    int caminos[][];
    int cOrigen,cDestino,ultimaC;
    Ventana v;
    
    @Override
    public void init() {
       setBackground(Color.orange);
       recojerParametros();
       v=new Ventana(this);
//       Pruebas
//       mostrarConjuntoCiudades();
//       mostrarConjuntoCaminos();
       
       
    }
    @Override
    public void paint(Graphics g){
        for(int i=0;i<nodos;i++){
            g.setColor(Color.black);
            g.fillOval(ciudades[i].getX(),ciudades[i].getY(),8,8);
        }
        for(int i=0;i<caminos.length;i++){
           g.setColor(Color.black);
           g.drawLine(ciudades[caminos[i][0]-1].getX()+4,ciudades[caminos[i][0]-1].getY()+4, ciudades[caminos[i][1]-1].getX()+4, ciudades[caminos[i][1]-1].getY()+4);  //Mover 4px para que el camino comienze desde el centro del punto
        }
        
        if(cOrigen>0 && cDestino>0 && cOrigen<=nodos && cDestino<=nodos){
            calcularMejorRuta(cOrigen,cDestino,g);
        }
    }
    
    public void recojerParametros(){
       try{
           FileReader fr=new FileReader(archivoEntrada);
           BufferedReader br=new BufferedReader(fr);
           int lineas=1,numCiudad=1,contC=0,numCamino=0;
           String siguienteLinea;
           while((siguienteLinea=br.readLine())!=null){
               if(!siguienteLinea.equals("")){
                   if(lineas==1){
                        this.nodos=Integer.parseInt(siguienteLinea);
                        ciudades=new Ciudad[Integer.parseInt(siguienteLinea)];
                        System.out.println("Hay "+ciudades.length+" Ciudades");
                   }else if(lineas==2){
                        this.aristas=Integer.parseInt(siguienteLinea);
                        caminos=new int[Integer.parseInt(siguienteLinea)][3];
                        System.out.println("Hay "+caminos.length+" Caminos");
                   }else if(lineas==3) {
                        String x = siguienteLinea.substring(0,siguienteLinea.length()/2);
                        String y = siguienteLinea.substring(siguienteLinea.length()/2,siguienteLinea.length());
//                        cOrigen=quitarEspacios(x);
//                        cDestino=quitarEspacios(y);
                        System.out.println("Ciudad Origen: "+cOrigen);
                        System.out.println("Ciudad Destino: "+cDestino);
                   }else if (lineas>3 && lineas<=nodos+3){
                       String x = siguienteLinea.substring(0,siguienteLinea.length()/2);
                       String y = siguienteLinea.substring(siguienteLinea.length()/2,siguienteLinea.length());
                       int r=quitarEspacios(x);
                       int z=quitarEspacios(y);
                       ciudades[contC]= new Ciudad(r,z,numCiudad);
                       contC++;
                       numCiudad++;
                   }else{
                       String s=siguienteLinea;
                       int aux[];
                       aux=recojeCaminos(siguienteLinea);
                       caminos[numCamino][0]=aux[0];
                       caminos[numCamino][1]=aux[1];
                       caminos[numCamino][2]=aux[2];
                       numCamino++;
                   }
                   lineas++;
               }
           } 
           System.out.println("Total lineas:  "+lineas);
           br.close();
       }catch(IOException e){
           System.out.println("Error---"+e.toString());
	}
    }
    public int quitarEspacios(String x){
        String aux="";
        for(int i=0;i<x.length();i++){
           String a = String.valueOf(x.charAt(i));
           if(!a.contains(" ")){
               aux = aux.concat(a);
           }
        }
        return Integer.parseInt(aux);
    }
    
    public int[] recojeCaminos(String s){
        int aux[]=new int[3];
        int cont=0;
        String auxS="";
        for(int i=0;i<s.length();i++){
            String a = String.valueOf(s.charAt(i));
            if(!a.contains(" ")){
               auxS = auxS.concat(a);
               if(i==(s.length()-1)){
                    aux[cont]=Integer.parseInt(auxS);
                }
            }else{
                aux[cont]=Integer.parseInt(auxS);
                cont++;
                auxS="";
            }
        }
        return aux;
    }
    
    public void calcularMejorRuta(int origen,int destino,Graphics g){
        int totalD=0;
        Ciudad ciudadO=devuelveCiudadNodo(origen);
        Ciudad ciudadD=devuelveCiudadNodo(destino);
        Ciudad aux2=new Ciudad(ciudadO.getX(),ciudadO.getY(),ciudadO.getNodo());
        Ciudad aux=new Ciudad(ciudadO.getX(),ciudadO.getY(),ciudadO.getNodo());
        System.out.println("Ciudad Origen X="+ciudadO.getX()+" Y:"+ciudadO.getY()+" N:"+ciudadO.getNodo());
        System.out.println("Ciudad Destino X="+ciudadD.getX()+" Y:"+ciudadD.getY()+" N:"+ciudadD.getNodo());
        pintarOrigenDestino(ciudadO,ciudadD,g);
        System.out.print("["+aux.getNodo()+"]  ");
        calcularPosiblesAristas(ciudadO.getNodo());
        calcularPosiblesAristas(ciudadD.getNodo());
        while(aux.getNodo()!=ciudadD.getNodo()){
            aux=calcularSiguienteMejorNodo(aux,ciudadD,g);
            System.out.print("["+aux.getNodo()+"]  ");
            pintarCamino(aux2.getNodo(),aux.getNodo(),g);
            int d=calcularDistancia(aux2,aux);
            System.out.print("  Distancia:"+d+"  ");
            totalD=totalD+d;
            aux2=aux;
            try{
                Thread.currentThread().sleep(200);
            }catch(InterruptedException ie){
                System.out.println("Error---------"+ie.toString());
            }
        }
        System.out.println();
        System.out.println("Distancia total recorrida: "+totalD);
        String res = "Distancia total recorrida: "+totalD;
        g.drawString(res,this.getWidth()-200,this.getHeight()-10);
    }
    
    public Ciudad devuelveCiudadNodo(int n){
        Ciudad cAux=new Ciudad();
        for(int i=0;i<ciudades.length;i++){
            if(ciudades[i].getNodo()==n){
                cAux.setNodo(ciudades[i].getNodo());
                cAux.setX(ciudades[i].getX());
                cAux.setY(ciudades[i].getY());
            }
        }
        return cAux;
    }
    
    public void pintarOrigenDestino(Ciudad cO,Ciudad cD,Graphics g){
        g.setColor(Color.green);
        g.fillOval(ciudades[cO.getNodo()-1].getX(),ciudades[cO.getNodo()-1].getY(),8,8);
        g.setColor(Color.red);
        g.fillOval(ciudades[cD.getNodo()-1].getX(),ciudades[cD.getNodo()-1].getY(),8,8);
    }
    
    public void pintarCamino(int nodoO,int nodoD,Graphics g){
        
        g.setColor(Color.RED);
        g.drawLine(ciudades[nodoO-1].getX()+4,ciudades[nodoO-1].getY()+4,ciudades[nodoD-1].getX()+4,ciudades[nodoD-1].getY()+4);
    }
    
    
    
    public Ciudad calcularSiguienteMejorNodo(Ciudad cO,Ciudad cD,Graphics g){
        Ciudad c=new Ciudad();
        if(!cO.equals(cD)){
            int a[]=calcularPosiblesAristas(cO.getNodo());
            int aux[]=new int[a.length];
            for(int i=0;i<a.length;i++){
                if(caminos[a[i]][0] != cO.getNodo()){
                    aux[i]=caminos[a[i]][0];
                }else{
                    aux[i]=caminos[a[i]][1];
                }
            }
            c=calcularMejorTrayecto(aux,cD.getNodo());
        }
        return c;
    }
    
    public int[] calcularPosiblesAristas(int x){
        
        int cont=0;
        for(int i=0;i<caminos.length;i++){
            if(caminos[i][0]==x || caminos[i][1]==x){
                    cont++;
            }
        }
        int aux[]=new int[cont];
        cont=0;
        for(int i=0;i<caminos.length;i++){
            if(caminos[i][0]==x || caminos[i][1]==x){
                aux[cont]=i;
                cont++;
            }
        }
        return aux;
    }
    
    public Ciudad calcularMejorTrayecto(int posiblesN[],int d){
        Ciudad c=new Ciudad();
        int aux=0,cont=0;
        for(int i=0;i<posiblesN.length;i++){
//            int difX=Math.abs((ciudades[posiblesN[i]-1].getX())-ciudades[d-1].getX());
//            int difY=Math.abs((ciudades[posiblesN[i]-1].getY())-ciudades[d-1].getY());
//            int total=difX+difY;
            double difX=Math.abs((ciudades[posiblesN[i]-1].getX())-ciudades[d-1].getX());
            double difY=Math.abs((ciudades[posiblesN[i]-1].getY())-ciudades[d-1].getY());
            int total=(int)(Math.sqrt((Math.pow(difX,2))+(Math.pow(difY,2))));
            if(cont==0){
                aux=total;
                c=new Ciudad(ciudades[posiblesN[i]-1].getX(),ciudades[posiblesN[i]-1].getY(),ciudades[posiblesN[i]-1].getNodo());
                cont++;
            }else if(cont>0){
                if(total<=aux){
                    c=new Ciudad(ciudades[posiblesN[i]-1].getX(),ciudades[posiblesN[i]-1].getY(),ciudades[posiblesN[i]-1].getNodo());
                    aux=total;
                }
            }
        }
        return c;
    }
    
    public int calcularDistancia(Ciudad o,Ciudad d){
        int distancia=0;
        for(int i=0;i<caminos.length;i++){
            if(caminos[i][0]==o.getNodo() || caminos[i][1]==o.getNodo()){
                if(caminos[i][0]==d.getNodo() || caminos[i][1]==d.getNodo())
                    distancia=caminos[i][2];
            }
        }
        return distancia;
    }
    
  
    
    //Metodos para pruebas
    public void mostrarConjuntoCiudades(){
        for(int i=0;i<ciudades.length;i++){
            System.out.println("Nº nodo:"+(ciudades[i].getNodo())+"  Coord X Y: "+ciudades[i].getX()+" "+ciudades[i].getY());
        }
    }
    
    public void mostrarConjuntoCaminos(){
        System.out.println("Tamaño caminos: "+caminos.length);
        for(int i=0;i<caminos.length;i++){
            System.out.println("Punto 1: "+caminos[i][0]+"  Punto 2: "+caminos[i][1]+"  Distancia: "+caminos[i][2]);
        }
    }
}

class Ventana extends Frame{
    
    Principal p;
    Label l1,l2,l3;
    Button b1,b2;
    TextField t1,t2;
    Panel pNorte,pCentro,pSur;
     
    Ventana(Principal a){
        this.p=a;
        setLayout(new BorderLayout());
        l1=new Label("Origen: ");
        l1.setBackground(Color.WHITE);
        t1=new TextField(5);
        pNorte=new Panel(new FlowLayout());
        pNorte.add(l1);
        pNorte.add(t1);
        l2=new Label("Destino: ");
        l2.setBackground(Color.WHITE);
        t2=new TextField(5);
        pCentro = new Panel(new FlowLayout());
        pCentro.add(l2);
        pCentro.add(t2);
        b1=new Button("Calcular");
        b2=new Button("Borrar");
        crearBotones();
        pSur=new Panel(new FlowLayout());
        pSur.add(b1);
        pSur.add(b2);
        
        add(pNorte,BorderLayout.NORTH);
        add(pCentro,BorderLayout.CENTER);
        add(pSur,BorderLayout.SOUTH);
        setTitle("GPS");
        setBackground(Color.BLUE);
        setBounds(820, 450, 500,250);
        setVisible(true);
        
//        addWindowListener(new WindowAdapter(){                    Comentado para no permitir cierre de la ventana
//            @Override
//            public void windowClosing(WindowEvent e){
//                 dispose();
//            }
//        });
    }
    
    public void crearBotones(){ 
        b1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                actualizarOrigenDestino();
                System.out.println("Presionado el boton b1");
            }
        });
        
        b2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                borrar();
                System.out.println("Presionado el boton b2");
            }
        });
    }
    
    public void actualizarOrigenDestino(){
        try{
            int o=Integer.parseInt(t1.getText());
            int d=Integer.parseInt(t2.getText());
            System.out.println("Ha elejido calcular de "+o+" a "+d);
            p.cOrigen=o;
            p.cDestino=d;
            p.repaint();
        }catch(NumberFormatException nfe){
            System.out.println("Introduce un numero entre 1 y "+p.nodos+", no carácteres");
        }
    }
    
    public void borrar(){
        t1.setText("");
        System.out.println("Borrado de "+t1.getName());
        t2.setText("");
        System.out.println("Borrado de "+t2.getName());
        p.cDestino=0;
        p.cOrigen=0;
        p.repaint();
    }
}

