package hTRU2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KNNTraining {
    String[] lines;
    int section,fold,kNN;
    int row,column;

    double [][] resultData;
    String [][] resultType;

    double[][] data;
    String[] type;
    double[][] testData;
    String[] testType;

    double tp, tn, fp, fn;
    double accuracy, f_measure, precision, recall;

    public KNNTraining(String[] lines, int section, int fold, int kNN){

        this.lines = lines;
        this.section = section;
        this.fold = fold;
        this.kNN = kNN;

    }

    public void trained(int round) throws IOException {

        //System.out.println("-------Round number : "+(round+1));
        row=(fold-1)*section;
        String temp = lines[0];
        String[] word = temp.split(",");
        column = word.length;

        //System.out.println("length: " + column);

        String[][] chosenLine = new String[fold*section][column];
        data = new double[row][column-1];
        type = new String[row];
        testData = new double[section][column-1];
        testType = new String[section];

        int index=0,index2=0;

        for(int i=0; i<lines.length; i++){

            temp =lines[i];
            chosenLine[i]=temp.split(",");

            if((round*section) <= i && ((round*section)+section-1) >= i){
                //System.out.print("Test i:"+i);
                for(int j=0; j<column; j++) {
                    //System.out.print(chosenLine[i][j]+" ");
                    if (j == (column - 1)) {
                        testType[index2] = chosenLine[i][j];
                    } else {
                        String str = chosenLine[i][j];
                        testData[index2][j] = Double.parseDouble(str);

                    }
                }

                index2++;
            }

            else{
                //System.out.print("Data i:"+i);

                for(int j=0; j<column; j++) {
                    //System.out.print(chosenLine[i][j]+", ");
                    if(j==(column-1)){
                        type[index] = chosenLine[i][j];
                        //System.out.print("  " +type[index] +"   ");

                    }
                    else{
                        //System.out.println("dhuke!!!!!!");
                        String str = chosenLine[i][j];
                        data[index][j] = Double.parseDouble(str);
                        //System.out.print("  " +data[index][j] +"   ");

                    }
                }

                index++;
            }
            //System.out.println();



        }

    }

    public void sortValue(double value,String type, int section, int rowNum){

        if(rowNum < kNN){
            resultData[section][rowNum] = value;
            resultType[section][rowNum] = type;
        }else if(resultData[section][kNN-1] < value){
            resultData[section][kNN-1] = value;
            resultType[section][kNN-1] = type;
        }

        int size=0;

        if(rowNum < kNN){
            size = rowNum;
        }else{
            size = kNN-1;
        }

        for(int i=size; i>0; i--){
            if(resultData[section][i] > resultData[section][i-1]){
                double d = resultData[section][i];
                String t = resultType[section][i];
                resultData[section][i] = resultData[section][i-1];
                resultType[section][i] = resultType[section][i-1];
                resultData[section][i-1] = d;
                resultType[section][i-1] = t;
            }
            else break;
        }
    }

    public void calculateKNN(int round) throws IOException {

        //System.out.println("eikhane");
        resultData = new double[section][kNN];
        resultType = new String[section][kNN];

        for(int i=0; i<section; i++){
            for(int j=0; j<row; j++){
                double sum=0;
                for(int k=0; k<column-1; k++){

                    double value = (testData[i][k]-data[j][k]);
                    sum+=Math.pow(value,2);
                }
                sum=Math.sqrt(sum);

                //resultData[i][j] = 1/sum;
                //resultType[i][j] = type[j];
                sortValue(1/sum, type[j], i, j);

            }
        }

        FileWriter writer = new FileWriter("train_"+round+".txt");
        BufferedWriter bw = new BufferedWriter(writer);
        String str=null;

        for(int i=0; i<section; i++){
            for(int j=0; j<kNN; j++){
                bw.write(resultData[i][j]+", "+ resultType[i][j]+"\n");
            }
        }
        bw.close();


    }

    public void calculate(int round) throws IOException {

        tp=0; tn=0; fp=0; fn=0;

        accuracy=0; f_measure=0; precision=0; recall=0;

        double [][]resultD = new double[section][kNN];
        String [][] resultT = new String[section][kNN];

        FileReader fileReader = new FileReader("train_"+round+".txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int count=0;
        String line = bufferedReader.readLine();
        for(int i=0; i<section && line!=null; i++){
            for(int j=0; j<kNN; j++){
                String[] subString = line.split(",");
                resultD[i][j] = Double.parseDouble(subString[0]);

                resultT[i][j] = subString[1];
                line = bufferedReader.readLine();
                if (line == null) break;
            }
        }


        for(int i=0; i<section; i++){

            double c1=0,c2=0,total=0;
            for(int j=0; j<kNN; j++)
            {
                total+=resultD[i][j];

                if(resultT[i][j].equals(" 0")){
                    c1+=resultD[i][j];
                } else if(resultT[i][j].equals(" 1")){
                    c2+=resultD[i][j];
                }

            }

            c1/=total;
            c2/=total;

            if(c1>c2 ){
                if(testType[i].equals("0")) tp++;
                else fp++;
            }

            else if(c2>c1){
                if(testType[i].equals("1")) tn++;
                else fn++;
            }

        }

        accuracy = (tp+tn)/section;
        recall = tp/(tp+fn);
        precision = tp/(tp+fp);
        f_measure = 2*(precision*recall)/(precision+recall);
    }


    public double getAccuracy() {
        return accuracy;
    }

    public double getRecall() {
        return recall;
    }

    public double getPrecision() {
        return precision;
    }

    public double getF_measure() {
        return f_measure;
    }



}

