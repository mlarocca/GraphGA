package org.GCGA.client;
import org.GCGA.client.utility.DoublePoint;
import org.GCGA.client.Graphs.Graph;
import org.GCGA.client.utility.Point;
import org.GCGA.client.GUI.PersonalInfoPanel;
import org.GCGA.client.GUI.GraphPanel;
import org.GCGA.client.algorithm.GeneticAlgorithm;
import org.GCGA.client.utility.Pair;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import java.io.PrintStream;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Owner
 */
public class GA_Element implements GA_Element_Interface{
    
    private Chromosome chromosome;
    private boolean changed;
    private long fitness;

    private static final byte CROSS_VERTEX_MULTIPLIER_FACTOR = 4;

    public GA_Element(Chromosome c){
        this.chromosome = (Chromosome)c.clone();
        fitness = 0;
        this.markChanged();
    }

    public GA_Element(int chromosome_length){
        this.chromosome = new Chromosome(chromosome_length, GeneticAlgorithm.getGrid().gridSize());
        fitness = 0;
        this.markChanged();
    }

    public Object clone(){
        return new GA_Element(this.chromosome);
    }

    public Chromosome getChromosome(){
        return chromosome;
    }

    public long fitness(){
        return this.fitness(true);  //Calcola anche la sovrapposizione di vertici e archi
    }

    public long crossingNumber(){
        return this.fitness(false); //Calcola solo l'incrocio tra archi (ma non la sovrapposizione con vertici)
    }

    public String toJSON(Graph g){
        
        JSONObject element = new  JSONObject();

        element.put("graphType", new JSONNumber(g.getType()) );
        element.put("vNbr", new JSONNumber(g.verticesNumber()) );
        element.put("gridSideSize", new JSONNumber(GeneticAlgorithm.getGrid().sideSize()) );
        element.put("crossingNbr", new JSONNumber( this.crossingNumber() ) );
        element.put("gridPositions", new JSONString( this.getChromosome().toJSONArray().toString() )  );
        element.put("valid",  JSONBoolean.getInstance( this.isValid() ) );
        element.put("showEmail",  JSONBoolean.getInstance( PersonalInfoPanel.getShowEmail() ) );
        element.put("email", new JSONString( LoginHandler.get().getEmail() )  );
        //element.put("ip", new JSONString( "user_ip" ) );

        return "["+element.toString()+"]";
    }

