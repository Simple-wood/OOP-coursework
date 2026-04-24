import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Menu
{
    protected JPanel container = new JPanel(new BorderLayout());
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
        HelperUtilities.addPanelPadding(content, 25);

        JPanel description = new JPanel(new BorderLayout());
        description.setBackground(Color.WHITE);

        JLabel error = HelperUtilities.createError("Invalid Input!");

        JTextArea text = new JTextArea();
        text.setLineWrap(true);
        JScrollPane scrollText = new JScrollPane(text);

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
        
        container.add(content, BorderLayout.CENTER);
        container.add(description, BorderLayout.WEST);
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
                   System.out.println(gameInformation.getPassage());
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

class TypistCountMenu extends Menu
{
    public TypistCountMenu(GameInfo gameInformation)
    {
        super(gameInformation);
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        JPanel container = new JPanel(new BorderLayout());
        JPanel content = new JPanel(new BorderLayout());
        JPanel numberWrapper = new JPanel(new BorderLayout());
        HelperUtilities.addTitle(container);
        JLabel error = HelperUtilities.createError("Invalid Input!");

        JButton submit = new JButton("Submit number of typists");

        JLabel numberText = new JLabel("Please enter the number of typists you would like (2-6) - ");
        HelperUtilities.centerText(numberText);
        HelperUtilities.addLabelPadding(numberText, 5);
        content.add(numberText, BorderLayout.NORTH);

        JTextField numberInput = new JTextField();
        numberWrapper.add(numberInput, BorderLayout.NORTH);
        HelperUtilities.addPanelPadding(numberWrapper, 15);

        submit.addActionListener(e -> {
            String response = numberInput.getText();
            GameInfo information = getGameInfo();

            if(HelperUtilities.isNumber(response))
            {
                int integerResponse = Integer.parseInt(response);

                if(integerResponse >= 2 && integerResponse <= 6)
                {
                    information.setNumberOfTypists(integerResponse);
                    cards.show(menus, nextMenu);
                }
                else{
                    content.add(error, BorderLayout.SOUTH);
                    HelperUtilities.refresh(content);             
                }

            }
            else{
                content.add(error, BorderLayout.SOUTH);
                HelperUtilities.refresh(content);
            }
        });

        content.add(numberWrapper, BorderLayout.CENTER);
        container.add(content, BorderLayout.CENTER);
        container.add(submit, BorderLayout.SOUTH);

        return container;
    }
}

class AddTypistMenu extends Menu
{
    private int currentNumberOfTypists = 0;
    private ArrayList<String> usedNames = new ArrayList<>();
    private ArrayList<Character> usedSymbols = new ArrayList<>();
    private ArrayList<TypistSimulation> typists = new ArrayList<>();

    public AddTypistMenu(GameInfo gameInformation)
    {
        super(gameInformation);
    }

    public JPanel createMenu(JPanel menus, CardLayout cards, String nextMenu)
    {
        GameInfo information = getGameInfo();

        JPanel container = new JPanel(new BorderLayout());
        HelperUtilities.addTitle(container);
        JPanel content = new JPanel(new BorderLayout());
        JPanel input = new JPanel();
        JLabel typistInfo = new JLabel("Typist no. " + (currentNumberOfTypists + 1));
        HelperUtilities.addLabelPadding(typistInfo, 10);

        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

        JLabel nameLabel =  new JLabel("Please enter the name of your typist - ");
        HelperUtilities.addLabelPadding(nameLabel, 10);
        JTextField typistName = new JTextField();

        JLabel symbolLabel =  new JLabel("Please enter the symbol of your typist - ");
        HelperUtilities.addLabelPadding(symbolLabel, 10);
        JTextField typistSymbol= new JTextField();

        JLabel accuracyLabel =  new JLabel("Please enter the accuracy of your typist (0-1) - ");
        HelperUtilities.addLabelPadding(accuracyLabel, 10);
        JTextField typistAccuracy = new JTextField();

        JButton submit = new JButton("Add typist");
        JLabel invalidInputError = HelperUtilities.createError("Invalid Input!");
        JLabel existingError = HelperUtilities.createError("There is an already existing typist registered with that name or symbol!");

        input.add(nameLabel);
        input.add(typistName);
        input.add(symbolLabel);
        input.add(typistSymbol);
        input.add(accuracyLabel);
        input.add(typistAccuracy);

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
                    TypistSimulation typist = new TypistSimulation(cSymbol, name, dAccuracy);
                    usedNames.add(name);
                    usedSymbols.add(cSymbol);
                    typists.add(typist);
                    currentNumberOfTypists++;

                    if(currentNumberOfTypists == information.getNumberOfTypists())
                    {
                        cards.show(menus, nextMenu);
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