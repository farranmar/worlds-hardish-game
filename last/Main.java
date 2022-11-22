package last;

import engine.support.FXApplication;
import engine.support.FXFrontEnd;
import last.App;

public class Main {

    public static void main(String[] args) {
        FXFrontEnd app = new App("Last");
        FXApplication application = new FXApplication();
        application.begin(app);
    }

}
