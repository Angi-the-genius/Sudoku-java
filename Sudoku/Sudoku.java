package Sudoku;
import Enum.Enum;
import Metodi.Metodi;

import java.util.Scanner;
import javax.crypto.IllegalBlockSizeException;
import javax.lang.model.util.ElementScanner14;
import javax.security.auth.SubjectDomainCombiner;
public class Sudoku {
    public static void main(String args[]) {
        Sudoku sudoku = new Sudoku(4);
        sudoku.leggo();
        sudoku.risolvo();
        sudoku.stampo();
    }
    private int LATO;
    private int lato;
    private Cella celle[][];
    private Riga righe[];
    private Colonna colonne[];
    private Tabella tabelle[];
    public void set_LATO(int LATO) {
        this.LATO = LATO;
        lato = (int) Math.sqrt(LATO);
    }
    public int get_LATO() {
        return LATO;
    }
    public int get_lato() {
        return lato;
    }

    public void set_celle() {
        for (int i = 0; i < LATO; i++) {
            for (int j = 0; j < LATO; j++)
                celle[i][j] = new Cella(i * LATO + j, LATO);
        }
    }
    public void set_celle(Cella celle[][]){
        this.celle=celle;
        set_settori();
    }
    public Cella get_cella(int posiz) {
        return celle[(int) posiz / LATO][posiz % LATO];
    }

    public void set_settori() {
        for(int i=0;i<LATO;i++){
            righe[i].set_celle(celle[i]);
            Cella vet_colonna[]=new Cella[LATO];
            for(int j=0;j<LATO;j++)
                vet_colonna[j]=celle[j][i];
            colonne[i].set_celle(vet_colonna);
            Tabella tabella=new Tabella(LATO,i);
            Cella vet_tabella[]=new Cella[LATO];
            for(int j=0;j<LATO;j++)
                vet_tabella[j]=celle[tabella.get_cella(j).get_riga()][tabella.get_cella(j).get_colonna()];
            tabelle[i].set_celle(vet_tabella);
        }
    }
    public Riga[] get_righe() {
        return righe;
    }
    public Riga get_riga(int index) {
        return righe[index];
    }
    public Colonna[] get_colonne() {
        return colonne;
    }
    public Colonna get_colonna(int index) {
        return colonne[index];
    }
    public Tabella[] get_tabelle() {
        return tabelle;
    }
    public Tabella get_tabella(int index) {
        return tabelle[index];
    }
    public int get_tabella(int i, int j) {
        return (int) (i / lato) * lato + (int) (j / lato);
    }
    public Sudoku(int LATO) {
        set_LATO(LATO);
        celle = new Cella[LATO][LATO];
        righe = new Riga[LATO];
        colonne = new Colonna[LATO];
        tabelle = new Tabella[LATO];
        for (int i = 0; i < LATO; i++) {
            righe[i] = new Riga(LATO, i);
            colonne[i] = new Colonna(LATO, i);
            tabelle[i] = new Tabella(LATO, i);
        }
        set_celle();
    }
    //METODI DELLA CLASSE
    public void stampo() {
        for (int i = 0; i < LATO; i++) {
            for (int j = 0; j < LATO; j++) {
                System.out.print(celle[i][j].get_carattere());
                if (j % lato == lato - 1)
                    System.out.print(" ");
            }
            System.out.print("\n");
            if (i % lato == lato - 1)
                System.out.print("\n");
        }
    }
    public void leggo() {
        System.out.println("Inserisci sudoku:\n");
        Scanner tastiera = new Scanner(System.in);
        for (int i = 0; i < LATO; i++)
            for (int j = 0; j < LATO; j++) {
                int numero = tastiera.nextInt();
                if (numero >= 0 && numero <= LATO)// se numero inserito è 0 la cella è vuota
                    set_numero(i, j, numero);
                else {
                    System.out.println("il numero " + numero + " non e' compresp fra 1 e " + LATO + ".\nrienserisci: ");
                    j--;// funziona anche quando j=0
                }
            }

    }
    public void stampo_candidati() {
        for (int i = 0; i < lato * LATO; i++) {
            for (int j = 0; j < LATO * lato; j++) {
                if ((i % lato) * lato + j % lato < celle[(int) (i / lato)][(int) (j / lato)].get_candidati().length)
                    System.out.print(celle[(int) (i / lato)][(int) (j / lato)].get_candidati()[(i % lato) * lato + j % lato]);
                else
                    System.out.print(" ");
                if (j % lato == lato - 1)
                    System.out.print(" ");
                if (j % LATO == LATO - 1)
                    System.out.print(" ");
            }
            System.out.println("");
            if (i % lato == lato - 1)
                System.out.println("");
            if (i % LATO == LATO - 1)
                System.out.println("");

        }
    }
    public Cella[] set_numero(int i, int j, int numero) {//ritorna un vettore che contiene le celle dalle quali è stato rimosso un numero dai candidati
        //se numero==0 ritorna null
        Cella vet_celle[]=new Cella[3*LATO];//vettore che verra ritornato contiene le celle dalle quali e stato rimosso un numero
        int cont=0;//conta il numero di celle da dove è stato rimosso un numero
        if (numero != 0) {
            int celle_comune[] = celle[i][j].celle_in_comune(celle[i][j]);
            for (int k = 0; k < celle_comune.length; k++)
                if(celle[(int) celle_comune[k] / LATO][celle_comune[k] % LATO].rimuovi_candidato(numero))
                    vet_celle[cont++]=celle[(int) celle_comune[k] / LATO][celle_comune[k] % LATO];
        }
        celle[i][j].set_numero(numero);
        if (numero != 0)
            stampo();
        if(cont==0)
            return new Cella[0];
        Cella temp[]=new Cella[cont];
        for(int k=0;k<cont;k++)
            temp[k]=vet_celle[k];
        return temp;       
    }

