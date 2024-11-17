import java.util.Objects;
import java.util.UUID;








abstract class User{
    String name;
    String lastName;
    String gender;
    int age;
    String type;
    String id;
    User(String name,String lastName,String gender,int age){
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        type = getClass().getName();
        this.id = UUID.randomUUID().toString().substring(0,5);
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        User user = (User) o;
        return age == user.age && Objects.equals(name, user.name) && Objects.equals(lastName, user.lastName) && Objects.equals(gender, user.gender) && Objects.equals(type, user.type) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, gender, age, type, id);
    }
}

