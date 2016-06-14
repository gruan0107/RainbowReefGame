package myGames;


/**
 *
 * the usage of this class is to set and get degree when there is a collision or reflection.
 * Created by guoyiruan on 12/3/15.
 */
public class Degree {
//    private double degree = Math.PI / 2;

    //Degree: random degree between 1/4 Pi to 3/4 Pi;
    private double degree = (Math.random() / 2.0 + 0.25) * Math.PI;
    public void setDegree(double degree) {
        this.degree = degree;
    }
    public double getDegree() {
        return degree;
    }
}