package Sudoku;
public class Colonna extends Settore{
    
    public Colonna(int LATO,int index){
        set_LATO(LATO);
        set_type();
        set_index(index);
        set_celle();
        
    }
    public void set_celle()
    {
        super.set_celle();
        for(int i=0;i<get_LATO();i++)
            super.set_cella(get_index()+i*get_LATO(),i);
    }
    public void set_type(){
        super.set_type("Colonna");
    }
    
}