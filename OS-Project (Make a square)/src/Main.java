import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose 4 shapes : ");
        int[] arr = new int[4];
        for(int i=0;i<4;i++){
            arr[i] = sc.nextInt();
        }
        int n;
        if(arr[0] <= 1) n=4;
        else n=2;
        outer.Multithreading[] thread = new outer.Multithreading[n];
        Thread[] proc = new Thread[n];
        for(int i=0;i<n;i++){ // multithreading
            Shapes shapes = new Shapes();
            shapes.pre();
            thread[i] = new outer.Multithreading(0,i,shapes.grid,arr,shapes.arr,i);
            proc[i] = new Thread(thread[i]);
            proc[i].start();
        }
        for(int i=0;i<n;i++){
            proc[i].join();
        }
        if(!outer.check) System.out.println("NO RESULT");
    }
}

