import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GUI extends Component implements ActionListener {
    private final JTextArea input;
    private JTextArea output;
    private final JTextField filePath;
    private boolean f=false;
    JFrame mainFrame = new JFrame("Huffman");
    JPanel inputPanel = new JPanel();
    JPanel outputPanel = new JPanel();
    JPanel filePanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JButton chooseFile = new JButton("Choose file");

    //choosing desired file
    public String fileChoose(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile.getAbsolutePath();
        }
        else return null;
    }

    //constructor
    GUI() {
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setSize(700,600);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        input = new JTextArea(10,40);

        filePath = new JTextField(30);
        filePath.setEditable(false);


        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Input"));
        JScrollPane scrollableinput = new JScrollPane(input);
        scrollableinput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableinput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inputPanel.add(scrollableinput);

        output = new JTextArea(10, 40);
        output.setEditable(false);
        JScrollPane scrollableTextArea = new JScrollPane(output);
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputPanel.setLayout(new FlowLayout());
        outputPanel.add(new JLabel("Output"));
        outputPanel.add(scrollableTextArea);

        JRadioButton fromFile = new JRadioButton("choose from file");
        fromFile.setMnemonic(KeyEvent.VK_B);
        fromFile.setActionCommand("choose from file");

        JRadioButton fromText = new JRadioButton("Write to text box");
        fromText.setMnemonic(KeyEvent.VK_C);
        fromText.setActionCommand("Write to text box");
        fromText.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(fromFile);
        group.add(fromText);

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout());
        radioButtonPanel.add(fromText);
        radioButtonPanel.add(fromFile);
        fromText.addActionListener(this);
        fromFile.addActionListener(this);

        filePanel.setLayout(new FlowLayout());
        filePanel.add(new JLabel("Selected file"));
        filePath.setText("No file selected");
        filePanel.add(filePath);
        filePanel.add(chooseFile);

        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(new JLabel("Press button"));

        JButton compressButton = new JButton("Compress");
        compressButton.addActionListener(this);
        buttonsPanel.add(compressButton);

        JButton decompressButton = new JButton("Decompress");
        decompressButton.addActionListener(this);
        buttonsPanel.add(decompressButton);

        chooseFile.addActionListener(this);
        chooseFile.setVisible(false);

        filePanel.setVisible(false);
        mainFrame.add(radioButtonPanel, BorderLayout.WEST);
        mainFrame.add(inputPanel, BorderLayout.WEST);
        mainFrame.add(filePanel,SpringLayout.WEST);
        mainFrame.add(outputPanel, BorderLayout.WEST);
        mainFrame.add(buttonsPanel, BorderLayout.WEST);
        mainFrame.setVisible(true);
    }

    //implementing buttons actions
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("choose from file")){
            filePanel.setVisible(true);
            chooseFile.setVisible(true);
            inputPanel.setVisible(false);
        }
        else if(e.getActionCommand().equals("Write to text box")){
            filePanel.setVisible(false);
            chooseFile.setVisible(false);
            inputPanel.setVisible(true);
        }

        if (e.getActionCommand().equals("Compress")) {
            Compress c = new Compress();
            if(f) {
                try {
                    output.setText(c.compress(Path.of(filePath.getText())));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                f=false;
                filePath.setText("No file selected");
            }
            else {
                try {
                    output.setText(c.compress(input.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
        else if (e.getActionCommand().equals("Decompress")) {
            Decompress c = new Decompress();
            //if user provided a path, decompress file , else decompress text area.
            if(f) {
                try {
                    output.setText(c.decompress(Path.of(filePath.getText())));
                    f = false;
                    filePath.setText("No file selected");
                }catch (IndexOutOfBoundsException x) {
                    System.out.println(x.getMessage());
                }
            }
            else {
                try {
                    output.setText(c.decompress(input.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getActionCommand().equals("Choose file")) {
            filePath.setText(fileChoose());
            f=true;
        }
    }
}
