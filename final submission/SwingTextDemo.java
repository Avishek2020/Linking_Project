package org.upb.music.artist.similarity.measures;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class SwingTextDemo extends JPanel {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Linking-Music Class Simililarity");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//frame.setLayout(new GridLayout(3,1));
		JPanel panOuter = new JPanel(new BorderLayout());
		JPanel panLeft = new JPanel(new BorderLayout());
		panLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel panRight = new JPanel(new BorderLayout());
		panRight.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel panBottom = new JPanel(); // default is FlowLayout
		panBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel panInput = new JPanel(new BorderLayout());
		panInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//1 JPanel panConsole = new JPanel(new BorderLayout());

		Border outsideBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border insideBorder = BorderFactory.createTitledBorder("The Console ");
		Border theBorder = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
		//2 panConsole.setBorder(theBorder);

		//header label
		String text = " ";
		JTextArea textArea = new JTextArea(text);
		textArea.setLineWrap(true);
		textArea.setBackground(Color.WHITE);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(false);



		panInput.add(panLeft, BorderLayout.WEST);
		panInput.add(panRight, BorderLayout.EAST);
		panInput.add(panBottom, BorderLayout.SOUTH);

		panOuter.add(panInput, BorderLayout.NORTH);
		//3 panOuter.add(panConsole, BorderLayout.CENTER);
		panOuter.add(textArea);


		JLabel  SourceLabel = new JLabel("Source Endpoint URL", JLabel.LEFT);

		JLabel  TargetLabel = new JLabel("Target  Endpoint URL", JLabel.LEFT);



		//final JTextField sourceText = new JTextField(20);
		//final JTextField targetText = new JTextField(20); 

		// create an empty combo box with items of type String
		JComboBox<String> comboSourceEndpoint = new JComboBox<String>();
		JComboBox<String> comboTargetEndpoint = new JComboBox<String>();


		//comboSourceEndpoint.setLocation(230,10);comboSourceEndpoint.setSize(250,22);

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
		comboTargetEndpoint.setEditable(true);				String getComboTvalue = String.valueOf(comboTargetEndpoint.getSelectedItem());






		JButton runButton = new JButton("Run");

		JTextArea txtConsole = new JTextArea(5, 8);

		JScrollPane srcPane = new JScrollPane(txtConsole,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


		panLeft.add(SourceLabel, BorderLayout.NORTH);
		//panLeft.add(sourceText, BorderLayout.CENTER);

		panLeft.add(comboSourceEndpoint, BorderLayout.CENTER);

		panRight.add(TargetLabel, BorderLayout.NORTH);
		//panRight.add(targetText, BorderLayout.CENTER);
		panRight.add(comboTargetEndpoint, BorderLayout.CENTER);  	


		panBottom.add(runButton);


		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				Object source = e.getSource();
				String lv="";

				if(source == runButton){
					System.out.println("RUN button is clicked");
					SwingSampleOutPutConsole s = new SwingSampleOutPutConsole();

					textArea.append(s.ConsoleDisplayText());


				}
				//		              } else if (source == exitBtn){
				//		                  System.out.println("EXIT button is clicked");
				//		              }
				//				 


				String data = "running....";
				//statusLabel.setText(data);  

			}
		}); 




		//4 panConsole.add(srcPane, BorderLayout.CENTER);

		frame.setContentPane(panOuter);
		frame.pack();
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
}