    public boolean isValid(){

        int l = GeneticAlgorithm.getGraph().edgesNumber();

        long intersections = 0; //Fattore di moltiplicazione;
        Pair<Integer, Integer> e1; //Archi
        GridCoordinates v, w; //Vertex coordinates inside the grid

        int min_r, max_r, min_c, max_c, i, k, step_c, step_r; //Variabili temporanee di utilità

        int delta_col, delta_row;

        for (i=0; i<l; i++){                //Esamina tutti gli archi
            //Invariante: le coppie di vertici devono essere ordinate!
            e1 = GeneticAlgorithm.getGraph().getEdgeVertices(i);
            v = GeneticAlgorithm.getGrid().gridCoordinates(chromosome.getGeneValue( e1.getFirst() ) );
            w = GeneticAlgorithm.getGrid().gridCoordinates(chromosome.getGeneValue( e1.getSecond() ) );
//PER PRIMA COSA, controlla se l'arco attraversa altri vertici
//Se l'arco attraversa altri vertici, il fattore di moltiplicazione della fitness viene raddoppiato

            delta_row = Math.abs( v.row() - w.row() );
            delta_col = Math.abs( v.column() - w.column() );

            //Arco tra vertici di una stessa riga
            if (   v.row() == w.row() && delta_col > 1  ){

                min_c = Math.min(v.column() , w.column());
                max_c = Math.max(v.column() , w.column());
                for ( k = min_c + 1; k<max_c; k++ ){
                    //Se c'è un vertice sul percorso dell'arco
                    if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(v.row(), k)) ) )
                       intersections++;
                }

            }
            //Arco tra vertici di una stessa colonna
            else if ( v.column() == w.column() && delta_row > 1 ){

                min_r = Math.min(v.row() , w.row());
                max_r = Math.max(v.row() , w.row());
                for ( k = min_r + 1; k<max_r; k++ ){
                    //Se c'è un vertice sul percorso dell'arco
                    if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(k, v.column()) )) )
                        intersections++;
                }

            }
            //Arco in diagonale (perfetta)
            else if ( delta_col == delta_row ){
             //INVARIANTE: righe e colonne devono avere valori diversi (perché la differenza è uguale e quindi non può essere per entrambe nulla)


                if ( v.row() < w.row() ){
                    min_r = v.row();
                    max_r = w.row();
                    min_c = v.column();

                    step_c = v.column() < w.column() ? 1 : -1;
                }else{
                    //w.row < v.row
                    min_r = w.row();
                    max_r = v.row();
                    min_c = w.column();
                    step_c = w.column() < v.column() ? 1 : -1;
                }

                for ( k = min_r + 1; k<max_r; k++ ){
                    //Se c'è un vertice sul percorso dell'arco
                    min_c += step_c;
                    if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(k, min_c) )) )
                        intersections++;
                }
                 }else if (delta_col*delta_row>0){
                    //Diagonale non perfetta
                    //CONTROLLARE CHE IL RESTO DELLA DIVISONE DEI RAPPORTI SIA ZERO

                    //Calcola il massimo comun divisore
                    int mcd = UtilityFunctions.MCD(delta_col, delta_row);
                    if ( mcd > 1){
                        step_c = delta_col/mcd;
                        step_r = delta_row/mcd;

                        if ( v.row() < w.row() ){
                            min_r = v.row();
                            max_r = w.row();
                            min_c = v.column();
                            if (w.column() < v.column())
                                step_c = -step_c;
                        }else{
                            //w.row < v.row
                            min_r = w.row();
                            max_r = v.row();
                            min_c = w.column();
                            if (v.column() < w.column())
                                step_c = -step_c;
                        }
                        for ( k = min_r + step_r; k<max_r; k+=step_r ){
                            //Se c'è un vertice sul percorso dell'arco
                            min_c += step_c;
                            if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(k, min_c) )) )
                                intersections++;
                        }
                    }
                }
        }
        return intersections == 0;
    }
    
    private long fitness(boolean computeMultiplier){

        if ( !this.changed ){
            return this.fitness;
        }

        int l = GeneticAlgorithm.getGraph().edgesNumber();

        long f = 0; //Fitness value;
        long multiplier = 1; //Fattore di moltiplicazione;
        Pair<Integer, Integer> e1, e2; //Archi
        GridCoordinates v, w, a, b, tmp_c; //Vertex coordinates inside the grid

        int min_r, max_r, min_c, max_c, i, j, k, step_c, step_r; //Variabili temporanee di utilità

        Point p_v, p_w, p_a, p_b; //Coordinate cartesiane dei vertici;
        DoublePoint p_iv = new DoublePoint(), p_iw = new DoublePoint(); //Variabili di utilità per memorizzare x_1,x_2 o y_1,y_2 (a seconda dei casi) nel calcolo dei punti I1, I2 per determinare la zona rossa

        int delta_col, delta_row;

        final int DELTA = 5;

        double teta_A, teta_B, alpha_V, alpha_B, beta_W, beta_B;
        
        for (i=0; i<l; i++){                //Esamina tutti gli archi
            //Invariante: le coppie di vertici devono essere ordinate!
            e1 = GeneticAlgorithm.getGraph().getEdgeVertices(i);
            v = GeneticAlgorithm.getGrid().gridCoordinates(chromosome.getGeneValue( e1.getFirst() ) );
            w = GeneticAlgorithm.getGrid().gridCoordinates(chromosome.getGeneValue( e1.getSecond() ) );
//PER PRIMA COSA, controlla se l'arco attraversa altri vertici
//Se l'arco attraversa altri vertici, il fattore di moltiplicazione della fitness viene raddoppiato

            if ( computeMultiplier ){   //Lo calcola solo se indicato!
                delta_row = Math.abs( v.row() - w.row() );
                delta_col = Math.abs( v.column() - w.column() );

                //Arco tra vertici di una stessa riga
                if (   v.row() == w.row() && delta_col > 1  ){

                    min_c = Math.min(v.column() , w.column());
                    max_c = Math.max(v.column() , w.column());
                    for ( k = min_c + 1; k<max_c; k++ ){
                        //Se c'è un vertice sul percorso dell'arco
                        if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(v.row(), k)) ) )
                            multiplier *= CROSS_VERTEX_MULTIPLIER_FACTOR;
                    }

                }
                //Arco tra vertici di una stessa colonna
                else if ( v.column() == w.column() && delta_row > 1 ){

                    min_r = Math.min(v.row() , w.row());
                    max_r = Math.max(v.row() , w.row());
                    for ( k = min_r + 1; k<max_r; k++ ){
                        //Se c'è un vertice sul percorso dell'arco
                        if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(k, v.column()) )) )
                            multiplier *= CROSS_VERTEX_MULTIPLIER_FACTOR;
                    }

                }
                //Arco in diagonale (perfetta)
                else if ( delta_col == delta_row ){
                 //INVARIANTE: righe e colonne devono avere valori diversi (perché la differenza è uguale e quindi non può essere per entrambe nulla)


                    if ( v.row() < w.row() ){
                        min_r = v.row();
                        max_r = w.row();
                        min_c = v.column();

                        step_c = v.column() < w.column() ? 1 : -1;
                    }else{
                        //w.row < v.row
                        min_r = w.row();
                        max_r = v.row();
                        min_c = w.column();
                        step_c = w.column() < v.column() ? 1 : -1;
                    }

                    for ( k = min_r + 1; k<max_r; k++ ){
                        //Se c'è un vertice sul percorso dell'arco
                        min_c += step_c;
                        if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(k, min_c) )) )
                            multiplier *= CROSS_VERTEX_MULTIPLIER_FACTOR;
                    }
                }else if (delta_col*delta_row>0){
                    //Diagonale non perfetta
                    //CONTROLLARE CHE IL RESTO DELLA DIVISONE DEI RAPPORTI SIA ZERO

                    //Calcola il massimo comun divisore
                    int mcd = UtilityFunctions.MCD(delta_col, delta_row);
                    if ( mcd > 1){
                        step_c = delta_col/mcd;
                        step_r = delta_row/mcd;

                        if ( v.row() < w.row() ){
                            min_r = v.row();
                            max_r = w.row();
                            min_c = v.column();
                            if (w.column() < v.column())
                                step_c = -step_c;
                        }else{
                            //w.row < v.row
                            min_r = w.row();
                            max_r = v.row();
                            min_c = w.column();
                            if (v.column() < w.column())
                                step_c = -step_c;
                        }
                        for ( k = min_r + step_r; k<max_r; k+=step_r ){
                            //Se c'è un vertice sul percorso dell'arco
                            min_c += step_c;
                            if ( chromosome.hasPosition(GeneticAlgorithm.getGrid().gridPosition(new GridCoordinates(k, min_c) )) )
                                multiplier *= CROSS_VERTEX_MULTIPLIER_FACTOR;
                        }
                    }
                }
            }

