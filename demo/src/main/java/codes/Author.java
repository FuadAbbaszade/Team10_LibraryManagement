package codes;

public class Author {
    private String fullname;
    private String nationality;

    
    public Author(String fullname, String nationality) {
        this.fullname = fullname;
        this.nationality = nationality;
    }


    public String getFullName() {
        return fullname;
    }

    
    public String getNationality() {
        return nationality;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    @Override
    public String toString(){
        return fullname + " ("+ nationality + ")";
    }
}
