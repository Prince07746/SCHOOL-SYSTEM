package SchoolCore.UserSystem;

import java.util.ArrayList;

public class Teacher extends User{
    double salary;
    String course;
    Teacher(String name,String lastName,String gender,int age,double salary,String course){
        super(name, lastName, gender, age);
        this.salary = salary;
        this.course = course;
    }

    public double getSalary() {
        return salary;
    }

    public double getSalaryAfterNet(ArrayList<Double> tax){
        double totalTax = 0;
        for(double i:tax){
            totalTax += i;
        }
        return getSalary()-((totalTax/100)*getSalary());
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
