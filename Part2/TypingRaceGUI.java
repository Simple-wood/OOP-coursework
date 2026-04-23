import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class TypingRaceGUI
{
    private String passage = null;
    private int typistCount = 0;
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

        menus.add(menuPanel, "MENU");
        menus.add(numberPanel, "NUMBER");
        window.add(menus);

        cards.show(menus, "NUMBER");

        window.setSize(300, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private JPanel PassageMenu()
    {
        JPanel container = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        JPanel options = new JPanel();
        JPanel radioButtons = new JPanel(new GridBagLayout());
        JPanel content = new JPanel(new BorderLayout());
        addPanelPadding(content, 25);

        JPanel description = new JPanel(new BorderLayout());
        description.setBackground(Color.WHITE);

        JLabel error = new JLabel("Invalid Input");
        centerText(error);
        addLabelPadding(error, 5);

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

        JLabel title = new JLabel("Kishal's Typing Race Simulator!");
        centerText(title);
        addLabelPadding(title, 5);
        titlePanel.add(title);

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
        
        container.add(titlePanel, BorderLayout.NORTH);
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
        JPanel titleWrapper = new JPanel();
        JLabel title = new JLabel("Kishal's Typing Race Simulator!");
        JButton submit = new JButton("Submit number of typists");

        centerText(title);
        addLabelPadding(title, 5);
        titleWrapper.add(title);
        titleWrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        titleWrapper.setBackground(Color.white);
        container.add(titleWrapper, BorderLayout.NORTH);

        JLabel numberText = new JLabel("Please enter the number of typists you would like - ");
        centerText(numberText);
        addLabelPadding(numberText, 5);
        content.add(numberText, BorderLayout.NORTH);

        JTextField numberInput = new JTextField();
        numberWrapper.add(numberInput, BorderLayout.NORTH);
        addPanelPadding(numberWrapper, 15);

        content.add(numberWrapper, BorderLayout.CENTER);
        container.add(content, BorderLayout.CENTER);

        return container;
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