import javax.swing.*;
import java.util.Collections;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 * The Menu class serves as a base container for UI menu screens
 * within the typing race application.
 *
 * It provides a standard JPanel layout (BorderLayout) that child
 * menu classes can build upon, ensuring consistent spacing and structure.
 *
 * The class also maintains a reference to the GameInfo object,
 * allowing menus to access and modify shared game configuration data.
 *
 */
class Menu
{
    protected JPanel container; // Main panel with spacing between components
    private GameInfo gameInformation; // Shared game configuration data

    /**
     * Constructs a Menu with a reference to the current game information.
     *
     * @param gameInformation shared GameInfo object
     */
    public Menu(GameInfo gameInformation)
    {
        this.gameInformation = gameInformation;
        this.container = new JPanel(new BorderLayout(25, 5));
    }

    /**
     * Gets the current GameInfo object.
     *
     * @return game configuration data
     */
    public GameInfo getGameInfo()
    {
        return gameInformation;
    }

    /**
     * Sets the GameInfo object.
     *
     * @param gameInfo new game configuration data
     */
    public void setGameInfo(GameInfo gameInfo)
    {
        gameInformation = gameInfo;
    }
}

/**
 * The PassageMenu class represents the UI screen where the user selects
 * or enters a passage for the typing race.
 *
 * Users can:
 * - Choose a predefined passage or enter a custom one
 * - Select the number of typists
 * - Enable gameplay modifiers (autocorrect, caffeine, night mode)
 *
 * This class builds and returns a fully configured JPanel for use
 * within a CardLayout-based menu system.
 */
class PassageMenu extends Menu
{
    /**
     * Constructs a PassageMenu with shared game information.
     *
     * @param gameInformation the GameInfo object storing global game settings
     */
    public PassageMenu(GameInfo gameInformation)
    {
        super(gameInformation);
    }

