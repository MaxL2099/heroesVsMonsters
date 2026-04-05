package utils;

public class PressAnyKeyToContinue {

    public static void pressAnyKey() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }
}
