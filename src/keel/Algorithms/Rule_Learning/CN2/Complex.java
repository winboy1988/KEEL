package keel.Algorithms.Rule_Learning.CN2;

import java.util.*;

/**
 * <p>Title: Complex</p>
 * <p>Description: Structure to represent a complex of one rule</p>
 * @author Written by Jos� Ram�n Cano de Amo (University of Granada) 28/07/2004
 * @author Modified by Alberto Fern�ndez (University of Granada) 15/06/2006
 * @version 1.1
 * @since JDK1.5
 */
public class Complex implements Comparable {

    /**
     *  This class stores conjunctions of selectors
     */

    private LinkedList compl;
    private int clas;
    private int nclass;
    private int distrib[]; //it contains the number of examples per class that satisfy the antecedent
    private double heuristic;
    private String[] nombreAtributos;

    /**
     * Builder. Initialize the container variable
     */
    public Complex() {
        compl = new LinkedList();
    }

    /**
     * Builder
     * @param nClas int Number of classes
     */
    public Complex(int nClas) {
        compl = new LinkedList();
        nclass = nClas;
        distrib = new int[nClas];
        for (int i = 0; i < nclass; i++) {
            distrib[i] = 0;
        }
    }

    /**
     * Complete builder. It add the number of classes and a selector
     * @param sel Selector
     * @param nClas Number of classes
     */
    public Complex(Selector sel, int nClas) {
        int i;
        compl = new LinkedList();
        compl.add(sel);
        nclass = nClas;
        distrib = new int[nClas];
        for (i = 0; i < nclass; i++) {
            distrib[i] = 0;
        }
    }

    /**
     * Builder. It creates a selector from an attribute,operator value and builds the complex
     * @param atr Id of the input attribute
     * @param op operator code (higher or equal, distinct...)
     * @param val Attribute value
     */
    public Complex(int atr, int op, double val) {
        int i;
        Selector s;
        compl = new LinkedList();
        s = new Selector(atr, op, val);
        compl.add(s);
        distrib = new int[nclass];
        for (i = 0; i < nclass; i++) {
            distrib[i] = 0;
        }
    }

    /**
     * Comparison function between two objects of the class complex
     * @param o "complex" object to compare
     * @return 0 if the heuristics are equal. 1 if the current complex has a higher heuristic .
     * -1 if the complex to compare has a higher heuristic
     */
    public int compareTo(Object o) {
        Complex c2 = (Complex) o;
        int sal = 0;

        if ((heuristic == c2.getheuristic()) && (c2.size() == compl.size())) {
            sal = 0;
        } else if ((heuristic == c2.getheuristic()) && (compl.size() < c2.size())) {
            sal = -1;
        } else if ((heuristic == c2.getheuristic()) && (compl.size() > c2.size())) {
            sal = 1;
        } else if (heuristic > c2.getheuristic()) {
            sal = -1;
        } else if (heuristic < c2.getheuristic()) {
            sal = 1;
        }

        return (sal);
    }

    /**
     * It checks if a complex is equal to another. They must have the same attribute, operator and value.
     * @param c Complex to compare
     * @return True if they are the same. False in other case
     */
    public boolean same(Complex c) {
        boolean iguales = false;
        if (c.size() == compl.size()) {
            iguales = true;
            for (int i = 0; (i < c.size()) && (iguales); i++) {
                iguales = (this.getSelector(i).compareTo(c.getSelector(i)) == 0);
            }
        }
        return iguales;
    }

    /**
     * It checks if a complex is almost the same to another
     * @param c Complex to compare
     * @return True if they are the same but the last selector. False in other case
     */
    public boolean almostSame(Complex c) {
        boolean iguales = true;
        if (c.size() == compl.size()) {
            iguales = true;
            for (int i = 0; (i < c.size() - 1) && (iguales); i++) {
                iguales = (this.getSelector(i).compareTo(c.getSelector(i)) == 0);
            }
        }
        return iguales;
    }