    /**
     * Builds and returns the passage selection menu panel.
     *
     * This method creates all UI components required for:
     * - Selecting a predefined passage OR entering a custom one
     * - Choosing the number of typists
     * - Enabling optional gameplay modifiers
     *
     * It also handles:
     * - Input validation
     * - Error display
     * - Transitioning to the next menu
     *
     * @param menuContainer the parent panel using CardLayout
     * @param layoutManager the CardLayout controller
     * @param nextMenuName the name of the next menu to display
     * @return the constructed JPanel representing this menu
     */
    public JPanel createMenu(JPanel menuContainer, CardLayout layoutManager, String nextMenuName)
    {
        // =========================
        // Base Layout Setup
        // =========================
        // Adds the main title banner to the container panel
        HelperUtilities.addTitle(container);

        JPanel passageOptionsPanel = new JPanel(); // Holds predefined passage list
        JPanel selectionButtonsPanel = new JPanel(new GridBagLayout()); // Holds radio buttons
        JPanel mainContentPanel = new JPanel(new BorderLayout()); // Dynamic content area
        JPanel contentWrapperPanel = new JPanel(new BorderLayout()); // Wraps content + modifiers
        JPanel modifierPanel = new JPanel(new FlowLayout()); // Mode toggles
        JPanel typistCountPanel = new JPanel(new BorderLayout()); // Typist count selector
        JPanel descriptionPanel = new JPanel(new BorderLayout()); // Length descriptions

        // Adds padding to main content area for spacing
        HelperUtilities.addPanelPadding(mainContentPanel, 25);

        // Sets background for description panel
        descriptionPanel.setBackground(Color.WHITE);

        // =========================
        // Input Components
        // =========================

        /**
         * Custom passage input area.
         * Users type their own passage here.
         */
        JTextArea customPassageArea = new JTextArea();
        customPassageArea.setLineWrap(true);
        JScrollPane customPassageScroll = new JScrollPane(customPassageArea);

        /**
         * Predefined passage options.
         * Users can select one from a list instead of typing.
         */
        String[] predefinedPassages = {
            "hello my name is Kishal and I have a little brother called Raayan",
            "The quick lazy fox jumps over the fence"
        };

        JList<String> predefinedPassageList = new JList<>(predefinedPassages);

        // Ensures only one passage can be selected at a time
        predefinedPassageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane predefinedScrollPane = new JScrollPane(predefinedPassageList);
        passageOptionsPanel.add(predefinedScrollPane);

        // =========================
        // Typist Count Selector
        // =========================

        /**
         * Spinner allowing the user to choose number of typists.
         * Range: 2 to 6 (inclusive)
         */
        JSpinner typistCountSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 6, 1));

        // Center-aligns the spinner text field
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) typistCountSpinner.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

        // Adds padding inside the spinner field
        HelperUtilities.addTextFieldPadding(spinnerEditor.getTextField(), 15);

        // Spacer label for layout balancing
        JLabel spacerLabel = new JLabel("                                    ");
        HelperUtilities.addLabelPadding(spacerLabel, 15);

        // Adds a titled border to the panel
        HelperUtilities.addTitleBorder(typistCountPanel, "Seat Count");

        typistCountPanel.add(typistCountSpinner, BorderLayout.NORTH);
        typistCountPanel.add(spacerLabel, BorderLayout.SOUTH);

        // =========================
        // Mode Toggles
        // =========================

        /**
         * Gameplay modifier toggles.
         * Each checkbox activates a specific global mode.
         */
        JCheckBox autoCorrectCheck = new JCheckBox("Autocorrect mode");
        JCheckBox caffeineCheck = new JCheckBox("Caffeine mode");
        JCheckBox nightModeCheck = new JCheckBox("Night mode");

        modifierPanel.add(autoCorrectCheck);
        modifierPanel.add(caffeineCheck);
        modifierPanel.add(nightModeCheck);

        HelperUtilities.addTitleBorder(modifierPanel, "Difficulty Modifiers");

        // =========================
        // Selection Controls
        // =========================

        /**
         * Radio buttons to choose input type:
         * - Predefined list
         * - Custom text input
         */
        JRadioButton predefinedOption = new JRadioButton("Choose from a list of pre-defined passages?");
        JRadioButton customOption = new JRadioButton("Enter a custom passage?");
        ButtonGroup selectionGroup = new ButtonGroup();

        selectionGroup.add(predefinedOption);
        selectionGroup.add(customOption);

        selectionButtonsPanel.add(predefinedOption);
        selectionButtonsPanel.add(customOption);

        mainContentPanel.add(selectionButtonsPanel, BorderLayout.NORTH);

        HelperUtilities.addTitleBorder(mainContentPanel, "Select Passage");

        // =========================
        // Error Display
        // =========================

        /**
         * Error label shown when input validation fails.
         */
        JLabel errorLabel = HelperUtilities.createError("Invalid Input!");

        // =========================
        // Description Panel
        // =========================

        /**
         * Displays passage length guidelines for the user.
         */
        JLabel shortLabel = new JLabel("Short: 1-15 characters long!");
        HelperUtilities.centerText(shortLabel);
        HelperUtilities.addLabelPadding(shortLabel, 5);

        JLabel mediumLabel = new JLabel("Medium: 16-45 characters long!");
        HelperUtilities.centerText(mediumLabel);
        HelperUtilities.addLabelPadding(mediumLabel, 5);

        JLabel longLabel = new JLabel("Long: 46-70 characters long!");
        HelperUtilities.centerText(longLabel);
        HelperUtilities.addLabelPadding(longLabel, 5);

        descriptionPanel.add(shortLabel, BorderLayout.NORTH);
        descriptionPanel.add(mediumLabel, BorderLayout.CENTER);
        descriptionPanel.add(longLabel, BorderLayout.SOUTH);

        // =========================
        // Action Listeners
        // =========================

        /**
         * Handles switching to predefined passage selection.
         * Removes custom input area if present.
         */
        predefinedOption.addActionListener(e -> {
            if(mainContentPanel.isAncestorOf(customPassageScroll))
            {
                mainContentPanel.remove(customPassageScroll);
            }

            if(mainContentPanel.isAncestorOf(errorLabel))
            {
                mainContentPanel.remove(errorLabel);
            }

            mainContentPanel.add(passageOptionsPanel, BorderLayout.CENTER);
            HelperUtilities.refresh(mainContentPanel); 
        });

        /**
         * Handles switching to custom passage input.
         * Removes predefined list if present.
         */
        customOption.addActionListener(e -> {
            if(mainContentPanel.isAncestorOf(passageOptionsPanel))
            {
                mainContentPanel.remove(passageOptionsPanel);
            }

            if(mainContentPanel.isAncestorOf(errorLabel))
            {
                mainContentPanel.remove(errorLabel);
            }
            
            mainContentPanel.add(customPassageScroll, BorderLayout.CENTER);
            HelperUtilities.refresh(mainContentPanel);
        });

        // =========================
        // Submit Button
        // =========================

        /**
         * Handles submission of passage input.
         * Validates input and updates GameInfo.
         * Transitions to next menu if successful.
         */
        JButton submitButton = new JButton("Submit passage");

        submitButton.addActionListener(e ->{
            String passageBuffer;
            GameInfo gameInfo = super.getGameInfo();

            // Case: Custom passage input
            if(mainContentPanel.isAncestorOf(customPassageScroll))
            {
                passageBuffer = customPassageArea.getText();

                if(passageBuffer.trim().isEmpty() || passageBuffer.length() > 70)
                {
                    mainContentPanel.add(errorLabel, BorderLayout.SOUTH);
                    HelperUtilities.refresh(mainContentPanel);
                }
                else{
                   gameInfo.setPassage(passageBuffer);
                   gameInfo.setNumberOfWords(HelperUtilities.countWords(passageBuffer));
                   gameInfo.setNumberOfTypists((int) typistCountSpinner.getValue());

                   if(autoCorrectCheck.isSelected()) gameInfo.activateAutoCorrect();
                   if(caffeineCheck.isSelected()) gameInfo.activateCaffeine();
                   if(nightModeCheck.isSelected()) gameInfo.activateNight();

                   layoutManager.show(menuContainer, nextMenuName);
                }
            }
            // Case: Predefined passage selection
            else if(mainContentPanel.isAncestorOf(passageOptionsPanel))
            {
                passageBuffer = predefinedPassageList.getSelectedValue();

                if(passageBuffer == null)
                {
                    mainContentPanel.add(errorLabel, BorderLayout.SOUTH);
                    HelperUtilities.refresh(mainContentPanel);
                }
                else{
                    gameInfo.setPassage(passageBuffer);
                    gameInfo.setNumberOfWords(HelperUtilities.countWords(passageBuffer));
                    gameInfo.setNumberOfTypists((int) typistCountSpinner.getValue());

                    if(autoCorrectCheck.isSelected()) gameInfo.activateAutoCorrect();
                    if(caffeineCheck.isSelected()) gameInfo.activateCaffeine();
                    if(nightModeCheck.isSelected()) gameInfo.activateNight();

                    layoutManager.show(menuContainer, nextMenuName);
                }
            }
            // Case: No selection made
            else{
                mainContentPanel.add(errorLabel, BorderLayout.SOUTH);
                HelperUtilities.refresh(mainContentPanel);
            }
        });

        // =========================
        // Final Layout Assembly
        // =========================

        contentWrapperPanel.add(modifierPanel, BorderLayout.SOUTH);
        contentWrapperPanel.add(mainContentPanel, BorderLayout.CENTER);

        container.add(contentWrapperPanel, BorderLayout.CENTER);
        container.add(typistCountPanel, BorderLayout.WEST);
        container.add(submitButton, BorderLayout.SOUTH);

        return container;
    }
}

/**
 * The AddTypistMenu class represents the UI screen where users create
 * and configure typists before the typing race begins.
 *
 * Users can:
 * - Enter a name, symbol, and accuracy for each typist
 * - Choose typing style, keyboard, and accessories
 * - Ensure no duplicate names or symbols exist
 *
 * Once the required number of typists is reached, the simulation
 * automatically starts.
 */
class AddTypistMenu extends Menu
{
    // =========================
    // State Tracking Fields
    // =========================

    private int currentNumberOfTypists = 0;

    // Stores already used names and symbols to prevent duplicates
    private ArrayList<String> usedNames = new ArrayList<>();
    private ArrayList<Character> usedSymbols = new ArrayList<>();

    // Stores created typists for the simulation
    private ArrayList<TypistSimulation> typists = new ArrayList<>();

    // Timer used to run the simulation loop
    private Timer timer;

    /**
     * Constructs the AddTypistMenu.
     *
     * @param gameInformation shared game configuration data
     */
    public AddTypistMenu(GameInfo gameInformation)
    {
        super(gameInformation);
    }

    /**
     * Builds and returns the Add Typist menu UI.
     *
     * This method creates:
     * - Input fields for typist creation
     * - UI panels for styles, keyboard, accessories
     * - Validation and duplicate checking
     * - Simulation startup once all typists are added
     *
     * @param menus parent panel using CardLayout
     * @param cards CardLayout controller
     * @param nextMenu next screen name
     * @return constructed JPanel
     */
    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        // =========================
        // Game Data Reference
        // =========================
        GameInfo gameInformation = getGameInfo();

