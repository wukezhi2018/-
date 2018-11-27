package top.wukezhi.personalmanagement.Gson_joke;

public class Joke_show {
    private int idImage;//图片id
    private String name;//标题
    public Joke_show(int idImage,String name){
        this.idImage=idImage;
        this.name=name;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdImage() {

        return idImage;
    }

    public String getName() {
        return name;
    }
}
