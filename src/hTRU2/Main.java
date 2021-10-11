package hTRU2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        String[] lines = shuffleData("./HTRU.csv");

        Scanner Cin =  new Scanner(System.in);
        int fold=5, kNN;

        System.out.print("Enter the KNN value: ");
        kNN = Cin.nextInt();
        int section = lines.length/fold;

        KNNTraining training = new KNNTraining(lines, section, fold, kNN);

        double[] accuracy = new double[fold];
        double[] precision = new double[fold];
        double[] recall = new double[fold];
        double[] f_measure = new double[fold];


        for (int i=0; i<fold; i++){

            System.out.println("round: " + (i+1));

            training.trained(i);
            training.calculateKNN(i);
            training.calculate(i);
            accuracy[i] = training.getAccuracy();
            precision[i] = training.getPrecision();
            recall[i] = training.getRecall();
            f_measure[i] = training.getF_measure();

            System.out.print("Accuracy: "+accuracy[i]+" ");
            System.out.print("Precision: "+precision[i]+" ");
            System.out.print("Recall: "+recall[i]+" ");
            System.out.println("F_measure: "+f_measure[i]);

        }

        double sumAccuracy=0, sumF_measure=0, sumRecall=0, sumPrecision=0;

        for (int i=0; i<fold; i++){
            sumAccuracy+=accuracy[i];
            sumPrecision+=precision[i];
            sumRecall+=recall[i];
            sumF_measure+=f_measure[i];
        }

        System.out.println("Final average output ---");
        System.out.print("average accuracy :"+ (sumAccuracy)/fold+" ");
        System.out.print("average precision :"+ (sumPrecision)/fold+" ");
        System.out.print("average recall :"+ (sumRecall)/fold+" ");
        System.out.println("average f_measure :"+ (sumF_measure)/fold);

    }



    public static String [] shuffleData(String fileName) throws IOException {

        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        ArrayList<String> arrayListOflines = new ArrayList<>();
        String line = bufferedReader.readLine();
        arrayListOflines.add(line);

        while(line!=null) {
            line = bufferedReader.readLine();
            if (line == null) break;
            arrayListOflines.add(line);
        }
        bufferedReader.close();

        String[] arrayOfLines = arrayListOflines.toArray(new String[arrayListOflines.size()]);

        List<String> list = Arrays.asList(arrayOfLines);
        //System.out.println(list);
        Collections.shuffle(list);

        String[] lines = list.toArray(new String[list.size()]);
        //System.out.println(l);

        return lines;
    }


}




