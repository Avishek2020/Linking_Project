package org.upb.music.artist.similarity.measures;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingControlDemo {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;

	public SwingControlDemo(){
		prepareGUI();
	}
	public static void main(String[] args){
		SwingControlDemo  swingControlDemo = new SwingControlDemo();      
		swingControlDemo.showTextFieldDemo();
	}
	private void prepareGUI(){
		mainFrame = new JFrame("Linking");
		mainFrame.setSize(800,800);
		mainFrame.setLayout(new GridLayout(3, 1));

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});    
		headerLabel = new JLabel("", JLabel.CENTER);        
		statusLabel = new JLabel("",JLabel.CENTER);    
		statusLabel.setSize(350,100);

		controlPanel = new JPanel();
		// controlPanel.setLayout(new FlowLayout());
		controlPanel.setLayout(null);

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);  
	}
	private void showTextFieldDemo(){
		headerLabel.setText("Music Class Simililarity"); 

		JLabel  SourceLabel = new JLabel("Source Endpoint URL", JLabel.RIGHT);

		JLabel  TargetLabel = new JLabel("Target  Endpoint URL", JLabel.RIGHT);

		final JTextField sourceText = new JTextField(80);
		final JTextField targetText = new JTextField(80);     

		JButton runButton = new JButton("Run");


		// create an empty combo box with items of type String
		JComboBox<String> comboSourceEndpoint = new JComboBox<String>();
		JComboBox<String> comboTargetEndpoint = new JComboBox<String>();

		// add items to the combo box
		comboSourceEndpoint.addItem("Select Source Endpoint");
		comboSourceEndpoint.addItem("http://dbtune.org/magnatune/sparql");
		comboSourceEndpoint.addItem("http://dbtune.org/jamendo/sparql");
		comboSourceEndpoint.addItem("http://dbtune.org/musicbrainz/sparql");
		comboSourceEndpoint.setEditable(true);
		String getComboSvalue = String.valueOf(comboSourceEndpoint.getSelectedItem());
		//System.out.println(svalue);

		comboTargetEndpoint.addItem("Select Target Endpoint");
		comboTargetEndpoint.addItem("http://dbtune.org/jamendo/sparql");
		comboTargetEndpoint.addItem("http://dbtune.org/magnatune/sparql");
		comboTargetEndpoint.addItem("http://dbtune.org/musicbrainz/sparql");
		comboTargetEndpoint.setEditable(true);
		String getComboTvalue = String.valueOf(comboTargetEndpoint.getSelectedItem());
		//System.out.println(tvalue);

		// Add displayedMnemonic and labelFor property values
		SourceLabel.setLabelFor(sourceText);
		TargetLabel.setLabelFor(targetText);

		String text = "Similarity of Music ";
		JTextArea textArea = new JTextArea(text);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		String appendText = "Classes ,all properties.";
		textArea.append(appendText);
		textArea.setPreferredSize(new Dimension(1000, 1000));
		textArea.setLayout(new BorderLayout());
		//textArea.add(scrollPane, BorderLayout.CENTER);





		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				SwingSampleOutPutConsole s = new SwingSampleOutPutConsole();

				textArea.append(s.ConsoleDisplayText());
				String data = " Record processing ..."; 
				statusLabel.setText(data);  

			}
		}); 


		controlPanel.add(SourceLabel);
		controlPanel.add(sourceText);
		controlPanel.add(TargetLabel);
		controlPanel.add(targetText);
		controlPanel.add(runButton);
		controlPanel.add(textArea);
		controlPanel.add(comboSourceEndpoint);
		controlPanel.add(comboTargetEndpoint);

		SourceLabel.setLocation(10,10); SourceLabel.setSize(200,22);
		//sourceText.setLocation(230,10); sourceText.setSize(250,22);
		comboSourceEndpoint.setLocation(230,10);comboSourceEndpoint.setSize(250,22);

		TargetLabel.setLocation(10,35); TargetLabel.setSize(200,22);
		//targetText.setLocation(230,35); targetText.setSize(250,22);
		comboTargetEndpoint.setLocation(230,35);comboTargetEndpoint.setSize(250,22);

		runButton.setLocation(380,60); runButton.setSize(100,22);

		textArea.setLocation(50,100); textArea.setSize(500,500);

		// comboSourceEndpoint.setLocation(50,80);comboSourceEndpoint.setSize(350,20);

		// SourceLabel.setSize(200,15);
		//sourceText.setSize(200,15);
		mainFrame.setVisible(true);  

	}
}