        // =========================
        // Main Container Setup
        // =========================
        HelperUtilities.addTitle(container);

        JPanel content = new JPanel(new BorderLayout());
        JPanel input = new JPanel();

        JLabel typistInfo = new JLabel("Typist no. " + (currentNumberOfTypists + 1));
        HelperUtilities.addLabelPadding(typistInfo, 5);

        JLabel filler = new JLabel("                  ");

        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

        // =========================
        // Name Input
        // =========================
        JPanel namePanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(namePanel, "Enter Typist Name");

        JTextField typistName = new JTextField();
        namePanel.add(typistName, BorderLayout.CENTER);

        // =========================
        // Symbol Input
        // =========================
        JPanel symbolPanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(symbolPanel, "Enter Typist Symbol");

        JTextField typistSymbol = new JTextField();
        symbolPanel.add(typistSymbol, BorderLayout.CENTER);

        // =========================
        // Colour Selection
        // =========================
        JPanel colourPanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(colourPanel, "Choose Colour");

        JButton colourButton = new JButton(" ");
        colourButton.setBackground(Color.GREEN);

        colourPanel.add(filler, BorderLayout.SOUTH);
        colourPanel.add(colourButton, BorderLayout.CENTER);

        colourButton.addActionListener(e -> {
            Color chosenColour = JColorChooser.showDialog(null, "Choose Colour", Color.GREEN);

            if(chosenColour != null)
            {
                colourButton.setBackground(chosenColour);
            }
        });

        // =========================
        // Accuracy Input
        // =========================
        JPanel accuracyPanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(accuracyPanel, "Enter Typist Accuracy (0 - 1)");

        JTextField typistAccuracy = new JTextField();
        accuracyPanel.add(typistAccuracy, BorderLayout.CENTER);

        // =========================
        // Typing Style Selection
        // =========================
        JPanel typingStylePanel = new JPanel(new BorderLayout());

        JTextArea typingStyleInfo = new JTextArea(
            "Touch Typist - 1.1x accuracy, burnout chance cap lifted to 12%" + 
            "\nHunt & Peck - 1.2x accuracy, burnout duration decreased to 2 turns" +
            "\nPhone Thumbs - 0.9x accuracy, burnout chance cap decreased tp 2%" + 
            "\nVoice-to-Text - 0.7x accuracy, burnout duration increased to 5 turns"
        );

        typingStyleInfo.setEditable(false);
        HelperUtilities.addTextAreaPadding(typingStyleInfo, 10);

        HelperUtilities.addTitleBorder(typingStylePanel, "Select Typing Style");

        String[] typingStyles = {"None", "Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
        JComboBox<String> typingStyleOptions = new JComboBox<>(typingStyles);

        typingStylePanel.add(typingStyleOptions, BorderLayout.CENTER);
        typingStylePanel.add(typingStyleInfo, BorderLayout.SOUTH);

        // =========================
        // Keyboard Selection
        // =========================
        JPanel keyboardPanel = new JPanel(new BorderLayout());

        JTextArea keyboardInfo = new JTextArea(
            "Mechanical - 3x typing speed, mistype chance cap lifted to 40%" +
            "\nMembrane - 2x typing speed, mistype chance cap lifted to 35%" +
            "\nTouchscreem - mistype chance cap decreased to 20%" +
            "\nStenography - 5x typing speed, mistype chance cap lifed to 60%"
        );

        keyboardInfo.setEditable(false);
        HelperUtilities.addTextAreaPadding(keyboardInfo, 10);

        HelperUtilities.addTitleBorder(keyboardPanel, "Select Keyboard");

        String[] keyboards = {"None", "Mechanical", "Membrane", "Touchscreen", "Stenography"};
        JComboBox<String> keyboardOptions = new JComboBox<>(keyboards);

        keyboardPanel.add(keyboardOptions, BorderLayout.CENTER);
        keyboardPanel.add(keyboardInfo, BorderLayout.SOUTH);

        // =========================
        // Accessories Selection
        // =========================
        JPanel accessories = new JPanel(new BorderLayout());
        JPanel checkButtons = new JPanel(new FlowLayout());

        JTextArea accessoriesInfo = new JTextArea(
            "Wrist Support - 0.5x burnout duration" +
            "\nEnergy Drink - 2x original accuracy for first 15 turns, 0.5x accuracy afterwards" +
            "\nHeadPhones - 0.5x the mistype chance cap"
        );

        accessoriesInfo.setEditable(false);
        HelperUtilities.addTextAreaPadding(accessoriesInfo, 10);

        HelperUtilities.addTitleBorder(accessories, "Select Accessories");

        JCheckBox wristSupport = new JCheckBox("Wrist Support");
        JCheckBox energyDrink = new JCheckBox("Energy Drink");
        JCheckBox headphones = new JCheckBox("Headphones");

        checkButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        checkButtons.add(wristSupport);
        checkButtons.add(energyDrink);
        checkButtons.add(headphones);

        accessories.add(checkButtons, BorderLayout.NORTH);
        accessories.add(accessoriesInfo, BorderLayout.CENTER);

        // =========================
        // Errors + Submit Button
        // =========================
        JButton submit = new JButton("Add typist");

        JLabel invalidInputError = HelperUtilities.createError("Invalid Input!");
        JLabel existingError = HelperUtilities.createError(
            "There is an already existing typist registered with that name or symbol!"
        );

        // =========================
        // Assemble Input Panel
        // =========================
        input.add(namePanel);
        input.add(symbolPanel);
        input.add(colourPanel);
        input.add(accuracyPanel);
        input.add(typingStylePanel);
        input.add(keyboardPanel);
        input.add(accessories);

        JScrollPane scrollInput = new JScrollPane(input);

        content.add(scrollInput, BorderLayout.CENTER);
        content.add(typistInfo, BorderLayout.NORTH);

        // =========================
        // Submit Logic
        // =========================
        submit.addActionListener(e -> {

            String name = typistName.getText();
            String symbol = typistSymbol.getText();
            String accuracy = typistAccuracy.getText();

            // Validate input format
            if(validTypistFields(name, symbol, accuracy))
            {
                Double dAccuracy = Double.parseDouble(accuracy);
                char cSymbol = symbol.charAt(0);

                // Check duplicates
                if(!usedNames.contains(name) && !usedSymbols.contains(cSymbol))
                {
                    // =========================
                    // Accessory Flags
                    // =========================
                    boolean[] accessoriesChoice = {false, false, false};

                    if(wristSupport.isSelected()) accessoriesChoice[0] = true;
                    if(energyDrink.isSelected()) accessoriesChoice[1] = true;
                    if(headphones.isSelected()) accessoriesChoice[2] = true;

                    // Create typist
                    String typingProfile = (String) typingStyleOptions.getSelectedItem();
                    String keyboard = (String) keyboardOptions.getSelectedItem();
                    Color colour = colourButton.getBackground();

                    TypistSimulation typist = new TypistSimulation(
                        cSymbol, name, dAccuracy,
                        typingProfile, keyboard,
                        colour, accessoriesChoice
                    );

                    // Register typist
                    usedNames.add(name);
                    usedSymbols.add(cSymbol);
                    typists.add(typist);
                    currentNumberOfTypists++;

                    // =========================
                    // Start Simulation Condition
                    // =========================
                    if(currentNumberOfTypists == gameInformation.getNumberOfTypists())
                    {
                        gameInformation.setTypists(typists);

                        SimulationMenu simulationMenu = new SimulationMenu(gameInformation);
                        ArrayList<PerformanceMetric> results = new ArrayList<>();
                        HashMap<TypistSimulation, RacingHistory> racingHistories = new HashMap<>();

                        simulationMenu.setupSimulation();

                        menus.add(simulationMenu.createMenu(menus, cards, "MENU"), "GAME");
                        cards.show(menus, nextMenu);

                        timer = new Timer(200, f -> {

                            simulationMenu.updateSimulation(results, racingHistories);

                            if(simulationMenu.getFinished())
                            {
                                timer.stop();
                                simulationMenu.finishedRace();

                                ResultsMenu resultsMenu = new ResultsMenu(gameInformation, results);

                                menus.add(
                                    resultsMenu.createMenu(menus, cards, "GAME", timer, racingHistories),
                                    "RESULTS"
                                );

                                cards.show(menus, "RESULTS");
                            }
                        });

                        timer.start();
                    }

                    // =========================
                    // Cleanup Errors + Reset UI
                    // =========================
                    if(content.isAncestorOf(invalidInputError))
                    {
                        content.remove(invalidInputError);
                    }
                    else if(content.isAncestorOf(existingError))
                    {
                        content.remove(existingError);
                    }

                    typistInfo.setText("Typist no. " + (currentNumberOfTypists + 1));

                    typistName.setText("");
                    typistAccuracy.setText("");
                    typistSymbol.setText("");

                    HelperUtilities.refresh(content);
                }
                else
                {
                    if(content.isAncestorOf(invalidInputError))
                    {
                        content.remove(invalidInputError);
                    }

                    content.add(existingError, BorderLayout.SOUTH);
                    HelperUtilities.refresh(content);
                }
            }
            else
            {
                if(content.isAncestorOf(existingError))
                {
                    content.remove(existingError);
                }

                content.add(invalidInputError, BorderLayout.SOUTH);
                HelperUtilities.refresh(content);
            }
        });

        // =========================
        // Final Layout Assembly
        // =========================
        container.add(content, BorderLayout.CENTER);
        container.add(submit, BorderLayout.SOUTH);

        return container;
    }

