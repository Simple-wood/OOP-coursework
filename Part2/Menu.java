import javax.swing.*;
import java.util.Collections;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class Menu
{
    protected JPanel container = new JPanel(new BorderLayout(25, 5));
    private GameInfo gameInformation;

    public Menu(GameInfo gameInformation)
    {
        this.gameInformation = gameInformation;
    }

    public GameInfo getGameInfo()
    {
        return gameInformation;
    }

    public void setGameInfo(GameInfo gameInfo)
    {
        gameInformation = gameInfo;
    }
}

class PassageMenu extends Menu
{
    public PassageMenu(GameInfo gameInformation)
    {
        super(gameInformation);
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        HelperUtilities.addTitle(container);
        JPanel options = new JPanel();
        JPanel radioButtons = new JPanel(new GridBagLayout());
        JPanel content = new JPanel(new BorderLayout());
        JPanel contentWrapper = new JPanel(new BorderLayout());
        JPanel checkPanel = new JPanel(new FlowLayout());
        JPanel typistNumberPanel = new JPanel(new BorderLayout());

        JCheckBox autoCorrect = new JCheckBox("Autocorrect mode");
        JCheckBox caffeine = new JCheckBox("Caffeine mode");
        JCheckBox night = new JCheckBox("Night mode");
        checkPanel.add(autoCorrect);
        checkPanel.add(caffeine);
        checkPanel.add(night);
        
        HelperUtilities.addPanelPadding(content, 25);

        JPanel description = new JPanel(new BorderLayout());
        description.setBackground(Color.WHITE);

        JLabel error = HelperUtilities.createError("Invalid Input!");

        JTextArea text = new JTextArea();
        text.setLineWrap(true);
        JScrollPane scrollText = new JScrollPane(text);

        JSpinner typistCount = new JSpinner(new SpinnerNumberModel(2, 2, 6, 1));
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) typistCount.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        HelperUtilities.addTextFieldPadding(spinnerEditor.getTextField(), 15);

        JLabel paddingLabel = new JLabel("                                    ");
        HelperUtilities.addLabelPadding(paddingLabel, 15);
        HelperUtilities.addTitleBorder(typistNumberPanel, "Seat Count");
        typistNumberPanel.add(typistCount, BorderLayout.NORTH);
        typistNumberPanel.add(paddingLabel, BorderLayout.SOUTH);

        JButton submit = new JButton("Submit passage");
        JRadioButton preMade = new JRadioButton("Choose from a list of pre-defined passages?");
        JRadioButton custom = new JRadioButton("Enter a custom passage?");
        ButtonGroup buttonOptions = new ButtonGroup();
        buttonOptions.add(preMade);
        buttonOptions.add(custom);
        radioButtons.add(preMade);
        radioButtons.add(custom);
        content.add(radioButtons, BorderLayout.NORTH);

        String[] passages = {"hello my name is Kishal and I have a little brother called Raayan", "The quick lazy fox jumps over the fence"};
        JList<String> listPassages = new JList<>(passages);
        listPassages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Ensures that you can only pick one passage at a time
        JScrollPane scroll = new JScrollPane(listPassages);
        options.add(scroll);

        preMade.addActionListener(e -> {
            if(content.isAncestorOf(scrollText))
            {
                content.remove(scrollText);
            }

            if(content.isAncestorOf(error))
            {
                content.remove(error);
            }

            content.add(options, BorderLayout.CENTER);
            HelperUtilities.refresh(content); 
        });

        custom.addActionListener(e -> {
            if(content.isAncestorOf(options))
            {
                content.remove(options);
            }

            if(content.isAncestorOf(error))
            {
                content.remove(error);
            }
            
            content.add(scrollText, BorderLayout.CENTER);
            HelperUtilities.refresh(content);
        });

        HelperUtilities.addTitleBorder(content, "Select Passage");
        HelperUtilities.addTitleBorder(checkPanel, "Difficulty Modifiers");

        contentWrapper.add(checkPanel, BorderLayout.SOUTH);
        contentWrapper.add(content, BorderLayout.CENTER);

        JLabel shortDescription = new JLabel("Short: 1-15 characters long!");
        HelperUtilities.centerText(shortDescription);
        HelperUtilities.addLabelPadding(shortDescription, 5);
        JLabel mediumDescription = new JLabel("Medium: 16-45 characters long!");
        HelperUtilities.centerText(mediumDescription);
        HelperUtilities.addLabelPadding(mediumDescription, 5);
        JLabel longDescription = new JLabel("Long: 46-70 characters long!");
        HelperUtilities.centerText(longDescription);
        HelperUtilities.addLabelPadding(longDescription, 5);

