package Metodi;
import Sudoku.*;
import Enum.Enum;
import Metodi.Metodi;
public class Metodi{
    public static int[] celle_in_comune(int LATO,Cella celle[]){//ritorna un vettore formato dalle posizioni delle celle viste da tutti gli elementi di celle[]
        if(celle.length==0)
            return new int[0];
        if(celle.length==1)
            return celle[0].celle_in_comune(celle[0]);
        return celle[celle.length-1].celle_in_comune(Metodi.accorcio_lunghezza(celle,celle.length-1));
    }    
    public static Cella[] celle_in_comune(int LATO,Cella celle[],Cella sudoku[][]){
        int vet_int[]=Metodi.celle_in_comune(LATO,celle);
        Cella vet_celle[]=new Cella[vet_int.length];
        for(int i=0;i<vet_celle.length;i++)
            vet_celle[i]=sudoku[(int)vet_int[i]/LATO][vet_int[i]%LATO];
        return vet_celle;
    }
   
    public static char inverto_A(char carattere){
        if((int)carattere%2==1)
            return(char)((int)(carattere)+1);
        else
            return(char)((int)(carattere)-1);
    }   
    
    public static int[] numeri_opposti(int vet_num[],int LATO){//ritorna i numeri di un sudoku di lato LATO che non sono in vet_num
        int vet_num1[]=new int[LATO-vet_num.length],cont=0;
        for(int numero=1;numero<=LATO;numero++){
            boolean trovato=false;//dice se numero si trova in vet_num
            for(int i=0;i<vet_num.length && trovato==false;i++)
                if(vet_num[i]==numero)
                    trovato=true;//numero si trova in vet_num quindi numero non va inserito in vet_num1
            if(trovato==false)
                vet_num1[cont++]=numero;
        }
        return vet_num1;
    }
    public static int[] settori_opposti(int vet_settore[],int LATO){
        int vet_settore1[]=new int[LATO-vet_settore.length],cont=0;
        for(int i=0;i<LATO;i++){
            boolean trovato=false;
            for(int j=0;j<vet_settore.length && trovato==false;j++)
                if(i==vet_settore[j])
                    trovato=true;
            if(trovato==false)
                vet_settore1[cont++]=i;
        }
        return vet_settore1;
    }
    public static int[] celle_opposte(int vet_celle[],int LATO){
        return Metodi.settori_opposti(vet_celle,LATO);
    }
    
    public static Cella[] set_numero(int i, int j, int numero,Cella sudoku[][]){
        Sudoku sudoku1=new Sudoku(sudoku.length);
        sudoku1.set_celle(sudoku);
        return sudoku1.set_numero(i,j,numero);
    }
    public static Cella[] set_numero(int posiz,int numero,Cella sudoku[][]){
        return set_numero((int)posiz/sudoku.length,posiz%sudoku.length,numero,sudoku);
    }
    public static Cella[] set_numero(Cella cella,int numero,Cella sudoku[][]){
        return set_numero(cella.get_riga(),cella.get_colonna(),numero,sudoku);
    }
   
    public static int[][] conta_numero(int numero,int vet_settore[],String settore,Cella sudoku[][]){
        Sudoku sudoku1=new Sudoku(sudoku.length);
        sudoku1.set_celle(sudoku);
        return sudoku1.conta_numero(numero,vet_settore,settore);


    }
    