    /**
     * It checks if two complexes have the same class distribution
     * @param c Complex to compare
     * @return boolean True if they have the same class distribution for all classes. False in other case
     */
    public boolean sameDistribution(Complex c) {
        boolean igual = true;
        for (int i = 0; (i < nclass) && (igual); i++) {
            igual = this.getClassDistribution(i) == c.getClassDistribution(i);
        }
        return igual;
    }

    /**
     * Increments the class distribution
     * @param i Class id
     */
    public void addClassDistribution(int i) {
        distrib[i]++;
    }

    /**
     * Id adds the selector if it is not in the list
     * @param sel The selector to add [attr op value]
     */
    public void addSelector(Selector sel) {
        compl.add(sel);
    }

    /**
     * It returns one of the selectors associated to the complex
     * @param i Selector index
     * @return The i-th selector of the complex
     */
    public Selector getSelector(int i) {
        //Selector s = (Selector) compl.get(i);
        //Selector dos = new Selector(s.getAtributo(),s.getOperador(),s.getValores());
        return (Selector) compl.get(i);
    }

    /**
     * It returns the number of selectors of the complex
     * @return Complex size
     */
    public int size() {
        return (compl.size());
    }

    /**
     * It prints the complex content
     */
    public void print() {
        for (int x = 0; x < compl.size(); x++) {
            Selector s = (Selector) compl.get(x);
            System.out.print("(" + nombreAtributos[s.getAttribute()] + " ");
            switch (s.getOperator()) {
            case 0:
                System.out.print("=");
                break;
            case 1:
                System.out.print("<>");
                break;
            case 2:
                System.out.print("<=");
                break;
            default:
                System.out.print(">");
            }
            double[] valores = s.getValues();
            if (valores.length > 1) {
                System.out.print(" " + valores[0]);
                for (int i = 1; i < valores.length - 1; i++) {
                    System.out.print(" ^ " + valores[i]);
                }
                System.out.print(" ^ " + valores[valores.length - 1] + ")");
            } else {
                System.out.print(" " + valores[0] + ")");
            }
            if (x < compl.size() - 1) {
                System.out.print(" AND ");
            }
        }
    }

    /**
     * It prints on a string the content of the complex
     * @return String a string with the content of the complex
     */
    public String printString() {
        String cad = "";
        for (int x = 0; x < compl.size(); x++) {
            Selector s = (Selector) compl.get(x);
            cad += nombreAtributos[s.getAttribute()] + " ";
            switch (s.getOperator()) {
            case 0:
                cad += "=";
                break;
            case 1:
                cad += "<>";
                break;
            case 2:
                cad += "<=";
                break;
            case 3:
                cad += ">";
                break;
            }
            double[] valores = s.getValues();
            if (valores.length > 1) {
                cad += " " + valores[0];
                for (int i = 1; i < valores.length - 1; i++) {
                    cad += " ^ " + valores[i];
                }
                cad += " ^ " + valores[valores.length - 1] + "";
            } else {
                cad += " " + valores[0] + "";
            }
            if (x < compl.size() - 1) {
                cad += " AND ";
            }
        }
        return cad;
    }

    /**
     * It returns the class distribution
     * @return the class distribution
     */
    public int[] getDistribution() {
        int ret[] = new int[nclass];
        int i;

        for (i = 0; i < nclass; i++) {
            ret[i] = distrib[i];

        }
        return ret;
    }

    /**
     * It returns the class distribution of a specified class
     * @param i clas id
     * @return the class distribution of a specified class
     */
    public int getClassDistribution(int i) {
        return distrib[i];
    }

    /**
     * It resets the distribution value for the complex
     */
    public void deleteDistribution() {
        for (int i = 0; i < nclass; i++) {
            distrib[i] = 0;
        }
    }

    /**
     * It prints the class distribution
     */
    public void printDistribution() {
        int i;

        System.out.print("   [");
        for (i = 0; i < nclass; i++) {
            System.out.print(" " + distrib[i]);
        }
        System.out.print("]");
    }