        description.add(shortDescription, BorderLayout.NORTH);
        description.add(mediumDescription, BorderLayout.CENTER);
        description.add(longDescription, BorderLayout.SOUTH);
        
        container.add(contentWrapper, BorderLayout.CENTER);
        container.add(typistNumberPanel, BorderLayout.WEST);
        container.add(submit, BorderLayout.SOUTH);

        submit.addActionListener(e ->{
            String buffer;
            GameInfo gameInformation = super.getGameInfo();

            if(content.isAncestorOf(scrollText))
            {
                buffer = text.getText();
                if(buffer.trim().isEmpty() || buffer.length() > 70)
                {
                    content.add(error, BorderLayout.SOUTH);
                    HelperUtilities.refresh(content);
                }
                else{
                   gameInformation.setPassage(buffer);
                   gameInformation.setNumberOfWords(HelperUtilities.countWords(buffer));
                   gameInformation.setNumberOfTypists((int) typistCount.getValue());

                   if(autoCorrect.isSelected())
                   {
                        gameInformation.activateAutoCorrect();
                   }

                   if(caffeine.isSelected())
                   {
                        gameInformation.activateCaffeine();
                   }

                   if(night.isSelected())
                   {
                        gameInformation.activateNight();
                   }

                   cards.show(menus, nextMenu);
                }
            }
            else if(content.isAncestorOf(options))
            {
                buffer = listPassages.getSelectedValue();
                if(buffer == null)
                {
                    content.add(error, BorderLayout.SOUTH);
                    HelperUtilities.refresh(content);
                }
                else{
                    gameInformation.setPassage(buffer);
                    gameInformation.setNumberOfWords(HelperUtilities.countWords(buffer));
                    gameInformation.setNumberOfTypists((int) typistCount.getValue());

                   if(autoCorrect.isSelected())
                   {
                        gameInformation.activateAutoCorrect();
                   }

                   if(caffeine.isSelected())
                   {
                        gameInformation.activateCaffeine();
                   }

                   if(night.isSelected())
                   {
                        gameInformation.activateNight();
                   }

                    cards.show(menus, nextMenu);
                }
            }
            else{
                content.add(error, BorderLayout.SOUTH);
                HelperUtilities.refresh(content);
            }
        });

        return container;
    }
}

class AddTypistMenu extends Menu
{
    private int currentNumberOfTypists = 0;
    private ArrayList<String> usedNames = new ArrayList<>();
    private ArrayList<Character> usedSymbols = new ArrayList<>();
    private ArrayList<TypistSimulation> typists = new ArrayList<>();
    private Timer timer;

