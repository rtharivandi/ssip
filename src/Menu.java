public class Menu {
    public static void main(String[] args) {
        App app = new App();
        app.setTitle("SSIP Pre-Alpha");
        try {
            app.startSlideshow();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