    public Cella[] set_numero(Cella cella,int numero){
        return set_numero(cella.get_riga(),cella.get_colonna(),numero);
    }
    public int[][] conta_numero(int numero,int vet_settore[],String settore){//restituisce vet nella cui i-esima cella ce conta_numero(numero,0) del settore vet_settore[i]
        int vet[][]=new int[vet_settore.length][LATO];
        if(settore=="Riga" || settore=="Righe")
            for(int i=0;i<vet_settore.length;i++)
                vet[i]=righe[vet_settore[i]].conta_numero(numero,0);
        if(settore=="Colonna" || settore=="Colonne")
            for(int i=0;i<vet_settore.length;i++)
                vet[i]=colonne[vet_settore[i]].conta_numero(numero,0);
        if(settore=="Tabella" || settore=="Tabelle")
            for(int i=0;i<vet_settore.length;i++)
                vet[i]=tabelle[vet_settore[i]].conta_numero(numero,0);
        return vet;
    }
    public int[][] dove_numero(int numero) {// restituisce un vettore vet che in vet[0] ha gli indici delle righe dove si
        // puo inserire numero in vet[1] delle colonne e in vet[2] delle tabelle
        int vet[][] = new int[3][0];
        vet[0]=dove_numero_riga(numero);
        vet[1]=dove_numero_colonna(numero);
        vet[2]=dove_numero_tabella(numero);
        return vet;
    }
    public int[][] dove_numero(int numero,int quante_volte){//restituisce un vettore che in vet[0] ha gli indici delle righe dove numero puo andare un numero di vole <= a quante_volte in vet[1] delle colonne e vet[2] delle tabelle
        int vet[][]=new int[3][0];
        vet[0]=dove_numero_riga(numero,quante_volte);
        vet[1]=dove_numero_colonna(numero,quante_volte);
        vet[2]=dove_numero_tabella(numero,quante_volte);
        return vet;
    }
    public int[][] dove_numero_esatto(int numero,int quante_volte){
        int vet[][]=new int[3][0];
        vet[0]=dove_numero_riga_esatto(numero, quante_volte);
        vet[1]=dove_numero_colonna_esatto(numero,quante_volte);
        vet[2]=dove_numero_tabella_esatto(numero,quante_volte);
        return vet;
    }
    public int[] dove_numero_riga(int numero){//restituisce un vettore formato dagli indici delle righe dove puo andare numero
        int vet_riga[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(righe[i].ce_numero(numero)==false)
                vet_riga[cont++]=i;
        return Metodi.accorcio_lunghezza(vet_riga, cont);
    }
    public int[] dove_numero_riga(int numero,int quante_volte){//restituisce un vettore che contiene gli indici delle righe dove numero può andare un numero di volte <= quante_volte
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(righe[i].ce_numero(numero)==false && righe[i].conta_numero(numero)<=quante_volte)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet,cont);
    }
    public int[] dove_numero_riga_esatto(int numero,int quante_volte){//restituisce gli indici delle righe dove numero puo andare esattamente in quante_volte celle
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(righe[i].conta_numero(numero)==quante_volte)
                vet[cont++]=i;
        System.out.println("Ci sono "+cont+"righe dove il "+numero+" puo andare in due celle");
        return Metodi.accorcio_lunghezza(vet, cont);
    }
    public int[] dove_numero_colonna(int numero){//restituisce vettore formato dagli indici delle colonne dove puo andare numero
        int vet_colonna[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(colonne[i].ce_numero(numero)==false)
                vet_colonna[cont++]=i;
        return Metodi.accorcio_lunghezza(vet_colonna, cont);
    }
    public int[] dove_numero_colonna(int numero,int quante_volte){
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(colonne[i].ce_numero(numero)==false && colonne[i].conta_numero(numero)<=quante_volte)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet,cont);
    }
    public int[] dove_numero_colonna_esatto(int numero,int quante_volte){//restituisce gli indici delle righe dove numero puo andare esattamente in quante_volte celle
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(colonne[i].conta_numero(numero)==quante_volte)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet, cont);
    }
    public int[] dove_numero_tabella(int numero){//restituisce vettore formato dagli indici delle colonne dove puo andare numero
        int vet_tabella[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(tabelle[i].ce_numero(numero)==false)
                vet_tabella[cont++]=i;
        return Metodi.accorcio_lunghezza(vet_tabella, cont);
    } 
    public int[] dove_numero_tabella(int numero,int quante_volte){
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(tabelle[i].ce_numero(numero)==false && tabelle[i].conta_numero(numero)<=quante_volte)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet,cont);
    }
    public int[] dove_numero_tabella_esatto(int numero,int quante_volte){//restituisce gli indici delle righe dove numero puo andare esattamente in quante_volte celle
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<LATO;i++)
            if(tabelle[i].conta_numero(numero)==quante_volte)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet, cont);
    }
    public Cella[] celle_in_comune(Cella celle[]){//ritorna vet in cui mette le celle viste da tutti gli elementi di celle(diverso da this.celle)
        int vet_int[]=Metodi.celle_in_comune(LATO, celle);//vet_int contiene le posizioni delle celle viste in comune
        Cella vet[]=new Cella[vet_int.length];
        for(int i=0;i<vet.length;i++)
            vet[i]=this.celle[(int)vet_int[i]/LATO][vet_int[i]%LATO];
        return vet;
    }
    public boolean finito(){
        boolean finito=true;
        for(int i=0;i<LATO && finito;i++)
            for(int j=0;j<LATO && finito;j++)
                if(celle[i][j].get_carattere()=='-')
                    finito=false;
        return finito;
    }
    private boolean risolvo(int inutile){
        if(finito()){
            System.out.println("Ho risolto il sudoku");
            return true;
        }
        return risolvo();
    }
    public boolean risolvo(){
        if(slittamento())
            risolvo(0);
        if(naked_pairs())
            risolvo(0);
        if(esclusione())
            risolvo(0);
        if(coppie())
            risolvo(0);
        if(x_wing())
            risolvo(0);
        if(colore())
            risolvo(0);
        if(xy_wing())
            risolvo(0);
        if(sky_screaper())
            risolvo(0);
        if(finito()==false)
            System.out.println("Con le tecniche a disposizione non sono in grado di risolvere il sudoku");
        return false;
    }
    public void metto_A(int numero,int vet_settore[],String settore){//mette A o B nell'ultimo settore
    //vet_settore contiene gli indici delle righe/colonne/.. dove numero puo andare due volte
    //il metodo nell'ultimo settore di vet_settore mette in una cella  A e nell'altra mette B
        if(settore=="Riga"){
            righe[vet_settore[vet_settore.length-1]].dove_numero(numero,0)[0].set_carattere('A');//settore è l'ultima riga in cui numero puo andare due volte
            righe[vet_settore[vet_settore.length-1]].dove_numero(numero,0)[1].set_carattere('B');
        }
        else if(settore=="Colonna"){
            colonne[vet_settore[vet_settore.length-1]].dove_numero(numero,0)[0].set_carattere('A');//settore è l'ultima riga in cui numero puo andare due volte
            colonne[vet_settore[vet_settore.length-1]].dove_numero(numero,0)[1].set_carattere('B');
        }
        else{
            tabelle[vet_settore[vet_settore.length-1]].dove_numero(numero,0)[0].set_carattere('A');//settore è l'ultima riga in cui numero puo andare due volte
            tabelle[vet_settore[vet_settore.length-1]].dove_numero(numero,0)[1].set_carattere('B');
        }
        stampo();
    }
 

                        //!!!POST-TECNICHE RISOLUTIVE!!!
    public static void post_slittamento(int numero,Cella celle[],Cella sudoku[][]){
        for(int i=0;i<celle.length;i++)//se celle.length=0 non entra nel for
            post_slittamento(numero,celle[i],sudoku);
    }
    public static void post_slittamento(int numero,Cella cella,Cella sudoku[][]){
        if(cella==null){
            System.out.println("Errore NullPointerExeption a Sudoku.post_slittamento()");
            return;
        }
        cella.esclusione(sudoku);
        Metodi.get_tabella(cella.get_tabella(),sudoku).slittamento(numero,sudoku);
        Metodi.get_riga(cella.get_riga(),sudoku).slittamento(numero,sudoku);
        Metodi.get_colonna(cella.get_colonna(),sudoku).slittamento(numero,sudoku);

    }
    public static void post_slittamento(Cella celle[],Cella sudoku[][]){//Dalle celle di celle è stato rimosso qualche candidato
        for(int numero=1;numero<=sudoku.length;numero++)
            post_slittamento(numero,celle,sudoku);
    }
    public static void post_naked_pairs(Cella celle[],Cella sudoku[][]){
        post_slittamento(celle,sudoku);
        for(int numero=1;numero<=sudoku.length;numero++)
            post_naked_pairs(numero,celle,sudoku);
    }
    public static void post_naked_pairs(int numero,Cella celle[],Cella sudoku[][]){
        post_slittamento(numero,celle,sudoku);//fa esclusione della cella e slittamento dei settori della cella
        for(int i=0;i<celle.length;i++)
            post_naked_pairs(numero,celle[i],sudoku);
    }
    public static void post_naked_pairs(int numero,Cella cella,Cella sudoku[][]){
        if(cella==null){
            System.out.println("Errore NullPointerExeption a post_naked_pairs()");
            return;
        }
        post_slittamento(numero,cella,sudoku);
        Metodi.get_tabella(cella.get_tabella(),sudoku).naked_pairs(numero,sudoku);
        Metodi.get_riga(cella.get_riga(),sudoku).naked_pairs(numero,sudoku);
        Metodi.get_colonna(cella.get_colonna(),sudoku).naked_pairs(numero,sudoku);

    }
    public static void post_remote_pairs(int vet_num[],Cella celle[],Cella sudoku[][]){
        for(int i=0;i<vet_num.length;i++)
            post_x_wing(vet_num[i],celle,sudoku);
    }
    public static void post_coppie(Cella celle[],Cella sudoku[][]){
        System.out.println("Entro in post_coppie alle celle: ");
        for(int i=0;i<celle.length;i++)
            System.out.print(celle[i].get_posiz()+" ");
        System.out.println("");
        post_naked_pairs(celle,sudoku);
        for(int numero=1;numero<=sudoku.length;numero++)
            post_coppie(numero,celle,sudoku);
    }
    public static void post_coppie(int numero,Cella celle[],Cella sudoku[][]){
        post_naked_pairs(numero,celle,sudoku);
        for(int i=0;i<celle.length;i++)
            post_coppie(numero,celle[i],sudoku);
    }
    public static void post_coppie(int vet_num[],Cella cella,Cella sudoku[][]){
        for(int i=0;i<vet_num.length;i++)
            post_coppie(vet_num[i],cella,sudoku);
    }
    public static void post_coppie(int vet_num[],Cella celle[],Cella sudoku[][]){
        for(int i=0;i<vet_num.length;i++)
            post_coppie(vet_num[i],celle,sudoku);
    }
    public static void post_coppie(int numero,Cella cella,Cella sudoku[][]){
        if(cella==null){
            System.out.println("Errore null_pointerException in Sudoku.post_coppie");
            return;
        }
        post_naked_pairs(numero,cella,sudoku);
        Metodi.get_tabella(cella.get_tabella(),sudoku).coppie(sudoku);
        Metodi.get_riga(cella.get_riga(),sudoku).coppie(sudoku);
        Metodi.get_colonna(cella.get_colonna(),sudoku).coppie(sudoku);
    }
    public static void post_x_wing(Cella celle[],Cella sudoku[][]){
        for(int numero=1;numero<=sudoku.length;numero++)
            post_x_wing(numero,celle,sudoku);
    }
    public static void post_x_wing(int numero,Cella celle[],Cella sudoku[][]){
        for(int i=0;i<celle.length;i++)
            post_x_wing(numero,celle[i],sudoku);
    }
    public static void post_x_wing(int numero,Cella cella,Cella sudoku[][]){
        post_coppie(numero,cella,sudoku);
        if(cella==null){
            System.out.println("Errore NullPointerExeption in post_x_wing()");
            return;
        }
        Metodi.get_riga(cella.get_riga(),sudoku).x_wing(numero,sudoku);
        Metodi.get_colonna(cella.get_colonna(),sudoku).x_wing(numero,sudoku);
    }
    public static void post_xy_wing(int numero,Cella celle[],Cella sudoku[][]){
        return;
    }
    public static void post_ipotesi(Cella celle[],Cella sudoku[][]){
        post_x_wing(celle,sudoku);
    }
    //TECNICHE RISOLUTIVE
    public boolean esclusione() {//VERIFICATA
        for (int i = 0; i < LATO; i++)
            for (int j = 0; j < LATO; j++)
                if(celle[i][j].esclusione(this.celle)!=0){
                    esclusione();
                    return true;
                }
        return false;
    }
    public boolean slittamento() {//VERIFICATA
        for (int numero = 1; numero <= LATO; numero++)
            if (slittamento(numero)){
                slittamento();//avendo inserito un numero ri-incomincia da capo
                return true;
            }
        return false;
    }
    public boolean slittamento(int inutile1,int inutile2){
        slittamento();
        return true;
    }
    public boolean slittamento(int numero){
        System.out.println("Entro in slittamento numero: "+numero);
        int vet[]=dove_numero_tabella(numero);//in vet ci sono gli indici delle tabelle dove puo andare numero
        for(int i=0;i<vet.length;i++)//scorro sulle tabelle
            if(tabelle[vet[i]].slittamento(numero,this.celle))
                return slittamento(0,0);
        vet=dove_numero_riga(numero);
        for(int i=0;i<vet.length;i++)   
            if(righe[vet[i]].slittamento(numero,this.celle))
                return slittamento(0,0);
        vet=dove_numero_colonna(numero);
        for(int i=0;i<vet.length;i++)//scorro sulle tabelle
            if(colonne[vet[i]].slittamento(numero,this.celle))
                return slittamento(0,0);
        return false;
    }
    public boolean naked_pairs(){
        for(int numero=1;numero<=LATO;numero++)
            if(naked_pairs(numero)){
                naked_pairs();
                return true;
            }
        return false;
    }
    public boolean naked_pairs(int inutile1,int inutile2){
        naked_pairs();
        return true;
    }
    public boolean naked_pairs(int numero){
        System.out.println("Entro in naked pairs al numero "+numero);
        int vet[]=dove_numero_tabella(numero);//in vet ci sono gli indici delle tabelle dove puo andare numero
        for(int i=0;i<vet.length;i++)//scorro sulle tabelle dove non puo andare numero
            if(tabelle[vet[i]].naked_pairs(numero,celle))
                return naked_pairs(0,0);
        vet=dove_numero_riga(numero);//in vet ci sono gli indici delle righe dove va inserito numero
        for(int i=0;i<vet.length;i++)//scorro sulle righe dove va inserito numero
            if(righe[vet[i]].naked_pairs(numero,celle))
                return naked_pairs(0,0);
        vet=dove_numero_colonna(numero);//in vet ci sono gli indici delle colonne doe non puo andare numero
        for(int i=0;i<vet.length;i++)//scorro sulle colonne dove non puo andare numero
            if(colonne[vet[i]].naked_pairs(numero,celle))
                return naked_pairs(0,0);
        return false;
    }
    public boolean coppie() {
        boolean applicato = false;
        for (int i = 0; i < LATO; i++)// TABELLE
            if (tabelle[i].coppie(this.celle))
                applicato = true;//meglio non invocare qui coppie
        for (int i = 0; i < LATO; i++)// RIGHE
            if (righe[i].coppie(this.celle))
                applicato = true;
        for (int i = 0; i < LATO; i++)// COLONNE
            if (colonne[i].coppie(this.celle))
                applicato = true;
        if(applicato)
            coppie();
        return applicato;
    }
    public boolean x_wing() {
        boolean applicato = false;
        for (int numero = 1; numero <= LATO; numero++)
            if (x_wing(numero))//x_wing numero fa tutto per numero non ce bisogno di mettere numero--
                applicato = true;
        return applicato;
    }
    public boolean x_wing(int numero,int inutile){
        x_wing(numero);
        return true;
    }
    public boolean x_wing(int numero) {
        System.out.println("Entro in x-wing al numero: "+numero);
        int vet[]=dove_numero_riga(numero);
        for(int i=0;i<vet.length-1;i++)//scorro sulle righe
            if(righe[vet[i]].x_wing(numero,this.celle))
                return x_wing(numero,0);
        vet=dove_numero_colonna(numero);
        for(int i=0;i<vet.length-1;i++)
            if(colonne[vet[i]].x_wing(numero,this.celle))
                return x_wing(numero,0);
        return false;
    }
    public static int[] x_wing_generale(int vet[][]) {// gli elementi di vet sono 0 o 1 il metodo torna un vettore che contiene qualle j per le quali esiste i tale che vet[i][j]=1
        // vet[0],vet[1],... devono tutti essere lunghi LATO
        int somma[] = new int[vet[0].length];
        for (int i = 0; i < vet[0].length; i++) {
            somma[i] = 0;
            for (int j = 0; j < vet.length; j++)
                somma[i] = somma[i] + vet[j][i];
        }
        int cont = 0, vettore[] = new int[100];

        for (int i = 0; i < somma.length; i++)
            if (somma[i] > 0)
                vettore[cont++] = i;
        int temp_vet[] = vettore;
        vettore=new int[cont];
        for (int i = 0; i < cont; i++)
            vettore[i] = temp_vet[i];
        return vettore;
    }
    public boolean xy_wing(){
        boolean applicato=false;
        for(int i=0;i<LATO;i++)//scorre sulle righe
            for(int j=0;j<LATO;j++)
                if(celle[i][j].xy_wing(this.celle))//Vede se celle[i][j forma xy-wing con altre due celle e fa quelo che deve fare
                    applicato=true;
        return applicato;
    }
    public boolean colore() {
        boolean applicato = false;
        for (int numero = 1; numero <= LATO; numero++){
            if (colore(numero)) 
                applicato = true;
            for (int i = 0; i < LATO; i++)
                for (int j = 0; j < LATO; j++)
                    if ((int) celle[i][j].get_carattere() >= 65 && (int) celle[i][j].get_carattere() <= 90)
                        celle[i][j].set_carattere('-');
            stampo();
        }
        return applicato;
    }
    public boolean colore(int numero) {
        int vet_riga[] = dove_numero_riga_esatto(numero,2);// vettori che contengono gli indici dei settori dove numero ha a disposizione 2 celle;
        int vet_colonna[] = dove_numero_colonna_esatto(numero,2);
        int vet_tabella[] = dove_numero_tabella_esatto(numero,2);
        System.out.print("vet_riga: ");
        for(int i=0;i<vet_riga.length;i++)
            System.out.print(vet_riga[i]+" ");
        System.out.print("\nvet_colonna: ");
        for(int i=0;i<vet_colonna.length;i++)
            System.out.print(vet_colonna[i]+" ");
        System.out.print("vet_tabella: ");
        for(int i=0;i<vet_tabella.length;i++)
            System.out.print(vet_tabella[i]+" ");
        System.out.println("");
        if (vet_riga.length > 0) {// metto A e B nell'ultima riga di vet_riga
            metto_A(numero,vet_riga,"Riga");
            colore(numero, Metodi.accorcio_lunghezza(vet_riga, vet_riga.length-1), vet_colonna, vet_tabella);//riempie il sudoku di A e B
        } else if (vet_colonna.length > 0) {// forse è inutile controllare le colonne
            metto_A(numero,vet_colonna,"Colonna");
            colore(numero, vet_riga, Metodi.accorcio_lunghezza(vet_colonna, vet_colonna.length-1), vet_tabella);// riempie il sudoku di A e B
        } // è inutile controllare le tabelle
        if(vet_riga.length>0 || vet_colonna.length>0){
            colore(numero,"Riga");//cerca se in qualche riga ci sono due A o due B in quel caso fa quello che deve fare
            colore(numero,"Colonna");
            colore(numero,"Tabella");
            System.out.println("Nessuna riga colonna e tabella contiene due lettere uguali");
            Cella vet_celle[]=new Cella[LATO*LATO];
            int cont=0;
            for(int i=0;i<LATO;i++)
                for(int j=0;j<LATO;j++)
                    if(celle[i][j].get_carattere()>=65 && celle[i][j].get_carattere()<=90){
                        char carattere=Metodi.inverto_A(celle[i][j].get_carattere());
                        for(int k=0;k<LATO;k++)
                            for(int h=0;h<LATO;h++)
                                if(celle[k][h].get_carattere()==carattere){
                                    Cella celle_comune[]=celle[i][j].celle_in_comune(celle[k][h],this.celle);
                                    for(int q=0;q<celle_comune.length;q++)
                                        if(celle_comune[q].rimuovi_candidato(numero))
                                            vet_celle[cont++]=celle_comune[q];
                                }
                        celle[i][j].set_carattere('-');
                    }
            if(cont>0){
                post_x_wing(Metodi.accorcio_lunghezza(vet_celle, cont),this.celle);
                return true;
            }
        }
        return false;
    }
    public boolean colore(int numero,String settore){//ritorna true se c'è un settore con la stessa lettera due volte
        int vet_settore[],cont=0;
        Settore settori[];
        if(settore=="Riga"){
            vet_settore = dove_numero_riga_esatto(numero,2);// vettori che contengono gli indici dei settori dove numero ha a disposizione 2 celle;
            settori=righe;
        }
        else if(settore=="Colonna"){
            vet_settore = dove_numero_colonna_esatto(numero,2);
            settori=colonne;
        }
        else if(settore=="Tabella"){
            vet_settore = dove_numero_tabella(numero,2);
            settori=tabelle;
        }
        else{
            System.out.println("Errore invocato il metodo colore(numero,settore) con argomento settore="+settore);
            return false;
        }
        Cella vet_celle[]=new Cella[LATO*LATO];//contiene tutte le celle dalle quali è stato rimosso un candidato
        for(int i=0;i<vet_settore.length;i++){
            System.out.println(settori[vet_settore[i]].dove_numero(numero,0).length);
            if(settori[vet_settore[i]].dove_numero(numero,0)[0].get_carattere()==settori[vet_settore[i]].dove_numero(numero,0)[1].get_carattere()){
                for(int j=0;j<LATO;j++)//cci sono due lettere uguali nella settore vet_settore[i]
                    for(int k=0;k<LATO;k++)
                        if(celle[j][k].get_carattere()==settori[vet_settore[i]].dove_numero(numero,0)[0].get_carattere()){
                            celle[j][k].set_carattere('-');
                            celle[j][k].rimuovi_candidato(numero);
                            vet_celle[cont++]=celle[j][k];
                        }
                        else{
                            char carattere;
                            if((int)(settori[vet_settore[i]].dove_numero(numero,0)[0].get_carattere())%2==1)
                                carattere=(char)((int)(settori[vet_settore[i]].dove_numero(numero,0)[0].get_carattere())+1);
                            else
                                carattere=(char)((int)(settori[vet_settore[i]].dove_numero(numero,0)[0].get_carattere())-1);
                            if(celle[i][j].get_carattere()==carattere){
                                Cella vettore[]=set_numero(celle[i][j],numero);//vettore contiene le celle dalle quali si è rimosso un candidato inserendo numero
                                vet_celle[cont++]=celle[i][j];//faremo post_colore con la cella dove si è inserito il numero
                                for(int h=0;h<vettore.length;h++)
                                    vet_celle[cont++]=vettore[h];
                            }
                        }
                if(cont>0){
                    Sudoku.post_x_wing(Metodi.accorcio_lunghezza(vet_celle, cont),this.celle);
                    return true;
                }
            }
        }
        return false;
    }
    public void colore(int numero,int vet_riga[],int vet_colonna[],int vet_tabella[]){//riempie tutto il sudoku di A e B
        System.out.println("Invocato colore al numero: "+numero);
        for(int i=0;i<LATO;i++)//scorro su tutto il sudoku studiando celle[i][j]
            for(int j=0;j<LATO;j++)
                if(celle[i][j].get_carattere()>=65 &&celle[i][j].get_carattere()<=90){
                    System.out.println("Controllo se dalla cella +"+(i*LATO+j)+" puo partire una catena");
                    colore(numero,i,vet_riga,vet_colonna,vet_tabella,"Riga");
                    colore(numero,j,vet_riga,vet_colonna,vet_tabella,"Colonna");
                    colore(numero,celle[i][j].get_tabella(),vet_riga,vet_colonna,vet_tabella,"tabella");
                }
    }
    public void colore(int numero,int i_settore,int vet_riga[],int vet_colonna[],int vet_tabella[],String settore){
        //ce una cella nel settore i_settore con una A o una B(la cella è celle[i][j] del metodo precedente)
        //il metodo mette prima una lettera nel settore (attraverso Settore.colore(numero)) e poi invoca il metodo precedente dopo aver modificato i tre vettori
        int vet_settore[]=new int[0];
        if(settore=="Riga"){
            vet_settore=vet_riga;
        }else if(settore=="Colonna"){
            vet_settore=vet_colonna;
        }else if(settore=="Tabella"){
            vet_settore=vet_tabella;
        }
        boolean trovato=false,applicato=false;
        int i;
        for(i=0;i<vet_settore.length && trovato==false;i++)
            if(vet_settore[i]==i_settore)
                trovato=true;
        i--;
        if(trovato){//mette A o B nella riga di celle[i][j]
            System.out.println("Nella "+settore+" "+i_settore+" il numero "+numero+" puo andare in solo 2 celle");
            if(righe[i].colore(numero))//mette A o B nell'altra cella della riga
                applicato=true;
            trovato=false;
        }
        if(applicato){//il programma ha riempito la riga i di A e B
            if(vet_settore.length>1){//elimino la cella i da vet_riga
                for(int k=i;k<vet_settore.length-1;k++)
                    vet_settore[k]=vet_settore[k+1];
                if(settore=="Riga")
                    colore(numero,Metodi.accorcio_lunghezza(vet_riga, vet_riga.length-1),vet_colonna,vet_tabella);//se vet_riga.length=0 non ce problema va direttamente a controllare le colonne
                else if(settore=="Colonna")
                    colore(numero,vet_riga,Metodi.accorcio_lunghezza(vet_settore,vet_settore.length-1),vet_tabella);
                else if(settore=="Tabella")
                    colore(numero,vet_riga,vet_colonna,Metodi.accorcio_lunghezza(vet_settore,vet_settore.length-1));
            }
            if(settore=="Riga")
                colore(numero,new int[0],vet_colonna,vet_tabella);//se vet_riga.length=0 non ce problema va direttamente a controllare le colonne
            else if(settore=="Colonna")
                colore(numero,vet_riga,new int[0],vet_tabella);
            else if(settore=="Tabella")
                colore(numero,vet_riga,vet_colonna,new int[0]);
        }
    }
    public boolean sky_screaper(){
        boolean applicato=false;
        for(int numero=1;numero<=LATO;numero++)
            if(sky_screaper(numero)){
                applicato=true;
                numero--;
            }
        return applicato;
    }
    public boolean sky_screaper(int numero){
        System.out.println("Cerco di fare sky-screaper con il numero "+numero);
        boolean applicato=false;
        for(int i=0;i<LATO;i++)//scorro sulle righe
            if(righe[i].conta_numero(numero)>=2 && righe[i].conta_numero(numero)<=lato){//se tutte tranne la prima cella della riga o tutte tranne l'ultima cella della riga sono nella stesssa tabella allora ci puo essere sky-screaper
                int vet[]=righe[i].dove_numero(numero),tabella=-1;//vet contiene gli indici delle celle della riga dove puo andare numero
                Cella cella1=null,cella2=null;
                boolean trovato=true;
                for(int j=0;j<vet.length-2 && trovato;j++)
                    if(righe[i].get_cella(vet[j]).get_tabella()!=righe[i].get_cella(vet[j+1]).get_tabella())
                        trovato=false;
                if(trovato && righe[i].get_cella(vet[0]).get_tabella()!=righe[i].get_cella(vet[vet.length-1]).get_tabella()){
                    tabella=righe[i].get_cella(vet[0]).get_tabella();
                    cella1=righe[i].get_cella(vet[vet.length-1]);
                    System.out.println("La riga "+i+" puo' formare sky-screaper con ala "+cella1.get_posiz()+" e perno la tabella "+tabella);
                }
                else{
                    trovato=true;
                    for(int j=0;j<vet.length-2 && trovato;j++)
                        if(righe[i].get_cella(vet[j+1]).get_tabella()!=righe[i].get_cella(vet[j+2]).get_tabella())
                            trovato=false;
                    if(trovato && righe[i].get_cella(vet[0]).get_tabella()!=righe[i].get_cella(vet[1]).get_tabella()){
                        tabella=righe[i].get_cella(vet[0]).get_tabella();
                        cella1=righe[i].get_cella(vet[0]);
                        System.out.println("La riga "+i+" puo' formare sky-screaper con ala "+cella1.get_posiz()+" e perno la tabella "+tabella);
                    }
                }
                if(tabella!=-1){//tabella contiene l'indice della tabella dalla cui puo partire una colonna che forma sky screaper
                    int i_colonne[]=new int[lato-vet.length+1],cont=0;//contiene gli indici delle colonne da controllare per fprmare sky-screaper
                    for(int j=0;j<lato;j++){//riempio i_colonne
                        trovato=false;
                        for(int k=0;k<vet.length && trovato==false;k++)
                            if(tabelle[tabella].get_cella(j).get_colonna()==vet[k])
                                trovato=true;
                        if(trovato==false)
                            i_colonne[cont++]=tabelle[tabella].get_cella(j).get_colonna();
                    }
                    for(int j=0;j<i_colonne.length;j++){//vedo se le colonne di i_colonne formano sky-screaper con la riga i
                        System.out.println("Vedo se la colonna "+j+" forma sky-screaper");
                        int vet1[]=colonne[i_colonne[j]].dove_numero(numero);
                        trovato=true;
                        for(int k=0;k<vet1.length-2 && trovato;k++)
                            if(colonne[i_colonne[j]].get_cella(vet1[k]).get_tabella()!=colonne[i_colonne[j]].get_cella(vet1[k+1]).get_tabella())
                                trovato=false;
                        if(trovato==true && colonne[i_colonne[j]].get_cella(vet1[0]).get_tabella()!=colonne[i_colonne[j]].get_cella(vet1[vet1.length-1]).get_tabella()){
                            cella2=colonne[i_colonne[j]].get_cella(vet1[vet1.length-1]);
                            System.out.println("La colonna "+i_colonne[j]+" forma sky-screaper con ala la cella "+cella2.get_posiz());
                        }
                        trovato=true;
                        for(int k=0;k<vet1.length-2 && trovato;k++)
                            if(colonne[i_colonne[j]].get_cella(vet1[k+1]).get_tabella()!=colonne[i_colonne[j]].get_cella(vet1[k+2]).get_tabella())
                                trovato=false;
                        if(trovato==true && colonne[i_colonne[j]].get_cella(vet1[0]).get_tabella()!=colonne[i_colonne[j]].get_cella(vet1[1]).get_tabella()){
                            cella2=colonne[i_colonne[j]].get_cella(vet1[0]);
                            System.out.println("La colonna "+i_colonne[j]+" forma sky-screaper con ala la cella "+cella2.get_posiz());
                        }
                        if(cella2!=null){
                            int vettore[]=cella1.celle_in_comune(cella2);
                            for(int k=0;k<vettore.length;k++)
                                if(celle[Cella.get_riga(vettore[k],LATO)][Cella.get_colonna(vettore[k],LATO)].rimuovi_candidato(numero)){
                                    System.out.println("Applicato sky-screaper.Il numero "+numero+" non puo andare nella cella "+vettore[k]);
                                    applicato=true;
                                }
                        }
                    }
                }
            }
        return applicato;
    }
    public boolean ipotesi(){
        boolean applicato=false;
        for(int i=0;i<LATO;i++)
            for(int j=0;j<LATO;j++){
                Gruppo gruppo=celle[i][j].creo_catena(this.celle);//crea un gruppo con prima cella celle[i][j] di tutte celle comunicanti e con due candidati
                if(gruppo!=null){
                    System.out.println("Faccio ipotesi partendo dalla cella: "+(i*LATO+j));
                    stampo();
                    Gruppo gruppo1=gruppo.creo_identico(),gruppo2=new gruppo.creo_identico();
                    gruppo1.set_numero(celle[i][j].get_candidati()[0],0);
                    gruppo2.set_numero(celle[i][j].get_candidati()[1],0);
                    gruppo1.risolvo(celle);
                    gruppo2.risolvo(celle);

                        if(gruppo1.assurdo() && gruppo2.assurdo()){
                            System.out.println("Facendo ipotesi mi sono accorto che il sudoku è impossibile perche nella cella non ci possono andare entrambi i candidati")
                        }else if(gruppo1.assurdo()){
                            System.out.println("Applicato ipotesi alla cella il numero "+celle[i][j].get_candidati()[0]+" inserisco numero "+celle[i][j].get_candidati()[1]);
                            set_numero(celle[i][j].get_candidati()[1]);
                        }else if(gruppo1.assurdo()){
                            System.out.println("Applicato ipotesi alla cella il numero "+celle[i][j].get_candidati()[1]+" inserisco numero "+celle[i][j].get_candidati()[0]);
                            set_numero(celle[i][j].get_candidati()[0]);
                        }else{//se entrambi i grupi non sono assurdi
                            Cella vettore_di_celle[]=new Cella[3*LATO];
                            for(int k=0;k<gruppo1.get_celle().length;k++)
                                if(gruppo1.get_cella(k).get_numero()!=0)
                                    for(int h=0;h<gruppo2.get_celle().length;h++)
                                        if(gruppo2.get_cella(h).get_numero()==gruppo1.get_cella(k).get_numero()){
                                            Cella celle_comune[]=gruppo1.get_cella(k).celle_in_comune(gruppo2.get_cella(h),this.celle);
                                            int cont=0;
                                            for(int q=0;q<celle_comune.length;q++)
                                                if(celle_comune[q].rimuovi_candidato(gruppo1.get_cella(k).get_numero())){
                                                    System.out.println("Applicato ipotesi il numero "+gruppo1.get_cella(k).get_numero+" non puo andare nella cella "+celle_comune[q].get_posiz());
                                                    vettore_di_celle[cont++]=celle_comune[q];//si segna tutte le celle da cui è stato rimosso un candidato
                                                }
                                        }
                            Sudoku.post_ipotesi(vettore_di_celle,sudoku);
                        }
                    
                    for(int k=0;k<LATO;k++)
                        for(int h=0;h<LATO;h++)
                            if(celle[k][h].get_carattere()=='A')
                                celle[k][h].set_carattere('-');
                }
            }
        return applicato;
    }
    public boolean tentativi(){
        Sudoku sudoku1=new Sudoku(LATO),sudoku2=new Sudoku(LATO);
        sudoku1.set_celle(celle);
        sudoku2.set_celle(celle);
        for(int i=0;i<LATO;i++)
            for(int j=0;j<LATO;j++){
                if(celle[i][j].get_candidati().length==2){
                    sudoku1.set_numero(i,j,celle[i][j].get_candidati()[0]);
                    sudoku2.set_numero(i,j,celle[i][j].get_candidati()[1]);
                    sudoku1.risolvo();//risolve con i tentativi
                    sudoku2.risolvo();
                    if(sudoku1.finito() && sudoku2.finito()==false){
                        System.out.println("Applicato tentativi inserisco numero "+celle[i][j].get_candidati()[0]+" nella cella "+(i*LATO+j));
                        set_numero(i,j,celle[i][j].get_candidati()[0]);
                        stampo();
                        risolvo();
                        return true;
                    }else if(sudoku1.finito()==false && sudoku2.finito()){
                        System.out.println("Applicato tentativi inserisco numero "+celle[i][j].get_candidati()[1]+" nella cella "+(i*LATO+j));
                        set_numero(i,j,celle[i][j].get_candidati()[1]);
                        stampo();
                        risolvo();//in realta si sa già la soluzione
                        return true;
                    }else if(sudoku1.finito() && sudoku2.finito()){//Manca il caso in cui anche sudoku1 o sudoku2 hanno due soluzioni
                        System.out.println("Il sudoku ha due soluzioni");
                        sudoku1.stampo();
                        sudoku2.stampo();
                        return false;
                    }else{//sia sudoku1.finito() che sudoku2.finito ritornano false cioe non sono stati conclusi i sudoku
                        //dopo aver applicato tentativi quindi sono entrambi impossibili
                        System.out.println("Il sudoku non ha soluzione");
                        return false;
                    }
                }
            }
        return false;//Non ce nessuna cella con due candidati e basta
    }
    public boolean remote_pairs(){// non ha senso metterlo dopo colore perchè fare prima colore rende inefficace remote_pairs()
        boolean applicato=false;
        for(int i=0;i<LATO;i++)
            for(int j=0;j<LATO;j++){
                Gruppo gruppo=celle[i][j].creo_catena_remote_pairs(this.celle);//Gruppo è una catena di A e B che 
                if(gruppo!=null){//con una catena di tre celle non si fa nulla
                    stampo();
                    if(gruppo.get_celle().length>=4){
                    //controllo se ci sono due A o due B che si vedono
                        for(int k=0;k<gruppo.get_celle().length;k++)
                            for(int h=0;h<gruppo.get_cella(k).get_celle_gruppo_viste().length;h++)
                                if(gruppo.get_cella(k).get_carattere()==gruppo.get_cella(k).get_cella_gruppo_vista(h).get_carattere()){
                                    System.out.println("Il sudoku e' impossibile perche' la lettera "+gruppo.get_cella(k).get_carattere()+" si autovede");
                                    System.exit(0);
                                }
                        Cella vettore_di_celle[]=new Cella[LATO*LATO];
                        int cont=0;
                        for(int k=0;k<gruppo.get_celle().length-1;k++){
                            for(int h=k+1;h<gruppo.get_celle().length;h++){//se in gruppo.get_cella(k) ce la A nelle altrre celle viste c'è la B
                                if(gruppo.get_cella(k).get_carattere()==Metodi.inverto_A(gruppo.get_cella(h).get_carattere())){
                                    Cella celle_comune[]=gruppo.get_cella(k).celle_in_comune(gruppo.get_cella(h),this.celle);
                                    for(int q=0;q<celle_comune.length;q++)
                                        if(celle_comune[q].rimuovi_candidato(celle[i][j].get_candidati()).length>0){
                                            System.out.println("Applicato remote pairs i numeri non possono andare nella cella "+celle_comune[q].get_posiz());
                                            vettore_di_celle[cont++]=celle_comune[q];//è inutile dire che celle_comune[q] puo essere inserito in vettore_di_celle solo se non è gia stato inserito
                                            applicato=true;
                                        }
                                }
                            }
                            gruppo.get_cella(k).set_carattere('-');
                        }
                        gruppo.get_cella(gruppo.get_celle().length-1).set_carattere('-');
                        if(cont>0)
                            Sudoku.post_remote_pairs(celle[i][j].get_candidati(),vettore_di_celle,this.celle);
                    }
                    gruppo.distruggo_gruppo();//resetta gli attributi celle_gruppo e celle_gruppo_viste delle celle del gruppo
                }   
            }
        return applicato;
    }
    public boolean xy_wing_generalizzato(){
        Cella vet[]=new Cella[LATO*LATO];
        boolean applicato=false;
        for(int i=0;i<LATO*LATO;i++)
            vet[i]=celle[(int)i/LATO][i%LATO];
        for(int i=0;i<vet.length;i++){
            Gruppo gruppo=vet[i].creo_catena_xy_wing(this.celle);
            if(gruppo!=null){
                gruppo.stampo();
                if(gruppo.xy_wing_generalizzato(celle)){
                    i=-1;
                    vet=Metodi.rimuovo_celle_al_vettore(vet,gruppo.get_celle());
                    applicato=true;
                }
                gruppo.distruggo_gruppo();//sistema le celle_gruppo delle varie celle
            }
        }
        return applicato;
    }
}