package utils;
import enums.Armor;
import java.util.Scanner;

public class FightMenu {

    private static final int MENU_WIDTH = 128;
    private static Scanner scanner = null;

    private static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    public static String getHpBar(int hp, int maxHp, int barSize) {
        if (maxHp <= 0) return "░".repeat(barSize);
        if (hp < 0) hp = 0;
        if (hp > maxHp) hp = maxHp;

        int filled = (int) Math.ceil(((double) hp / maxHp) * barSize);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barSize; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        return bar.toString();
    }

    public static int showFightMenu(int hp, int maxHp, int str, int end, int pot,
                                    String eName, int eHp, int eMaxHp,
                                    Armor boots, Armor pants, Armor chestplate, int totalArmor) {
        Scanner sc = getScanner();

        String playerBar = getHpBar(hp, maxHp, 20);
        String enemyBar = getHpBar(eHp, eMaxHp, 20);

        String armorIcon = getArmorIcon(totalArmor);

        // --- LINE 1: Your HP on the left | Enemy HP on the right ---
        String line1Left = String.format(" HP: %s (%d/%d)", playerBar, hp, maxHp);
        String line1Right = String.format("ENEMY HP: %s (%d/%d)", enemyBar, eHp, eMaxHp);

        // --- LINE 2: Your stats + armor on one line | Enemy Name on the right ---
        String line2Left = String.format(" STR: %-2d | END: %-2d | 🧪: %-2d | %s ARMOR: 👢%s | 👖%s | 🦺%s | Total: %d",
                str, end, pot,
                armorIcon,
                formatArmor(boots),
                formatArmor(pants),
                formatArmor(chestplate),
                totalArmor);
        String line2Right = String.format("[%s]", eName.toUpperCase());

        // DISPLAY
        System.out.println("=".repeat(MENU_WIDTH));

        // Two-column layout
        System.out.printf("%-64s%64s%n", line1Left, line1Right);
        System.out.println(line2Left + " ".repeat(Math.max(0, MENU_WIDTH - line2Left.length() - line2Right.length())) + line2Right);

        System.out.println("-".repeat(MENU_WIDTH));

        // Action Options
        System.out.println("                [1]⚔️ Attack    [2]🛡️ Defend    [3]🧪 Potion    [4]🔥 Special    [5]🏃 Flee");

        System.out.println("=".repeat(MENU_WIDTH));
        System.out.print(" Choice > ");

        int choice = sc.hasNextInt() ? sc.nextInt() : -1;
        if (sc.hasNextLine()) sc.nextLine();
        return choice;
    }

    private static String formatArmor(Armor armor) {
        if (armor == Armor.NONE) {
            return "---";
        }
        return armor.name();
    }

    private static String getArmorIcon(int totalArmor) {
        if (totalArmor >= 15) return "💎"; // Diamond level
        if (totalArmor >= 12) return "🏆"; // Gold level
        if (totalArmor >= 9) return "⚙️";  // Steel level
        if (totalArmor >= 6) return "🔩";  // Iron/Chain level
        if (totalArmor >= 3) return "🛡️";  // Leather level
        return "NO"; // No/minimal armor
    }
}