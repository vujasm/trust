package com.inn.gui;

/*
 * #%L
 * trusthings-client-simple
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.inn.gui.action.Controller;
import com.inn.gui.tree.Node;
import com.inn.gui.util.ResourceReader;

import javax.swing.JSlider;

public class Application {

	private JFrame frame;

	
	
	private JPanel panelCriteria;
	
	private JScrollPane scrollPane;
	
	JTree tree ;
	
	int demo = 0;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		
		
		final JPopupMenu popup = new JPopupMenu();
	    JMenuItem menuItem = new JMenuItem("Add..");
	    popup.add(menuItem);
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				com.inn.gui.tree.Node n = (Node) tree.getSelectionPath().getLastPathComponent();
				JPanel panelCriterion = null;
				if (n.getOntNode().getName().startsWith("Security") == false){
					panelCriterion = createPanelCritera(n);
				}
				else{
					panelCriterion = createPanelCriteraSec(n);
				}
				panelCriteria.add(panelCriterion);
				scrollPane.validate();
				scrollPane.repaint();
			}
		});
		
		frame = new JFrame();
		frame.setBounds(100, 100, 810, 537);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setBounds(0, 0, 800, 466);
		frame.getContentPane().add(splitPane);
		
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setBounds(100, 100, 800, 400);
		
		tree = new JTree();
		tree.setBounds(10, 31, 149, 368);
		tree.setModel(Controller.getTreeModel());
		tree.setCellRenderer(new DefaultTreeCellRenderer());
		tree.addMouseListener(new MouseInputAdapter() {
//			public void mousePressed(MouseEvent e) {
//			    if (e.isPopupTrigger()) {
//			        popup.show((Component)e.getSource(), e.getX(), e.getY());
//			    }
//			}
			public void mouseReleased(MouseEvent e) {
			    if (e.isPopupTrigger()) {
			        popup.show((Component)e.getSource(),e.getX(), e.getY());
			    }
			}
		});
		splitPane.add(tree);
		
		@SuppressWarnings("rawtypes")
		final JList list = new JList();
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setBounds(10, 31, 425, 58);
		leftPanel.add(list);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBounds(10, 100, 550, 253);
		leftPanel.add(scrollPane);

		
		JButton btnGetTrustScore = new JButton("Get Trust Score");
		btnGetTrustScore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (demo == 1){
					showAgentDescr(Controller.getScorestDemo1(), "Trust scores (resources are ordered from trusted to less trusted)");
				}
				else if (demo == 2){
					showAgentDescr(Controller.getScorestDemo2(),"Trust scores (resources are ordered from trusted to less trusted)");
				}
				else {
					showAgentDescr(Controller.getScorestDemo3(),"Trust scores (resources are ordered from trusted to less trusted)");
				}
			}
		});
		btnGetTrustScore.setBounds(244, 376, 127, 23);
		leftPanel.add(btnGetTrustScore);
		
		JLabel lblNewLabel = new JLabel("Discovered service matching functional goal..");
		lblNewLabel.setBounds(10, 6, 300, 14);
		leftPanel.add(lblNewLabel);
		
		
		panelCriteria= new JPanel();
		panelCriteria.setLayout(new FlowLayout());
		panelCriteria.setPreferredSize(new Dimension(510, 300));
		
		
		scrollPane.setViewportView(panelCriteria);
		
		JButton btnView = new JButton("View..");
		btnView.setBounds(445, 29, 75, 23);
		leftPanel.add(btnView);
		
		JButton btnNewButton = new JButton("View criteria JSON");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = "";
				if (demo == 1){
					path = "/criteria/demo/criteria_sc_a.json";
				}
				else if (demo == 2){
					path = "/criteria/demo/criteria_sc_b.json";
				}
				else {
					path = "/criteria/demo/criteria_sc_c.json";
				}
				if (path!=null){
				String descr = ResourceReader.getResourceAsStringForClasspath(path);
				showAgentDescr(descr, "Trust criteria in JSON syntax");
				}
			}
		});
		btnNewButton.setBounds(10, 376, 181, 23);
		leftPanel.add(btnNewButton);
		
	
		splitPane.add(leftPanel);
		
		JButton button = new JButton("Filter");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = "Trusted services- filtering using trust threshold of 0.5";
				switch (demo){
				case 1:showAgentDescr(Controller.getFilteredDemo1(), title); break;
				case 2:showAgentDescr(Controller.getFilteredDemo2(), title); break;
				case 3:showAgentDescr(Controller.getFilteredDemo3(), title); break;
				}
			}});
		
		button.setBounds(393, 376, 127, 23);
		leftPanel.add(button);
		
		btnView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String descr = ""; 
				String item = (String) list.getSelectedValue();
				String title = "";
				if (item !=null){
					String id = parse(item);
					descr = ResourceReader.getTrustDescr(id);
					 title = "Trust profile (trust-related attributes) of "+id;
				}
				showAgentDescr(descr,title);
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Choose demo..");
		menuBar.add(menu);
		
		JMenuItem menuItem1 = new JMenuItem("Demo 1");
		menu.add(menuItem1);
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				demo = 1;
				list.setModel(getDemo1Model());
				panelCriteria.removeAll();
				panelCriteria.validate();
				panelCriteria.repaint();
			}
		});
		
		JMenuItem menuItem2 = new JMenuItem("Demo 2");
		menu.add(menuItem2);
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				demo = 2;
				list.setModel(getDemo2Model());
				panelCriteria.removeAll();
				panelCriteria.validate();
				panelCriteria.repaint();
			}
		});
		
		JMenuItem menuItem3 = new JMenuItem("Demo 3");
		menu.add(menuItem3);
		menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				demo = 3;
				list.setModel(getDemo3Model());
				panelCriteria.removeAll();
				panelCriteria.validate();
				panelCriteria.repaint();
			}
		});
	}
		
	
	
	private JPanel createPanelCritera(Node n) {
		JTextField textFieldAttribute = null;
//		JTextField textFieldRelevance = null;
		JTextField textFieldValue= null;
		JPanel panelCriteria = new JPanel();
//		panelCriteria.setBounds(5, 5, 510, 50);
		panelCriteria.setPreferredSize(new Dimension(480, 50));
//		leftPanel.add(panelCriteria);
		panelCriteria.setLayout(null);
		
		
		JLabel lblNewLabel_1 = new JLabel("Attribute");
		lblNewLabel_1.setBounds(7, 7, 156, 14);
		panelCriteria.add(lblNewLabel_1);
		
		textFieldAttribute = new JTextField();
		textFieldAttribute.setBounds(7, 25, 256, 20);
		panelCriteria.add(textFieldAttribute);
		textFieldAttribute.setColumns(10);
		textFieldAttribute.setText(n.getOntNode().getName());
		
		JLabel lblValue = new JLabel("Value");
		lblValue.setBounds(272, 7, 88, 14);
		panelCriteria.add(lblValue);
//		
//		textFieldRelevance = new JTextField();
//		textFieldRelevance.setBounds(370, 25, 86, 20);
//		panelCriteria.add(textFieldRelevance);
//		textFieldRelevance.setColumns(10);
		final JSlider slider = new JSlider(0, 100, 100);  
	    slider.setMajorTickSpacing(25);  
	    slider.setPaintTicks(true);  
	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
	    labelTable.put(new Integer(100), new JLabel("1.0"));  
	    labelTable.put(new Integer(75), new JLabel("0.75"));  
	    labelTable.put(new Integer(50), new JLabel("0.50"));  
	    labelTable.put(new Integer(25), new JLabel("0.25"));  
	    labelTable.put(new Integer(0), new JLabel("0.0"));  
	    slider.setLabelTable( labelTable );  
	    slider.setPaintLabels(true);  
		slider.setBounds(370, 25, 100, 50);
		panelCriteria.add(slider);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				 JSlider source = (JSlider)e.getSource();
			        if (!source.getValueIsAdjusting()) {
			            int fps = (int)source.getValue();
			        }    
				
			}
		});
		
		JLabel lblRelevance = new JLabel("Relevance 0-1");
		lblRelevance.setBounds(370, 7, 88, 14);
		panelCriteria.add(lblRelevance);
		
		textFieldValue = new JTextField();
		textFieldValue.setColumns(10);
		textFieldValue.setBounds(273, 25, 86, 20);
		
		
		
		panelCriteria.add(textFieldValue);
		
		return panelCriteria;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JPanel createPanelCriteraSec(Node n) {

		JPanel panelCriteria = new JPanel();
//		panelCriteria.setBounds(5, 5, 510, 50);
		panelCriteria.setPreferredSize(new Dimension(480, 50));
//		leftPanel.add(panelCriteria);
		panelCriteria.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Goal");
		lblNewLabel_1.setBounds(7, 7, 156, 14);
		panelCriteria.add(lblNewLabel_1);
		
		JComboBox comboBoxGoal = new JComboBox();
		comboBoxGoal.setBounds(7, 25, 120, 20);
		comboBoxGoal.setModel(new DefaultComboBoxModel(new String[] 
				{"Authentication","Authorization","Availability", "Anonymity", 
						"Accountability", "Confidentiality", "Correctness", "Identification", "Policy Compliance", "Privacy", ""}));
		panelCriteria.add(comboBoxGoal);
		
		JLabel lblValue = new JLabel("Mechanism");
		lblValue.setBounds(128, 7, 88, 14);
		panelCriteria.add(lblValue);
		
		JComboBox comboBoxMechanism = new JComboBox();
		comboBoxMechanism.setBounds(128, 25, 120, 20);
		comboBoxMechanism.setModel(new DefaultComboBoxModel(new String[] 
				{"Access Control", "Certificate", "Cryptography", 
				"Token", "Certification", "Certificate Exchange", "Monitoring", "Password Exchange", "Usage Control", ""}));
		panelCriteria.add(comboBoxMechanism);
		
		JLabel lblTechno = new JLabel("Technology");
		lblTechno.setBounds(250, 7, 88, 14);
		panelCriteria.add(lblTechno);
		
		JComboBox comboBoxTech = new JComboBox();
		comboBoxTech.setBounds(250, 25, 120, 20);
		comboBoxTech.setModel(new DefaultComboBoxModel(new String[] 
				{"RSA-SHA1",
				"RSA",
				"RSA-MD5",
				"HTTPDigestAuth",
				"OAuth",
				"OAuth2",
				"OpenID",
				"APIKey","HTTPBasicAuth" ,"SessionID", "CryptographicProtocol" ,"TSL" ,"SSL", "XACML"
				,"DSA-SHA1" ,"X509","HMAC-SHA1","AES","AES256bit","AES128bit","SAML","DES", ""}));
		panelCriteria.add(comboBoxTech);
		
		//
		JLabel lblRelevance = new JLabel("Relevance 0-1");
		lblRelevance.setBounds(370, 7, 88, 14);
		panelCriteria.add(lblRelevance);
		//
		final JSlider slider = new JSlider(0, 100, 100);  
	    slider.setMajorTickSpacing(25);  
	    slider.setPaintTicks(true);  
	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
	    labelTable.put(new Integer(100), new JLabel("1.0"));  
	    labelTable.put(new Integer(75), new JLabel("0.75"));  
	    labelTable.put(new Integer(50), new JLabel("0.50"));  
	    labelTable.put(new Integer(25), new JLabel("0.25"));  
	    labelTable.put(new Integer(0), new JLabel("0.0"));  
	    slider.setLabelTable( labelTable );  
	    slider.setPaintLabels(true);  
		slider.setBounds(370, 25, 100, 50);
		panelCriteria.add(slider);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				 JSlider source = (JSlider)e.getSource();
			        if (!source.getValueIsAdjusting()) {
			            int fps = (int)source.getValue();
			        }    
				
			}
		});
		
		return panelCriteria;
	}

	protected void showAgentDescr(String descr, String title) {
		ViewerDialog dialog = new ViewerDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setContent("");
		dialog.setContent(descr);
		dialog.setLocationRelativeTo(null);
		dialog.setTitle(title);
		dialog.setVisible(true);
	}

	@SuppressWarnings("rawtypes")
	private AbstractListModel getDemo1Model(){
		return new AbstractListModel() {
			String[] values = new String[] {"Weather Service A [http://localhost/services/Weather_API_A]",
					"Weather Service B [http://localhost/services/Weather_API_B]", 
					"Weather Service C [http://localhost/services/Weather_API_C]"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private AbstractListModel getDemo2Model(){
		return new AbstractListModel() {
			String[] values = new String[] {"Geolocation API A [http://localhost/services/Geolocation_API_A]",
					"Geolocation API B  [http://localhost/services/Geolocation_API_B]", 
					"Geolocation API C  [http://localhost/services/Geolocation_API_C]"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private AbstractListModel getDemo3Model(){
		return new AbstractListModel() {
			String[] values = new String[] {"Wind Sensor API A [http://localhost/services/Wind_Sensor_A]",
					"Wind Sensor API B  [http://localhost/services/Wind_Sensor_B]", 
					"Wind Sensor API C  [http://localhost/services/Wind_Sensor_C]"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		};
	}
	
	private String parse(String item) {
		int from = item.indexOf("[");
		int to = item.indexOf("]");
		return item.substring(from+1, to);
	}
	
	public JFrame getFrame() {
		return frame;
	}
}

