import java.util.ArrayList;
import java.util.UUID;




public class Student extends User{
    double marks;
    String course;
    Student(String name,String lastName,String gender,int age){
        super(name, lastName, gender, age);
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}