    /**
     * Validates user input for creating a typist.
     *
     * Ensures:
     * - Name is not empty
     * - Symbol is a single character
     * - Accuracy is a valid double between 0 and 1
     *
     * @return true if input is valid
     */
    private boolean validTypistFields(String name, String symbol, String accuracy)
    {
        if(name.trim().isEmpty() || !HelperUtilities.isChar(symbol) || !HelperUtilities.isDouble(accuracy))
        {
            return false;
        }

        double dAccuracy = Double.parseDouble(accuracy);
        return !(dAccuracy < 0 || dAccuracy > 1);
    }
}

/**
 * The SimulationMenu class is responsible for running and displaying the
 * live typing race simulation.
 *
 * It manages:
 * - Rendering each typist's track (grid-based passage display)
 * - Updating typist progress each simulation tick
 * - Displaying real-time state (burnout, mistypes, position)
 * - Tracking race completion and generating results
 *
 * This class acts as the "engine + UI bridge" between TypingRaceSimulation
 * and the Swing-based visual representation.
 */
class SimulationMenu extends Menu
{
    private HashMap<JPanel, JTextArea[]> trackMap;
    private JPanel[] tracks;
    private JTextArea[] playerinformations;
    private String passage;
    private int passageLength;
    private int passageWordCount;
    private TypingRaceSimulation simulation;
    private boolean isFinished = false;

    /**
     * Constructs the simulation menu and initializes the race engine.
     *
     * @param gameInformation shared game state containing typists, passage,
     *                        and mode configuration
     */
    public SimulationMenu(GameInfo gameInformation)
    {
        super(gameInformation);
        trackMap = new HashMap<>();
        passage = gameInformation.getPassage();
        passageWordCount = gameInformation.getNumberOfWords();
        passageLength = passage.length();
        tracks = new JPanel[gameInformation.getNumberOfTypists()];
        
        boolean[] modes = {
            gameInformation.isAutoCorrect(),
            gameInformation.isCaffeine(),
            gameInformation.isNight()
        };

        simulation = new TypingRaceSimulation(
            passageLength - 1,
            gameInformation.getTypists(),
            modes
        );
    }

    /**
     * Resets all typists and prepares the simulation for a new run.
     */
    public void setupSimulation()
    {
        simulation.resetTypists(); 
    }

    /**
     * Builds and returns the simulation UI panel.
     *
     * This method constructs:
     * - Typist tracks (grid-based passage visualization)
     * - Player status panels
     * - Scrollable simulation area
     *
     * @param menus the main CardLayout container
     * @param cards the CardLayout manager controlling screens
     * @param nextMenu the identifier for the next menu screen
     * @return fully constructed simulation JPanel
     */
    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {   
        simulation.configureGame();

        JPanel content = new JPanel(new BorderLayout());
        JPanel simulationP = new JPanel();
        simulationP.setLayout(new BoxLayout(simulationP, BoxLayout.Y_AXIS));

        GameInfo gameInformation = getGameInfo();
        playerinformations = new JTextArea[gameInformation.getNumberOfTypists()];
        ArrayList<TypistSimulation> typists = gameInformation.getTypists();

        int gridLength = 30;
        if (passageLength < gridLength)
        {
            gridLength = passageLength;
        }

        for (int i = 0; i < gameInformation.getNumberOfTypists(); i++)
        {
            JPanel track = new JPanel(new GridLayout(0, gridLength, 5, 5));
            JTextArea[] gridCells = new JTextArea[passageLength];
            TypistSimulation typist = typists.get(i);

            JTextArea firstCell = new JTextArea(2, 1);
            firstCell.setText(typist.getSymbol() + "\n" + passage.charAt(0));
            firstCell.setEditable(false);
            HelperUtilities.addTextAreaPadding(firstCell, 7);
            track.add(firstCell);
            gridCells[0] = firstCell;

            for (int j = 1; j < passageLength; j++)
            {
                JTextArea cell = new JTextArea(2, 1);
                cell.setText("\n" + passage.charAt(j));
                cell.setEditable(false);
                HelperUtilities.addTextAreaPadding(cell, 7);
                track.add(cell);
                gridCells[j] = cell;
            }

            JPanel trackWrapper = new JPanel(new BorderLayout());
            HelperUtilities.addPanelPadding(trackWrapper, 15);

            JTextArea player = new JTextArea(3, 1);
            setPlayerInfo(player, typist);
            player.append("                                     ");
            player.setEditable(false);
            HelperUtilities.addTextAreaPadding(player, 5);

            trackWrapper.add(player, BorderLayout.WEST);
            playerinformations[i] = player;

            trackMap.put(track, gridCells);
            tracks[i] = track;

            HelperUtilities.addPanelPadding(track, 5);
            trackWrapper.add(track, BorderLayout.CENTER);

            simulationP.add(trackWrapper);
        }

        JScrollPane scrollSimulation = new JScrollPane(simulationP);
        content.add(scrollSimulation, BorderLayout.CENTER);

        container.add(content, BorderLayout.CENTER);
        HelperUtilities.addTitle(container);

        return container;
    }

