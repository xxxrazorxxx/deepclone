import java.util.List;

public class Man {
    private String name;
    private int age;
    private List<String> favoriteBooks;
    private Man child;

    public Man(String name, int age, List<String> favoriteBooks, Man child) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
        this.child = child;
    }

    public Man(List<String> favoriteBooks) {
        this.name = "test";
        this.age = 123;
        this.favoriteBooks = favoriteBooks;
        this.child = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public Man getChild() {
        return child;
    }

    public void setChild(Man child) {
        this.child = child;
    }
}
