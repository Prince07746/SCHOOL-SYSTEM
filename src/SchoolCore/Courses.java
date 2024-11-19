package SchoolCore;

import java.util.Objects;
import java.util.UUID;








class Courses{
    String name;
    double maxPoints;
    String id;
    public Courses(String name,double maxPoints){
        this.name = name;
        this.maxPoints = maxPoints;
        this.id = name.substring(0,2)+ UUID.randomUUID().toString().substring(0,3);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courses courses = (Courses) o;
        return Objects.equals(id, courses.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}