package bsu.rfct.course2.group9.Indyukov;

import java.util.Objects;

public class BubbleGum extends Food {

    private String flavour = "";

    private static int mintGumsAmount = 0;
    private static int cherryGumsAmount = 0;
    private static int watermelonGumsAmount = 0;
    static final String Mint = "Mint";
    static final String Cherry = "Cherry";
    static final String Watermelon = "Watermelon";

    public BubbleGum(String flavour) throws InvalidFlavourException {
        this.flavour = flavour;
        this.name = "Bubblegum";

        switch (flavour) {
            case (Mint):
                this.calories = 100;
                mintGumsAmount++;
                return;
            case (Cherry):
                this.calories = 200;
                cherryGumsAmount++;
                return;
            case (Watermelon):
                this.calories = 300;
                watermelonGumsAmount++;
                return;
            default:
                throw new InvalidFlavourException("No such bubblegum flavour");
        }
    }

    public int Amount(){
        return switch (flavour) {
            case (Mint) -> mintGumsAmount;
            case (Cherry) -> cherryGumsAmount;
            case (Watermelon) -> watermelonGumsAmount;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return "BubbleGum{" +
                "flavour='" + flavour + '\'' +
                ", calories=" + calories +
                '}';
    }

    public void consume(){
        System.out.println(flavour + " " + name + " has been eaten.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BubbleGum bubbleGum = (BubbleGum) o;
        return Objects.equals(flavour, bubbleGum.flavour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flavour);
    }

    @Override
    public int calculateCalories() {
        return calories;
    }
}
