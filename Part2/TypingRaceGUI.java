import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TypingRaceGUI
{
    private String passage = null;
    private int typistCount = 0;
    private int activeTypistCount = 1;
    private TypistSimulation[] typists = null;  
    private CardLayout cards = new CardLayout();
    private JPanel menus = new JPanel(cards);
    public static void main(String[] args)
    {
        TypingRaceGUI menu = new TypingRaceGUI();
        menu.configureMenu();
    }

    public void configureMenu()
    {
        JFrame window = new JFrame("Typing Race Simulator!");
        JPanel menuPanel = PassageMenu();
        JPanel numberPanel = TypistCountMenu();
        JPanel configureTypistPanel = configureTypistMenu();

        menus.add(menuPanel, "MENU");
        menus.add(numberPanel, "NUMBER");
        menus.add(configureTypistPanel, "CONFIGURE");
        window.add(menus);

        cards.show(menus, "MENU");

        window.setSize(300, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private JPanel PassageMenu()
    {
        JPanel container = new JPanel(new BorderLayout());
        addTitle(container);
        JPanel options = new JPanel();
        JPanel radioButtons = new JPanel(new GridBagLayout());
        JPanel content = new JPanel(new BorderLayout());
        addPanelPadding(content, 25);

        JPanel description = new JPanel(new BorderLayout());
        description.setBackground(Color.WHITE);

        JLabel error = createError("Invalid Input!");

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
            refresh(content); 
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
            refresh(content);
        });

        JLabel shortDescription = new JLabel("Short: 1-15 characters long!");
        centerText(shortDescription);
        addLabelPadding(shortDescription, 5);
        JLabel mediumDescription = new JLabel("Medium: 16-45 characters long!");
        centerText(mediumDescription);
        addLabelPadding(mediumDescription, 5);
        JLabel longDescription = new JLabel("Long: 46-70 characters long!");
        centerText(longDescription);
        addLabelPadding(longDescription, 5);

        description.add(shortDescription, BorderLayout.NORTH);
        description.add(mediumDescription, BorderLayout.CENTER);
        description.add(longDescription, BorderLayout.SOUTH);
        
        container.add(content, BorderLayout.CENTER);
        container.add(description, BorderLayout.WEST);
        container.add(submit, BorderLayout.SOUTH);

        submit.addActionListener(e ->{
            String buffer;

            if(content.isAncestorOf(scrollText))
            {
                buffer = text.getText();
                if(buffer.trim().isEmpty() || buffer.length() > 70)
                {
                    content.add(error, BorderLayout.SOUTH);
                    refresh(content);
                }
                else{
                   passage = buffer;
                   cards.show(menus, "NUMBER");
                }
            }
            else if(content.isAncestorOf(options))
            {
                buffer = listPassages.getSelectedValue();
                if(buffer == null)
                {
                    content.add(error, BorderLayout.SOUTH);
                    refresh(content);
                }
                else{
                    passage = buffer;
                    cards.show(menus, "NUMBER");
                }
            }
            else{
                content.add(error, BorderLayout.SOUTH);
                refresh(content);
            }
        });

        return container;
    }

    private JPanel TypistCountMenu()
    {
        JPanel container = new JPanel(new BorderLayout());
        JPanel content = new JPanel(new BorderLayout());
        JPanel numberWrapper = new JPanel(new BorderLayout());
        addTitle(container);
        JLabel error = createError("Invalid Input!");

        JButton submit = new JButton("Submit number of typists");

        JLabel numberText = new JLabel("Please enter the number of typists you would like (2-6) - ");
        centerText(numberText);
        addLabelPadding(numberText, 5);
        content.add(numberText, BorderLayout.NORTH);

        JTextField numberInput = new JTextField();
        numberWrapper.add(numberInput, BorderLayout.NORTH);
        addPanelPadding(numberWrapper, 15);

        submit.addActionListener(e -> {
            String response = numberInput.getText();

            if(HelperUtilities.isNumber(response))
            {
                int integerResponse = Integer.parseInt(response);

                if(integerResponse >= 2 && integerResponse <= 6)
                {
                    typistCount = integerResponse;
                    cards.show(menus, "CONFIGURE");
                }
                else{
                    content.add(error, BorderLayout.SOUTH);
                    refresh(content);             
                }

            }
            else{
                content.add(error, BorderLayout.SOUTH);
                refresh(content);
            }
        });

        content.add(numberWrapper, BorderLayout.CENTER);
        container.add(content, BorderLayout.CENTER);
        container.add(submit, BorderLayout.SOUTH);

        return container;
    }

    private JPanel configureTypistMenu()
    {
        ArrayList<String> usedNames = new ArrayList<>();
        ArrayList<Character> usedSymbols = new ArrayList<>();

        JPanel container = new JPanel(new BorderLayout());
        addTitle(container);
        JPanel content = new JPanel(new BorderLayout());
        JPanel input = new JPanel();
        JLabel typistInfo = new JLabel("Typist no. " + activeTypistCount);
        addLabelPadding(typistInfo, 10);

        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

        JLabel nameLabel =  new JLabel("Please enter the name of your typist - ");
        addLabelPadding(nameLabel, 10);
        JTextField typistName = new JTextField();

        JLabel symbolLabel =  new JLabel("Please enter the symbol of your typist - ");
        addLabelPadding(symbolLabel, 10);
        JTextField typistSymbol= new JTextField();

        JLabel accuracyLabel =  new JLabel("Please enter the accuracy of your typist (0-1) - ");
        addLabelPadding(accuracyLabel, 10);
        JTextField typistAccuracy = new JTextField();

        JButton submit = new JButton("Add typist");
        JLabel invalidInputError = createError("Invalid Input!");
        JLabel existingError = createError("There is an already existing typist registered with that name or symbol!");

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
                    usedNames.add(name);
                    usedSymbols.add(cSymbol);

                    if(content.isAncestorOf(invalidInputError))
                    {
                        content.remove(invalidInputError);
                    }
                    else if(content.isAncestorOf(existingError))
                    {
                        content.remove(existingError);
                    }

                    activeTypistCount++;
                    typistInfo.setText("Typist no. " + activeTypistCount);
                    typistName.setText("");
                    typistAccuracy.setText("");
                    typistSymbol.setText("");
                    
                    refresh(content);
                }
                else{
                    if(content.isAncestorOf(invalidInputError))
                    {
                        content.remove(invalidInputError);
                    }

                    content.add(existingError, BorderLayout.SOUTH);
                    refresh(content);
                }
            }
            else{
                if(content.isAncestorOf(existingError))
                {
                    content.remove(existingError);
                }
                content.add(invalidInputError, BorderLayout.SOUTH);
                refresh(content);               
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

    private void addTitle(JPanel container)
    {
        JPanel titleWrapper = new JPanel();
        JLabel title = new JLabel("Kishal's Typing Race Simulator!");

        centerText(title);
        addLabelPadding(title, 5);
        titleWrapper.add(title);
        titleWrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        titleWrapper.setBackground(Color.white);
        container.add(titleWrapper, BorderLayout.NORTH);
    }

    private JLabel createError(String errorMessage)
    {
        JLabel error = new JLabel(errorMessage);
        centerText(error);
        addLabelPadding(error, 5);
        error.setForeground(Color.RED); 
        
        return error;
    }

    private void refresh(JFrame window)
    {
        window.revalidate();
        window.repaint();
    }

    private void refresh(JPanel panel)
    {
        panel.revalidate();
        panel.repaint();
    }

    private void centerText(JLabel label)
    {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    private void addPanelPadding(JPanel panel, int size)
    {
        panel.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    private void addLabelPadding(JLabel label, int size)
    {
        label.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }
}