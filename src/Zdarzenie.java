// klasa zawierająca informacje na temat zdarzenia, będzie wykorzystywana do określenia danych szczegółowych zdarzenia,
// t0, t1 oraz zapasu czasowego, następców oraz poprzedników zdarzeń

import java.util.LinkedList;
import java.util.List;

public class Zdarzenie {
    // zmienne dla zdarzenia w "kółku"
    public int nr_zdarzenia;        // góra
    public double t0;                  // lewo
    public double t1;                  // prawo
    public double zapas_czasowy;       // dół
    public double tc, tm, tp, war_oczekiwana;

    // stworzenie list, żeby móc wczytać z pliku dane, a później się do nich odnosić
    public List<Integer> nastepcy = new LinkedList<>();                         // 1 kolumna
    public List<Integer> poprzednicy = new LinkedList<>();                      // 2 kolumna
    public List<Double> czasy_opt = new LinkedList<>();                        // 3 kolumna
    public List<Double> czasy_naj_prawdopodobne = new LinkedList<>();          // 4 kolumna
    public List<Double> czasy_pes = new LinkedList<>();                        // 5 kolumna
    public List<Double> wartosci_oczekiwane = new LinkedList<>();


    // gettery & settery
    public Zdarzenie(int nr_zdarzenia) {
        this.nr_zdarzenia = nr_zdarzenia;
        this.t0 = 0;
        this.t1 = 0;
        this.zapas_czasowy = 0;
    }
    public double getNr_zdarzenia() {
        return nr_zdarzenia;
    }
    public void setNr_zdarzenia(int nr_zdarzenia) {
        this.nr_zdarzenia = nr_zdarzenia;
    }
    public double getT0() {
        return t0;
    }
    public void setT0(int t0) {
        this.t0 = t0;
    }
    public double getT1() {
        return t1;
    }
    public void setT1(int t1) {
        this.t1 = t1;
    }
    public double getZapas_czasowy() {
        return zapas_czasowy;
    }
    public void setZapas_czasowy(int zapas_czasowy) {
        this.zapas_czasowy = zapas_czasowy;
    }
    public double getTc() { return tc; }
    public void setTc(int tc) { this.tc = tc; }
    public double getTm() { return tm; }
    public void setTm(int tm) { this.tm = tm; }
    public double getTp() { return tp; }
    public void setTp(int tp) { this.tp = tp; }
    public double getWar_oczekiwana() { return war_oczekiwana; }
    public void setWar_oczekiwana(int tc) { this.war_oczekiwana = war_oczekiwana; }


    public List getNastepcy() {
        return nastepcy;
    }
    public void setNastepcy(List nastepcy) {
        this.nastepcy = nastepcy;
    }
    public List getPoprzednicy() {
        return poprzednicy;
    }
    public void setPoprzednicy(List poprzednicy) {
        this.poprzednicy = poprzednicy;
    }
    public List getCzasy_opt() { return czasy_opt; }
    public void setCzasy_opt(List czasy_opt) { this.czasy_opt = czasy_opt; }
    public List getCzasy_naj_prawdopodobne() { return czasy_naj_prawdopodobne; }
    public void setCzasy_naj_prawdopodobne(List czasy_naj_prawdopodobne) {
        this.czasy_naj_prawdopodobne = czasy_naj_prawdopodobne; }
    public List getCzasy_pes() { return czasy_pes; }
    public void setCzasy_pes(List czasy_pes) { this.czasy_pes = czasy_pes; }


    // wyświetlenie co zawierają poszczególne zdarzenia
    @Override
    public String toString() {
        return "Zdarzenie " + (nr_zdarzenia + 1) + "   →  t0 = " + t0 + ", t1 = " + t1 + ", zapas = " + zapas_czasowy
                // w razie gdyby trzeba było wypisać następców/poprzedników ile ma każde ze zdarzeń to odkomentować
                + ",\t\t   nastepcy = " + nastepcy + ", poprzednicy = " + poprzednicy;
    }
}