    /**
     * Advances the simulation by one tick for all typists and updates UI state.
     *
     * This includes:
     * - Moving typists forward or backward
     * - Handling mistypes and burnout states
     * - Updating grid visuals
     * - Checking race completion
     * - Recording performance metrics
     *
     * @param results list used to store performance results after race completion
     * @param racingHistories map tracking historical performance per typist
     */
    public void updateSimulation(ArrayList<PerformanceMetric> results,HashMap<TypistSimulation, RacingHistory> racingHistories)
    {
        ArrayList<TypistSimulation> typists = getGameInfo().getTypists();

        for (int i = 0; i < getGameInfo().getNumberOfTypists(); i++)
        {
            TypistSimulation typist = typists.get(i);
            int previousProgress = typist.getProgress();

            simulation.advanceTypist(typist);

            int progress = typist.getProgress();
            JPanel track = tracks[i];
            JTextArea[] gridCells = trackMap.get(track);

            if (previousProgress != progress)
            {
                int maxProgress = Math.max(previousProgress, progress);

                for (int j = 0; j <= maxProgress; j++)
                {
                    JTextArea cell = gridCells[j];
                    cell.setBackground(Color.WHITE);
                    cell.setText("\n" + passage.charAt(j));

                    if (j <= progress)
                    {
                        cell.setForeground(typist.getColour());
                    }
                    else
                    {
                        cell.setForeground(Color.BLACK);
                    }
                }

                JTextArea currentCell = gridCells[progress];
                currentCell.setText(typist.getSymbol() + "\n" + passage.charAt(progress));
                currentCell.setBackground(Color.YELLOW);
            }

            JTextArea playerInfo = playerinformations[i];
            setPlayerInfo(playerInfo, typist);

            if (typist.isBurntOut())
            {
                gridCells[progress].setForeground(Color.RED);
                gridCells[progress].setBackground(Color.WHITE);
                playerInfo.append("BURNT OUT (" + typist.getBurnoutTurnsRemaining() + " turns) ");
                playerInfo.setForeground(Color.RED);
            }
            else if (typist.isMistyped())
            {
                gridCells[progress].setForeground(Color.RED);
                playerInfo.append("← just mistyped           ");
                playerInfo.setForeground(Color.RED);
            }
            else
            {
                gridCells[progress].setForeground(Color.BLACK);
                gridCells[progress].setBackground(Color.YELLOW);
                playerInfo.append("                                     ");
                playerInfo.setForeground(Color.BLACK);
            }

            if (simulation.raceFinishedBy(typist))
            {
                gridCells[progress].setBackground(Color.WHITE);
                gridCells[progress].setForeground(typist.getColour());
            }

            if (simulation.raceConcluded())
            {
                isFinished = true;
            }
        }

        if (isFinished)
        {
            simulation.updateAndGetResults(results, passageWordCount, 0.2);

            for (PerformanceMetric metric : results)
            {
                TypistSimulation typist = metric.getTypist();

                if (racingHistories.containsKey(typist))
                {
                    racingHistories.get(typist).addMetric(metric);
                }
                else
                {
                    racingHistories.put(typist, new RacingHistory(metric));
                }
            }
        }
    }

    /**
     * Updates the player info display panel for a given typist.
     *
     * @param playerInfo the text area used to display player stats
     * @param typist the typist whose stats are being displayed
     */
    private void setPlayerInfo(JTextArea playerInfo, TypistSimulation typist)
    {
        playerInfo.setText(
            typist.getName() +
            " ( " + typist.getSymbol() + " )" +
            "\n(Accuracy: " + typist.getAccuracy() + ")\n"
        );
    }

    /**
     * Returns whether the simulation has finished.
     *
     * @return true if race is complete, false otherwise
     */
    public boolean getFinished()
    {
        return isFinished;
    }

    /**
     * Resets simulation state after a race ends.
     */
    public void finishedRace()
    {
        simulation.restartRace();
        clearStates();
        isFinished = false;
    }

    /**
     * Clears all UI grid states and resets them for a new race.
     */
    private void clearStates()
    {
        GameInfo gameInformation = getGameInfo();

        for (int i = 0; i < gameInformation.getNumberOfTypists(); i++)
        {
            JPanel track = tracks[i];
            JTextArea[] gridCells = trackMap.get(track);

            for (int j = 0; j < gridCells.length; j++)
            {
                JTextArea cell = gridCells[j];
                cell.setForeground(Color.BLACK);
                cell.setBackground(Color.WHITE);
                cell.setText("\n" + passage.charAt(j));
            }
        }
    }
}

/**
 * The ResultsMenu class is responsible for displaying the final results
 * of a completed typing race.
 *
 * It shows:
 * - Each typist's ranking and performance stats
 * - WPM, accuracy, accuracy change, and burnout count
 * - Navigation options such as restart, compare, quit, and history view
 *
 * It also allows users to inspect individual typist race histories
 * via a linked HistoryMenu.
 */
class ResultsMenu extends Menu
{
    private ArrayList<PerformanceMetric> results;
    private HashMap<JButton, TypistSimulation> buttonTypistMap = new HashMap<>();

    /**
     * Constructs the ResultsMenu with game state and final race results.
     *
     * @param gameInformation shared game state
     * @param results list of performance metrics from the completed race
     */
    public ResultsMenu(GameInfo gameInformation, ArrayList<PerformanceMetric> results)
    {
        super(gameInformation);
        this.results = results;
    }

