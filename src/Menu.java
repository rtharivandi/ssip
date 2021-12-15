public class Menu {
    public static void main(String[] args) {
        App app = new App();
        app.setTitle("SSIP Pre-Alpha");
        try {
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
