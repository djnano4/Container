package simulador;

import java.util.ArrayList;

public class Aeropuerto extends Dibujable{
    
    private final ArrayList<Dibujable> objDibujables = new ArrayList<>();
    //si ponemos final en una clase quiere decir q no puede haber clases q la hereden.
    public Aeropuerto()
    {
    //constructor, de momento vacío.
    }
    
    @Override//Indicamos que vamos a redifinir en esta clase hija un metodo que
    //tiene la clase padre. Cuando se llame a estos metodos, las clases hijas
    //ejecutaran el metodo redefinido en estas y en las que no se hayan redefinido
    //se ejecutara el de clase padre.
    public void draw() 
    {
       System.out.println("Dibujando Aeropuerto... ");
    }
    
    public void creador_pista (float pos_x, float pos_y, float pos_z)
    {
        Pista pista = new Pista (pos_x,pos_y,pos_z,1);
        System.out.println("Pista añadida"); 
        objDibujables.add(pista);
    }
     
    public void creador_avion(float pos_x, float pos_y, float pos_z, float ide_vuelo) {
        Avion avion = new Avion(pos_x,pos_y,pos_z,2,ide_vuelo);
        System.out.println("Avión añadido");
        objDibujables.add(avion);
    }    
    
    public void creador_torre(float pos_x, float pos_y, float pos_z){
        Torre torre = new Torre (pos_x,pos_y,pos_z,3);
        System.out.println("Torre añadida");
        objDibujables.add(torre);
    }
    
    public ArrayList<Dibujable> pull_type (int tipo_in) 
    {
        //Array para guardar los objetos dibujables que quieras sacar.
        final ArrayList<Dibujable> outDibujables;
        outDibujables = new ArrayList<>();
        //Buscamos en el array los objetos de ese tipo.
        //Recuerda pista1 avion2 torre3
        for (int i = 0; i < objDibujables.size(); i++)
        {
            //la forma de coger el objeto es con get.
            if (tipo_in == objDibujables.get(i).get_tipo()){
                outDibujables.add(objDibujables.get(i));
            } 
        }
        return outDibujables;
        
    }

    public void come_alive()
    {
        //Creamos los objetos en las posiciones deseadas:
        creador_pista(3,2,2);
        creador_avion(1,1,1,0001);
        creador_avion(2,1,1,0002);
        creador_torre(2,2,2);
        
        while(true) {
        //Dibujamos cada uno de los objetos creados:
        draw();
        for (int i = 0; i < objDibujables.size(); i++)
        {
            objDibujables.get(i).draw();
            
        }
        //Imprimimos aviones y posiciones:
        System.out.println("En el Aeropuerto hay: " + pull_type(2).size());
        for (int i = 0; i < pull_type(2).size(); i++)
        {
            System.out.println("Avion "+ i + " (" + 
                    pull_type(2).get(i).get_x() + "," +
                    pull_type(2).get(i).get_y() + "," +
                    pull_type(2).get(i).get_z() + ")");   
        }
        }
    }
}
