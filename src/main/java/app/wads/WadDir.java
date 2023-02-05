package app.wads;

public class WadDir {
    private static String base_dir = "D:\\GZDoom\\wads";

    private String name;
    private String path;

    public WadDir(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public WadDir(String name) {
        this.name = name;
        this.path = base_dir;
    }

    public static String getBase_dir() {
        return base_dir;
    }

    public static void setBase_dir(String newBase_dir) {
        base_dir = newBase_dir;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public WadDir setPathReturnThis(String path) {
        setPath(path);
        return this;
    }
}
