package bsu.rfct.course2.group9.Indyukov;

public class Food implements Nutritious{
    protected int calories = 0;

    @Override
    public int calculateCalories(){
        return calories;
    }
}
