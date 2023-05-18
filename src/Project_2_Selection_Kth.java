import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Project_2_Selection_Kth {

    public static void main(String[] args) throws IOException {

        // Initial Testing Case
        /*
        int[] testing_array = new int[]{10, 9, 8, 3, 6, 5, 4, 7, 2, 1};
        System.out.println("Testing Array: " + Arrays.toString(testing_array));
        for (int i = 0; i < 10; i++){
            ////array = testing_array;
            System.out.println("i: " + i + " expected answer: " + (i + 1));
            //System.out.println("Algoirthm 1: " + Algo1(testing_array, i));
            //System.out.println("Algoirthm 2: " + Algo1(testing_array, i));
            System.out.println("Algoirthm 3: " + Algo1(testing_array, i));
        }*/

        CustomFileWriter2 sizeLog = new CustomFileWriter2("sizeLog");
        CustomFileWriter2 algo1timeLog = new CustomFileWriter2("algo1timeLog");
        CustomFileWriter2 algo2timeLog = new CustomFileWriter2("algo2timeLog");
        CustomFileWriter2 algo3timeLog = new CustomFileWriter2("algo3timeLog");
        while (true){
            doTestCases((10), 10);
        }

    }

    public static long[] doTestCases(int n, int numTests){
        try {
            long[] algo1_time = new long[numTests];
            long[] algo2_time = new long[numTests];
            long[] algo3_time = new long[numTests];

            for (int i = 0; i < numTests; i++){
                int[] testArray = new int[n];
                for (int j = 0; j < n; j++){
                    testArray[j] = (int)(Math.random() * 2147483647);
                }
                int kth = (int)(Math.random() * n);
                //System.out.println("Test array: " + Arrays.toString(testArray));
                int ans;

                long startTime = System.currentTimeMillis();

                ans = Algo1(testArray, kth);
                algo1_time[i] = System.currentTimeMillis() - startTime;
                System.out.println("Algorithm 1 took " + algo1_time[i] + " for i = " + i);
                startTime = System.currentTimeMillis();

                ans = Algo2(testArray, n, kth);
                algo1_time[i] = System.currentTimeMillis() - startTime;
                System.out.println("Algorithm 1 took " + algo1_time[i] + " for i = " + i);
                startTime = System.currentTimeMillis();

                ans = Select2(testArray, n, kth);
                algo1_time[i] = System.currentTimeMillis() - startTime;
                System.out.println("Algorithm 1 took " + algo1_time[i] + " for i = " + i);
            }
            return new long[]{getTrialAverage(algo1_time), getTrialAverage(algo2_time), getTrialAverage(algo3_time)};

        } catch (Exception e){
            System.out.println("Test Failed " + e);
        }
        return null;
    }

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

    static int Algo1(int[] A, int kth){
        MergeSort(A, 0, A.length - 1);
        return A[kth];
    }

    static void MergeSort(int[] A, int low, int high){
        if (low < high){
            int mid = (int) Math.floor((low + high) / 2);
            MergeSort(A, low, mid);
            MergeSort(A, mid + 1, high);
            Merge(A, low, mid, high);
        }
    }

    static void Merge(int[] A, int low, int mid, int high){
        int i = low;
        int j = mid + 1;
        int k = low;
        int[] U = new int[high + 1];
        while (i <= mid && j <= high){
            if (A[i] < A[j]){
                U[k] = A[i];
                i++;
            } else {
                U[k] = A[j];
                j++;
            }
            k++;
        }

        if (i > mid){
            while (j <= high){
                U[k] = A[j];
                k++;
                j++;
            }
        } else {
            while (i <= mid){
                U[k] = A[i];
                k++;
                i++;
            }
        }

        for (int p = low; p <= high; p++){
            A[p] = U[p];
        }
        //System.out.println("Array: " + Arrays.toString(array));
    }

    static int Algo2(int[] A, int n, int k){
        int m = 0;
        int j = n - 1;
        //k = k-1;
        while (true){
            //System.out.println(k);
            int pivotposition = Partition(A, m, j);
            if (k == pivotposition){
                return A[k];
            } else if (k < pivotposition){
                j = pivotposition - 1;
            } else {
                m = pivotposition + 1;
                //k = k - pivotposition;
            }
        }
    }

    static int Partition(int[] partition_array, int low, int high){
        int v = partition_array[low];
        int j = low;
        for (int i = low + 1; i <= high; i++){
            if (partition_array[i] < v) {
                j++;
                //System.out.println("Switched: " + partition_array[i] + " and " + partition_array[j]);
                int holder = partition_array[i];
                partition_array[i] = partition_array[j];
                partition_array[j] = holder;
            }
        }
        int pivotposition = j;
        //System.out.println("pivot position: " + j);
        int holder = partition_array[low];
        partition_array[low] = partition_array[pivotposition];
        partition_array[pivotposition] = holder;
        //System.out.println("holder: " + holder);
        //System.out.println("array :" + Arrays.toString(partition_array));
        return pivotposition;
    }

    static int PartitionSetPivot(int[] partition_array, int low, int high, int pivot){
        //int holder = partition_array[low];
        //partition_array[low] = pivot;
        //partition_array[pivot_index] = holder;

        int v = pivot;
        int j = low;
        int pivot_index = 0;
        //System.out.println("v: " + v + " part: " + Arrays.toString(partition_array));
        for (int i = low; i < high; i++){
            if (partition_array[i] < v) {
                //System.out.println("Switched: " + partition_array[i] + " and " + partition_array[j]);
                int holder = partition_array[i];
                partition_array[i] = partition_array[j];
                partition_array[j] = holder;
                j++;
            }
            if (partition_array[i] == pivot){
                pivot_index = i - 1;
            }
        }
        int pivotposition = j;
        //int holder = partition_array[pivotposition];
        //partition_array[pivotposition] = pivot;
        //partition_array[pivot_index] = holder;
        //System.out.println("partition: " + Arrays.toString(partition_array));
        return pivotposition;
    }

    static int r = 3;

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
            if (endindex > n) {
                break;
                //endindex = n - 1;
            }
            int[] temporaryArray = Arrays.copyOfRange(A, i, endindex);
            Arrays.sort(temporaryArray);
            int median = temporaryArray[(temporaryArray.length - 1) / 2];
            medians[index] = median;
            index++;
        }

        int v = Select2(medians, num_subsets, (int) Math.ceil(num_subsets / 2));


        int pivotposition = PartitionSetPivot(A, 0, n, v);
        /*System.out.println();
        System.out.println("########################");
        System.out.println("n: " + n + " k: " + k + " v: " + v + " pivot: " + pivotposition);
        System.out.println("A: " + Arrays.toString(A));
        System.out.println("########################");*/
        if (k == pivotposition) {
            return v;
        } else if (k < pivotposition){
            int[] S = Arrays.copyOfRange(A, 0, pivotposition);
            //System.out.println("S: " + Arrays.toString(S));
            return Select2(S, pivotposition, k);
        } else {
            int[] R = Arrays.copyOfRange(A, pivotposition, n);
            //System.out.println("R: " + Arrays.toString(R));
            return Select2(R, n-pivotposition, k-pivotposition);
        }
    }
}

class CustomFileWriter2 {
    File file;
    FileWriter fileWriter;

    /**
     * Constructor that allows initialization of a filewriter given a name.
     * @param name
     * @throws IOException
     */
    public CustomFileWriter2(String name) throws IOException {
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
