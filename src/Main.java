// Badania operacyjne i logistyka (zajęcia projektowe)
// Temat: Projekt1_PERT (Program Evaluation and Review Technique)
// Dajana Kopera & Kaja Kwiatkowska (Inżynieria Obliczeniowa, rok 4)

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.*;
import static java.lang.Math.abs;

// klasa zawiera wczytywanie pliku (poprzez wybór użytkownika, ze względu na różne typy zadań), następnie plik
// wczytywany jest odpowiednio do poszczególnych list reprezentujących czasy, następnie wypisuje informacje w konsoli,
// wykonuje operacje ustawiając poprzedników oraz następców, oblicza dla każdego zdarzenia t0, t1 oraz zapas czasu
// następnie wyznacza ścieżkę krytyczną, dla niej oblicza również sumę sigm, otoczenie oraz prawdopodobieństwo
public class Main {
    private static List<Zdarzenie> zdarzenia = new LinkedList<>();                    // lista zdarzeń
    private static List<Integer> sciezka_krytyczna = new ArrayList<>();               // lista zdarzeń, które są na SK

    public static void main(String[] args) {
        System.out.println(" ---- ----    PERT (Program Evaluation and Review Technique    ---- ---- ");
        System.out.println("\nWczytaj plik\nPlik zawiera 5 kolumn: nr_zdarzenia, jego następce oraz czas pomiędzy " +
                "nimi \n  oraz czasy: optymistyczny, najbardziej prawdopodobny oraz pesymistyczny");
        PERT();
    }


    public static void PERT() {
        // uruchomienie okna dialogowego, by wybrać plik zawierający informacje o zadaniu
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Wybierz plik z zadaniem (*.txt)");

        String sciezka_pliku = "C:\\Users\\Dajana\\";
        double returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            sciezka_pliku = (jfc.getSelectedFile().getPath());
        }

        // uruchomienie z wczytanej ścieżki pliku
        try {
            wczytajDane(sciezka_pliku);     // ----> ta metoda również zawiera operacje na danych wczytanych
        } catch (IOException e) {
            e.printStackTrace();
        }

