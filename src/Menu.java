import App.App;

public class Menu {
    public static void main(String[] args) {
        App app = new App();
        app.setTitle("SSIP Pre-Alpha");
        try {
            app.createAndShowGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