    /**
     * Builds and returns the results screen UI.
     *
     * Displays:
     * - Ranked list of typists
     * - Performance statistics per typist
     * - Navigation controls (restart, compare, quit)
     *
     * @param menus CardLayout container holding all menus
     * @param cards layout manager for switching screens
     * @param nextMenu name of the menu to return to on restart
     * @param timer active simulation timer (used when restarting)
     * @param racingHistories historical performance data per typist
     * @return fully constructed results JPanel
     */
    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu, Timer timer, 
        HashMap<TypistSimulation, RacingHistory> racingHistories)
    {
        // =========================
        // Main Results Container
        // =========================
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // =========================
        // Build each result entry
        // =========================
        Iterator<PerformanceMetric> resultsIterator = results.iterator();
        PerformanceMetric currentResult;

        while (resultsIterator.hasNext())
        {
            currentResult = resultsIterator.next();

            JPanel track = new JPanel(new BorderLayout(10, 10));
            JPanel trackContent = new JPanel(new BorderLayout());
            JPanel stats = new JPanel();
            stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

            // =========================
            // Position + Name
            // =========================
            JLabel positionLabel =
                new JLabel(currentResult.getPosition() + ". " +
                           currentResult.getTypist().getName());

            JPanel positionPanel = new JPanel(new BorderLayout());
            JLabel padding = new JLabel("                   ");

            positionPanel.add(positionLabel, BorderLayout.CENTER);
            positionPanel.add(padding, BorderLayout.SOUTH);
            positionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            HelperUtilities.centerText(positionLabel);

            track.add(positionPanel, BorderLayout.WEST);

            // =========================
            // WPM Panel
            // =========================
            JPanel wpmPanel = new JPanel();
            HelperUtilities.addTitleBorder(wpmPanel, "WPM");
            JLabel wpm = new JLabel("" + currentResult.getWPM());
            wpmPanel.add(wpm);
            stats.add(wpmPanel);

            // =========================
            // Accuracy Panel
            // =========================
            JPanel accuracyPanel = new JPanel();
            HelperUtilities.addTitleBorder(accuracyPanel, "True Accuracy");
            JLabel accuracy = new JLabel("" + currentResult.getTrueAccuracy());
            accuracyPanel.add(accuracy);
            stats.add(accuracyPanel);

            // =========================
            // Accuracy Change Panel
            // =========================
            JPanel changePanel = new JPanel();
            HelperUtilities.addTitleBorder(changePanel, "Accuracy Change");
            JLabel change = new JLabel("" + currentResult.getAccuracyChange());
            changePanel.add(change);
            stats.add(changePanel);

            // =========================
            // Burnout Panel
            // =========================
            JPanel burnoutPanel = new JPanel();
            HelperUtilities.addTitleBorder(burnoutPanel, "Number Of Burnouts");
            JLabel burnout = new JLabel("" + currentResult.getBurnoutCount());
            burnoutPanel.add(burnout);
            stats.add(burnoutPanel);

            // =========================
            // Race History Button
            // =========================
            JButton historyButton = new JButton("Race History");
            buttonTypistMap.put(historyButton, currentResult.getTypist());

            historyButton.addActionListener(e -> {
                TypistSimulation selectedTypist =
                    buttonTypistMap.get(historyButton);

                RacingHistory history =
                    racingHistories.get(selectedTypist);

                HistoryMenu historyMenu =
                    new HistoryMenu(history, selectedTypist, getGameInfo());

                menus.add(
                    historyMenu.createMenu(menus, cards, "RESULTS"),
                    "HISTORY"
                );

                cards.show(menus, "HISTORY");
            });

            trackContent.add(stats, BorderLayout.CENTER);
            track.add(trackContent, BorderLayout.CENTER);
            track.add(historyButton, BorderLayout.EAST);

            HelperUtilities.addPanelPadding(track, 10);
            content.add(track);
        }

        // =========================
        // Bottom Control Buttons
        // =========================
        JPanel buttonGroup = new JPanel(new FlowLayout());

        JButton restartButton = new JButton("Race again");
        JButton compareButton = new JButton("Compare Typists");
        JButton quitButton = new JButton("Quit");

        buttonGroup.add(restartButton);
        buttonGroup.add(compareButton);
        buttonGroup.add(quitButton);

        // =========================
        // Button Actions
        // =========================
        quitButton.addActionListener(e -> System.exit(0));

        restartButton.addActionListener(e -> {
            cards.show(menus, nextMenu);
            timer.start();
        });

        compareButton.addActionListener(e -> {
            ConfigureCompareMenu compareMenu =
                new ConfigureCompareMenu(getGameInfo());

            menus.add(
                compareMenu.createMenu(menus, cards, nextMenu, results),
                "COMPARE_CONFIGURE"
            );

            cards.show(menus, "COMPARE_CONFIGURE");
        });

        // =========================
        // Final Layout Assembly
        // =========================
        JScrollPane scrollContent = new JScrollPane(content);

        container.add(scrollContent, BorderLayout.CENTER);
        container.add(buttonGroup, BorderLayout.SOUTH);
        HelperUtilities.addTitle(container);

        return container;
    }
}

/**
 * The HistoryMenu class displays a detailed breakdown of a single typist's
 * past race performances.
 *
 * It shows:
 * - Personal stats (name, WPM personal best, current accuracy)
 * - Overall accuracy trend across races
 * - A chronological list of race performance metrics
 *
 * This screen is accessed from the ResultsMenu via a typist's history button.
 */
class HistoryMenu extends Menu
{
    RacingHistory typistHistory;
    TypistSimulation typist;

    /**
     * Constructs the HistoryMenu for a specific typist.
     *
     * @param typistHistory historical race data for the typist
     * @param typist the typist whose history is being displayed
     * @param gameInformation shared game state
     */
    public HistoryMenu(
        RacingHistory typistHistory,
        TypistSimulation typist,
        GameInfo gameInformation
    )
    {
        super(gameInformation);
        this.typistHistory = typistHistory;
        this.typist = typist;
    }

    /**
     * Builds and returns the history screen UI.
     *
     * Displays:
     * - Typist summary stats
     * - Overall performance trend
     * - List of past race results in reverse chronological order
     *
     * @param menus CardLayout container
     * @param cards layout manager for navigation
     * @param nextMenu menu to return to
     * @return fully constructed history JPanel
     */
    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        // =========================
        // Main Containers
        // =========================
        JPanel content = new JPanel(new BorderLayout());