    public static Cella[] aggiungo_cella_al_vettore(Cella vet_celle[],Cella cella){// allunga vet_celle inserendo in fondo al vettore la cella cella
        Cella vet[]=new Cella[1];
        vet[0]=cella;
        return aggiungo_celle_al_vettore(vet_celle,vet);
    }
    public static Cella[] aggiungo_celle_al_vettore(Cella vet_celle[],Cella vet_celle1[]){//gli argomenti del metodo non devono essere null questa opzione non è volutamente contemplata per intercettare gli errori run-time
        if(vet_celle==null && vet_celle1==null)
            return new Cella[0];
        if(vet_celle==null)
            return vet_celle1;
        if(vet_celle1==null)
            return vet_celle;
        Cella temp[]=new Cella[vet_celle.length+vet_celle1.length];
        for(int i=0;i<vet_celle.length;i++)
            temp[i]=vet_celle[i];
        for(int i=0;i<vet_celle1.length;i++)
            temp[vet_celle.length+i]=vet_celle1[i];
        return temp;
    }
    public static int[] aggiungo_int_al_vettore(int vet[],int num){//allunga vet inserendo in fondo al vettore num
        int vet1[]=new int[1];
        vet1[0]=num;
        return aggiungo_int_al_vettore(vet,vet1);
    }
    public static int[] aggiungo_int_al_vettore(int vet[],int vet1[]){
        int temp[]=new int[vet.length+vet1.length];
        for(int i=0;i<vet.length;i++)
            temp[i]=vet[i];
        for(int i=0;i<vet1.length;i++)
            temp[i+vet.length]=vet1[i];
        return temp;
    }
    public static Cella[] rimuovo_cella_al_vettore(Cella vet_celle[],Cella cella){//
        Cella vet[]=new Cella[1];
        vet[0]=cella;
        return rimuovo_celle_al_vettore(vet_celle,vet);
    }
    public static Cella[] rimuovo_celle_al_vettore(Cella vet_celle[],Cella vet_celle1[]){//rimuove da vet_celle gli elementi di vet_celle1
        for(int i=0;i<vet_celle1.length;i++)
            vet_celle=rimuovo_elementi_nella_posizione(vet_celle,dove_elemento(vet_celle,vet_celle1[i],0));
        return vet_celle;
    }
    public static int[] rimiuovo_int_al_vettore(int vet[],int num){//allunga vet inserendo in fondo al vettore num
        int vet1[]=new int[1];
        vet1[0]=num;
        return rimuovo_int_al_vettore(vet,vet1);
    }
    public static int[] rimuovo_int_al_vettore(int vet[],int vet1[]){
        for(int i=0;i<vet1.length;i++)
            vet=rimuovo_elementi_nella_posizione(vet,dove_elemento(vet, vet1[i], 0));
        return vet;
    }
   
    public static int[] accorcio_lunghezza(int vet[],int cont){//se cont==0 ritorna new int[0]
        int temp[]=new int[cont];
        for(int i=0;i<cont;i++)
            temp[i]=vet[i];
        return temp;
    }
    public static Cella[] accorcio_lunghezza(Cella vet[],int cont){
        Cella temp[]=new Cella[cont];
        for(int i=0;i<cont;i++)
            temp[i]=vet[i];
        return temp;
    }
    
    public static boolean ce_elemento(int vet[],int elemento){//ritorna true se ce un elemento di vet il cui valore è elemento
        boolean trovato=false;
        for(int i=0;i<vet.length && trovato==false;i++)
            if(vet[i]==elemento)
                trovato=true;
        return trovato;
    }
    public static boolean ce_elemento(Cella vet[],Cella cella){
        if(vet==null)
            return false;
        boolean trovato=false;
        for(int i=0;i<vet.length && trovato==false;i++)
            if(vet[i]==cella)
                trovato=true;
        return trovato;
    }
    public static int dove_elemento(int vet[],int elemento){//ritorna l'indice della prima cella di vet[] dove c'è elemento altrimenti ritorna -1
        for(int i=0;i<vet.length;i++)
            if(vet[i]==elemento)
                return i;
        return -1;
    }
    public static int dove_elemento(Cella vet[],Cella elemento){
        for(int i=0;i<vet.length;i++)
            if(vet[i]==elemento)
                return i;
        return -1;
    }
    public static int[] dove_elemento(int vet[],int elemento,int inutile){//ritorna un vettore formsto da tutti gli indici delle celle di vet dove puo andare elemento, se elemento non può andare in nessuna cella ritorna new int[0]
        int vettore[]=new int[vet.length],cont=0;
        for(int i=0;i<vet.length;i++)
            if(vet[i]==elemento)
                vettore[cont++]=i;
        return accorcio_lunghezza(vettore,cont);
    }
    public static int[] dove_elemento(cella vet[],Cella elemento,int inutile){
        Cella vettore[]=new Cella[vet.length];
        int cont=0;
        for(int i=0;i<vet.length;i++)
            if(vet[i]==elemento)
                vettore[cont++]=i;
        return accorcio_lunghezza(vettore,cont);
    }