        //wyświetlanie informacji o zdarzeniach
        for (Zdarzenie zdarzenie : zdarzenia) {
            System.out.println(zdarzenie);
        }
    }


    //----------------------------------------------------------------------------ustawianie wartości wczytanych z pliku
    private static void wczytajDane(String sciezka) throws IOException {
        List<Integer> listaZdarzen = new ArrayList<>();                          // 1 kolumna z pliku
        List<Integer> nastepcy = new ArrayList<>();                              // 2 kolumna z pliku
        List<Double> czasy_opt = new LinkedList<>();                             // 3 kolumna
        List<Double> czasy_naj_prawdopodobne = new LinkedList<>();               // 4 kolumna
        List<Double> czasy_pes = new LinkedList<>();                             // 5 kolumna
        List<Double> wartosci_oczekiwane = new LinkedList<>();  // uzupełniona niżej na podstawie wzoru dla rozkładu B


        // przejście po liniach wczytywanego pliku
        BufferedReader reader = new BufferedReader(new FileReader(sciezka));
        int lines = 0;
        while (reader.readLine() != null) { lines++; }
        reader.close();

        // dodanie do list elementów z pliku
        Scanner scanner = new Scanner(new File(sciezka));
        for (int i = 0; i < lines; i++) {
            listaZdarzen.add(scanner.nextInt());
            nastepcy.add(scanner.nextInt());
            czasy_opt.add(scanner.nextDouble());
            czasy_naj_prawdopodobne.add(scanner.nextDouble());
            czasy_pes.add(scanner.nextDouble());
            if (i < lines - 1) scanner.nextLine();
        }

        // obliczenie według rozkładu beta wartości oczekiwanych dla poszczególnych zdarzeń
        for (int i = 0; i < listaZdarzen.size(); i++) {
            // uzupełnianie od razu listy
            wartosci_oczekiwane.add(((czasy_opt.get(i) + 4*czasy_naj_prawdopodobne.get(i) + czasy_pes.get(i))/6));
        }

        // wypisanie czynności oraz ich czasów z pliku na konsole
        System.out.println("\nZdarzenia poprzedzające oraz następujące: ");
        for (int i = 0; i < listaZdarzen.size(); i++) { System.out.print(listaZdarzen.get(i) + "\t"); }
        System.out.println();
        for (int i = 0; i < listaZdarzen.size(); i++) { System.out.print("↓\t"); }
        System.out.println();
        for (int i = 0; i < nastepcy.size(); i++) { System.out.print(nastepcy.get(i) + " \t"); }

        System.out.println("\nWartości czasów czynności: ");
        System.out.println("Czasy otymistyczne & najbardziej prawdopodobne & czasy pesymistyczne: ");
        for (int i = 0; i < czasy_opt.size(); i++) { System.out.print(czasy_opt.get(i) + " \t"); }
        System.out.println();
        for (int i = 0; i < listaZdarzen.size(); i++) { System.out.print("↨\t\t"); }
        System.out.println();
        for (int i = 0; i < czasy_naj_prawdopodobne.size(); i++) { System.out.print(czasy_naj_prawdopodobne.get(i) + " \t"); }
        System.out.println();
        for (int i = 0; i < listaZdarzen.size(); i++) { System.out.print("↨\t\t"); }
        System.out.println();
        for (int i = 0; i < czasy_pes.size(); i++) { System.out.print(czasy_pes.get(i) + " \t"); }

        System.out.println("\nWartości oczekiwane: ");
        for (int i = 0; i < wartosci_oczekiwane.size(); i++) { System.out.print(wartosci_oczekiwane.get(i) + " \t"); }
        System.out.println();

        double max = Collections.max(nastepcy);

        // dla wypisania następców zdarzeń
        for (int i = 0; i < max; i++) {
            Zdarzenie zdarzenie = new Zdarzenie(i);
            // dodanie zdarzeń do listy
            zdarzenia.add(zdarzenie);
        }

        // dodanie następców oraz czasów do zdarzeń
        for (int i = 0; i < listaZdarzen.size(); i++) {
            zdarzenia.get(listaZdarzen.get(i) - 1).nastepcy.add(nastepcy.get(i));
            zdarzenia.get(listaZdarzen.get(i) - 1).czasy_opt.add(czasy_opt.get(i));
            zdarzenia.get(listaZdarzen.get(i) - 1).czasy_naj_prawdopodobne.add(czasy_naj_prawdopodobne.get(i));
            zdarzenia.get(listaZdarzen.get(i) - 1).czasy_pes.add(czasy_pes.get(i));
            zdarzenia.get(listaZdarzen.get(i) - 1).wartosci_oczekiwane.add(wartosci_oczekiwane.get(i));
        }

        // wstawianie poprzedników
        for (int j = 0; j < zdarzenia.size(); j++) { zdarzenia.get(j).poprzednicy = wstawPoprzednikow(j); }
        System.out.println();
        obliczT0();
        obliczT1();
        wyznaczSciezkeSigmeOrazX();
    }


    // ----------------------------------------------------------------------------------ustawienie poprzedników zdarzeń
    private static List<Integer> wstawPoprzednikow(int nr) {
        List<Integer> poprzednicy = new LinkedList<>();
        for (int i = 0; i < zdarzenia.size(); i++) {
            if (zdarzenia.get(i).nastepcy.contains(nr + 1)) {       //contains == zawiera
                poprzednicy.add((i + 1));
            }
        }
        return poprzednicy;
    }


    // ----------------------------------------------------------------------------------------------------obliczanie t0
    private static void obliczT0() {
        for (int i = 0; i < zdarzenia.size(); i++) {
            for (int j = 0; j < zdarzenia.get(i).nastepcy.size(); j++) {
                // ustawienie wartości dla pierwszego zdarzenia = 0
                if (i == 0) {
                    zdarzenia.get(i).t0 = 0;
                    zdarzenia.get(i).t1 = 0;
                    zdarzenia.get(i).zapas_czasowy = 0;
                } else {
                    //sprawdz poprzedników pod względem czasów, wybierz największy
                    double wartoscCzasu;
                    double t0Poprzednika;
                    double wartoscCzasuMax;
                    wartoscCzasuMax = -1;
                    for (int k = 0; k < zdarzenia.get(i).poprzednicy.size(); k++) {
                        int idzDo = zdarzenia.get(i).poprzednicy.get(k);
                        t0Poprzednika = zdarzenia.get(idzDo - 1).t0;
                        wartoscCzasu = zdarzenia.get(idzDo - 1).wartosci_oczekiwane.get(zdarzenia.get(idzDo - 1).
                                nastepcy.indexOf(i + 1));
                        if ((wartoscCzasu + t0Poprzednika) > wartoscCzasuMax) {
                            wartoscCzasuMax = (wartoscCzasu + t0Poprzednika);
                        }
                        zdarzenia.get(i).t0 = wartoscCzasuMax;
                    }
                }
            }
        }

        // operacje dla ostatniego zdarzenia
        double wartoscCzasu;
        double t0Poprzednika;
        double wartoscCzasuMax;
        wartoscCzasuMax = -1;
        for (int k = 0; k < zdarzenia.get(zdarzenia.size() - 1).poprzednicy.size(); k++) {
            int idzDo = zdarzenia.get(zdarzenia.size() - 1).poprzednicy.get(k);
            t0Poprzednika = zdarzenia.get(idzDo - 1).t0;
            wartoscCzasu = zdarzenia.get(idzDo - 1).wartosci_oczekiwane.get(zdarzenia.get(idzDo - 1).nastepcy.indexOf(zdarzenia
                    .size()));
            if ((wartoscCzasu + t0Poprzednika) > wartoscCzasuMax) {
                wartoscCzasuMax = (wartoscCzasu + t0Poprzednika);
            }
            zdarzenia.get(zdarzenia.size() - 1).t0 = wartoscCzasuMax;
        }
    }


    // ----------------------------------------------------------------------------------------------------obliczanie t1
    private static void obliczT1() {
        // przejście od tyłu dla wyznaczenia t1
        for (int i = zdarzenia.size() - 1; i >= 0; i--) {
            if (i == (zdarzenia.size() - 1)) {
                zdarzenia.get(i).t1 = zdarzenia.get(i).t0;
                zdarzenia.get(i).zapas_czasowy = abs(zdarzenia.get(i).t1 = zdarzenia.get(i).t0);
            } else {
                //sprawdz poprzedników pod względem czasów, wybierz najmniejszy
                double wartoscCzasu;
                double t1Nastepcy;
                double wartoscCzasuMin;
                wartoscCzasuMin = 1000000;
                for (int k = 0; k < zdarzenia.get(i).nastepcy.size(); k++) {
                    //t1 = t0 nastepcy -> wartość czasu następcy  ma być jak najmnijesza
                    int idzDo = zdarzenia.get(i).nastepcy.get(k);
                    t1Nastepcy = zdarzenia.get(idzDo - 1).t1;
                    wartoscCzasu = zdarzenia.get(i).wartosci_oczekiwane.get(k);
                    if ((Math.abs(t1Nastepcy - wartoscCzasu)) < wartoscCzasuMin) {
                        wartoscCzasuMin = (t1Nastepcy - wartoscCzasu);
                    }
                    zdarzenia.get(i).t1 = wartoscCzasuMin;
                }
            }
            zdarzenia.get(i).zapas_czasowy = abs(zdarzenia.get(i).t1 - zdarzenia.get(i).t0);
        }
    }


    // obliczenie wartości sigmy dla zdarzeń na ścieżce krytycznej oraz jej zsumowanie do jednej wartości
    public static double wyznaczenieSigmy(int sciezka) {
        double sigmyZsumowane = 0;
        for (int i = 0; i < sciezka; i++) {
            double wartoscDanejSigmy = 0;
            // sprawdzenie dla każdego ze ścieżki czasów sigma (= ((pes - opt)/6)^2)
            for (int j = 0; j < zdarzenia.size(); j++) {
                if (((zdarzenia.get(j).nr_zdarzenia) + 1) == sciezka_krytyczna.get(i)) {
                    for (int k = 0; k < zdarzenia.get(j).nastepcy.size(); k++) {
                        if (zdarzenia.get(j).nastepcy.get(k) == (sciezka_krytyczna.get(i + 1))) {
                            // obliczanie sigmy
                            if (!zdarzenia.get(j).czasy_pes.get(k).equals(zdarzenia.get(j).czasy_opt.get(k))) {
                                double a = (Double.valueOf(zdarzenia.get(j).czasy_pes.get(k)));
                                double b = (Double.valueOf(zdarzenia.get(j).czasy_opt.get(k)));
                                wartoscDanejSigmy = Math.pow(((a - b) / 6), 2);
                                // System.out.println(wartoscDanejSigmy);
                            }
                        }
                    }
                }
            }
            sigmyZsumowane += wartoscDanejSigmy;
        }
        return sigmyZsumowane;
    }

    // -----------------------------------------------------------------------------------wyznaczanie ścieżki krytycznej
    private static void wyznaczSciezkeSigmeOrazX() {
        double czas_sciezki = 0;
        double czas_dyrektywny = 0;
        double X = 0;
        for (int i = 0; i < zdarzenia.size(); i++) {
            if (i == 0) {
                // dodanie pierwszego elementu na ścieżkę krytyczną
                sciezka_krytyczna.add((i + 1));
            } else {
                boolean zawiera = false;
                for (int j = 0; j < zdarzenia.get(i).poprzednicy.size(); j++) {
                    if (sciezka_krytyczna.contains(zdarzenia.get(i).poprzednicy.get(j))) {
                        // jeśli zapas czasowy = 0
                        if (zdarzenia.get(i).zapas_czasowy == 0) {
                            zawiera = true;
                        }
                    }
                }
                if (zawiera) {
                    sciezka_krytyczna.add((i + 1));
                }
            }
        }
        // ---------------------------------------------------------------------------------wypisanie ścieżki krytycznej
        System.out.println("Ścieżka krytyczna: ");
        for (int k = 0; k < sciezka_krytyczna.size(); k++) {
            if (k == 0) System.out.print(sciezka_krytyczna.get(k));
            if (k > 0 && k < sciezka_krytyczna.size()) {
                System.out.print(" → " + sciezka_krytyczna.get(k));
            }
        }
        System.out.println();
        czas_sciezki = zdarzenia.get(zdarzenia.size() - 1).t0;
        System.out.println("Czas ścieżki krytycznej: " + czas_sciezki);                       // czas ścieżki krytycznej

        // --------------------------------------------------------------------------------------------wyznaczenie sigmy
        System.out.println("\nObliczanie ∑σ^2 dla ścieżki krytycznej: ");
        double sigma_kwadrat = wyznaczenieSigmy(sciezka_krytyczna.size() - 1);
        System.out.println("∑σ^2 = " + sigma_kwadrat);

        // --------------------------------------------------------------------------------------------------otoczenie X
        System.out.println("Otoczenie dla czasu: " + czas_sciezki + " →\t <" + (czas_sciezki - sigma_kwadrat) + ", "
                + (czas_sciezki + sigma_kwadrat) + ">\n");

        // ----------------------------------------------------pobieranie czasu dyrektywnego od użytkownika z klawiatury
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj wartość czasu dyrektywnego: ");
        czas_dyrektywny = scanner.nextDouble();
        System.out.println("Czas_dyrektywny = " + czas_dyrektywny + ", czas modelowy = " + czas_sciezki);

        // ----------------------------------------------------------------------------------------------------------- X
        X = (czas_dyrektywny - czas_sciezki)/Math.sqrt(sigma_kwadrat);
        System.out.println("Wartość X, po wstawieniu do wzoru = " + X + "\n");

        if (X > 0) {
            System.out.println("Wartość X > 0, zastosowanie wzoru: P(td <= tr) =  Ф(x)\n");
        } else {
            System.out.println("Wartość X < 0, zastosowanie wzoru: P(td <= tr) =  1 - Ф(-x)\n");
        }

        // -------------------------------------------------------------------------------wczytanie dystrybuanty z pliku
        double dystrybuantaOdczyt;
        try {
            Scanner odczyt = new Scanner(new File("dystrybuanta.txt"));
            // zapis odczytu pliku do utworzonej macierzy
            double[][] dystrybuanta = new double[36][10];
            int licznik = 0;
            while(odczyt.hasNextLine()) {
                String text = odczyt.nextLine();
                String[] pom = text.split(" ");
                // 10, bo dziesięć kolumn
                for(int i = 0; i < 10; i++) { dystrybuanta[licznik][i] = Double.parseDouble(pom[i]); }
                licznik++;
            }

            int wiersz, kolumna;
            double pomoc;
            // rzutowanie dzięki X, by prościej przechodzić pomiędzy wierszami
            pomoc = X * 10;
            wiersz = (int)pomoc;
            pomoc -= wiersz;

            pomoc = pomoc * 10;
            kolumna = (int)pomoc;
            // System.out.println("wiersz " + wiersz + " kolumna " + kolumna);

            // --------------------------------------------------------------------------odczytywanie prawdopodobieństwa
            if(kolumna > 0 && wiersz > 0) {
                dystrybuantaOdczyt = dystrybuanta[wiersz][kolumna];
                System.out.println("Prawdopodobienstwo = " + dystrybuantaOdczyt * 100 + " %\n\n");
            } else {
                if (kolumna < 0)
                    kolumna = kolumna * (-1);
                if (wiersz < 0)
                    wiersz = wiersz * (-1);
                // System.out.println("wiersz " + wiersz + " kolumna " + kolumna);
                dystrybuantaOdczyt = (1 - dystrybuanta[wiersz+1][kolumna+1]);
                System.out.println("Prawdopodobienstwo = " + dystrybuantaOdczyt * 100 + " %\n\n");
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }
}