    public AddTypistMenu(GameInfo gameInformation)
    {
        super(gameInformation);
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        GameInfo gameInformation = getGameInfo();
        HelperUtilities.addTitle(container);
        JPanel content = new JPanel(new BorderLayout());
        JPanel input = new JPanel();
        JLabel typistInfo = new JLabel("Typist no. " + (currentNumberOfTypists + 1));
        JLabel filler = new JLabel("                  ");
        HelperUtilities.addLabelPadding(typistInfo, 5);

        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(namePanel, "Enter Typist Name");
        JTextField typistName = new JTextField();
        namePanel.add(typistName, BorderLayout.CENTER);

        JPanel symbolPanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(symbolPanel, "Enter Typist Symbol");
        JTextField typistSymbol = new JTextField();
        symbolPanel.add(typistSymbol, BorderLayout.CENTER);

        JPanel colourPanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(colourPanel, "Choose Colour");
        JButton colourButton = new JButton(" ");
        colourButton.setBackground(Color.GREEN);
        colourPanel.add(filler, BorderLayout.SOUTH);
        colourPanel.add(colourButton, BorderLayout.CENTER);

        colourButton.addActionListener(e ->{
            Color chosenColour = JColorChooser.showDialog(null, "Choose Colour", Color.GREEN);

            if(chosenColour != null)
            {
                colourButton.setBackground(chosenColour);
            }
        });

        JPanel accuracyPanel = new JPanel(new BorderLayout());
        HelperUtilities.addTitleBorder(accuracyPanel, "Enter Typist Accuracy (0 - 1)");
        JTextField typistAccuracy = new JTextField();
        accuracyPanel.add(typistAccuracy, BorderLayout.CENTER);

        JPanel typingStylePanel = new JPanel(new BorderLayout());
        JTextArea typingStyleInfo = new JTextArea("Touch Typist - 1.1x accuracy, burnout chance cap lifted to 12%" + 
                "\nHunt & Peck - 1.2x accuracy, burnout duration decreased to 2 turns" +
                "\nPhone Thumbs - 0.9x accuracy, burnout chance cap decreased tp 2%" + 
                "\nVoice-to-Text - 0.7x accuracy, burnout duration increased to 5 turns");

        typingStyleInfo.setEditable(false);
        HelperUtilities.addTextAreaPadding(typingStyleInfo, 10);

        HelperUtilities.addTitleBorder(typingStylePanel, "Select Typing Style");
        String[] typingStyles = {"None", "Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
        JComboBox<String> typingStyleOptions = new JComboBox<>(typingStyles);
        typingStylePanel.add(typingStyleOptions, BorderLayout.CENTER);
        typingStylePanel.add(typingStyleInfo, BorderLayout.SOUTH);

        JPanel keyboardPanel = new JPanel(new BorderLayout());
        JTextArea keyboardInfo = new JTextArea("Mechanical - 3x typing speed, mistype chance cap lifted to 40%" +
            "\nMembrane - 2x typing speed, mistype chance cap lifted to 35%" +
            "\nTouchscreem - mistype chance cap decreased to 20%" +
            "\nStenography - 5x typing speed, mistype chance cap lifed to 60%");
        
        keyboardInfo.setEditable(false);
        HelperUtilities.addTextAreaPadding(keyboardInfo, 10);

        HelperUtilities.addTitleBorder(keyboardPanel, "Select Keyboard");
        String[] keyboards = {"None", "Mechanical", "Membrane", "Touchscreen", "Stenography"};
        JComboBox<String> keyboardOptions = new JComboBox<>(keyboards);
        keyboardPanel.add(keyboardOptions, BorderLayout.CENTER);
        keyboardPanel.add(keyboardInfo, BorderLayout.SOUTH);

        JPanel accessories = new JPanel(new BorderLayout());
        JPanel checkButtons = new JPanel(new FlowLayout());
        JTextArea accessoriesInfo = new JTextArea("Wrist Support - 0.5x burnout duration" +
            "\nEnergy Drink - 2x original accuracy for first 15 turns, 0.5x accuracy afterwards" +
            "\nHeadPhones - 0.5x the mistype chance cap");

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

        JButton submit = new JButton("Add typist");
        JLabel invalidInputError = HelperUtilities.createError("Invalid Input!");
        JLabel existingError = HelperUtilities.createError("There is an already existing typist registered with that name or symbol!");

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

        submit.addActionListener(e -> {
            String name = typistName.getText();
            String symbol = typistSymbol.getText();
            String accuracy = typistAccuracy.getText();
            
            if(validTypistFields(name, symbol, accuracy))
            {
                Double dAccuracy = Double.parseDouble(accuracy);
                char cSymbol = symbol.charAt(0);

                if(! usedNames.contains(name) && ! usedSymbols.contains(cSymbol))
                {
                    boolean[] accessoriesChoice = {false, false, false};

                    if(wristSupport.isSelected())
                    {
                        accessoriesChoice[0] = true;
                    }

                    if(energyDrink.isSelected())
                    {
                        accessoriesChoice[1] = true;
                    }

                    if(headphones.isSelected())
                    {
                        accessoriesChoice[2] = true;
                    }

                    String typingProfile = (String) typingStyleOptions.getSelectedItem();
                    String keyboard = (String) keyboardOptions.getSelectedItem();
                    Color colour = colourButton.getBackground();
                    TypistSimulation typist = new TypistSimulation(cSymbol, name, dAccuracy, typingProfile, keyboard, colour, accessoriesChoice);

                    usedNames.add(name);
                    usedSymbols.add(cSymbol);
                    typists.add(typist);
                    currentNumberOfTypists++;

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
                                menus.add(resultsMenu.createMenu(menus, cards, "GAME", timer, racingHistories), "RESULTS");
                                cards.show(menus, "RESULTS");
                            }

                        });

                        timer.start();
                    }

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
                else{
                    if(content.isAncestorOf(invalidInputError))
                    {
                        content.remove(invalidInputError);
                    }

                    content.add(existingError, BorderLayout.SOUTH);
                    HelperUtilities.refresh(content);
                }
            }
            else{
                if(content.isAncestorOf(existingError))
                {
                    content.remove(existingError);
                }
                content.add(invalidInputError, BorderLayout.SOUTH);
                HelperUtilities.refresh(content);               
            }
        });

        container.add(content, BorderLayout.CENTER);
        container.add(submit, BorderLayout.SOUTH);

        return container;
    }

    private boolean validTypistFields(String name, String symbol, String accuracy)
    {
        if(name.trim().isEmpty() || !HelperUtilities.isChar(symbol) || !HelperUtilities.isDouble(accuracy))
        {
            return false;
        }

        double dAccuracy = Double.parseDouble(accuracy);
        if(dAccuracy < 0 || dAccuracy > 1)
        {
            return false;
        }

        return true;
    }
}

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

    public SimulationMenu(GameInfo gameInformation)
    {
        super(gameInformation);
        trackMap = new HashMap<>();
        passage = gameInformation.getPassage();
        passageWordCount = gameInformation.getNumberOfWords();
        passageLength = passage.length();
        tracks = new JPanel[gameInformation.getNumberOfTypists()];
        
        boolean[] modes = {gameInformation.isAutoCorrect(), gameInformation.isCaffeine(), gameInformation.isNight()};
        simulation = new TypingRaceSimulation(passageLength - 1, gameInformation.getTypists(), modes);
    }

    public void setupSimulation()
    {
       simulation.resetTypists(); 
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {   
        simulation.configureGame();
        JPanel content = new JPanel(new BorderLayout());
        JPanel simulationP = new JPanel(new BorderLayout());
        simulationP.setLayout(new BoxLayout(simulationP, BoxLayout.Y_AXIS));

        GameInfo gameInformation = getGameInfo();
        playerinformations = new JTextArea[gameInformation.getNumberOfTypists()];
        ArrayList<TypistSimulation> typists = gameInformation.getTypists();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        int gridLength = 30;
        if(passageLength <  gridLength)
        {
            gridLength = passageLength;
        }

        for(int i = 0; i < gameInformation.getNumberOfTypists(); i++)
        {
            JPanel track = new JPanel(new GridLayout(0, gridLength, 5, 5));
            JTextArea[] gridCells = new JTextArea[passageLength];
            TypistSimulation typist = typists.get(i);
            
            JTextArea cell = new JTextArea(2, 1);
            cell.setText(typist.getSymbol() + "\n" + passage.charAt(0));
            cell.setEditable(false);
            HelperUtilities.addTextAreaPadding(cell, 7);
            track.add(cell);
            gridCells[0] = cell;

            for(int j = 1; j < passageLength; j++)
            {
                JTextArea cellN = new JTextArea(2, 1);
                cellN.setText("\n" + passage.charAt(j));
                cellN.setEditable(false);
                HelperUtilities.addTextAreaPadding(cellN, 7);
                track.add(cellN);
                gridCells[j] = cellN;
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

    public void updateSimulation(ArrayList<PerformanceMetric> results, HashMap<TypistSimulation, RacingHistory> racingHistories)
    {
        ArrayList<TypistSimulation> typists = getGameInfo().getTypists();

        for(int i = 0; i < getGameInfo().getNumberOfTypists(); i++)
        {
            TypistSimulation typist = typists.get(i);
            int progressBefore = typist.getProgress();
            simulation.advanceTypist(typist);
            int progress = typist.getProgress();
            JPanel track = tracks[i];
            JTextArea[] gridCells = trackMap.get(track);

            if(progressBefore != progress)
            {
                JTextArea currentCell = gridCells[progress];
                int currentMaxProgress;

                if(progressBefore > progress)
                {
                    currentMaxProgress = progressBefore;
                }
                else{
                    currentMaxProgress = progress;
                }

                for(int j = 0; j <= currentMaxProgress; j++)
                {
                    JTextArea coveredCell = gridCells[j];
                    coveredCell.setBackground(Color.WHITE);
                    coveredCell.setText("\n" + passage.charAt(j));

                    if(j <= progress)
                    {
                        coveredCell.setForeground(typist.getColour());
                    }
                    else{
                        coveredCell.setForeground(Color.BLACK);
                    }
                }
                
                currentCell.setText(typist.getSymbol() + "\n" + passage.charAt(progress));
                currentCell.setBackground(Color.YELLOW);
            }

            JTextArea playerInfo = playerinformations[i];
            setPlayerInfo(playerInfo, typist);
            
            if(typist.isBurntOut())
            {
                gridCells[progress].setForeground(Color.RED);
                gridCells[progress].setBackground(Color.WHITE);

                playerInfo.append("BURNT OUT (" + typist.getBurnoutTurnsRemaining() + " turns) ");
                playerInfo.setForeground(Color.RED);

            }

            else if(typist.isMistyped())
            {
                gridCells[progress].setForeground(Color.RED);   

                playerInfo.append("← just mistyped           ");
                playerInfo.setForeground(Color.RED);
            }

            else{
                gridCells[progress].setForeground(Color.BLACK);
                gridCells[progress].setBackground(Color.YELLOW);

                playerInfo.append("                                     ");
                playerInfo.setForeground(Color.BLACK);
                
            }

            if(simulation.raceFinishedBy(typist))
            {
                gridCells[progress].setBackground(Color.WHITE);
                gridCells[progress].setForeground(typist.getColour());
            }

            if(simulation.raceConcluded())
            {
                isFinished = true;
            }
        }

        if(isFinished)
        {
            simulation.updateAndGetResults(results, passageWordCount, 0.2);

            Iterator<PerformanceMetric> resultsIterator = results.iterator();
            PerformanceMetric currentMetrics = null;

            while(resultsIterator.hasNext())
            {
                currentMetrics = resultsIterator.next();
                TypistSimulation typist = currentMetrics.getTypist();

                if(racingHistories.containsKey(typist))
                {
                    RacingHistory history = racingHistories.get(typist);
                    history.addMetric(currentMetrics);
                }
                else{
                    RacingHistory history = new RacingHistory(currentMetrics);
                    racingHistories.put(typist, history);
                }
            }

        }
    }

    private void setPlayerInfo(JTextArea playerInfo, TypistSimulation typist)
    {
        playerInfo.setText(typist.getName() + " ( " + typist.getSymbol() + " )" + "\n" + "(Accuracy: " + typist.getAccuracy() + ")" + "\n");
    }

    public boolean getFinished()
    {
        return isFinished;
    }

    public void finishedRace()
    {
        simulation.restartRace();
        clearStates();
        isFinished = false;
    }

    private void clearStates()
    {
        GameInfo gameInformation = getGameInfo();
        for(int i = 0; i < gameInformation.getNumberOfTypists(); i++)
        {
            JPanel track = tracks[i];
            JTextArea[] gridCells = trackMap.get(track);

            for(int j = 0; j < gridCells.length; j++)
            {
                JTextArea currentCell = gridCells[j];
                currentCell.setForeground(Color.BLACK);
                currentCell.setBackground(Color.WHITE);
                currentCell.setText("\n" + passage.charAt(j));
            }
        }
    }
}

class ResultsMenu extends Menu
{
    private ArrayList<PerformanceMetric> results;
    private HashMap<JButton, TypistSimulation> buttonTypistMap = new HashMap<>();

    public ResultsMenu(GameInfo gameInformation, ArrayList<PerformanceMetric> results)
    {
        super(gameInformation);
        this.results = results;
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu, Timer timer, HashMap<TypistSimulation, RacingHistory> racingHistories)
    {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        Iterator<PerformanceMetric> resultsIterator = results.iterator();
        PerformanceMetric currentResult = null;

        while(resultsIterator.hasNext())
        {
            currentResult = resultsIterator.next();
            JPanel track = new JPanel(new BorderLayout(10, 10));
            JPanel trackContent = new JPanel(new BorderLayout());
            JPanel stats = new JPanel();
            stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
            JLabel position = new JLabel(currentResult.getPosition() + ". " + currentResult.getTypist().getName());
            JPanel positionPanel = new JPanel(new BorderLayout());
            JLabel positionPadding = new JLabel("                   ");
            positionPanel.add(position, BorderLayout.CENTER);
            positionPanel.add(positionPadding, BorderLayout.SOUTH);
            positionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            HelperUtilities.centerText(position);

            track.add(positionPanel, BorderLayout.WEST);

            JPanel wpmPanel = new JPanel();
            HelperUtilities.addTitleBorder(wpmPanel, "WPM");
            JLabel wpm = new JLabel("" + currentResult.getWPM());
            wpmPanel.add(wpm);
            stats.add(wpmPanel);

            JPanel tAcuuracyPanel = new JPanel();
            HelperUtilities.addTitleBorder(tAcuuracyPanel, "True Accuracy");
            JLabel tAccuracy = new JLabel("" + currentResult.getTrueAccuracy());
            tAcuuracyPanel.add(tAccuracy);
            stats.add(tAcuuracyPanel);

            JPanel aChangePanel = new JPanel();
            HelperUtilities.addTitleBorder(aChangePanel, "Accuracy Change");
            JLabel aChange = new JLabel("" + currentResult.getAccuracyChange());
            aChangePanel.add(aChange);
            stats.add(aChangePanel);

            JPanel burnoutCountPanel = new JPanel();
            HelperUtilities.addTitleBorder(burnoutCountPanel, "Number Of Burnouts");
            JLabel burnoutCount = new JLabel("" + currentResult.getBurnoutCount());
            burnoutCountPanel.add(burnoutCount);
            stats.add(burnoutCountPanel);

            JButton raceHistory = new JButton("Race History");
            buttonTypistMap.put(raceHistory, currentResult.getTypist());

            raceHistory.addActionListener(e -> {
                TypistSimulation selectedTypist = buttonTypistMap.get(raceHistory);
                RacingHistory history = racingHistories.get(selectedTypist);

                HistoryMenu historyMenu = new HistoryMenu(history, selectedTypist, getGameInfo());
                menus.add(historyMenu.createMenu(menus, cards, "RESULTS"), "HISTORY");

                cards.show(menus, "HISTORY");
            });


            trackContent.add(stats, BorderLayout.CENTER);
            track.add(trackContent, BorderLayout.CENTER);
            track.add(raceHistory, BorderLayout.EAST);
            HelperUtilities.addPanelPadding(track, 10);
            content.add(track);
        }

        JPanel buttonGroup = new JPanel(new FlowLayout());
        JButton restart = new JButton("Race again");
        JButton compare = new JButton("Compare Typists");
        JButton quit = new JButton("Quit");
        buttonGroup.add(restart);
        buttonGroup.add(compare);
        buttonGroup.add(quit);

        quit.addActionListener(e ->{
            System.exit(0);
        });

        restart.addActionListener(e ->{
            cards.show(menus, nextMenu);
            timer.start();
        });

        compare.addActionListener(e ->{
            ConfigureCompareMenu comparisionConfigure = new ConfigureCompareMenu(getGameInfo());
            menus.add(comparisionConfigure.createMenu(menus, cards, nextMenu, results), "COMPARE_CONFIGURE");

            cards.show(menus, "COMPARE_CONFIGURE");
        });

        JScrollPane scrollContent = new JScrollPane(content);
        container.add(scrollContent, BorderLayout.CENTER);
        container.add(buttonGroup, BorderLayout.SOUTH);
        HelperUtilities.addTitle(container);
        return container;
    }
}

class HistoryMenu extends Menu
{
    RacingHistory typistHistory;
    TypistSimulation typist;

    public HistoryMenu(RacingHistory typistHistory, TypistSimulation typist, GameInfo gameInformation)
    {
        super(gameInformation);
        this.typistHistory = typistHistory;
        this.typist = typist;
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        JPanel content = new JPanel(new BorderLayout());
        JPanel historyContent = new JPanel();
        historyContent.setLayout(new BoxLayout(historyContent, BoxLayout.Y_AXIS));
        HelperUtilities.addTitleBorder(historyContent, "Racing History");
        ArrayList<PerformanceMetric> races = new ArrayList<>(typistHistory.getRaceHistory());
        Collections.reverse(races);
        Iterator<PerformanceMetric> racesIterator = races.iterator();
        PerformanceMetric currentRace = null;

        JPanel typistInfo = new JPanel();
        typistInfo.setLayout(new BoxLayout(typistInfo, BoxLayout.Y_AXIS));
        HelperUtilities.addTitleBorder(typistInfo, "Typist");
        JLabel name = new JLabel(typist.getName() + " (" + typist.getSymbol() + ")     ");
        JLabel wpmPR = new JLabel("WPM PR - " + typistHistory.getBestWPM());
        JLabel accuracy = new JLabel("Accuracy - " + typist.getAccuracy());
        
        double overallAccuracyChange = HelperUtilities.truncate(3, typist.getAccuracy() - typistHistory.getRaceHistory().get(0).getAccuracy());
        JLabel change;
        if(overallAccuracyChange < 0.0)
        {
            change = new JLabel("-" + overallAccuracyChange);
            change.setForeground(Color.RED);
        }
        else{
            change = new JLabel("+" + overallAccuracyChange);
            change.setForeground(Color.GREEN);            
        }

        typistInfo.add(name);
        typistInfo.add(wpmPR);
        typistInfo.add(accuracy);
        typistInfo.add(change);

        content.add(typistInfo, BorderLayout.WEST);

        while(racesIterator.hasNext())
        {
            JPanel race = new JPanel(new FlowLayout());
            currentRace = racesIterator.next();

            JPanel racePosPanel = new JPanel();
            HelperUtilities.addTitleBorder(racePosPanel, "Position");
            JLabel racePos = new JLabel("" + currentRace.getPosition() + "                          ");
            racePosPanel.add(racePos);

            JPanel accuracyPanel = new JPanel();
            HelperUtilities.addTitleBorder(accuracyPanel, "Accuracy");
            accuracy = new JLabel("" + currentRace.getAccuracy() + "                          ");
            accuracyPanel.add(accuracy);

            JPanel trueAccuracyPanel = new JPanel();
            HelperUtilities.addTitleBorder(trueAccuracyPanel, "True Accuracy");
            JLabel trueAccuracy = new JLabel("" + currentRace.getTrueAccuracy() + "                          ");
            trueAccuracyPanel.add(trueAccuracy);

            JPanel wpmPanel = new JPanel();
            HelperUtilities.addTitleBorder(wpmPanel, "WPM");
            JLabel wpm = new JLabel("" + currentRace.getWPM() + "                          ");
            wpmPanel.add(wpm);

            JPanel changePanel = new JPanel();
            HelperUtilities.addTitleBorder(changePanel, "Change In Accuracy");
            JLabel changeAccuracy = new JLabel("" + currentRace.getAccuracyChange() + "                          ");
            changePanel.add(changeAccuracy);

            race.add(racePosPanel);
            race.add(wpmPanel);
            race.add(accuracyPanel);
            race.add(trueAccuracyPanel);
            race.add(changePanel);

            historyContent.add(race);
        }

        JButton exit = new JButton("Back");
        exit.addActionListener(e -> {
            cards.show(menus, nextMenu);
        });

        JScrollPane scrollHistory = new JScrollPane(historyContent);
        content.add(scrollHistory, BorderLayout.CENTER);
        container.add(content, BorderLayout.CENTER);
        container.add(exit, BorderLayout.SOUTH);
        HelperUtilities.addTitle(container);

        return container;
    }
}

class ConfigureCompareMenu extends Menu
{
    private ArrayList<TypistSimulation> typists;
    private ArrayList<TypistSimulation> selectedTypists;
    private HashMap<TypistSimulation, JCheckBox> typistButtonMap;
    private String selectedMetric;

    public ConfigureCompareMenu(GameInfo gameInformation)
    {
        super(gameInformation);
        typists = gameInformation.getTypists();
        selectedTypists = new ArrayList<>();
        typistButtonMap = new HashMap<>();
        selectedMetric = null; 
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu, ArrayList<PerformanceMetric> results)
    {
        JPanel content = new JPanel(new BorderLayout());
        JPanel typistsContent = new JPanel();
        typistsContent.setLayout(new BoxLayout(typistsContent, BoxLayout.Y_AXIS));

        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            JPanel typistInfo = new JPanel(new BorderLayout());
            JPanel nameWrapper = new JPanel();
            HelperUtilities.addTitleBorder(nameWrapper, "Typist");
            JLabel typistName = new JLabel(currentTypist.getName() + " ( " + currentTypist.getSymbol() + " )                     ");
            HelperUtilities.centerText(typistName);
            nameWrapper.add(typistName);

            JPanel checkPanel = new JPanel();
            HelperUtilities.addTitleBorder(checkPanel, "Tick");
            JCheckBox select = new JCheckBox("Select Typist");
            checkPanel.add(select);

            typistInfo.add(nameWrapper, BorderLayout.CENTER);
            typistInfo.add(checkPanel, BorderLayout.EAST);

            HelperUtilities.addPanelPadding(typistInfo, 10);

            typistButtonMap.put(currentTypist, select);

            typistsContent.add(typistInfo);
        }

        JRadioButton wpm = new JRadioButton("WPM");
        JRadioButton position = new JRadioButton("Position");
        JRadioButton burnoutCount = new JRadioButton("Burnout Count");
        JRadioButton trueAccuracy = new JRadioButton("True Accuracy");
        JLabel error = HelperUtilities.createError("Invalid Input");
        ButtonGroup metrics = new ButtonGroup();
        metrics.add(wpm);
        metrics.add(position);
        metrics.add(burnoutCount);
        metrics.add(trueAccuracy);
        JPanel radioButtons = new JPanel(new FlowLayout());
        radioButtons.add(wpm);
        radioButtons.add(position);
        radioButtons.add(burnoutCount);
        radioButtons.add(trueAccuracy);

        JButton previousMenu = new JButton("Back");
        JButton submit = new JButton("Submit");
        JPanel buttons = new JPanel(new FlowLayout());

        submit.addActionListener(e ->{
            Iterator<TypistSimulation> iteratorTypist = typists.iterator();
            TypistSimulation selectTypist = null;

            while(iteratorTypist.hasNext())
            {
               selectTypist = iteratorTypist.next();
               JCheckBox check = typistButtonMap.get(selectTypist);

               if(check.isSelected())
               {
                selectedTypists.add(selectTypist);
               }
            }

            if(wpm.isSelected())
            {
                selectedMetric = "WPM";
            }
            else if(position.isSelected())
            {
                selectedMetric = "POSITION";
            }
            else if(burnoutCount.isSelected())
            {
                selectedMetric = "BURNOUT_COUNT";
            }
            else if(trueAccuracy.isSelected())
            {
                selectedMetric = "TRUE_ACCURACY";
            }

            if(selectedTypists.isEmpty() || selectedMetric == null)
            {
                content.add(error, BorderLayout.NORTH);
                HelperUtilities.refresh(content);
            }
            else{
                CompareMenu compareMenu = new CompareMenu(getGameInfo(), selectedTypists, results);
                menus.add(compareMenu.createMenu(menus, cards, nextMenu, selectedMetric), "COMPARE");
                selectedMetric = null;
                selectedTypists.clear();

                if(content.isAncestorOf(error))
                {
                    content.remove(error);
                    HelperUtilities.refresh(content);
                }
                cards.show(menus, "COMPARE");
            }
        });

        previousMenu.addActionListener(e ->{
            cards.show(menus, "RESULTS");
        });

        buttons.add(previousMenu);
        buttons.add(submit);

        JScrollPane typistScroll = new JScrollPane(typistsContent);

        content.add(typistScroll, BorderLayout.CENTER); 
        content.add(radioButtons, BorderLayout.SOUTH);  
        container.add(content, BorderLayout.CENTER);
        container.add(buttons, BorderLayout.SOUTH);
        HelperUtilities.addTitle(container);
        
        return container;
    }
}

class CompareMenu extends Menu
{
    ArrayList<PerformanceMetric> selectedTypistMetrics;

    public CompareMenu(GameInfo gameInformation, ArrayList<TypistSimulation> selectedTypists, ArrayList<PerformanceMetric> results)
    {
        super(gameInformation);
        selectedTypistMetrics = new ArrayList<>();
        setupComparisions(selectedTypists, results);
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu, String selectedMetric)
    {
        JPanel content = new JPanel(new BorderLayout());
        JPanel typistsContent = new JPanel();
        typistsContent.setLayout(new BoxLayout(typistsContent, BoxLayout.Y_AXIS));
        PerformanceMetric currentMetrics = null;
        Iterator<PerformanceMetric> metricsIterator = selectedTypistMetrics.iterator();

        while(metricsIterator.hasNext())
        {
            currentMetrics = metricsIterator.next();
            TypistSimulation currentTypist = currentMetrics.getTypist();
            JPanel typistInfo = new JPanel(new BorderLayout());

            JPanel namePanel = new JPanel();
            HelperUtilities.addTitleBorder(namePanel, "Typist");
            JLabel typistName = new JLabel(currentTypist.getName() + " ( " + currentTypist.getSymbol() + " )                     ");
            HelperUtilities.centerText(typistName);
            namePanel.add(typistName);

            String padding = "                    ";

            JPanel metric = new JPanel();

            if(selectedMetric.equals("WPM"))
            {
                HelperUtilities.addTitleBorder(metric, "WPM");
                JLabel metricLabel = new JLabel(currentMetrics.getWPM() + padding);
                metric.add(metricLabel);
            }

            else if(selectedMetric.equals("POSITION"))
            {
                HelperUtilities.addTitleBorder(metric, "Position");
                JLabel metricLabel = new JLabel(currentMetrics.getPosition() + padding);
                metric.add(metricLabel);
            }
            else if(selectedMetric.equals("BURNOUT_COUNT"))
            {
                HelperUtilities.addTitleBorder(metric, "Burnout Count");
                JLabel metricLabel = new JLabel(currentMetrics.getBurnoutCount() + padding);
                metric.add(metricLabel);
            }
            else if(selectedMetric.equals("TRUE_ACCURACY"))
            {
                HelperUtilities.addTitleBorder(metric, "True Accuracy");
                JLabel metricLabel = new JLabel(currentMetrics.getTrueAccuracy() + padding);
                metric.add(metricLabel);
            }

            typistInfo.add(namePanel, BorderLayout.WEST);
            typistInfo.add(metric, BorderLayout.CENTER);
            typistsContent.add(typistInfo);
        }

        JButton button = new JButton("Back");
        button.addActionListener(e ->{
            cards.show(menus, "COMPARE_CONFIGURE");
        });

        JScrollPane typistScroll = new JScrollPane(typistsContent);

        content.add(typistScroll, BorderLayout.CENTER);
        container.add(content, BorderLayout.CENTER);
        container.add(button, BorderLayout.SOUTH);

        HelperUtilities.addTitle(container);

        return container;
    }

    private void setupComparisions(ArrayList<TypistSimulation> selectedTypists, ArrayList<PerformanceMetric> results)
    {
        PerformanceMetric currentMetric = null;
        //System.out.println(results.get(0));
        Iterator<PerformanceMetric> metricsIterator = results.iterator();

        while(metricsIterator.hasNext())
        {
            currentMetric = metricsIterator.next();
            if(selectedTypists.contains(currentMetric.getTypist()))
            {
                selectedTypistMetrics.add(currentMetric);
            }
        }
    }
}