import javax.swing.*;
import java.awt.*;

/**
 * TypingRaceGUI is the entry point for the application UI.
 *
 * It builds the JFrame window and manages navigation between menus
 * using a CardLayout system.
 *
 * The GUI is composed of multiple menu screens such as:
 * - PassageMenu (setup)
 * - AddTypistMenu (player creation)
 * - Game simulation screens
 */
public class TypingRaceGUI
{
    private JFrame mainWindow;
    private CardLayout cardLayout;
    private JPanel menuContainer;

    /**
     * Constructs the main GUI window and initializes the card system.
     */
    public TypingRaceGUI()
    {
        mainWindow = new JFrame("Typing Race Simulator!");
        cardLayout = new CardLayout();
        menuContainer = new JPanel(cardLayout);
    }

    /**
     * Starts the GUI application.
     *
     * Sets up menus, configures the window, and displays the first screen.
     */
    public void startRaceGUI()
    {
        setupMenus();

        mainWindow.setSize(300, 300);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }

    /**
     * Configures and registers all menus used in the application.
     *
     * This method links GameInfo with UI screens and adds them
     * to the CardLayout container.
     */
    public void setupMenus()
    {
        // Shared game state across menus
        GameInfo gameInfo = new GameInfo();

        // Menu screens
        PassageMenu passageMenu = new PassageMenu(gameInfo);
        AddTypistMenu addTypistMenu = new AddTypistMenu(gameInfo);

        // Register menus into card system
        menuContainer.add(passageMenu.createMenu(menuContainer, cardLayout, "ADD"), "MENU");
        menuContainer.add(addTypistMenu.createMenu(menuContainer, cardLayout, "GAME"), "ADD");

        // Attach to window
        mainWindow.add(menuContainer);

        // Show initial screen
        cardLayout.show(menuContainer, "MENU");
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args)
    {
        TypingRaceGUI app = new TypingRaceGUI();
        app.startRaceGUI();
    }
}