package Sudoku;
import Enum.Enum;
import Metodi.Metodi;
import javax.lang.model.util.ElementScanner14;
import java.util.Scanner;
public class Cella{
    public static void main(String args[]){
        int LATO=9,lato=(int)Math.sqrt(LATO);
        Sudoku sudoku=new Sudoku(LATO);
        sudoku.leggo();
        sudoku.stampo();
        if(sudoku.risolvo())
            System.out.println("Applicato remote-pairs");
        else
            System.out.println("Non applicato remote-pairs");
        sudoku.stampo_candidati();
    }
    private int posiz;
    private int numero;
    private char carattere;    
    private int LATO;//Lunghezza del sudoku a cui appartiene la cella
    private int lato;
    private int index_gruppo;//l'indice del gruppo a cui la cella appartiene
    private Cella celle_gruppo[];//contiene tutte le celle del gruppo a cui la cella appartiene
    private Cella celle_gruppo_viste[];//contiene le celle del gruppo che sono viste dalla cella(nel sudoku-killer ogni cella vede ogni altra cella in uno stesso gruppo)
    public void set_posiz(int posiz){
        this.posiz=posiz;
    }
    public int get_posiz(){
        return posiz;
    }
    
    public int get_numero(){
        return numero;
    }
    
    public char get_carattere(){
        return carattere;
    }
    public void set_carattere(char carattere){
        this.carattere=carattere;
    }
    private int candidati[];
    public int[] get_candidati(){
        return candidati;
    }
    public void set_candidati(int vet_num[]){
        this.candidati=vet_num;
    }
    public void set_numero(int numero){
        this.numero=numero;
        if(numero==0)
            this.carattere='-';
        else{
            this.carattere=(char)(numero+48);
            candidati=new int[1];
            candidati[0]=numero;
        }
    }

