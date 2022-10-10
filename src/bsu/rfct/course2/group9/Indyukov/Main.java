package bsu.rfct.course2.group9.Indyukov;

import javax.sound.midi.Soundbank;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        boolean cals = Arrays.stream(args).anyMatch("-calories"::equals);
        try {

            Food[] food = new Food[10];

            food[0] = new BubbleGum(BubbleGum.Mint);
            food[1] = new BubbleGum(BubbleGum.Cherry);
            food[2] = new BubbleGum(BubbleGum.Watermelon);
            food[3] = new Tea(Tea.Black);
            food[4] = new Tea(Tea.Green);
            food[5] = new Tea(Tea.Blue);
            food[6] = new Cake(Cake.Caramel);
            food[7] = new Cake(Cake.Chocolate);
            food[8] = new Cake(Cake.Creamy);
            food[9] = new BubbleGum(BubbleGum.Mint);

            int sumCalories = 0;
            for (int i = 0; i < 10; i++) {
                int curCalories = food[i].calculateCalories();
                sumCalories += curCalories;
                System.out.println(curCalories);
            }
            if (cals){
                System.out.printf("Sum of calories: " + sumCalories);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

}
