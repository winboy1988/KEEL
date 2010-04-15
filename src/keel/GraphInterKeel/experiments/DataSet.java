package keel.GraphInterKeel.experiments;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.*;
import java.util.Vector;

/**
 * This Class represents the data set node in the graph. It also stores the
 * data partitions and their respective paths
 * @author Modified by Julian Luengo 27/03/2009
 */
public final class DataSet extends Node {

    // List is now a table names. Each row contains a list of vectors associated to a dataset
    public Vector tableVector; // files associated to each dataset
    protected boolean modified;
    /** indicates if all the partitions are present */
    protected boolean complete;
    /** stores the missing partitions*/
    protected Vector missing;

    public DataSet() {
        super();
    }

    public DataSet(ExternalObjectDescription dsc, Point position, GraphPanel p,
            Vector newTable) {
        super(dsc, position, p.mainGraph.getId());

        File dir;
        String ficheros[];
        boolean metido, cont;
        Vector listas = new Vector();
        missing = new Vector();
        int folds;

        for (int i = 0; i < dsc.getNamesLength(); i++) {
            listas.addElement(new Vector());
            missing.addElement(new Vector());
        }

        /*System.out.println("Building DATASET with " +
        " name = " + dsc.enumerateNames() +
        " path = " + dsc.getPath() +
        " subtype = " + dsc.getSubtype() +
        " numdatasets =" + dsc.getNamesLength());*/

        actInputOutput(dsc, p);

        p.parent.setNumDatasets(dsc.getAllNames());

        p.mainGraph.setId(p.mainGraph.getId() + 1);
        type = type_Dataset;
        pd = p;
        image = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("/keel/GraphInterKeel/resources/ico/experiments/dataset.gif"));
        modified = false;
        complete = true;
        if (newTable == null) {
            this.tableVector = new Vector();
            for (int i = 0; i < dsc.getNamesLength(); i++) {
                this.tableVector.addElement(new Vector());
            }
            if (pd.parent.expType == Experiments.UNSUPERVISED) {
                for (int k = 0; k < dsc.getNamesLength(); k++) {
                    ((Vector) (listas.elementAt(k))).add(dsc.getName(k) + ".dat" + "," + dsc.getName(k) + ".dat");
                }
            } else {

                if (pd.parent.cvType == Experiments.PK) {
                    //add k fold cross validation files for each layer
                    folds = pd.parent.numberKFoldCross;
                    for (int k = 0; k < dsc.getNamesLength(); k++) {
//    		  dir = new File(dsc.getPath(k) + dsc.getNombre(k));
                        try {
                            dir = new File("." + dsc.getPath(k) + dsc.getName(k));
                            ficheros = dir.list();
                            //Nuevas funcionalidades .Bucle comentado para que coja un kfold especificado

                            for (int l = 1; l <= pd.parent.numberKFoldCross; l++) { //Nuevo bucle
                                cont = true;
                                String pareja = "";
                                metido = false;
                                for (int j = 0; j < ficheros.length; j++) {

                                    if (ficheros[j].compareTo(dsc.getName(k) + "-" + folds + "-" + l + "tra.dat") == 0) {
                                        metido = true;
                                        pareja = ficheros[j] + ",";

                                        break;

                                    }
                                }

                                if (!metido) {
                                    cont = false;
                                    complete = false;
                                    pareja = dsc.getName(k) + "-" + folds + "-" + l + "tra.dat,";
                                }
                                metido = false;
                                for (int j = 0; j < ficheros.length && cont; j++) {
                                    if (ficheros[j].compareTo(dsc.getName(k) + "-" + folds + "-" + l + "tst.dat") == 0) {
                                        metido = true;
                                        pareja += ficheros[j];
                                        break;
                                    }
                                }
                                //we put the current partition in the missing list if not found
                                if (!metido) {
                                    complete = false;
                                    pareja += dsc.getName(k) + "-" + folds + "-" + l + "tst.dat";
                                    ((Vector) missing.elementAt(k)).add(pareja);
                                } else {
                                    ((Vector) (listas.elementAt(k))).add(pareja);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (pd.parent.cvType == Experiments.P5X2) {
                    // add 5x2 cross validation files for each layer
                    for (int k = 0; k < dsc.getNamesLength(); k++) {
                        try {
                            dir = new File("." + dsc.getPath(k) + dsc.getName(k));
                            ficheros = dir.list();

                            for (int i = 1; i <= 5; i++) {
                                cont = true;
                                String pareja = "";
                                metido = false;
                                for (int j = 0; j < ficheros.length; j++) {
                                    if (ficheros[j].indexOf(dsc.getName(k) + "-5x2-" + i + "tra.dat") == 0) {
                                        pareja = ficheros[j] + ",";
                                        metido = true;
                                        break;
                                    }
                                }
                                if (!metido) {
                                    complete = false;
                                    cont = false;
                                    pareja = dsc.getName(k) + "-5x2-" + i + "tra.dat,";
                                }
                                metido = false;
                                for (int j = 0; j < ficheros.length && cont; j++) {
                                    if (ficheros[j].indexOf(dsc.getName(k) + "-5x2-" + i + "tst.dat") == 0) {
                                        pareja += ficheros[j];
                                        metido = true;
                                        break;
                                    }
                                }
                                //we put the current partition in the missing list if not founf
                                if (!metido) {
                                    complete = false;
                                    pareja += dsc.getName(k) + "-5x2-" + i + "tst.dat";
                                    ((Vector) missing.elementAt(k)).add(pareja);
                                } else {
                                    ((Vector) (listas.elementAt(k))).add(pareja);
                                }

                            }
                        } catch (Exception e) {
                        }
                    }
                } else {
                    boolean found=false;
                    for (int k = 0; k < dsc.getNamesLength(); k++) {
                        try {
                            dir = new File("." + dsc.getPath(k) + dsc.getName(k));
                            ficheros = dir.list();
                            for (int j = 0; j < ficheros.length && !found; j++) {
                                 if (ficheros[j].compareTo(dsc.getName(k) + "-10-1tra.dat") == 0) {
                                       ((Vector) (listas.elementAt(k))).add(dsc.getName(k) + "-10-1tra.dat" + "," + dsc.getName(k) + "-10-1tst.dat");
                                       found=true;
                                 }
                                 else{
                                     if(ficheros[j].compareTo(dsc.getName(k) + "-5-1tra.dat") == 0){
                                        ((Vector) (listas.elementAt(k))).add(dsc.getName(k) + "-5-1tra.dat" + "," + dsc.getName(k) + "-5-1tst.dat");
                                        found=true;
                                     }
                                 }
                            }
                            if (!found){
                                ((Vector) (listas.elementAt(k))).add(dsc.getName(k) + ".dat" + "," + dsc.getName(k) + ".dat");
                            }
                        }catch (Exception e) {
                        }
                    }

                }
            }
            for (int l = 0; l < dsc.getNamesLength(); l++) {
                this.tableVector.setElementAt((Vector) (((Vector) listas.elementAt(l)).clone()), l);
            }

        } else {
            this.tableVector = newTable;
        }
    }

    public DataSet(ExternalObjectDescription dsc, Point position, GraphPanel p,
            Vector table, boolean modified, int id) {
        super(dsc, position, id);

        actInputOutput(dsc, p);
        type = type_Dataset;
        pd = p;
        image = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("/keel/GraphInterKeel/resources/ico/experiments/dataset.gif"));
        this.modified = modified;

        if (table == null) {
            this.tableVector = new Vector();
            for (int i = 0; i < dsc.getNamesLength(); i++) {
                this.tableVector.addElement(new Vector());
            }
        } else {
            this.tableVector = table;
        }
    }

    public void showDialog() {
        if (pd.parent.cvType == Experiments.P5X2) {
            dialog = new DialogDataset2(pd.parent, "DataSet", true, this, Layer.layerActivo);
        } else {
            dialog = new DialogDataset(pd.parent, "DataSet", true, this, Layer.layerActivo);
        }

        // Center dialog
        dialog.setSize(464, 580);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = dialog.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        dialog.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public void actInputOutput(ExternalObjectDescription dsc, GraphPanel p) {

        // Initializing all variables to erase the last value (useful in case
        // that we have erased a data set

        m_bInputContinuous = m_bInputInteger = m_bInputNominal = m_bInputMissing = m_bInputImprecise = m_bInputMultiClass = m_bInputMultiOutput = false;
        m_bOutputContinuous = m_bOutputInteger = m_bOutputNominal = m_bOutputMissing = m_bOutputImprecise = m_bOutputMultiClass = m_bOutputMultiOutput = false;
        m_sDatasetHasContinuous = "";
        m_sDatasetHasInteger = "";
        m_sDatasetHasNominal = "";
        m_sDatasetHasMissing = "";
        m_sDatasetHasImprecise = "";
        m_sDatasetHasMultiClass = "";
        m_sDatasetHasMultiOutput = "";


        //Un Nodo puede contener m�s de un DataSet
        //lo busco y calculo el tipo de datos de este nodo
        for (int iname = 0; iname < dsc.name.length; iname++) {
            for (int i = 0; i < p.parent.listData.length; i++) {

                //System.out.println (" Data: "+p.padre.listData[i].nameAbr + " - " +  dsc.nombre[0] );
                if (p.parent.listData[i].nameAbr.equalsIgnoreCase(dsc.name[iname])) {
                    //System.out.println (" \n\nALGORISME TROBAT");
                    //System.out.println ("  > Continuous: "+p.padre.listAlgor[i].m_bInputContinuous );
                    //System.out.println ("  > Integer: "+p.padre.listAlgor[i].m_bInputInteger );
                    //System.out.println ("  > NOminal: "+p.padre.listAlgor[i].m_bInputNominal );
                    //System.out.println ("  > Missing: "+p.padre.listAlgor[i].m_bInputMissing );

                    m_bInputContinuous |= p.parent.listData[i].m_bContinuous;
                    m_bInputInteger |= p.parent.listData[i].m_bInteger;
                    m_bInputNominal |= p.parent.listData[i].m_bNominal;
                    m_bInputMissing |= p.parent.listData[i].m_bMissing;
                    m_bInputImprecise |= p.parent.listData[i].m_bImprecise;
                    m_bInputMultiClass |= p.parent.listData[i].m_bMultiClass;
                    m_bInputMultiOutput |= p.parent.listData[i].m_bMultiOutput;

                    m_bOutputContinuous |= p.parent.listData[i].m_bContinuous;
                    m_bOutputInteger |= p.parent.listData[i].m_bInteger;
                    m_bOutputNominal |= p.parent.listData[i].m_bNominal;
                    m_bOutputMissing |= p.parent.listData[i].m_bMissing;
                    m_bOutputImprecise |= p.parent.listData[i].m_bImprecise;
                    m_bOutputMultiClass |= p.parent.listData[i].m_bMultiClass;
                    m_bOutputMultiOutput |= p.parent.listData[i].m_bMultiOutput;


                    if (p.parent.listData[i].m_bContinuous) {
                        m_sDatasetHasContinuous = m_sDatasetHasContinuous + " " + p.parent.listData[i].nameAbr;
                    }
                    if (p.parent.listData[i].m_bInteger) {
                        m_sDatasetHasInteger = m_sDatasetHasInteger + " " + p.parent.listData[i].nameAbr;
                    }
                    if (p.parent.listData[i].m_bNominal) {
                        m_sDatasetHasNominal = m_sDatasetHasNominal + " " + p.parent.listData[i].nameAbr;
                    }
                    if (p.parent.listData[i].m_bMissing) {
                        m_sDatasetHasMissing = m_sDatasetHasMissing + " " + p.parent.listData[i].nameAbr;
                    }
                    if (p.parent.listData[i].m_bImprecise) {
                        m_sDatasetHasImprecise = m_sDatasetHasImprecise + " " + p.parent.listData[i].nameAbr;
                    }
                    if (p.parent.listData[i].m_bMultiClass) {
                        m_sDatasetHasMultiClass = m_sDatasetHasMultiClass + " " + p.parent.listData[i].nameAbr;
                    }
                    if (p.parent.listData[i].m_bMultiOutput) {
                        m_sDatasetHasMultiOutput = m_sDatasetHasMultiOutput + " " + p.parent.listData[i].nameAbr;
                    }
                    /*
                    System.out.println("m_sDatasetHasContinuous = " + m_sDatasetHasContinuous);
                    System.out.println("m_sDatasetHasInteger =    " + m_sDatasetHasInteger);
                    System.out.println("m_sDatasetHasNominal =    " + m_sDatasetHasNominal);
                    System.out.println("m_sDatasetHasMissing =    " + m_sDatasetHasMissing);
                    System.out.println("m_sDatasetHasImprecise =  " + m_sDatasetHasImprecise);
                    System.out.println("m_sDatasetHasMultiClass = " + m_sDatasetHasMultiClass);
                    System.out.println("m_sDatasetHasMultiOutput = " + m_sDatasetHasMultiOutput);*/


                    break;
                }
            }
        }
    //System.out.println ("####################################");
    //System.out.println ("  > Continuous: "+m_bInputContinuous );
    //System.out.println ("  > Integer: "+m_bInputInteger );
    //System.out.println ("  > Nominal: "+m_bInputNominal );
    //System.out.println ("  > Missing: "+m_bInputMissing );
    //System.out.println ("  > Imprecise: "+m_bOutputImprecise );
    //System.out.println ("  > MultiClass: "+m_bOutputMultiClass );
    //System.out.println ("  > MultiOutput: "+m_bOutputMultiOutput );

    }// end copyInputOutput

    public void draw(Graphics2D g2, boolean select) {
        Point pinit = new Point(centre.x - 25, centre.y - 25);
        Point pfin = new Point(centre.x + 25, centre.y + 25);
        figure = new RoundRectangle2D.Float(pinit.x, pinit.y,
                Math.abs(pfin.x - pinit.x),
                Math.abs(pfin.y - pinit.y), 20, 20);

        g2.setColor(Color.black);
        if (select) {
            Stroke s = g2.getStroke();
            g2.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{1, 1}, 0));
            g2.draw(figure);
            g2.setStroke(s);
        } else {
            g2.draw(figure);

        }
        g2.drawImage(image, centre.x - 25, centre.y - 25, 50, 50, pd);

        g2.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 12));
        FontMetrics metrics = g2.getFontMetrics();
        int width = metrics.stringWidth("data");
        int height = metrics.getHeight();
        g2.drawString("data", centre.x - width / 2, centre.y + 40);
    }

