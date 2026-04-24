import javax.swing.*;
import java.awt.*;

public class TypingRaceGUI
{
    private JFrame window = new JFrame("Typing Race Simulator!");
    private CardLayout cards = new CardLayout();
    private JPanel menus = new JPanel(cards);

    public void configureMenus()
    {
        GameInfo information = new GameInfo();
        PassageMenu passageMenu = new PassageMenu(information);
        TypistCountMenu countMenu = new TypistCountMenu(information);
        AddTypistMenu addTypistMenu = new AddTypistMenu(information);

        menus.add(passageMenu.createMenu(menus, cards, "NUMBER"), "MENU");
        menus.add(countMenu.createMenu(menus, cards, "ADD"), "NUMBER");
        menus.add(addTypistMenu.createMenu(menus, cards, "MENU"), "ADD");
        window.add(menus);

        cards.show(menus, "MENU");

        
        window.setSize(300, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] args)
    {
        TypingRaceGUI test = new TypingRaceGUI();
        test.configureMenus();
    }
}