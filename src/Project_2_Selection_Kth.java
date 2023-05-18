import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Project_2_Selection_Kth {

    public static void main(String[] args){

    }

    static int[] array;
    static int[] utemparray;


    public static long getTrialAverage(long[] timeTrials){
        long max = timeTrials[0];
        long min = timeTrials[0];
        long sum = 0;
        for (int i = 0; i < timeTrials.length; i++){
            sum += timeTrials[i];
            if (timeTrials[i] > max){
                max = timeTrials[i];
            }
            if (timeTrials[i] < min){
                min = timeTrials[i];
            }
        }
        sum -= max;
        sum -= min;
        return sum / (timeTrials.length - 2);
    }

    static int Algo1(int kth){
        MergeSort(0, array.length - 1);
        return array[kth];
    }

    static void MergeSort(int low, int high){
        if (low < high){
            int mid = (int) Math.floor((low + high) / 2);
            MergeSort(low, mid);
            MergeSort(mid + 1, high);
            Merge(low, mid, high);
        }
    }

    static void Merge(int low, int mid, int high){
        int i = low;
        int j = mid + 1;
        int k = low;

        while (i <= mid && j <= high){
            if (array[i] < array[j]){
                utemparray[k] = array[i];
                i++;
            } else {
                utemparray[k] = array[j];
                j++;
            }
        }

        if (i > mid){
            for (int index = j; index < high; index++){
                utemparray[index] = array[index];
            }
        } else {
            for (int index = i; index < mid; index++){
                utemparray[index] = array[index];
            }
        }

        for (int p = low; p < high; p++){
            array[p] = utemparray[p];
        }
    }

    static int Algo2(int n, int k){
        int m = 1;
        int j = n;
        while (true){
            int pivotposition = Partition(array, m, j);
            if (k == pivotposition){
                return array[k];
            } else if (k < pivotposition){
                j = pivotposition - 1;
            } else {
                m = pivotposition + 1;
                k = k - pivotposition;
            }
        }
    }

    static int Partition(int[] partition_array, int low, int high){
        int v = partition_array[low];
        int j = low;
        for (int i = low + 1; low <= high; low++){
            if (partition_array[i] < v){
                j++;
                int holder = partition_array[i];
                partition_array[i] = partition_array[j];
                partition_array[j] = holder;
            }
        }
        int pivotposition = j;
        int holder = partition_array[low];
        partition_array[low] = partition_array[pivotposition];
        partition_array[pivotposition] = holder;
        return pivotposition;
    }

    static int r = 5;

    static int Select2(int[] A, int n, int k) {
        if (n < r) {
            Arrays.sort(A);
            return A[k];
        }

        int num_subsets = (int) Math.floor(n / r);
        int[] medians = new int[num_subsets];
        int index = 0;
        for (int i = 0; i < n; i += r) {
            int endindex = i + r;
            if (endindex >= n) {
                endindex = n - 1;
            }
            int[] temporaryArray = Arrays.copyOfRange(A, i, endindex);
            int median = temporaryArray[(temporaryArray.length - 1) / 2];
            medians[index] = median;
            index++;
        }

        int v = Select2(medians, num_subsets, (int) Math.ceil(num_subsets / 2));

        int pivotposition = Partition(A, 0, n);

        if (k == pivotposition) {
            return v;
        } else if (k < pivotposition){
            int[] S = Arrays.copyOfRange(A, 1, pivotposition - 1);
            return Select2(S, pivotposition - 1, k);
        } else {
            int[] R = Arrays.copyOfRange(A, pivotposition + 1, n);
            return Select2(R, n-pivotposition, k-pivotposition);
        }
    }
}

class CustomFileWriter {
    File file;
    FileWriter fileWriter;

    /**
     * Constructor that allows initialization of a filewriter given a name.
     * @param name
     * @throws IOException
     */
    public CustomFileWriter(String name) throws IOException {
        try {
            file = new File(name + ".txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            fileWriter = new FileWriter(name + ".txt", true);
        } catch (IOException e){
            System.out.println("IOException" + e);
        }
    }

    /**
     * Simple write method to append to text file. close() repeated each time to save.
     * @param message
     */
    public void write(String message) {
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(message);
            fileWriter.close();
        } catch (IOException e){
            System.out.println("IOException" + e);
        }
    }
}
