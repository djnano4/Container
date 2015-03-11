package simulador;

public class Main {
    public static void main(String[] args)
    {
        Simulador simula = new Simulador();//Creo.
        
        simula.initGL(); //Se suele hacer en estos pasos.
        
        simula.run();     
    }    
}
