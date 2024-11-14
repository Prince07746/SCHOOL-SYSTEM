import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;


public class Main {
    public static void main(String[] args) {

        DatabaseConnection data = new DatabaseConnection("school","root","selemani");
        data.getDataOfTable("class");
        data.getDataOfTable("class","coding");
    }
}






class School{

    public School(){}






}






class Courses{
    String name;
    String teacher;
    double marks;
    public Courses(String name,String teacher,double marks){
        this.name = name;
        this.teacher = teacher;
        this.marks = marks;
    }

    public String getTeacher(){
        return teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }
    public void setTeacher(String newTeacher){
        this.teacher = newTeacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courses courses = (Courses) o;
        return Double.compare(marks, courses.marks) == 0 && Objects.equals(name, courses.name) && Objects.equals(teacher, courses.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teacher, marks);
    }
}

abstract class User{
    private String name;
    private String lastName;
    private String role;
    private int age;
    public User(String name,String lastName,int age){
        this.name = name;
        this.lastName = lastName;
        this.role = this.getClass().getName();
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && Objects.equals(name, user.name) && Objects.equals(lastName, user.lastName) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, role, age);
    }
}

class Student extends User{
    String className;
    ArrayList<Courses> courses;

    public Student(String name, String lastName,int age, String className, ArrayList<Courses> courses) {
        super(name, lastName, age );
        this.className = className;
        this.courses = courses;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<Courses> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Courses> courses) {
        this.courses = courses;
    }
}

class Teacher extends User{
    public Teacher(String name, String lastName, int age) {
        super(name, lastName, age);
    }
}



