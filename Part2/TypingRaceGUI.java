import javax.swing.*;
import java.awt.*;

public class TypingRaceGUI
{
    private JFrame window;
    private CardLayout cards;
    private JPanel menus;

    public TypingRaceGUI()
    {
        window = new JFrame("Typing Race Simulator!"); 
        cards = new CardLayout();
        menus = new JPanel(cards);
    }

    public void startRaceGUI()
    {
        configureMenus();
        window.setSize(300, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void configureMenus()
    {
        GameInfo information = new GameInfo();
        PassageMenu passageMenu = new PassageMenu(information);
        AddTypistMenu addTypistMenu = new AddTypistMenu(information);

        menus.add(passageMenu.createMenu(menus, cards, "ADD"), "MENU");
        menus.add(addTypistMenu.createMenu(menus, cards, "GAME"), "ADD");;
        window.add(menus);

        cards.show(menus, "MENU");
    }

    public static void main(String[] args)
    {
        TypingRaceGUI game = new TypingRaceGUI();
        game.startRaceGUI();
    }
}