    public int get_LATO(){
        return this.LATO;
    }
    public int get_lato(){
        return this.lato;
    }
    public void set_LATO(int LATO){
        this.LATO=LATO;
        lato=(int)Math.sqrt(LATO);
    }
    public int get_index_gruppo(){
        return index_gruppo;
    }
    public void set_index_gruppo(int index){
        index_gruppo=index;
    }
    public void set_celle_gruppo(Cella vet_celle[]){
        celle_gruppo=vet_celle;
    }
    public void set_celle_gruppo_viste(Cella vet_celle[]){
        celle_gruppo_viste=vet_celle;
    }
    public Cella[] get_celle_gruppo(){//è un metodo get
        return celle_gruppo;
    }
    public Cella get_cella_gruppo(int index){//è un metodo get
        return celle_gruppo[index];
    }
    public Cella[] get_celle_gruppo_viste(){//è un metodo get
        return celle_gruppo_viste;
    }
    public Cella get_cella_gruppo_vista(int index){//è un metodo get
        return celle_gruppo_viste[index];
    }
    public Cella(int posiz,int LATO){
        candidati=new int[LATO];
        numero=0;
        carattere='-';
        set_LATO(LATO);
        for(int i=0;i<LATO;i++)
            candidati[i]=i+1;
        this.posiz=posiz;
    }
    public int get_riga()//controllato
    {
        return (int)posiz/LATO;
    }
    public int get_colonna()//controllato
    {
        return posiz%LATO;
    }
    public int get_tabella()//controllato
    {
        return ((int)(get_riga()/lato))*lato+(int)(get_colonna()/lato);
    }
    public static Riga get_riga(int index,Cella sudoku[][]){
        Riga riga=new Riga(sudoku.length,index);
        for(int i=0;i<sudoku.length;i++)
            riga.set_cella(sudoku[index][i],i);
        return riga;
    }
   //METODI DELLA CLASSE
    public int get_index_tabella(){//restituisce l'indice che la cella ha nella tabella a cui appartiene
        Cella sudoku[][]=new Cella[LATO][LATO];
        for(int i=0;i<LATO;i++)
            for(int j=0;j<LATO;j++)
                sudoku[i][j]=new Cella(i*LATO+j,LATO);    
        for(int i=0;i<LATO;i++)
            if(Cella.get_tabella(get_tabella(),sudoku).get_cella(i).get_posiz()==get_posiz())
                return i;
        System.out.println("Errore guardare metodo get_index_tabella");
        return 0;
    }
    public boolean rimuovi_candidato(int numero){
        int vet_num[]=new int[1];
        vet_num[0]=numero;
        if(rimuovi_candidato(vet_num).length>0)
            return true;
        else
            return false;
    }
    public int[] rimuovi_candidato(int vet_num[]){//toglie tutti i numeri di vet_num dai candidati della cella
        //ritorna un vettore formato dai numeri che sonp stati rimossi dai candidati della cella
        int vet[]=new int[vet_num.length],cont=0;//si segne i numeri di vet_num che sono stati rimossi
        for(int j=0;j<vet_num.length;j++)
            for(int i=0;i<candidati.length;i++)
                if(candidati[i]==vet_num[j]){
                    for(int k=i;k<candidati.length-1;k++)
                        candidati[k]=candidati[k+1];
                    candidati=Metodi.accorcio_lunghezza(candidati, candidati.length-1);
                    vet[cont++]=vet_num[j];//vet contiee tutti i numeri che sono stati rimossi dalla cella invocante
                }
        return Metodi.accorcio_lunghezza(vet, cont);//se cont =0 ritorna new int[0]
    }
    public boolean ci_puo_andare(int numero){
        for(int i=0;i<candidati.length;i++)
            if(candidati[i]==numero)
                return true;
        return false;
    }
    public int[] celle_in_comune(Cella cella){//ritorna un vettore formato dalle posizioni delle celle che sono viste sia da cella sia dall'oggetto invocante
        int vet[],cont=0;
        if(get_riga()==cella.get_riga() && get_colonna()==cella.get_colonna()){//l'oggetto invocante e cella sono la stessa cella
            vet=new int[3*LATO-1-2*lato];
            for(int i=0;i<LATO;i++)//scorre sulla riga
                if(i!=get_colonna())
                    vet[cont++]=get_riga()*LATO+i;
            for(int i=0;i<LATO;i++)//scorro sulla colonna
                if(i!=get_riga())
                    vet[cont++]=i*LATO+get_colonna();
            Tabella tabella =new Tabella(LATO,get_tabella());
            for(int i=0;i<LATO;i++)
                if(tabella.get_cella(i).get_riga()!=get_riga() && tabella.get_cella(i).get_colonna()!=get_colonna())
                    vet[cont++]=tabella.get_cella(i).get_posiz();
            return vet;
        }
        if(get_riga()==cella.get_riga() && get_tabella()==cella.get_tabella()){
            vet=new int[2*LATO-2-lato];
            for(int i=0;i<LATO;i++)//scorre sulla riga dì cella
                if(i!=get_colonna() && i!=cella.get_colonna())
                    vet[cont++]=get_riga()*LATO+i;
            Tabella tabella=new Tabella(LATO,get_tabella());
            for(int i=0;i<LATO;i++)//corre sulla tabella di cella
                if(tabella.get_cella(i).get_riga()!=get_riga())
                    vet[cont++]=tabella.get_cella(i).get_posiz();
            return vet;
        }
        if(get_tabella()==cella.get_tabella() &&get_colonna()==cella.get_colonna()){
            vet=new int[2*LATO-2-lato];
            for(int i=0;i<LATO;i++)
                if(i!=get_riga() && i!=cella.get_riga())
                    vet[cont++]=i*LATO+get_colonna();
            Tabella tabella=new Tabella(LATO,get_tabella());
            for(int i=0;i<LATO;i++)
                if(tabella.get_cella(i).get_colonna()!=get_colonna())
                    vet[cont++]=tabella.get_cella(i).get_posiz();
            return vet;
        }

        if(get_riga()==cella.get_riga()){
            vet=new int[LATO-2];
            for(int i=0;i<LATO;i++)
                if(i!=get_colonna() && i!=cella.get_colonna())
                    vet[cont++]=get_riga()*LATO+i;//posizione della cella nella riga get_riga() e colonna i
            return vet;
        }
        if(get_tabella()==cella.get_tabella()){
            vet=new int[LATO-2];
            Tabella tabella=new Tabella(LATO,get_tabella());
            for(int i=0;i<LATO;i++)
                if(tabella.get_cella(i).get_posiz()!=posiz && tabella.get_cella(i).get_posiz()!=cella.get_posiz())
                    vet[cont++]=tabella.get_cella(i).get_posiz();
            return vet;
        }
        if(get_colonna()==cella.get_colonna()){
            vet=new int[LATO-2];
            for(int i=0;i<LATO;i++)
                if(i!=get_riga() && i!=cella.get_riga())
                    vet[cont++]=i*LATO+get_colonna();
            return vet;
        }
        //se l'oggetto invocante e cella non hanno in comune ne la riga ne la tabella e ne la colonna
        if((int)(get_riga()/lato)==(int)(cella.get_riga()/lato)){
            vet=new int[2*lato];
            cont=0;
            Tabella tabella1=new Tabella(LATO,get_tabella()),tabella2=new Tabella(LATO,cella.get_tabella());
            for(int i=0;i<LATO;i++)
                if(tabella1.get_cella(i).get_riga()==cella.get_riga())
                    vet[cont++]=tabella1.get_cella(i).get_posiz();
            for(int i=0;i<LATO;i++)
                if(tabella2.get_cella(i).get_riga()==get_riga())
                    vet[cont++]=tabella2.get_cella(i).get_posiz();
            return vet;
        }
        if((int)(get_colonna()/lato)==(int)(cella.get_colonna()/lato)){
            vet=new int[2*lato];
            cont=0;
            Tabella tabella1=new Tabella(LATO,get_tabella()),tabella2=new Tabella(LATO,cella.get_tabella());
            for(int i=0;i<LATO;i++)
                if(tabella1.get_cella(i).get_colonna()==cella.get_colonna())
                    vet[cont++]=tabella1.get_cella(i).get_posiz();
            for(int i=0;i<LATO;i++)
                if(tabella2.get_cella(i).get_colonna()==get_colonna())
                    vet[cont++]=tabella2.get_cella(i).get_posiz();
            return vet;
        }
        vet=new int[2];
        vet[0]=get_riga()*LATO+cella.get_colonna();
        vet[1]=cella.get_riga()*LATO+get_colonna();
        return vet;
    }
    public int[] celle_in_comune(Cella celle[]){//ritorna un vettore formato dalle posiziioni delle celle che sono viste sia dalla cella invocante che da tutte le celle del vettore celle
        int vet[][]=new int[celle.length][3*LATO],vettore[]=new int[LATO*LATO],cont=0;
        vet[0]=celle_in_comune(celle[0]);
        for(int i=0;i<celle.length-1;i++)
            vet[i+1]=celle[i].celle_in_comune(celle[i+1]);
        boolean trovato[]=new boolean[vet.length],applicato;
        for(int i=0;i<LATO;i++){
            for(int j=0;j<LATO;j++){
                applicato=true;
                for(int k=0;k<vet.length && applicato;k++){
                    trovato[k]=false;
                    for(int h=0;h<vet[k].length && trovato[k]==false;h++)
                        if(vet[k][h]==(i*LATO+j))
                            trovato[k]=true;
                    if(trovato[k]==false)
                        applicato=false;
                }
                if(applicato)
                    vettore[cont++]=i*LATO+j;
            }
        }
        return Metodi.accorcio_lunghezza(vettore, cont);
    }
    public Cella[] celle_in_comune(int posiz,Cella sudoku[][]){
        Cella cella=new Cella(posiz,LATO);
        return celle_in_comune(cella, sudoku);
    }
    public Cella[] celle_in_comune(Cella cella,Cella sudoku[][]){
        Cella celle[]=new Cella[1];
        celle[0]=cella;
        return celle_in_comune(celle,sudoku);
    }
    public Cella[] celle_in_comune(Cella celle[],Cella sudoku[][]){
        int vet[]=celle_in_comune(celle);
        Cella vet_celle[]=new Cella[vet.length];
        for(int i=0;i<vet.length;i++)
            vet_celle[i]=sudoku[(int)vet[i]/sudoku.length][vet[i]%sudoku.length];
        return vet_celle;    
    }

