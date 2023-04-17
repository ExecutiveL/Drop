package TxtEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;

public class TextEditor extends JFrame implements ActionListener{

 JTextArea textArea;
 JScrollPane scrollPane;
 JLabel fontLabel;
 JSpinner fontSizeSpinner;
 JButton fontColorButton;
 JComboBox fontBox;
 
 JMenuBar menuBar;
 JMenu fileMenu;
 JMenuItem openItem;
 JMenuItem saveItem;
 JMenuItem exitItem;
 
 JMenu EditMenu;
 JMenuItem CutItem;
 JMenuItem CopyItem;
 JMenuItem PasteItem;
 JMenuItem SelectAllItem;
 
 JMenu ToolsMenu;
 JMenuItem AboutItem;
 private JLabel WordCountLabel;

 
 TextEditor(){

 	getContentPane().setBackground(new Color(175, 238, 238));
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setTitle("Java Text Editor");
  this.setSize(500, 500);
  this.setLocationRelativeTo(null);
  
  textArea = new JTextArea();
  textArea.setLineWrap(true);
  textArea.setWrapStyleWord(true);
  textArea.setFont(new Font("Arial",Font.PLAIN,20));
  
  scrollPane = new JScrollPane(textArea);
  scrollPane.setBounds(17, 35, 450, 450);
  scrollPane.setPreferredSize(new Dimension(450,450));
  scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  
  fontLabel = new JLabel("Font: ");
  fontLabel.setBounds(17, 10, 34, 14);
  
  fontSizeSpinner = new JSpinner();
  fontSizeSpinner.setBounds(44, 5, 50, 25);
  fontSizeSpinner.setPreferredSize(new Dimension(50,25));
  fontSizeSpinner.setValue(20);
  fontSizeSpinner.addChangeListener(new ChangeListener() {

   @Override
   public void stateChanged(ChangeEvent e) {
    
    textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue())); 
   }
   
  });
  
  fontColorButton = new JButton("Color");
  fontColorButton.setBackground(new Color(248, 248, 255));
  fontColorButton.setBounds(104, 6, 75, 23);
  fontColorButton.addActionListener(this);
  
  String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
  
  fontBox = new JComboBox(fonts);
  fontBox.setBackground(new Color(0, 191, 255));
  fontBox.setBounds(189, 7, 168, 20);
  fontBox.addActionListener(this);
  fontBox.setSelectedItem("Arial");
	  this.setVisible(true);
	 
  // ----- menubar -----
  
   menuBar = new JMenuBar();
   menuBar.setBackground(new Color(176, 224, 230));
   fileMenu = new JMenu("File");
   openItem = new JMenuItem("Open");
   saveItem = new JMenuItem("Save");
   exitItem = new JMenuItem("Exit");
   
   openItem.addActionListener(this);
   saveItem.addActionListener(this);
   exitItem.addActionListener(this);
   
   fileMenu.add(openItem);
   fileMenu.add(saveItem);
   fileMenu.add(exitItem);
   menuBar.add(fileMenu);
  
  // ----- /menubar -----
   
  this.setJMenuBar(menuBar);
  getContentPane().setLayout(null);
  getContentPane().add(fontLabel);
  getContentPane().add(fontSizeSpinner);
  getContentPane().add(fontColorButton);
  getContentPane().add(fontBox);
  getContentPane().add(scrollPane);
  
  

  WordCountLabel = new JLabel("Word Count:");
  WordCountLabel.setBounds(367, 10, 117, 14);
  getContentPane().add(WordCountLabel);
  
  textArea.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
          updateWordCount();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
          updateWordCount();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
          updateWordCount();
      }
  });
 EditMenu = new JMenu("Edit");
 CutItem = new JMenuItem("Cut");
 CopyItem = new JMenuItem("Copy");
 PasteItem = new JMenuItem("Paste");
 SelectAllItem = new JMenuItem("Select All");
 
 CutItem.addActionListener(this);
 CopyItem.addActionListener(this);
 PasteItem.addActionListener(this);
 SelectAllItem.addActionListener(this);
 
 EditMenu.add(CopyItem);
 EditMenu.add(CutItem);
 EditMenu.add(PasteItem);
 EditMenu.add(SelectAllItem);
 menuBar.add(EditMenu);

 //=====AboutMenu======//
 ToolsMenu = new JMenu("Tools");
 AboutItem = new JMenuItem("About");
 
 AboutItem.addActionListener(this);
 
 ToolsMenu.add(AboutItem);
 menuBar.add(ToolsMenu);
}
private void updateWordCount() {
  Document doc = textArea.getDocument();
  int wordCount = 0;
  try {
      String text = doc.getText(0, doc.getLength()).trim();
      String[] words = text.split("\\s+");
      wordCount = words.length;
  } catch (BadLocationException ex) {
      ex.printStackTrace();
  }
  WordCountLabel.setText("Word Count: "+ wordCount);
  
}
 @Override
 public void actionPerformed(ActionEvent e) {
  
  if(e.getSource()==fontColorButton) {
   JColorChooser colorChooser = new JColorChooser();
   
   Color color = colorChooser.showDialog(null, "Choose a color", Color.black);
   
   textArea.setForeground(color);
  }
  
  if(e.getSource()==fontBox) {
   textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
  }
  
  if(e.getSource()==openItem) {
   JFileChooser fileChooser = new JFileChooser();
   fileChooser.setCurrentDirectory(new File("."));
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
   fileChooser.setFileFilter(filter);
   
   int response = fileChooser.showOpenDialog(null);
   
   if(response == JFileChooser.APPROVE_OPTION) {
    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    Scanner fileIn = null;
    
    try {
     fileIn = new Scanner(file);
     if(file.isFile()) {
      while(fileIn.hasNextLine()) {
       String line = fileIn.nextLine()+"\n";
       textArea.append(line);
      }
     }
    } catch (FileNotFoundException e1) {
     // TODO Auto-generated catch block
     e1.printStackTrace();
    }
    finally {
     fileIn.close();
    }
   }
  }
  if(e.getSource()==saveItem) {
   JFileChooser fileChooser = new JFileChooser();
   fileChooser.setCurrentDirectory(new File("."));
   
   int response = fileChooser.showSaveDialog(null);
   
   if(response == JFileChooser.APPROVE_OPTION) {
    File file;
    PrintWriter fileOut = null;
    
    file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    try {
     fileOut = new PrintWriter(file);
     fileOut.println(textArea.getText());
    } 
    catch (FileNotFoundException e1) {
     // TODO Auto-generated catch block
     e1.printStackTrace();
    }
    finally {
     fileOut.close();
    }   
   }
  }
  if(e.getSource()==exitItem) {
   System.exit(0);
  }  
 if(e.getSource()==CopyItem) {
    textArea.copy();
 }
 if(e.getSource()==CutItem) {
	 textArea.cut();
 }
 if(e.getSource()==PasteItem) {
	 textArea.paste();
 }
 if(e.getSource()==SelectAllItem) {
	 textArea.selectAll();
 }
if(e.getSource()==AboutItem) {
	JFrame search = new JFrame("Search");
	search.setBounds(100, 100, 350, 111);
	search.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	search.getContentPane().setLayout(null);
	search.setVisible(true);
	
	JLabel lblSearchFor = new JLabel("Search For:");
	lblSearchFor.setBounds(8, 11, 56, 50);
	search.getContentPane().add(lblSearchFor);
	
	JTextField searchField = new JTextField();
	searchField .setBounds(67, 26, 257, 20);
	search.getContentPane().add(searchField );
	searchField.setColumns(10);
	
	JButton btnNewButton = new JButton("Search");
	btnNewButton.setBounds(138, 49, 89, 23);
	search.getContentPane().add(btnNewButton);
}
	
}
}