//POI PASSA A CALCOLARE LE INTERSEZIONI EFFETTIVE


            for (j=i+1; j<l; j++){
                //Invariante: se j>i =>     edge[j].first > edge[i].first ||
                //                      || (edge[j].first == edge[i].first && edge[j].second > edge[i].second)
                e2 = GeneticAlgorithm.getGraph().getEdgeVertices(j);

                if ( e1.getFirst() == e2.getFirst() || e1.getFirst() == e2.getSecond() || e1.getSecond() == e2.getFirst() || e1.getSecond() == e2.getSecond() ){
                    //Archi incidenti su uno stesso vertice non possono intersecarsi
                    continue;
                }

                a = GeneticAlgorithm.getGrid().gridCoordinates(chromosome.getGeneValue( e2.getFirst() ) );
                b = GeneticAlgorithm.getGrid().gridCoordinates(chromosome.getGeneValue( e2.getSecond() ) );

                //PRECONDIZIONE: ra <=rb
                if ( a.row() > b.row() ){
                    tmp_c = a;
                    a = b;
                    b = tmp_c;
                }

                if ( v.column() == w.column() ){
                    //Caso 1 (particolare)
                    if ( (a.column() <= v.column()) == (b.column() <= v.column())
                       || (a.column() == v.column() || a.column() == w.column() )
                    ){
                        //A e B giacciono nello stesso semipiano rispetto ll'arco v->w
                        //1.1.a
                        continue;   //Nessuna intesezione possibile
                    }
                    //else
                    p_v = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(v));
                    p_w = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(w));
                    p_a = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(a));
                    p_b = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(b));

                    //Inutile calcolare teta_A e teta_B: a questo punto i due vertici sono in due semipiani opposti rispetto a v->w

                    //Calcola Iv ed Iw
                    if ( v.row() < w.row() ){
                        p_iv.x = p_v.x;
                        p_iv.y = p_v.y - DELTA;
                        p_iw.x = p_w.x;
                        p_iw.y = p_w.y + DELTA;
                    }else{
                        //INVARIANTE: rv > rw
                        p_iv.x = p_v.x;
                        p_iv.y = p_v.y + DELTA;
                        p_iw.x = p_w.x;
                        p_iw.y = p_w.y - DELTA;
                    }
                    
                }else{
                    //PRECONDIZIONE: cv <= cw
                    if ( v.column() > w.column() ){
                        tmp_c = v;
                        v = w;
                        w = tmp_c;
                    }

                    if ( ( v.row() == w.row() && a.row() == v.row() && (a.row() == v.row() || v.row() == b.row()) )
                       || b.row() < Math.min(v.row(), w.row()) || a.row() > Math.max(v.row(), w.row())
                    ){
                        //not(ra<rv and rv<rb ) = not(ra<rv) or not(rv<rb) = ra>=rv or rv>=rb
                        //2.1.a
                        continue;   //Nessuna intesezione possibile
                    }
                    //else
                    p_v = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(v));
                    p_w = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(w));
                    p_a = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(a));
                    p_b = GeneticAlgorithm.getGrid().cartesianCoordinates(GeneticAlgorithm.getGrid().gridPosition(b));
                    //Inutile calcolare teta_A e teta_B: a questo punto i due vertici sono in due semipiani opposti rispetto a v->w

                    teta_A = (double)(p_a.y - p_v.y)/(p_v.y - p_w.y) - (double)(p_a.x - p_v.x)/(p_v.x - p_w.x);

                    teta_B = (double)(p_b.y - p_v.y)/(p_v.y - p_w.y) - (double)(p_b.x - p_v.x)/(p_v.x - p_w.x);

                    if ( teta_A * teta_B > 0 ){
                        //Caso 2.2.b
                        continue;   //Nessuna intesezione possibile
                    }

                    //Calcola Iv ed Iw
                    //INVARIANTE: cv < vw
                    teta_A = (double)(p_v.y - p_w.y)/(p_v.x - p_w.x); //teta_A non serve + => viene riutilizzata
                    p_iv.x = p_v.x - DELTA ;
                    p_iv.y = p_w.y + teta_A * (p_iv.x - p_w.x) ;
                    p_iw.x = p_w.x + DELTA;
                    p_iw.y = p_w.y + teta_A * (p_iw.x - p_w.x) ;
                }//END ELSE [if v.column() == w.column()]

                //Calcola alpha_V ed alpha_B
                    //teta_A e teta_B hanno esaurito la loro vita => riutilizza teta_A per calcolare alpha_A e teta_B per calcolare alpha_B
                alpha_V = (double)(p_iv.y - p_a.y)/(p_v.y - p_a.y) - (double)(p_iv.x - p_a.x) / (p_v.x - p_a.x);
                alpha_B = (double)(p_b.y - p_a.y)/(p_v.y - p_a.y) - (double)(p_b.x - p_a.x) / (p_v.x - p_a.x);

                //Calcola beta_V ed beta_B

                beta_W = (double)(p_iw.y - p_a.y)/(p_w.y - p_a.y) - (double)(p_iw.x - p_a.x) / (p_w.x - p_a.x);
                beta_B = (double)(p_b.y - p_a.y)/(p_w.y - p_a.y) - (double)(p_b.x - p_a.x) / (p_w.x - p_a.x);

                if ( alpha_V * alpha_B < 0  &&  beta_W * beta_B < 0 ){
                    //C'è intersezione
                    f++;
                }

            }//END FOR

        }
        f = f != 0 ? f*multiplier : (multiplier-1);
        return f < 0 ? Long.MAX_VALUE : f;
    }

    public double computeDistance(GA_Element other) throws IllegalArgumentException{
        Chromosome g1 = this.getChromosome();
        Chromosome g2 = other.getChromosome();
        Grid tempGrid = GeneticAlgorithm.getGrid();

        int n = g1.genesNumber();
        double dist = 0;
        if ( n != g2.genesNumber() || n == 0)
            throw new IllegalArgumentException();

        GridCoordinates p1, p2;
        for(int i=0; i<n; i++){
            p1 = tempGrid.gridCoordinates(g1.getGeneValue(i));
            p2 = tempGrid.gridCoordinates(g2.getGeneValue(i));
            dist += Math.sqrt( Math.pow(p1.column() - p2.column(), 2) + Math.pow(p1.row() - p2.row(), 2) );
        }
        return dist/n;
    }

    public void print(PrintStream p){
        p.print(" - ");
        for (int i=0; i<chromosome.genesNumber(); i++){
            p.print(chromosome.getGeneValue(i) + " ");
        }
        p.print(" | fitness: " + fitness());
        p.println();
    }

    public void drawGraph(GraphPanel gP){

        //Prima i Vertici
        for (int i=0; i<chromosome.genesNumber(); i++){
            GeneticAlgorithm.getGrid().paintVertex(gP, i, chromosome.getGeneValue(i));
        }
        //infine gli archi...
        for (int i=0; i < GeneticAlgorithm.getGraph().edgesNumber(); i++ ){
            Pair vertices = GeneticAlgorithm.getGraph().getEdgeVertices(i);
            GeneticAlgorithm.getGrid().paintEdge(gP, (Integer)vertices.getFirst(), (Integer)vertices.getSecond());
        }

    }

    @Override
    public String toString(){
        return chromosome.toString();
    }

    public void markChanged(){
        changed = true;
    }

}