    public boolean se_candidati(int vet_num[]){//restituisce true se i candidati della cella sono esattamente i numeri di vet_num. ( cella.get_candidati.equals(vet_num) non funziona)
        Cella cella=new Cella(posiz,LATO);
        cella.set_candidati(candidati);
        if(candidati.length==vet_num.length){
            for(int i=0;i<vet_num.length;i++)
                if(candidati[0]==vet_num[i]){
                    cella.rimuovi_candidato(candidati[0]);
                    if(candidati.length==1)
                        return true;
                    else{
                        int vet_num1[]=new int[vet_num.length-1];
                        for(int j=0;j<i;j++)
                            vet_num1[j]=vet_num[j];
                        for(int j=i;j<vet_num1.length;j++)
                            vet_num1[j]=vet_num[j+1];
                        return cella.se_candidati(vet_num1);
                    }
                }
        }
        return false;
    }

    
    //CREO CATENA(colori,ipotesi,remote_pairs,xy_wing_generale)
    //i prossimi metodi ritornano un gruppo di celle dove è stato messo A o B
    //nella prima cella del gruppo ritornato c'eè la cella invocante
    public Gruppo creo_catena_ipotesi(Cella sudoku[][]){
        Gruppo gruppo=new Gruppo(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],0);//mette la cella invocante nella prima cella del gruppo
        if(get_candidati().length==2){
            set_carattere('A');
            Cella vet_celle[]=celle_in_comune(get_posiz(),sudoku);
            for(int i=0;i<vet_celle.length;i++)
                if(vet_celle[i].get_carattere()!='A')
                    gruppo=vet_celle[i].creo_catena(gruppo,sudoku);
        }
        else
            gruppo=null;
        return gruppo;
    }
    public Gruppo creo_catena_ipotesi(Gruppo gruppo,Cella sudoku[][]){
        if(get_candidati().length==2){
            gruppo.aggiungi_cella(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO]);
            set_carattere('A');
            Cella vet_celle[]=celle_in_comune(get_posiz(),sudoku);
            for(int i=0;i<vet_celle.length;i++)
                if(vet_celle[i].get_carattere()!='A')
                    gruppo=vet_celle[i].creo_catena(gruppo,sudoku);
        }
        return gruppo;
    }
    public Gruppo creo_catena_remote_pairs(Cella sudoku[][]){//la cella invocante ha solo due candidati
        //il metodo crea un gruppo formato solo da celle i cui candidati sono i candidati della cella invocante e mette A e B
        if(get_candidati().length==2){
            System.out.println("Creo un acatena remote_pairs a partire dalla cella "+get_posiz()+" con i candidati "+get_candidati()[0]+" "+get_candidati()[1]);
            Gruppo gruppo=new Gruppo(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],0);
            set_carattere('A');
            Cella celle_comune[]=celle_in_comune(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],sudoku);
            for(int i=0;i<celle_comune.length;i++)
                if(Metodi.uguali(celle_comune[i].get_candidati(),get_candidati()) && gruppo.ce_cella(celle_comune[i])==false)//in celle_comune[i] ci sono i candidatati giusti per allungare la catena
                    gruppo=celle_comune[i].creo_catena_remote_pairs(gruppo,sudoku);//aggiunge in coda al gruppo celle_comune[i] e va avanti
            return gruppo;
        }
        return null;
    }
    public Gruppo creo_catena_remote_pairs(Gruppo gruppo,Cella sudoku[][]){
        gruppo.aggiungi_cella(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO]);
        set_carattere(Metodi.inverto_A(celle_gruppo_viste[0].get_carattere()));
        Cella celle_comune[]=celle_in_comune(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],sudoku);
        for(int i=0;i<celle_comune.length;i++)
            if(Metodi.uguali(celle_comune[i].get_candidati(),get_candidati()) && gruppo.ce_cella(celle_comune[i])==false)
                gruppo=celle_comune[i].creo_catena_remote_pairs(gruppo,sudoku);
        return gruppo;
    }
    public Gruppo creo_catena_xy_wing(Cella sudoku[][]){//crea una catena dove ogni cella ha due candidati e celle che si vedono hanno un candidto in comune
        if(get_candidati().length==2){
            System.out.println("Creo una catena xy_wing a partire dalla cella "+get_posiz()+" con i candidati "+get_candidati()[0]+" "+get_candidati()[1]);
            Gruppo gruppo=new Gruppo(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],0);
            Cella celle_comune[]=celle_in_comune(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],sudoku);
            for(int i=0;i<celle_comune.length;i++)//se celle comune[i] ha gli stessi candidati della cella invocante comunque va aggiunto alla catena
                if(celle_comune[i].get_candidati().length==2 && (celle_comune[i].get_candidati()[0]==get_candidati()[0]  || celle_comune[i].get_candidati()[0]==get_candidati()[1] || celle_comune[i].get_candidati()[1]==get_candidati()[0] || celle_comune[i].get_candidati()[1]==get_candidati()[1]))
                    gruppo=celle_comune[i].creo_catena_xy_wing(gruppo,sudoku);
            return gruppo;
        }
        return null;
    }
    public Gruppo creo_catena_xy_wing(Gruppo gruppo,Cella sudoku[][]){
        gruppo.aggiungi_cella(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO]);
        Cella celle_comune[]=celle_in_comune(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],sudoku);
        for(int i=0;i<celle_comune.length;i++)
            if(celle_comune[i].get_candidati().length==2 && (celle_comune[i].get_candidati()[0]==get_candidati()[0]  || celle_comune[i].get_candidati()[0]==get_candidati()[1] || celle_comune[i].get_candidati()[1]==get_candidati()[0] || celle_comune[i].get_candidati()[1]==get_candidati()[1]) && Metodi.ce_elemento(gruppo.get_celle(),celle_comune[i])==false)
                gruppo=celle_comune[i].creo_catena_xy_wing(gruppo,sudoku);
        return gruppo;
    }
    //TECNICHE RISOLUTIVE
    public int esclusione(Cella sudoku[][]){//ritorna il numero che ha inserito nella cella o ritorna 0
        System.out.println("Entro in esclusione nella cella: "+get_posiz());
        if (get_candidati().length==1 && get_numero()==0) {
            System.out.println("Applico esclusione: inserisci il " + get_candidati()[0]+" nella cella: "+get_posiz());
            Metodi.set_numero(get_posiz(),get_candidati()[0],sudoku);
            Metodi.get_tabella(get_tabella(),sudoku).slittamento(sudoku);//slittamento di tutti i numeri perchè e stato inserito un numero nel settore in questione
            Metodi.get_riga(get_riga(),sudoku).slittamento(sudoku);
            Metodi.get_colonna(get_colonna(),sudoku).slittamento(sudoku);
            return get_numero();
        }
        return 0;
    }
    public boolean xy_wing(Cella sudoku[][]){//serve avere questo metodo perche quando il numero di candidati di una cella dienta due il metodo puo essere invocato
        boolean applicato=false;
        if(get_candidati().length==2){
            System.out.println("Entro in xy_wing alla cella "+get_posiz()+" con i candidati; "+get_candidati()[0]+" "+get_candidati()[1]);
            for(int i=0;i<LATO;i++)//scorro sulla riga della cella invocante
                if(sudoku[get_riga()][i].get_candidati().length==2 && i!=get_colonna())//se l'i-esima cella della riga è diversa dall'oggetto invocante e se ha due candidati
                    if(Metodi.get_riga(get_riga(),sudoku).xy_wing(get_colonna(),i,sudoku))//ritorna true anche se ha fatto coppie con le due celle
                        applicato=true;
            for(int i=0;i<LATO;i++)//scorro sulla tabella della cella invocante la seconda cella la cerco fra quelle che non hanno la stessa riga della cella invocante
                if(sudoku[Cella.get_tabella(get_tabella(),sudoku).get_cella(i).get_riga()][Cella.get_tabella(get_tabella(),sudoku).get_cella(i).get_colonna()].get_candidati().length==2 && Cella.get_tabella(get_tabella(),sudoku).get_cella(i).get_riga()!=get_riga())
                    if(Metodi.get_tabella(get_tabella(),sudoku).xy_wing(get_index_tabella(),i,sudoku))
                        applicato=true;
        }
        return applicato;
    }

    public boolean creo_catena_xy_wing(Cella cella,Cella sudoku[][]){
        if(get_candidati()[0]==cella.get_candidati()[0] && get_candidati()[1]==cella.get_candidati()[1])
            return false;//coppie è già stato applicato se siamo arrivati qui
        if(get_candidati()[0]!=cella.get_candidati()[0] && get_candidati()[0]!=cella.get_candidati()[1] && get_candidati()[1]!=cella.get_candidati()[0] && get_candidati()[1]!=cella.get_candidati()[1])
            return false;//la cella invocante e cella non hanno candidati in comune
        int perno=Metodi.get_perno(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],cella);
        int vet[]=Metodi.get_ali(sudoku[(int)get_posiz()/LATO][get_posiz()%LATO],cella);
        boolean applicato=false;//perno è sicuramente diverso da 0
        for(int i=0;i<celle_gruppo_viste.length;i++)//scorre fra le celle viste per trovare la terza cella che fa xy_wing
            if(get_cella_gruppo_vista(i)!=cella)
                if(xy_wing(vet_num,perno,cella,get_cella_gruppo_vista(i),sudoku))
                    applicato=true;
        return applicato;
    }
    public boolean xy_wing(int vet_num[],int perno,Cella ala1,Cella ala2,Cella sudoku[][]){//la cella invocante è il perno
        if(Metodi.uguali(ala2.get_candidati(),vet_num)){
            int numero,cont=0;
            if(ala1.get_candidati()[0]==perno)
                numero=ala1.get_candidati()[1];
            else
                numero=ala1.get_candidati()[0];
            Cella celle_comune[]=ala1.celle_in_comune(ala2);
            Cella vettore_di_celle[]=new Cella[celle_comune.length];
            for(int i=0;i<celle_comune.length;i++)
                if(celle_comune[i].rimuovi_candidato(numero)){//la cella invocante è in celle_comune[] ma non ha il numero numero fra i candidati
                    System.out.println("Applicato xy_wing il numero "+numero+" non puo andare nella cella "+celle_comune[i].get_posiz());
                    vettore_di_celle[cont++]=celle_comune[i];
                }
            Sudoku temp=new Sudoku(LATO);
            temp.set_celle(sudoku);
            Sudoku.post_xy_wing(numero,Metodi.accorcio_lunghezza(vettore_di_celle,cont),sudoku);
            if(cont>0)
                return true;
            else
                return false;
        }
        return false;
    }

}