    public static int[] rimuovo_elemento_nella_posizione(int vet[],int index){
        int vet1[]=new int[1];
        vet1[0]=index;
        return rimuovo_elementi(vet,vet1);
    }
    public static int[] rimuovo_elementi_nella_posizione(int vet[],int index[]){//toglie dal vettore gli elementi nele posizioni dettate da index[]
        int new_vet[]=new int[vet.length-index.length],cont=0;
        for(int i=0;i<vet.length;i++)
            if(ce_elemento(index,i)==false)//se vet[i] non si trova oin index metto vet[i] in new_vet
                new_vet[cont++]=vet[i];
        return new_vet;
    }
    public static Cella[] rimuovo_elemento_nella_posizione(Cella vet[],int index){
        int vet1[]=new int[1];
        vet1[0]=index;
        return rimuovo_elementi(vet,vet1);
    }
    public static Cella[] rimuovo_elementi_nella_posizione(Cella vet[],int index[]){//toglie dal vettore gli elementi nele posizioni dettate da index[]
        Cella new_vet[]=new Cella[vet.length-index.length],cont=0;
        for(int i=0;i<vet.length;i++)
            if(ce_elemento(index,vet[i])==false)//se vet[i] non si trova oin index metto vet[i] in new_vet
                new_vet[cont++]=vet[i];
        return new_vet;
    }
    
    public static boolean uguali(int vet1[],int vet2[]){//se vet1 e vet2 hanno gli stessi elementi ritorna true
        if(vet1.length!=vet2.length)
            return false;
        for(int i=0;i<vet1.length;i++)//cerco vet1[vet1.length-1] in vet2 se non lo trova rutorna false
            if(vet1[vet1.length-1]==vet2[i]){
                if(vet1.length==1)
                    return true;
                else
                    return uguali(accorcio_lunghezza(vet1,vet1.length-1),rimuovo_elemento(vet2,i));
            }
        return false;
    }
    public static boolean uguali(Cella vet1[],Cella vet2[]){
        if(vet1.length!=vet2.length)
            return false;
        for(int i=0;i<vet1.length;i++)//cerco vet1[vet1.length-1] in vet2 se non lo trova rutorna false
            if(vet1[vet1.length-1]==vet2[i]){
                if(vet1.length==1)
                    return true;
                else
                    return uguali(accorcio_lunghezza(vet1,vet1.length-1),rimuovo_elemento(vet2,i));
            }
        return false;
    }

