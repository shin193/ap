package Exercise.Ex1;

import java.util.Scanner;

public class E6_2 {
    public static void main(String[] args) {
        int[] num=new int[50];
        num [0]=0;
        int i=-1;
        do {
            i++;
            System.out.println("enter a number(press -1 to exit): ");
            Scanner sc = new Scanner(System.in);
            num [i] = sc.nextInt();
        }
        while (num[i]!=-1);
        //following lop is for biggest number
        int key=num[0];
        for (int j = 0; j < i; j++) {
            if(num[j+1]>num[j]){
                key=num[j+1];
            }

        }
        System.out.println("biggest number is : "+key);
        //following lop is for smallest number
        int key2=num[0];
        for (int j = 0; j < i-1; j++) {
            if(num[j+1]<num[j]){
                key2=num[j+1];
            }
        }
        System.out.println("smallest number is : "+key2);
        //following lop is for even or odd number
        int z=0,f=0;
        for (int j = 0; j < i; j++) {
            if(num[j]%2==0){
                z++;
            }
            else{
                f++;
            }
        }
        System.out.println("even numbers are : "+z);
        System.out.println("odd numbers are : "+f);


    }
}