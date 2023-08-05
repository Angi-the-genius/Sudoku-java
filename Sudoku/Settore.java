package Sudoku;
import Enum.Enum;
import Metodi.Metodi;
import javax.crypto.IllegalBlockSizeException;
import javax.lang.model.util.ElementScanner14;
import java.util.Scanner;
public class Settore extends Gruppo{
    
    public static void main(String args[]){
        int LATO=9;
        int lato=(int)Math.sqrt(LATO);
        Sudoku sudoku=new Sudoku(LATO);
        sudoku.leggo();
        if(sudoku.risolvo())
            System.out.println("Applicato x-wing");
        else    
            System.out.println("Non applicato x-wing");
        sudoku.stampo();
    }
    private int index;
    public int get_index(){
        return this.index;
    }
    public void set_index(int index){
        this.index=index;
    }
    private String type;
    public String get_type(){
        return type;
    }
    public void set_type(String type){
        this.type=type;
    }

     //TECNICHE RISOLUTIVE
    public boolean slittamento(Cella sudoku[][]){//ritorna il vettore di celle dove ha messo numeri
        for(int numero=1;numero<=get_LATO();numero++)
            if(slittamento(numero,sudoku)){
                slittamento(sudoku);
                return true;
            }
        return false;
    }
    public boolean slittamento(int numero,Cella sudoku[][]){//NON PUO ESISTERE slittamento()perche non si riesce ad inserire il numero e ce bisogno della classe sudoku
        System.out.println("Provo a fare slittamento del numero "+numero+" nella "+get_type()+" "+get_index());
        if(conta_numero(numero)==1 && ce_numero(numero)==false){
            System.out.println("Applico slittamento il numero "+numero+" nella cella "+dove_numero(numero,0)[0].get_posiz());
            Cella celle[]=Metodi.set_numero(dove_numero(numero,0)[0],numero,sudoku);//contiene le celle da cui set_numero ha rimosso un candidato
            Sudoku.post_slittamento(numero,celle,sudoku);//se celle.length==0 non ce problema
            return true;
        }
        return false;
    }
    public boolean naked_pairs(int numero,Cella sudoku[][]){
        System.out.println("Provo a fare naked-pairs del numero "+numero+" nella "+get_type()+" "+get_index());
        int cont=0;
        Cella vettore[]=dove_numero(numero,0),vettore_di_celle[]=new Cella[3*get_LATO()];//celle del settore dove puo andare numero
        if(vettore.length>=2 && vettore.length<=get_lato()){
            Cella celle_comune[]=Metodi.celle_in_comune(get_LATO(),vettore,sudoku);
            for(int i=0;i<celle_comune.length;i++)
                if(celle_comune[i].rimuovi_candidato(numero)){
                    System.out.println("Applico naked-pairs il "+numero+" non puo andare nella cella "+celle_comune[i].get_posiz()+" perche' la "+get_type()+" "+get_index()+" forma naked-pairs");
                    vettore_di_celle[cont++]=celle_comune[i];//celle accumula le celle da cui si e rimosso un candidato
                }
            if(cont>0){
                Sudoku.post_naked_pairs(Metodi.accorcio_lunghezza(vettore_di_celle,cont),sudoku);
                return true;
            }
        }
        return false;
    }
    public boolean coppie(Cella sudoku[][]){
        //il metodo toglie i candidati dalle celle una volta applicata la tecnica e ritorna il vettore dii celle da dove a tolto un candidato
        System.out.println("Provo a fare coppie nella "+get_type()+" "+get_index());
        int vet_num[]=quali_numeri();//contiene i numeri che possono andare nel settore
        boolean applicato=false;
        if(vet_num.length==1)
            return slittamento(vet_num[0],sudoku);
        else {
            int vet[]=new int[2];
            for(int i=0;i<vet_num.length-1;i++)
                for(int j=i+1;j<vet_num.length;j++){
                    vet[0]=vet_num[i];
                    vet[1]=vet_num[j];
                    if(coppie(vet,sudoku))
                        applicato=true;
                }
        }
        return applicato;
    }
    public boolean coppie(int vet_num[],Cella sudoku[][]){//Vet_num.length non è 1
        //ritorna un vettore formato dalle celle dalle quali è stato rimosso un candidato
        Cella vet_celle[]=dove_numeri(vet_num,0),vettore_di_celle[]=new Cella[2*get_LATO()];
        int cont=0;
        System.out.print("Invoco coppie ai numeri: ");
        for(int i=0;i<vet_num.length;i++)
            System.out.print(vet_num[i]+" ");
        System.out.print(".che possono andare nelle celle: ");
        for(int i=0;i<vet_celle.length;i++)
            System.out.print(vet_celle[i].get_posiz()+" ");
        System.out.println("");
        boolean applicato=false;
        if(vet_num.length==vet_celle.length){//i numeri di vet_num formano coppie nelle celle di vet_celle e vet_num.length non è 1
            Cella celle_comune[]=Metodi.celle_in_comune(get_LATO(), vet_celle,sudoku);//celle_comune contiene le celle viste da tutti gli elementi di vet_celle
            for(int i=0;i<celle_comune.length;i++){//tolgo dalle celle viste da vet_celle i numeri di vet_num
                int vet[]=celle_comune[i].rimuovi_candidato(vet_num);//contiene i numeri di vet_num che sono stati rimossi da rimuovi_candidato()
                if(vet.length>0){
                    System.out.print("Applicato coppie: tolgo candidati dalla cella "+celle_comune[i].get_posiz()+". candidati rimossi: ");
                    for(int j=0;j<vet.length;j++)
                        System.out.print(vet[j]+" ");
                    System.out.println("");
                    vettore_di_celle[cont++]=celle_comune[i];
                }
            }
            if(cont>0)
                Sudoku.post_coppie(vet_num,Metodi.accorcio_lunghezza(vettore_di_celle,cont),sudoku);
            int vet_num1[]=Metodi.numeri_opposti(vet_num,get_LATO());
            for(int i=0;i<vet_celle.length;i++){//rimuovo, dalle celle dove vanno i numeri di vet_num, i numeri che non vanno in vet_num
                int vet[]=vet_celle[i].rimuovi_candidato(vet_num1);
                if(vet.length>0){
                    System.out.print("Applicato coppie: tolgo candidato dalla cella "+vet_celle[i].get_posiz()+". Candidati: ");
                    for(int j=0;j<vet.length;j++)
                        System.out.print(vet[j]+" ");
                    System.out.println("");
                }
            }
        }else{
            for(int i=vet_num[vet_num.length-1]+1;i<=get_LATO();i++)//se vet_num==LATO semplicemente non entra nel for
                if(ce_numero(i)==false)
                    return coppie(Metodi.aggiungo_int_al_vettore(vet_num,i),sudoku);
        }
        return applicato;
    }
    public boolean x_wing(int numero,Cella sudoku[][]){
        boolean applicato=false;
        Settore settore=new Riga(sudoku.length,0);
        for(int i=get_index()+1;i<get_LATO();i++){//scorre sulle righe
            if(get_type()=="Riga")
                settore=Metodi.get_riga(i,sudoku);
            else if(get_type()=="Colonna")
                settore=Metodi.get_colonna(i,sudoku);
            else{
                System.out.println("Errore: invocato x-wing alle tabelle");
                return false;
            }
            if(settore.ce_numero(numero)==false){
                int vet_settore[]=new int[1];
                vet_settore[0]=i;
                if(x_wing(numero,vet_settore,sudoku))
                    applicato=true;
            }     
        }
        return applicato;
    }
    public boolean x_wing(int numero,int vet_settore[],Cella sudoku[][]){
        int vet_celle[]=Sudoku.x_wing_generale(conta_numero(numero,vet_settore,get_type(),sudoku));
        System.out.print("Cerco di fare x-wing alle "+get_type()+" numero: "+get_index()+" ");
        for(int i=0;i<vet_settore.length;i++)
            System.out.print(vet_settore[i]+" ");
        System.out.print(".Nelle celle: ");
        for(int i=0;i<vet_celle.length;i++)
            System.out.print(vet_celle[i]+" ");
        System.out.println("");
        boolean applicato=false;
        if(vet_settore.length+1==vet_celle.length){//i settori coinvolti sono il settore invocante e i settori di vet_settore per un totale di vet_settore.length+1
            int vet_settore1[]=settori_opposti(vet_settore),cont=0;
            Cella celle[]=new Cella[get_LATO()*get_LATO()];//vettore che contiene le celle di sudoku da cui e stato rimosso un candidato
            for(int i=0;i<vet_celle.length;i++){
                Settore settore=new Riga(get_LATO(),0);
                if(get_type()=="Riga")
                    settore=Metodi.get_colonna(vet_celle[i],sudoku);
                else
                    settore=Metodi.get_riga(vet_celle[i],sudoku);
                System.out.print("Cerco di rimuovere il numero "+numero+" dalle celle: ");
                for(int j=0;j<vet_settore1.length;j++)
                    System.out.print(vet_settore1[j]);
                System.out.println(" della "+settore.get_type()+" "+settore.get_index());
                int vettore[]=settore.rimuovi_candidato(numero,vet_settore1);//contiene gli indici delle celle del settore dalle quali si è eliminato un candidato
                
                if(vettore.length>0){
                    for(int j=0;j<vettore.length;j++)
                        celle[cont++]=sudoku[settore.get_cella(vettore[j]).get_riga()][settore.get_cella(vettore[j]).get_colonna()];
                    System.out.print("Applicato x-wing rimosso il numero "+numero+" dalle celle ");
                    for(int j=0;j<vettore.length;j++)
                        System.out.print(vettore[j]+" ");
                    System.out.println(" della "+settore.get_type()+" "+settore.get_index());
                    applicato=true;
                }
            }
            Cella temp[]=new Cella[cont];
            for(int i=0;i<cont;i++)
                temp[i]=celle[i];
            Sudoku.post_x_wing(numero,temp,sudoku);
        }else{
            for(int i=vet_settore[vet_settore.length-1]+1;i<get_LATO();i++){//se vet_num==LATO semplicemente non entra nel for
                Settore settore=new Riga(get_LATO(),0);
                if(get_type()=="Riga")
                    settore=Metodi.get_riga(i,sudoku);
                else if(get_type()=="Colonna")
                    settore=Metodi.get_colonna(i,sudoku);
                if(settore.ce_numero(numero)==false){
                    int new_vet[]=new int[vet_settore.length+1];
                    for(int j=0;j<vet_settore.length;j++)
                        new_vet[j]=vet_settore[j];
                    new_vet[vet_settore.length]=i;
                    return x_wing(numero,new_vet,sudoku);
                }
            }
        }
        return applicato;
    }
    public boolean xy_wing(int i,int j,Cella sudoku[][]){//vedere se si fa xy_wing fra la cella i e la cella y del settore
        //la cella i-esima è il perno e la cella j l'ala il metodo trova una erza cella vista dal perno
        boolean applicato=false;
        if(get_cella(i).get_candidati()[0]!=get_cella(j).get_candidati()[0] && get_cella(i).get_candidati()[0]!=get_cella(j).get_candidati()[1] && get_cella(i).get_candidati()[1]!=get_cella(j).get_candidati()[0] && get_cella(i).get_candidati()[1]!=get_cella(j).get_candidati()[1])
            return false;
        if(get_cella(i).get_candidati()[0]==get_cella(j).get_candidati()[0] && get_cella(i).get_candidati()[1]==get_cella(j).get_candidati()[1])
            if(coppie(sudoku))
                return true;
            else
                return false;
        System.out.println("La cella " + j + " della "+get_type()+" " + get_index() + " e' una possibile ala della cella con i candidati: "
            + get_cella(j).get_candidati()[0] + "," + get_cella(j).get_candidati()[1]);
        if(get_type()=="Riga"){
            System.out.println("get_cella(i): "+get_cella(i).get_candidati()[0]+" "+get_cella(i).get_candidati()[1]+"\nget_cella(j): "+get_cella(j).get_candidati()[0]+" "+get_cella(j).get_candidati()[1]);
            if(Metodi.get_tabella(get_cella(i).get_tabella(),sudoku).xy_wing(get_cella(i),get_cella(j),sudoku))
                applicato=true;
            System.out.println("get_cella(i): "+get_cella(i).get_candidati()[0]+" "+get_cella(i).get_candidati()[1]+"\nget_cella(j): "+get_cella(j).get_candidati()[0]+" "+get_cella(j).get_candidati()[1]);
            if(Metodi.get_colonna(get_cella(i).get_colonna(),sudoku).xy_wing(get_cella(i),get_cella(j),sudoku))
                applicato=true;
        }else if(get_type()=="Tabella")
            if(Metodi.get_colonna(get_cella(i).get_colonna(),sudoku).xy_wing(get_cella(i),get_cella(j),sudoku))
                applicato=true;
        else
            System.out.println("Errore invocato xy-wing alla colonna");
        return applicato;
    }
    public boolean xy_wing(Cella cella1,Cella cella2,Cella sudoku[][]){//cella1 e cella 2 hanno due candidati di cui uno in comune(uno e uno solo)
        //Il metodo trova una terza cella con i candidati giusti nel settore invocante e rimuove i candidati
        //ritorna true se ha rimosso qualche candidato. cella1 è il perno e cella2 è l'ala
        
        boolean applicato=false;
        int perno=Metodi.get_perno(cella1,cella2),vet=get_ali(cella1,cella2);//vet contiene i candidati da cercare nella terza cella per fare xy-wing
        System.out.println("Cella 1: "+cella1.get_candidati()[0]+" "+cella1.get_candidati()[1]+"\nCella 2: "+cella2.get_candidati()[0]+" "+cella2.get_candidati()[1]);

        if(perno==0){//non dovrebbe ebtrare mai qui
            System.out.println("Errore invocato xy-wing(cella1,cella2,sudoku) ma le celle non hanno candidati in comune");
            return false;
        }
        int cost = 0;//cost deve essere tolto dai candidati
        if (cella2.get_candidati()[0] == perno)//il numero che va rimosso è il candidato di cella2 che non è perno
            cost =cella2.get_candidati()[1];
        else
            cost = cella2.get_candidati()[0];
        System.out.println("Cerco nella "+get_type()+" "+get_index()+" la terza cella chd forma xy-wing(Cioe' con possibili candidati: "+vet[0]+" "+vet[1]);
        System.out.println("Cella 1: "+cella1.get_candidati()[0]+" "+cella1.get_candidati()[1]+"\nCella 2: "+cella2.get_candidati()[0]+" "+cella2.get_candidati()[1]);
        Cella vet_celle[]=get_celle_se_candidati(vet,0);
        System.out.println("Cella 1: "+cella1.get_candidati()[0]+" "+cella1.get_candidati()[1]+"\nCella 2: "+cella2.get_candidati()[0]+" "+cella2.get_candidati()[1]);

        for(int i=0;i<vet_celle.length;i++){
            System.out.println("La cella "+vet_celle[i].get_posiz()+" forma xy-wing con le due celle precedenti");
            Cella celle_comune[] = vet_celle[i].celle_in_comune(cella2,sudoku);
            System.out.print("cerco di togliere il numero " + cost + " dalle celle: ");
            for (int j = 0; j < celle_comune.length; j++)
                System.out.print(celle_comune[j].get_posiz() + " ");
            System.out.println("");
            Cella vettore_di_celle[]=new Cella[celle_comune.length];
            int cont=0;
            for(int j=0;j<celle_comune.length;j++)
                if(celle_comune[j].rimuovi_candidato(cost)){
                    vettore_di_celle[cont++]=celle_comune[j];
                    System.out.println("Applicato xy-wing il numero "+cost+ " non puo' andare nella cella "+celle_comune[j].get_posiz());
                    applicato=true;
                }
            if(applicato)
                Sudoku.post_x_wing(cost,Metodi.accorcio_lunghezza(vettore_di_celle,cont),sudoku);
        }
        System.out.println("Cella 1: "+cella1.get_candidati()[0]+" "+cella1.get_candidati()[1]+"\nCella 2: "+cella2.get_candidati()[0]+" "+cella2.get_candidati()[1]+"\nEsco da xy_wing piu interno");

        return applicato;
    }
    public boolean colore(int numero){//Nel settore numero puo andare in due celle almeno una delle qualoi è A o B
        boolean applicato=false;
        if(dove_numero(numero,0)[0].get_carattere()=='-'){
            System.out.println("Inserisco A o B nella cella "+dove_numero(numero)[0]);
            applicato=true;
            dove_numero(numero,0)[0].set_carattere(Sudoku.inverto_A(dove_numero(numero,0)[1].get_carattere()));
        }else if(dove_numero(numero,0)[1].get_carattere()=='-'){
            applicato=true;
            System.out.println("Inserisco A o B nella cella "+dove_numero(numero)[1]);
            dove_numero(numero,0)[1].set_carattere(Sudoku.inverto_A(dove_numero(numero,0)[0].get_carattere()));   
        }else
            System.out.println("Invocato Settore.colore(numero),ma il settore ha gia sia A che B");
        return applicato;
    }
    
}