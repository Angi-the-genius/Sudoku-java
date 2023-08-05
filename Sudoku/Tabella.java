package Sudoku;
public class Tabella extends Settore{
    public Tabella(int LATO,int index){
        set_LATO(LATO);
        set_type();
        set_index(index);
        set_celle();
        
    }
    public void set_celle()
    {
        super.set_celle();
        int temp=(get_index()-get_index()%get_lato())*get_LATO()+(get_index()%get_lato())*get_lato();//prima cella in alto a sinistra
        for(int i=0;i<get_lato();i++)
            for(int j=0;j<get_lato();j++)
                super.set_cella(temp+i*get_LATO()+j,i*get_lato()+j);
    }
    public void set_type(){
        super.set_type("Tabella");
    }
    
}