    /**
     * Get the training file at the indicated position
     * @param i The index of the training file
     * @return The indicated training file name
     */
    public String getTrainingAt(int i) {
        // return training file at i position
        Vector lista = (Vector) tableVector.elementAt(Layer.layerActivo);
        String s = (String) lista.elementAt(i);
        StringTokenizer partes = new StringTokenizer(s, ",");
        return partes.nextToken();
    }

    /**
     * Get the test file at the indicated position
     * @param i The index of the test file
     * @return The indicated test file name
     */
    public String getTestAt(int i) {
        // return test file at i position
        Vector lista = (Vector) tableVector.elementAt(Layer.layerActivo);
        String s = (String) lista.elementAt(i);
        StringTokenizer partes = new StringTokenizer(s, ",");
        partes.nextToken();
        return partes.nextToken();
    }

    /**
     * Test if the data set node has all the partitions (are available from disk)
     * @return If all the correspondent partitions are present
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Gets the missing partitions vector
     * @return A vector of vector with the missing partitions of each data set
     */
    public Vector getMissingVector() {
        return missing;
    }

    /**
     * Adds a missing partition to the vector at a given position
     * @param k position where the missing partition is inserted
     */
    public void addMissingPartition(String partition, int k) {
        ((Vector) missing.elementAt(k)).add(partition);
    }

    /**
     * Empties the missing partition vector
     */
    public void clearMissingVector() {
        missing.clear();
    }

    public void pushMissingIntoTable(int k) {
        ((Vector) tableVector.get(k)).addAll((Vector) missing.get(k));
        ((Vector) missing.get(k)).clear();
        Collections.sort((Vector) tableVector.get(k));
    }
}