    /**
     * It prints the class distribution on a string
     * @return a string with the class distribution
     */
    public String printDistributionString() {
        int i;
        String cad;

        cad = "   [";
        for (i = 0; i < nclass; i++) {
            cad += " " + distrib[i];
        }
        cad += "]";

        return cad;
    }

    /**
     * It return the class id
     * @return the class id
     */
    public int getClas() {
        return clas;
    }

    /**
     * It assigns a class to the complex
     * @param i the class id
     */
    public void setClass(int i) {
        clas = i;
    }

    /**
     * It copies the complex
     * @return A new cloned complex
     */
    public Complex copyRule() {
        int i;

        Complex c = new Complex(nclass);
        for (i = 0; i < compl.size(); i++) {
            Selector aux = (Selector) compl.get(i);
            Selector s = new Selector(aux.getAttribute(), aux.getOperator(),
                                      aux.getValue());
            c.addSelector(s);
        }
        c.distrib = this.getDistribution();
        c.setClass(clas);

        return c;
    }

    /**
     * It computes the Laplacian value for the complex
     */
    public void computeLaplacian() {
        double nc;
        double ntot;
        int i;

        nc = distrib[clas];

        for (i = 0, ntot = 0; i < nclass; i++) {
            ntot += distrib[i];

        }
        //heuristica = (ntot - nc + nClases - 1) / (ntot + nClases);
        heuristic = (nc + 1) / (ntot + nclass);

    }


    /**
     * Devuelve el valor heur�stico del Complex
     * @return double idem
     */
    public double getHeuristic() {
        return heuristic;
    }

    /**
     * Asigna un valor heuristico (Wracc) al Complex
     * @param heu double el valor heuristico
     */
    public void setHeuristic(double heu) {
        heuristic = heu;
    }

    /**
     * It checks if the complex covers a given example
     * @param m The example
     * @return boolean True if it covers the example. False in other case
     */
    public boolean covered(Instance m) {
        boolean cubierto = true;
        double[] ejemplo = m.getMuest();
        for (int i = 0; i < this.size() && cubierto; i++) {
            Selector s = this.getSelector(i);
            switch (s.getOperator()) {
            case 0: // equal operator
                double[] valor = s.getValues();
                cubierto = false;
                for (int j = 0; (j < valor.length) && (!cubierto); j++) {
                    cubierto = (ejemplo[s.getAttribute()] == valor[j]);
                }
                break;
            case 1: //distinct operator
                cubierto = ejemplo[s.getAttribute()] != s.getValue();
                break;
            case 2: //lower or equal
                cubierto = ejemplo[s.getAttribute()] <= s.getValue();
                break;
            case 3: //higher
                cubierto = ejemplo[s.getAttribute()] > s.getValue();
                break;
            }
        }
        return cubierto;
    }

    /**
     * It assigns a class distribution
     * @param dis the given class distribution
     */
    public void setDistribution(int dis[]) {
        int i;
        for (i = 0; i < nclass; i++) {
            distrib[i] = dis[i];
        }
    }

    /**
     * It returns the number of classes
     * @return the number of classes
     */
    public int getNclasses() {
        return nclass;
    }

    /**
     * It returns the heuristic of the complex (LEF function)
     * @return the heuristic of the example
     */
    public double getheuristic() {
        return heuristic;
    }

    /**
     * It assigns the heuristic of the complex computed as:<br/>
     * Covered positives - covered negatives / number of selectors
     * @param heuristic The heuristic
     */
    public void setheuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * It returns the number of selectors of the complex
     * @return The size or complexity of the complex
     */
    public int getSize() {
        return this.compl.size();
    }

    /**
     * It performs a local copy of the name of the input variables
     * @param atributos String[] an Array that stores the name of each variable
     */
    public void addAttributeNames(String[] atributos) {
        nombreAtributos = new String[atributos.length - 1];
        for (int i = 0; i < atributos.length - 1; i++) {
            nombreAtributos[i] = atributos[i];
        }
    }
}
