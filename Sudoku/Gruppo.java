package Sudoku;
import Metodi.Metodi;
import Enum.Enum;
public class Gruppo{//PUO ESSERE UNA SETTORE O ALTRO

    private int LATO;
    private int lato;
    private Cella celle[];
    public int get_LATO(){
        return LATO;
    }
    public int get_lato(){
        return lato;
    }
    public void set_LATO(int LATO){
        this.LATO=LATO;
        lato=(int)Math.sqrt(LATO);
    }
    public void set_celle(){
        celle=new Cella[LATO];
    }
    public void set_celle(Cella vet_celle[]){
        celle=new Cella[vet_celle.length];
        for(int i=0;i<vet_celle.length;i++)
            set_cella(vet_celle[i],i);
    }
    public void set_cella(Cella cella,int index){
        celle[index]=cella;
    }
    public void set_cella(int posiz,int index){
        celle[index]=new Cella(posiz,get_LATO());
    }
    public Cella get_cella(int i){
        return celle[i];
    }
    public Cella[] get_celle_se_candidati(int vet_num[],boolean inutile){
        Cella celle[]=new Cella[LATO];
        int cont=0;
        for(int i=0;i<celle.length;i++)
            if(get_cella(i).se_candidati(vet_num))
                celle[cont++]=get_cella(i);
        return Metodi.accorcio_lunghezza(celle, cont);
    }
    public int[] get_celle_se_candidati(int vet_num[]){//restituisce un vettore formato dagli indici delle celle i cui candidati sono esattamente quelli di vet_num
        int vet[]=new int[LATO],cont=0;
        for(int i=0;i<celle.length;i++)
            if(get_cella(i).se_candidati(vet_num))
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet, cont);
    }
    public Cella[] get_cella_se_candidati(int quantita,boolean inutile){//ritorna le celle del gruppo il cui numero di candidati è quantità
        Cella vet[]=new Cella[celle.length];
        int cont=0;
        for(int i=0;i<celle.length;i++)
            if(celle[i].get_candidati().length==quantita)
                vet[cont++]=celle[i];
        return Metodi.accorcio_lunghezza(vet,cont);
                
    }
    public int[] get_celle_se_candidati(int quantita){//ri
        int vet[]=new int[celle.length],cont=0;
        for(int i=0;i<celle.length;i++)
            if(celle[i].get_candidati().length==quantita)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet, cont);
    }

    public Cella[] get_celle(){
        return celle;
    }
    public void set_numero(int numero,int i){//mette nell'i-esima cella del gruppo la q.tità numero
        rimuovi_candidato(numero);
        get_cella(i).set_numero(numero);
    }
    public boolean rimuovi_candidato(int numero){//ritorna true se effettivamente ha rimosso qualcosa
        //toglie per ogni cella del settore il numero numero dai candidati della cella
        int vet[]=new int[get_LATO()];
        for(int i=0;i<celle.length;i++)
            vet[i]=i;
        return rimuovi_candidato(numero,vet);
    }
    public int[] rimuovi_candidato(int numero,int vet[]){//ritorna true se effettivamente ha rimosso qualcosa, vet contiene gli indici delle celle del gruppo dalle quali si vuole rimuovere numero
        int vettore[]=new int[get_LATO()],cont=0;//le celle di vettore contengono gli indici delle celle dalle quali si è tolto numero dai candidati
        for(int i=0;i<vet.length;i++)
            if(get_cella(vet[i]).rimuovi_candidato(numero))
                vettore[cont++]=vet[i];
        return Metodi.accorcio_lunghezza(vettore,cont);
    }
    public Gruppo(){
        celle=new Cella[0];
    }
    public Gruppo(Cella cella,int index){//Il lato è sottointeso
        set_index(index);
        set_LATO(cella.get_LATO());
        aggiungi_cella(cella);
    }
    public Gruppo(Cella celle[],int index){
        set_index(index);
        set_LATO(celle[0].get_LATO());
        aggiungi_celle(celle);
    }

    //METODI DI CLASSE
    public int conta_numero(int numero){//VERIFICATA//conta quante volte numero può andare nel settore
        int cont=0;
        for(int i=0;i<LATO;i++){
            if(celle[i].ci_puo_andare(numero))
                cont++;
            if(celle[i].get_numero()==numero){
                cont=1;
                i=LATO-1;
            }
        }
        return cont;
    }
    /*public int[][] conta_numero(int numero,int vet_settore[],String settore,Cella sudoku[][]){
        int vet_settore1[]=new int[vet_settore.length+1];
        vet_settore1[0]=get_index();
        for(int i=1;i<=vet_settore.length;i++)
            vet_settore1[i]=vet_settore[i-1];
        return Metodi.conta_numero(numero, vet_settore1, settore, sudoku);
    }*/
    public int[] conta_numero(int numero,int inutile){//mette 1 in vet nelle celle in cui puo andare numero altrimenti mette 0
        //inutile serve solo a distinguerlo dal metodo precedente
        int cont=0,vet[]=new int[LATO];
        for(int i=0;i<LATO;i++){
            if(celle[i].ci_puo_andare(numero)){
                cont++;
                vet[i]=1;
            }
            else
                vet[i]=0;
            if(celle[i].get_numero()==numero){
                vet[i]=1;
                cont=1;
                i=LATO-1;
            }
        }
        return vet;
    }
    public int[] quali_numeri(){//ritorna unn vettore formato dai numeri che possono andare nel settore
        int vet[]=new int[LATO],cont=0;
        for(int i=1;i<=LATO;i++)
            if(ce_numero(i)==false)
                vet[cont++]=i;
        return Metodi.accorcio_lunghezza(vet, cont);
    }
    public int[] dove_numero(int numero){// ritorna un vettore vet che contiene gli indici (non la posizione) delle celle del settore dove puo andare numero
        int vet_num[]=new int[1];
        vet_num[0]=numero;
        return dove_numeri(vet_num);
    }
    public Cella[] dove_numero(int numero,int inutile){//inutile serve a distinguere questo metodo dal precedente
        int vet_num[]=new int[1];
        vet_num[0]=numero;
        return dove_numeri(vet_num,inutile);
    }
    public int[] dove_numeri(int vet_num[]){//ritorna un vettore con gli indici(e non le posizioni) delle celle del settore dove puo andare almeno un numero di vet_num
        int cont=0,vet[][]=new int[vet_num.length][0],somma[]=new int[LATO];//la riga i-esima di vet contiene conta_numero(vet_num[i])
        for(int i=0;i<vet.length;i++)
            vet[i]=conta_numero(vet_num[i],0);
        for(int i=0;i<LATO;i++){
            somma[i]=0;
            for(int j=0;j<vet_num.length;j++)
                somma[i]=somma[i]+vet[j][i];
        }
        int vet_ritornato[]=new int[LATO];
        for(int i=0;i<LATO;i++)
            if(somma[i]>0)
                vet_ritornato[cont++]=i;
        int temp[]=new int [cont];
        for(int i=0;i<cont;i++)
            temp[i]=vet_ritornato[i];
        return temp;
    }
    public Cella[] dove_numeri(int vet_num[],int inutile){//Ritorna le celle del settore dove puo andare almeno un numero di vet_num
        int vet_celle[]=dove_numeri(vet_num);
        Cella celle[]=new Cella[vet_celle.length];
        for(int i=0;i<celle.length;i++)
            celle[i]=get_cella(vet_celle[i]);
        return celle;
    }
    public boolean ce_numero(int numero){
        for(int i=0;i<LATO;i++)
            if(celle[i].get_numero()==numero)
                return true;
        return false;
    }
    public boolean assurdo(){
        for(int i=0;i<celle.length;i++)
            if(celle[i].get_candidati().length==0){
                System.out.println("Il sudoku è assurdo perche' alla cella "+celle[i].get_posiz()+" non ci possono andare numeri");
                return true;
            }
        for(int i=0;i<celle.length-1;i++)
            if(celle[i].get_numero()!=0)
                for(j=i+1;j<celle.length;j++)
                    if(celle[i].get_numero()==celle[j].get_numero()){
                        System.out.println("Il sudoku è assurdo perche' nel gruppo il numero "+celle[i].get_numero()+" ci puo andare due volte");
                        return true;
                    }
        return false;
    }
    public Gruppo creo_identico(){
        Gruppo gruppo=new Gruppo(new Cella[celle.length],this.index);
        for(int i=0;i<celle.length;i++){
            gruppo.get_cella(i).set_numero(get_cella(i).get_numero());
            gruppo.get_cella(i).set_candidati(get_cella(i).get_candidati());
            gruppo.get_cella(i).set_posiz(get_cella(i).get_posiz());
        }
    }
    public int[] settori_opposti(int vet_settori[]){
        int vet[]=new int[vet_settori.length+1];
        vet[0]=get_index();
        for(int i=1;i<=vet_settori.length;i++)
            vet[i]=vet_settori[i-1];
        int vettore[]=Metodi.celle_opposte(vet,LATO);
        return vettore;
    }
    public void aggiungi_cella(Cella cella){
        if(cella==null || Metodi.ce_elemento(celle,cella))
            return;
        celle=Metodi.aggiungo_cella_al_vettore(celle,cella);
        for(int i=0;i<celle.length;i++)
            celle[i].set_celle_gruppo(celle);
        for(int i=0;i<celle.length-1;i++)
            if(celle[i].get_riga()==cella.get_riga() || celle[i].get_colonna()==cella.get_colonna() || celle[i].get_tabella()==cella.get_tabella()){
                if(Metodi.ce_elemento(celle[i].get_celle_gruppo_viste(),cella)==false)
                    celle[i].set_celle_gruppo_viste(Metodi.aggiungo_cella_al_vettore(celle[i].get_celle_gruppo_viste(),cella));
                if(Metodi.ce_elemento(cella.get_celle_gruppo_viste(),celle[i])==false)
                    cella.set_celle_gruppo_viste(Metodi.aggiungo_cella_al_vettore(cella.get_celle_gruppo_viste(),celle[i]));
            }
    }
    public void aggiungi_celle(Cella vet_celle[]){//aggiunge le celle di vet_celle in this.celle e aggiorna celle_gruppo, celle_gruppo_comune
        //il metodo dovrebbe controllare se la cella che si vuole inserire non si trova già nel gruppo
        if(celle==null){
            celle=vet_celle;
            return;
        }
        for(int i=0;i<celle.length;i++)
            aggiungi_cella(vet_celle[i]);
    }
    public void risolvo(Cella sudoku[][]){
        for(int i=0;i<celle.length;i++)
            if(celle[i].get_candidati().length==1)
                return;
    }
    public boolean ce_cella(Cella cella){
        for(int i=0;i<celle.length;i++)
            if(celle[i]==cella)
                return true;
        return false;
    }
    public void stampo(){
        for(int i=0;i<celle.length;i++){
            System.out.print("Cella "+i+" "+celle[i].get_posiz()+".Celle viste: ");
            for(int j=0;j<celle[i].get_celle_gruppo_viste().length;j++)
                System.out.print(celle[i].get_cella_gruppo_vista(j).get_posiz()+" ");
            System.out.println("");
        }
    }
    public void distruggo_gruppo(){//quando un gruppo non si deve più usare bisogna inizializzare le celle_gruppo_viste di ogni sua cella
        for(int i=0;i<celle.length;i++){
            celle[i].set_celle_gruppo(new Cella[0]);
            celle[i].set_celle_gruppo_viste(new Cella[0]);
        }
    }
    //TECNICHE RISOLUTIVE
    public boolean xy_wing_generalizzato(Cella sudoku[][]){
        boolean applicato=false;
        for(int i=0;i<celle.length-2;i++){//faccio partire catena con primo elemento celle[i](che sarà l'ala)
            for(int j=0;j<celle[i].get_celle_gruppo_viste().length;j++){
                if(celle[i].xy_wing(celle[i].get_cella_gruppo_vista(j),sudoku))//celle[i] è il perno mentre celle[i].get_cella_gruppo_vista(j) è l'ala
                    applicato=true;
                for(int k=0;k<celle[i].cella_gruppo_vista(j).celle_gruppo_viste().length;k++){
                    int perno=Metodi.get_perno(celle[i].cella_gruppo_vista(j),celle[i].cella_gruppo_vista(j).get_cella_gruppo_vista(k));
                    int vet[]=Metodi.get_ali(celle[i].cella_gruppo_vista(j),celle[i].cella_gruppo_vista(j).get_cella_gruppo_vista(k));
                    if(Metodi.uguali(vet,celle[i].get_candidati())==false){
                        Cella sudoku1[][]=sudoku;
                        sudoku1[(int)celle[i].cella_gruppo_vista(j).cella_gruppo_vista(k).get_posiz()/LATO][celle[i].cella_gruppo_vista(j).cella_gruppo_vista(k).get_posiz()%LATO].set_candidati(vet);
                        if(celle[i].xy_wing(celle[i].cella_gruppo_vista(j).cella_gruppo_vista(k),sudoku1))
                            applicato=true;
                    }
                }
            }

        }
        return applicato;
    }
}