package bsu.rfct.course2.group9.Indyukov;

import java.util.Objects;

public class Tea extends Food{

    private String color = "";


    private static int blackTeaAmount = 0;
    private static int greenTeaAmount = 0;
    private static int blueTeaAmount = 0;
    static final String Black = "Black";
    static final String Green = "Green";
    static final String Blue = "Blue";

    public Tea(String color) throws InvalidColorException {
        this.color = color;
        this.name = "Tea";

        switch (color) {
            case (Black):
                this.calories = 100;
                blackTeaAmount++;
                break;
            case (Green):
                this.calories = 200;
                greenTeaAmount++;
                break;
            case (Blue):
                this.calories = 300;
                blueTeaAmount++;
                break;
            default:
                throw new InvalidColorException("Invalid tea color");
        }
    }

    public int Amount(){
        return switch (color){
            case(Black) -> blackTeaAmount;
            case(Green) -> greenTeaAmount;
            case(Blue) -> blueTeaAmount;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return "Tea{" +
                "color='" + color + '\'' +
                ", calories=" + calories +
                '}';
    }

    @Override
    public void consume(){
        System.out.println(color + " " + name + " has been eaten.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tea tea = (Tea) o;
        return Objects.equals(color, tea.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public int calculateCalories() {
        return calories;
    }
}