        JPanel historyContent = new JPanel();
        historyContent.setLayout(new BoxLayout(historyContent, BoxLayout.Y_AXIS));
        HelperUtilities.addTitleBorder(historyContent, "Racing History");

        // =========================
        // Load and reverse race history
        // =========================
        ArrayList<PerformanceMetric> races =
            new ArrayList<>(typistHistory.getRaceHistory());

        Collections.reverse(races);
        Iterator<PerformanceMetric> raceIterator = races.iterator();

        // =========================
        // Typist Summary Panel
        // =========================
        JPanel typistInfo = new JPanel();
        typistInfo.setLayout(new BoxLayout(typistInfo, BoxLayout.Y_AXIS));
        HelperUtilities.addTitleBorder(typistInfo, "Typist");

        JLabel nameLabel =
            new JLabel(typist.getName() + " (" + typist.getSymbol() + ")     ");

        JLabel wpmPRLabel =
            new JLabel("WPM PR - " + typistHistory.getBestWPM());

        JLabel accuracyLabel =
            new JLabel("Accuracy - " + typist.getAccuracy());

        double overallAccuracyChange =
            HelperUtilities.truncate(
                3,
                typist.getAccuracy()
                - typistHistory.getRaceHistory().get(0).getAccuracy()
            );

        JLabel changeLabel;

        if (overallAccuracyChange < 0.0)
        {
            changeLabel = new JLabel("-" + overallAccuracyChange);
            changeLabel.setForeground(Color.RED);
        }
        else
        {
            changeLabel = new JLabel("+" + overallAccuracyChange);
            changeLabel.setForeground(Color.GREEN);
        }

        typistInfo.add(nameLabel);
        typistInfo.add(wpmPRLabel);
        typistInfo.add(accuracyLabel);
        typistInfo.add(changeLabel);

        content.add(typistInfo, BorderLayout.WEST);

        // =========================
        // Race History Entries
        // =========================
        while (raceIterator.hasNext())
        {
            PerformanceMetric raceData = raceIterator.next();

            JPanel raceRow = new JPanel(new FlowLayout());

            // Position
            JPanel positionPanel = new JPanel();
            HelperUtilities.addTitleBorder(positionPanel, "Position");
            JLabel positionLabel =
                new JLabel(raceData.getPosition() + "                          ");
            positionPanel.add(positionLabel);

            // Accuracy
            JPanel accuracyPanel = new JPanel();
            HelperUtilities.addTitleBorder(accuracyPanel, "Accuracy");
            JLabel raceAccuracyLabel =
                new JLabel(raceData.getAccuracy() + "                          ");
            accuracyPanel.add(raceAccuracyLabel);

            // True Accuracy
            JPanel trueAccuracyPanel = new JPanel();
            HelperUtilities.addTitleBorder(trueAccuracyPanel, "True Accuracy");
            JLabel trueAccuracyLabel =
                new JLabel(raceData.getTrueAccuracy() + "                          ");
            trueAccuracyPanel.add(trueAccuracyLabel);

            // WPM
            JPanel wpmPanel = new JPanel();
            HelperUtilities.addTitleBorder(wpmPanel, "WPM");
            JLabel wpmLabel =
                new JLabel(raceData.getWPM() + "                          ");
            wpmPanel.add(wpmLabel);

            // Accuracy Change
            JPanel changePanel = new JPanel();
            HelperUtilities.addTitleBorder(changePanel, "Change In Accuracy");
            JLabel changeAccuracyLabel =
                new JLabel(raceData.getAccuracyChange() + "                          ");
            changePanel.add(changeAccuracyLabel);

            // Assemble row
            raceRow.add(positionPanel);
            raceRow.add(wpmPanel);
            raceRow.add(accuracyPanel);
            raceRow.add(trueAccuracyPanel);
            raceRow.add(changePanel);

            historyContent.add(raceRow);
        }

        // =========================
        // Back Button
        // =========================
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            cards.show(menus, nextMenu);
        });

        // =========================
        // Final Layout Assembly
        // =========================
        JScrollPane scrollHistory = new JScrollPane(historyContent);

        content.add(scrollHistory, BorderLayout.CENTER);
        container.add(content, BorderLayout.CENTER);
        container.add(backButton, BorderLayout.SOUTH);

        HelperUtilities.addTitle(container);

        return container;
    }
}

/**
 * The ConfigureCompareMenu allows the user to select:
 * - Which typists to compare
 * - Which performance metric to compare (WPM, Position, Burnouts, Accuracy)
 *
 * It then passes this configuration into the CompareMenu.
 */
class ConfigureCompareMenu extends Menu
{
    private ArrayList<TypistSimulation> allTypists;
    private ArrayList<TypistSimulation> selectedTypists;
    private HashMap<TypistSimulation, JCheckBox> typistCheckboxMap;
    private String selectedMetric;

    public ConfigureCompareMenu(GameInfo gameInformation)
    {
        super(gameInformation);

        this.allTypists = gameInformation.getTypists();
        this.selectedTypists = new ArrayList<>();
        this.typistCheckboxMap = new HashMap<>();
        this.selectedMetric = null;
    }