    public static Riga get_riga(int index,Cella sudoku[][]){
        Riga riga=new Riga(sudoku.length,index);
        for(int i=0;i<sudoku.length;i++)
            riga.set_cella(sudoku[index][i],i);
        return riga;
    }
    public static Colonna get_colonna(int index,Cella sudoku[][]){
        Colonna colonna=new Colonna(sudoku.length,index);
        for(int i=0;i<sudoku.length;i++)
            colonna.set_cella(sudoku[i][index],i);
        return colonna;
    }
    public static Tabella get_tabella(int index,Cella sudoku[][]){
        Tabella tabella=new Tabella(sudoku.length,index);
        for(int i=0;i<sudoku.length;i++)
            tabella.set_cella(sudoku[tabella.get_cella(i).get_riga()][tabella.get_cella(i).get_colonna()],i);
        return tabella;
    }
    public static int get_riga(int posiz,int LATO){
        return (int)posiz/LATO;
    }
    public static int get_colonna(int posiz,int LATO){
        return posiz%LATO;
    }
    public static int get_tabella(int posiz,int LATO){
        int lato=(int)Math.sqrt(LATO);
        return ((int)(get_riga(posiz,LATO)/lato))*lato+(int)(get_colonna(posiz,LATO)/lato);
    }
    public static int get_riga(Cella cella,int LATO){
        return get_riga(cella.get_posiz(),LATO);
    }
    public static int get_colonna(Cella cella,int LATO){
        return get_colonna(cella.get_posiz(),LATO);
    }
    public static int get_tabella(Cella cella,int LATO){
        return get_tabella(cella.get_posiz(),LATO);
    }
    public static int[] get_ali(Cella cella1,Cella cella2){//se le due celle non hanno candidati in comune ritorna new int[0]
        //se le due celle hanno gli stessi candidati ritorna new int[0]
        //altrimenti ritorna i candidati da cercare in una terza cella per formare xy-wing con cella1,cella2
        int perno=get_perno(cella1,cella2);
        if(perno==-1)
            return new int[0];
        int vet[]=new int[2];
        if(celle1.get_candidati()[0]==perno)
            vet[0]=celle1.get_candidati()[1];
        else
            vet[0]=celle1.get_candidati()[0];
        if(celle2.get_candidati()[0]==perno)
            vet[1]=celle2.get_candidati()[1];
        else
            vet[1]=celle2.get_candidati()[0];
        return vet;
    }
    public static int get_perno(Cella cella1,Cella cella2){
        
        if(Metodi.uguali(cella1.get_candidati(),cella2.get_candidati()))
            return -1;
        if(cella1.get_candidati()[0]!=cella2.get_candidati()[0] && cella1.get_candidati()[0]!=cella2.get_candidati()[1] && cella1.get_candidati()[1]!=cella2.get_candidati()[0] && cella1.get_candidati()[1]!=cella2.get_candidati()[1])
            return -1;            
        if(cella1.get_candidati()[0]==cella2.get_candidati()[0] || cella1.get_candidati()[0]==cella2.get_candidati()[1])
            return cella1.get_candidati()[0];
        if(cella1.get_candidati()[1]==cella2.get_candidati()[0] || cella1.get_candidati()[1]==cella2.get_candidati()[1])
            return cella1.get_candidati()[1];
        return 0;//
    }
    public static Sudoku creo_sudoku(int numero){
        switch(numero){
            case 1: 
                Sudoku sudoku=new Sudoku(9);
                sudoku.set_numero(0,0,2);
                sudoku.set_numero(4,0,5);
                sudoku.set_numero(5,0,1);
                sudoku.set_numero(7,0,7);

                sudoku.set_numero(1,1,3);
                sudoku.set_numero(1,4,2);
                sudoku.set_numero(1,8,1);
                sudoku.set_numero(2,2,5);
                sudoku.set_numero(2,3,3);
                sudoku.set_numero(3,2,6);
                sudoku.set_numero(3,5,3);
                sudoku.set_numero(3,8,4);

                sudoku.set_numero(4,0,8);
                sudoku.set_numero(4,1,2);
                sudoku.set_numero(4,7,1);
                sudoku.set_numero(4,8,6);
                sudoku.set_numero(5,0,4);
                sudoku.set_numero(5,3,6);
                sudoku.set_numero(5,6,3);
                sudoku.set_numero(6,5,2);
                sudoku.set_numero(6,6,4);

                sudoku.set_numero(7,0,6);
                sudoku.set_numero(7,4,4);
                sudoku.set_numero(7,7,3);

                sudoku.set_numero(8,1,1);
                sudoku.set_numero(8,3,9);
                sudoku.set_numero(8,4,6);
                sudoku.set_numero(8,8,2);
                return sudoku;
        }
        return new Sudoku(0);
    }
}