    /**
     * Builds the compare configuration UI.
     *
     * @param menus main card container
     * @param cards layout manager for menu switching
     * @param nextMenu name of next menu to display
     * @param results race results history used for comparisons
     * @return fully constructed configuration JPanel
     */
    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu,
                             ArrayList<PerformanceMetric> results)
    {
        // =========================
        // Base layout
        // =========================
        JPanel rootPanel = new JPanel(new BorderLayout());
        JPanel typistListPanel = new JPanel();
        typistListPanel.setLayout(new BoxLayout(typistListPanel, BoxLayout.Y_AXIS));

        // =========================
        // Build typist selection list
        // =========================
        for (TypistSimulation typist : allTypists)
        {
            JPanel typistRow = new JPanel(new BorderLayout());

            JPanel namePanel = new JPanel();
            HelperUtilities.addTitleBorder(namePanel, "Typist");

            JLabel nameLabel = new JLabel(
                    typist.getName() + " ( " + typist.getSymbol() + " )"
            );
            HelperUtilities.centerText(nameLabel);
            namePanel.add(nameLabel);

            JPanel selectPanel = new JPanel();
            HelperUtilities.addTitleBorder(selectPanel, "Tick");

            JCheckBox selectBox = new JCheckBox("Select Typist");
            selectPanel.add(selectBox);

            typistRow.add(namePanel, BorderLayout.CENTER);
            typistRow.add(selectPanel, BorderLayout.EAST);

            HelperUtilities.addPanelPadding(typistRow, 10);

            typistCheckboxMap.put(typist, selectBox);
            typistListPanel.add(typistRow);
        }

        JScrollPane typistScrollPane = new JScrollPane(typistListPanel);

        // =========================
        // Metric selection
        // =========================
        JRadioButton wpmButton = new JRadioButton("WPM");
        JRadioButton positionButton = new JRadioButton("Position");
        JRadioButton burnoutButton = new JRadioButton("Burnout Count");
        JRadioButton accuracyButton = new JRadioButton("True Accuracy");

        ButtonGroup metricGroup = new ButtonGroup();
        metricGroup.add(wpmButton);
        metricGroup.add(positionButton);
        metricGroup.add(burnoutButton);
        metricGroup.add(accuracyButton);

        JPanel metricPanel = new JPanel(new FlowLayout());
        metricPanel.add(wpmButton);
        metricPanel.add(positionButton);
        metricPanel.add(burnoutButton);
        metricPanel.add(accuracyButton);

        JLabel errorLabel = HelperUtilities.createError("Invalid Input");

        // =========================
        // Buttons
        // =========================
        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Submit");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        // =========================
        // Submit logic
        // =========================
        submitButton.addActionListener(e ->
        {
            selectedTypists.clear();
            selectedMetric = null;

            for (TypistSimulation typist : allTypists)
            {
                JCheckBox box = typistCheckboxMap.get(typist);
                if (box.isSelected())
                {
                    selectedTypists.add(typist);
                }
            }

            if (wpmButton.isSelected())
            {
                selectedMetric = "WPM";
            }
            else if (positionButton.isSelected())
            {
                selectedMetric = "POSITION";
            }
            else if (burnoutButton.isSelected())
            {
                selectedMetric = "BURNOUT_COUNT";
            }
            else if (accuracyButton.isSelected())
            {
                selectedMetric = "TRUE_ACCURACY";
            }

            if (selectedTypists.isEmpty() || selectedMetric == null)
            {
                rootPanel.add(errorLabel, BorderLayout.NORTH);
                HelperUtilities.refresh(rootPanel);
            }
            else
            {
                CompareMenu compareMenu =
                        new CompareMenu(getGameInfo(), selectedTypists, results);

                menus.add(compareMenu.createMenu(menus, cards, nextMenu, selectedMetric),
                          "COMPARE");

                if (rootPanel.isAncestorOf(errorLabel))
                {
                    rootPanel.remove(errorLabel);
                }

                cards.show(menus, "COMPARE");
            }
        });

        // =========================
        // Navigation
        // =========================
        backButton.addActionListener(e ->
                cards.show(menus, "RESULTS")
        );

        // =========================
        // Final assembly
        // =========================
        rootPanel.add(typistScrollPane, BorderLayout.CENTER);
        rootPanel.add(metricPanel, BorderLayout.SOUTH);

        container.add(rootPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        HelperUtilities.addTitle(container);

        return container;
    }
}

/**
 * CompareMenu displays a side-by-side comparison of selected typists
 * based on a chosen performance metric (WPM, Position, Burnout Count, or True Accuracy).
 *
 * It filters relevant PerformanceMetric objects and renders them in a scrollable UI.
 */
class CompareMenu extends Menu
{
    private ArrayList<PerformanceMetric> filteredMetrics;

    /**
     * Constructs the comparison view and filters relevant metrics.
     *
     * @param gameInformation shared game state
     * @param selectedTypists typists chosen for comparison
     * @param results full race results dataset
     */
    public CompareMenu(GameInfo gameInformation,
                       ArrayList<TypistSimulation> selectedTypists,
                       ArrayList<PerformanceMetric> results)
    {
        super(gameInformation);
        filteredMetrics = new ArrayList<>();
        setupComparisons(selectedTypists, results);
    }

    /**
     * Builds the comparison UI panel.
     *
     * @param menus card container
     * @param cards layout manager
     * @param nextMenu return menu identifier
     * @param selectedMetric metric type to display
     * @return constructed comparison JPanel
     */
    public JPanel createMenu(JPanel menus, CardLayout cards,
                             String nextMenu, String selectedMetric)
    {
        // =========================
        // Base layout
        // =========================
        JPanel rootPanel = new JPanel(new BorderLayout());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // =========================
        // Build comparison rows
        // =========================
        for (PerformanceMetric metric : filteredMetrics)
        {
            TypistSimulation typist = metric.getTypist();

            JPanel row = new JPanel(new BorderLayout());

            // ---- Name panel ----
            JPanel namePanel = new JPanel();
            HelperUtilities.addTitleBorder(namePanel, "Typist");

            JLabel nameLabel = new JLabel(
                    typist.getName() + " ( " + typist.getSymbol() + " )"
            );
            HelperUtilities.centerText(nameLabel);
            namePanel.add(nameLabel);

            // ---- Metric panel ----
            JPanel metricPanel = new JPanel();
            String padding = "                    ";

            if (selectedMetric.equals("WPM"))
            {
                HelperUtilities.addTitleBorder(metricPanel, "WPM");
                metricPanel.add(new JLabel(metric.getWPM() + padding));
            }
            else if (selectedMetric.equals("POSITION"))
            {
                HelperUtilities.addTitleBorder(metricPanel, "Position");
                metricPanel.add(new JLabel(metric.getPosition() + padding));
            }
            else if (selectedMetric.equals("BURNOUT_COUNT"))
            {
                HelperUtilities.addTitleBorder(metricPanel, "Burnout Count");
                metricPanel.add(new JLabel(metric.getBurnoutCount() + padding));
            }
            else if (selectedMetric.equals("TRUE_ACCURACY"))
            {
                HelperUtilities.addTitleBorder(metricPanel, "True Accuracy");
                metricPanel.add(new JLabel(metric.getTrueAccuracy() + padding));
            }

            row.add(namePanel, BorderLayout.WEST);
            row.add(metricPanel, BorderLayout.CENTER);

            listPanel.add(row);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);

        // =========================
        // Navigation
        // =========================
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e ->
                cards.show(menus, "COMPARE_CONFIGURE")
        );

        // =========================
        // Final assembly
        // =========================
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        container.add(rootPanel, BorderLayout.CENTER);
        container.add(backButton, BorderLayout.SOUTH);

        HelperUtilities.addTitle(container);

        return container;
    }

    /**
     * Filters results to only include selected typists.
     *
     * @param selectedTypists typists chosen by the user
     * @param results full results list
     */
    private void setupComparisons(ArrayList<TypistSimulation> selectedTypists,
                                  ArrayList<PerformanceMetric> results)
    {
        for (PerformanceMetric metric : results)
        {
            if (selectedTypists.contains(metric.getTypist()))
            {
                filteredMetrics.add(metric);
            }
        }
    }
}