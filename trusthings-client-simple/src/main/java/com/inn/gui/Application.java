package com.inn.gui;

/*
 * #%L
 * trusthings-client-simple
 * %%
 * Copyright (C) 2015 COMPOSE project
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
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.inn.demo.review2014.FeedUtil;
import com.inn.gui.action.Controller;
import com.inn.gui.tree.Node;
import com.inn.gui.util.TemplateRequestBody;
import com.inn.gui.util.ToStringArray;
import com.inn.gui.valuemodel.ValueBean;
import com.inn.gui.valuemodel.ValueBeanToJson;
import com.sun.syndication.feed.synd.SyndFeed;

public class Application {

	private JFrame frmComposeServiceRecommender;

//	http://abiell.pc.ac.upc.edu:9081/
	String iServeHost = "abiell.pc.ac.upc.edu";
	String  iServePort = "9081";
	
	GridBagConstraints c = new GridBagConstraints();
	
	
	private ArrayList<JPanel> listPanelCriterion = Lists.newArrayList();
	
	private JPanel panelCriteria;
	
	private JScrollPane scrollPane;
	
	JTree tree ;
	
	int demo = 0;
	private JTextField textFieldSearch;
	
	private boolean isFiltering = false;


	private JComboBox comboBoxRankig;


	private JDialog progressBarDialog;

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Application window = new Application();
					window.frmComposeServiceRecommender.setVisible(true);
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
		iServeHost  = System.getProperty("iserve.host", iServeHost);
		iServePort = System.getProperty("iserve.port", iServePort);
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
				if (n.isSupported() == false){
					JOptionPane.showMessageDialog(null, "Not supported yet");
					return;
				}
				
				if (n.getOntNode().getName().startsWith("Security")==false  &&
						n.getOntNode().getName().startsWith("Certifi")
						== false){
					panelCriterion = createPanelCritera(n);
				}
				else if (n.getOntNode().getName().startsWith("Certifi")){
						panelCriterion = createPanelCriteraCertificateDetails(n);
					}
				else{
					panelCriterion = createPanelCriteraSec(n);
				}
				
				listPanelCriterion.add(panelCriterion);
//				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = listPanelCriterion.size()-1;
				c.ipady = 40;      //make this component tall
//				c.weightx = 0.0;
				c.weighty = 1.0;
				c.gridwidth = 3;
//				c.anchor = GridBagConstraints.NORTHWEST;
				panelCriteria.add(panelCriterion, c);
			
				
				scrollPane.validate();
				scrollPane.repaint();
			}
		});
		
		frmComposeServiceRecommender = new JFrame();
		frmComposeServiceRecommender.setTitle("COMPOSE Service Recommender - Demo App for Trust Filtering and Scoring");
		frmComposeServiceRecommender.setBounds(100, 100, 1000, 537);
		frmComposeServiceRecommender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmComposeServiceRecommender.getContentPane().setLayout(null);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setBounds(0, 0, 1000, 466);
		frmComposeServiceRecommender.getContentPane().add(splitPane);
		
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setBounds(100, 100, 1000, 400);
		
		tree = new JTree();//
		tree.setBounds(10, 31, 149, 368);
		tree.setModel(Controller.getTreeModel());
		TreeCellRenderer renderer = new DefaultTreeCellRenderer() {
			  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			    JLabel ret = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			   com.inn.gui.tree.Node node = (com.inn.gui.tree.Node) value;
			    if (node.isSupported()) {
			    	ret.setForeground(Color.BLUE);
//			      	ret.setIcon(...);
			    }
			    return ret;
			  }
			};
		tree.setCellRenderer(renderer );
		tree.addMouseListener(new MouseInputAdapter() {
			public void mouseReleased(MouseEvent e) {
			    if (e.isPopupTrigger()) {
			        popup.show((Component)e.getSource(),e.getX(), e.getY());
			    }
			}
		});
		splitPane.add(tree);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBounds(20, 120, 612, 253);
		leftPanel.add(scrollPane);
		
		JButton btnGetTrustScore = new JButton("Recommend Services");
		btnGetTrustScore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					String requestBody = formulateRequestBody();
					if (requestBody == null){
						JOptionPane.showMessageDialog(null, "No trust dimensions are specified. Please specify at least one");
						return;
					}
					if (requestBody.startsWith("Either")){
						JOptionPane.showMessageDialog(null, requestBody);
						return;
					}
					showProgressBar();
					Stopwatch timer = new Stopwatch().start();
					String ep ="http://"+Application.this.iServeHost+":"+Application.this.iServePort+"/iserve/discovery/";
					SyndFeed feeds = FeedUtil.syndFeedForUrlPOST(ep, requestBody);
					timer.stop();
					System.out.println("Time passed :"+timer.elapsed(TimeUnit.MILLISECONDS)+" "+timer.elapsed(TimeUnit.MINUTES));
				    hideProgressBar();
					showViewText(FeedUtil.toHTMLString(feeds),"Result", false);
				} catch (Exception ex) {
					StringWriter errors = new StringWriter();
					ex.printStackTrace();
					ex.printStackTrace(new PrintWriter(errors));
					showViewText(errors.toString(), "There was a problem with this action..", true);	
				}
			}
		});
		btnGetTrustScore.setBounds(302, 410, 190, 23);
		leftPanel.add(btnGetTrustScore);
		
		JLabel lblNewLabel = new JLabel("iServe - Search (text-based): Enter keyword");
		lblNewLabel.setBounds(20, 86, 265, 23);
		leftPanel.add(lblNewLabel);
		
		
		panelCriteria= new JPanel();
//		panelCriteria.setLayout(new BoxLayout(panelCriteria, BoxLayout.Y_AXIS));
		panelCriteria.setLayout(new GridBagLayout());
		panelCriteria.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//		panelCriteria.setPreferredSize(new Dimension(510, 300));
		
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		pane.add(panelCriteria);
		scrollPane.setViewportView(pane);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Apply Filtering (Y/N)");
		chckbxNewCheckBox.setBounds(31, 380, 139, 23);
		leftPanel.add(chckbxNewCheckBox);
		chckbxNewCheckBox.addActionListener(new ActionListener() {			
		
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFiltering == true) {
					isFiltering = false;
				}else{
					isFiltering = true;
				}
				
			}
		});
		
		JButton btnNewButton = new JButton("Preview request");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String requestBody = formulateRequestBody();
				if (requestBody == null){
					JOptionPane.showMessageDialog(null, "No trust dimensions are specified. Please specify at least one");
					return;
				}
				if (requestBody.startsWith("Either")){
					JOptionPane.showMessageDialog(null,requestBody);
					return;
				}
								
				showViewText(requestBody, "Request Body", true);
			}
		});
		btnNewButton.setBounds(71, 410, 204, 23);
		leftPanel.add(btnNewButton);
		
	
		splitPane.add(leftPanel);
		
		
		
		comboBoxRankig = new JComboBox();
		comboBoxRankig.setModel(new DefaultComboBoxModel(new String[] {"no ranking", "standard", "inverse"}));
		comboBoxRankig.setBounds(267, 379, 127, 20);
		leftPanel.add(comboBoxRankig);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Application.class.getResource("/logo.png")));
		lblNewLabel_2.setBounds(0, 0, 265, 81);
		leftPanel.add(lblNewLabel_2);
		
		JLabel lblRanking = new JLabel("Rank order:");
		lblRanking.setBounds(181, 384, 75, 14);
		leftPanel.add(lblRanking);
		
		JButton btnSearch = new JButton("search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String keyword = textFieldSearch.getText();
				if (keyword != null && keyword.isEmpty() == false){
					try{
					showProgressBar();
					String result = FeedUtil.toHTMLString(FeedUtil.syndFeedForUrlGET("http://"+Application.this.iServeHost+":"+Application.this.iServePort+"/iserve/discovery/svc/search?q="+keyword));
					hideProgressBar();
					showViewText(result, "Search Result", false);
					}catch(Exception ex){
						StringWriter errors = new StringWriter();
						ex.printStackTrace();
						ex.printStackTrace(new PrintWriter(errors));
						showViewText(errors.toString(), "There was a problem with this action..", true);	
					}
					
				}
				
			}
		});
		btnSearch.setBounds(456, 86, 89, 23);
		leftPanel.add(btnSearch);
		
		textFieldSearch = new JTextField();
		textFieldSearch.setBounds(251, 87, 195, 20);
		leftPanel.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		JLabel lblTrustDimensions = new JLabel("User's trust dimensions");
		lblTrustDimensions.setBounds(20, 107, 166, 14);
		leftPanel.add(lblTrustDimensions);
		
		JButton btnClearAll = new JButton("Clear all");
		btnClearAll.setBounds(646, 120, 89, 23);
		btnClearAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JPanel panel : listPanelCriterion) {
					panelCriteria.remove(panel);	
				}
				listPanelCriterion.clear();
				panelCriteria.repaint();
			}
		});
		leftPanel.add(btnClearAll);
		
		JMenuBar menuBar = new JMenuBar();
		frmComposeServiceRecommender.setJMenuBar(menuBar);
	}
		
	
	
	protected void hideProgressBar() {
		progressBarDialog.setVisible(false);
	}

	protected void showProgressBar() {
		if (progressBarDialog == null){
			progressBarDialog = new JDialog();
			progressBarDialog.setDefaultCloseOperation(JDialog. HIDE_ON_CLOSE);
			progressBarDialog.setSize(200,50);
			progressBarDialog.setLocationRelativeTo(null);
			progressBarDialog.setTitle("Please wait..");
		}
		progressBarDialog.setVisible(true);
	
	}

	protected String formulateRequestBody() {
		
		String attributes = getAttributeFromCriteriaPanels();
		if (attributes == null)
			 return null;
		
		String keyword = textFieldSearch.getText();
		
		String ranking = null;
		if (!comboBoxRankig.getSelectedItem().toString().equals("no ranking")){
			ranking = comboBoxRankig.getSelectedItem().toString();
		}
		
		if (isFiltering && ranking ==null){
			return TemplateRequestBody.getRequestFilteringOnly(attributes, keyword);
		}
		if (isFiltering == false && ranking !=null){
			return TemplateRequestBody.getRequestRankingOnly(attributes, ranking, keyword);
		}
		if (isFiltering != false && ranking !=null){
			return  TemplateRequestBody.getRequestFilteringRanking(attributes, ranking, keyword);
		}
		return "Either filtering or ranking must be selected at least, if not both the filtering and ranking.";
	}

	private String getAttributeFromCriteriaPanels() {
		
		ArrayList<ValueBean> list = Lists.newArrayList();
		if (listPanelCriterion !=null && listPanelCriterion.isEmpty() == false){
			for (JPanel panel : listPanelCriterion) {
				ValueBean vb = new ValueBean();
				vb.setType(panel.getName());
				vb.setValue1(obtainValue("Value1", panel));
				vb.setValue2(obtainValue("Value2", panel));
				vb.setValue3(obtainValue("Value3", panel));
				vb.setRelevance(new Double(obtainValue("Relevance", panel))/100);
				list.add(vb);
			}
		}
		JsonNode json = ValueBeanToJson.toJson(list);
		if (json != null)
			return ValueBeanToJson.toPrettyJson(json);
		else{
			return null;
		}
	}

	private String obtainValue(String name, JPanel panel) {
		Component[] comps = panel.getComponents();
		for (int i = 0; i < comps.length; i++) {
			Component comp = comps[i];
			if (comp.getName()!=null && comp.getName().equalsIgnoreCase(name)){
				if (comp instanceof JTextField){
					return ((JTextField)comp).getText();
				}
				if (comp instanceof JComboBox<?>){
					return ((JComboBox)comp).getSelectedItem().toString();
				}
				if (comp instanceof JSlider){
					return Integer.valueOf(((JSlider)comp).getValue()).toString();
				}
			}
		}
		return null;
	}

	private JPanel createPanelCritera(Node n) {
		JTextField textFieldAttribute = null;
//		JTextField textFieldRelevance = null;
		JTextField textFieldValue= null;
		JPanel panelCriteria = new JPanel();
		panelCriteria.setName("Quantified");
//		panelCriteria.setBounds(5, 5, 510, 50);
		panelCriteria.setPreferredSize(new Dimension(480, 20));
//		leftPanel.add(panelCriteria);
		panelCriteria.setLayout(null);
		
		
		JLabel lblNewLabel_1 = new JLabel("Attribute");
		lblNewLabel_1.setBounds(7, 7, 156, 14);
		panelCriteria.add(lblNewLabel_1);
		
		textFieldAttribute = new JTextField();
		textFieldAttribute.setBounds(7, 25, 256, 20);
		panelCriteria.add(textFieldAttribute);
		textFieldAttribute.setColumns(10);
		textFieldAttribute.setName("Value1");
		textFieldAttribute.setText(n.getOntNode().getName());
		
		JLabel lblValue = new JLabel("Threshold value");
		lblValue.setBounds(272, 7, 88, 14);
		panelCriteria.add(lblValue);
//		
//		textFieldRelevance = new JTextField();
//		textFieldRelevance.setBounds(370, 25, 86, 20);
//		panelCriteria.add(textFieldRelevance);
//		textFieldRelevance.setColumns(10);
		final JSlider slider = new JSlider(0, 100, 100);
		slider.setName("Relevance");
	    slider.setMajorTickSpacing(25);  
	    slider.setPaintTicks(true);  
//	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
//	    labelTable.put(new Integer(100), new JLabel("1"));  
//	    labelTable.put(new Integer(75), new JLabel("0.75"));  
//	    labelTable.put(new Integer(50), new JLabel("0.5"));  
//	    labelTable.put(new Integer(25), new JLabel("0.2"));  
//	    labelTable.put(new Integer(0), new JLabel("0"));  
//	    slider.setLabelTable( labelTable );  
//	    slider.setPaintLabels(true);  
		slider.setBounds(370, 25, 100, 30);
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
		textFieldValue.setName("Value2");
		textFieldValue.setColumns(10);
		textFieldValue.setBounds(273, 25, 86, 20);
		panelCriteria.add(textFieldValue);
		
		return panelCriteria;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JPanel createPanelCriteraSec(Node n) {

		JPanel panelCriteria = new JPanel();
		panelCriteria.setName("Security");
//		panelCriteria.setBounds(5, 5, 510, 50);
		panelCriteria.setPreferredSize(new Dimension(480, 20));
//		leftPanel.add(panelCriteria);
		panelCriteria.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Goal");
		lblNewLabel_1.setBounds(7, 7, 156, 14);
		panelCriteria.add(lblNewLabel_1);
		
		JComboBox comboBoxGoal = new JComboBox();
		comboBoxGoal.setName("Value1");
		comboBoxGoal.setBounds(7, 25, 120, 20);
		comboBoxGoal.setModel(new DefaultComboBoxModel(new String[] 
				{"Authentication","Authorization","Availability", "Anonymity", 
						"Accountability", "Confidentiality", "Correctness", "Identification", "PolicyCompliance", "Privacy", ""}));
		panelCriteria.add(comboBoxGoal);
		
		JLabel lblValue = new JLabel("Mechanism");
		lblValue.setBounds(128, 7, 88, 14);
		panelCriteria.add(lblValue);
		
		JComboBox comboBoxMechanism = new JComboBox();
		comboBoxMechanism.setBounds(128, 25, 120, 20);
		comboBoxMechanism.setName("Value2");
		comboBoxMechanism.setModel(new DefaultComboBoxModel(new String[] 
				{"AccessControl", "Certificate", "Cryptography", 
				"Token", "Certification", "CertificateExchange", "Monitoring", "PasswordExchange", "UsageControl", ""}));
		panelCriteria.add(comboBoxMechanism);
		
		JLabel lblTechno = new JLabel("Technology");
		lblTechno.setBounds(250, 7, 88, 14);
		panelCriteria.add(lblTechno);
		
		JComboBox comboBoxTech = new JComboBox();
		comboBoxTech.setBounds(250, 25, 120, 20);
		comboBoxTech.setName("Value3");
		comboBoxTech.setModel(new DefaultComboBoxModel(ToStringArray.getSecurityTechnologies()));
		panelCriteria.add(comboBoxTech);
		
		//
		JLabel lblRelevance = new JLabel("Relevance 0-1");
		lblRelevance.setBounds(370, 7, 88, 14);
		panelCriteria.add(lblRelevance);
		//
		final JSlider slider = new JSlider(0, 100, 100); 
		slider.setName("Relevance");
	    slider.setMajorTickSpacing(25);  
	    slider.setPaintTicks(true);  
//	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
//	    labelTable.put(new Integer(100), new JLabel("1.0"));  
//	    labelTable.put(new Integer(75), new JLabel("0.75"));  
//	    labelTable.put(new Integer(50), new JLabel("0.50"));  
//	    labelTable.put(new Integer(25), new JLabel("0.25"));  
//	    labelTable.put(new Integer(0), new JLabel("0.0"));  
//	    slider.setLabelTable( labelTable );  
//	    slider.setPaintLabels(true);  
		slider.setBounds(370, 25, 100, 30);
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JPanel createPanelCriteraCertificateDetails(Node n) {

		JPanel panelCriteria = new JPanel();
		panelCriteria.setName("Certificate");
//		panelCriteria.setBounds(5, 5, 510, 50);
		panelCriteria.setPreferredSize(new Dimension(480, 20));
//		leftPanel.add(panelCriteria);
		panelCriteria.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("CA Authority");
		lblNewLabel_1.setBounds(7, 7, 156, 14);
		panelCriteria.add(lblNewLabel_1);
		
		JComboBox comboBoxGoal = new JComboBox();
		comboBoxGoal.setBounds(7, 25, 120, 20);
		comboBoxGoal.setName("Value1");
		comboBoxGoal.setModel(new DefaultComboBoxModel(ToStringArray.getCAProviders()));
		panelCriteria.add(comboBoxGoal);
		
		JLabel lblValue = new JLabel("CA Country");
		lblValue.setBounds(128, 7, 88, 14);
		panelCriteria.add(lblValue);
		
		JComboBox comboBoxMechanism = new JComboBox();
		comboBoxMechanism.setBounds(128, 25, 120, 20);
		comboBoxMechanism.setName("Value2");
		comboBoxMechanism.setModel(new DefaultComboBoxModel(new String[] {"US", "Europe", ""}));
		panelCriteria.add(comboBoxMechanism);
		//
		JLabel lblRelevance = new JLabel("Relevance 0-1");
		lblRelevance.setBounds(370, 7, 88, 14);
		panelCriteria.add(lblRelevance);
		//
		final JSlider slider = new JSlider(0, 100, 100);  
	    slider.setMajorTickSpacing(25);
	    slider.setName("Relevance");
	    slider.setPaintTicks(true);  
//	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
//	    labelTable.put(new Integer(100), new JLabel("1.0"));  
//	    labelTable.put(new Integer(75), new JLabel("0.75"));  
//	    labelTable.put(new Integer(50), new JLabel("0.50"));  
//	    labelTable.put(new Integer(25), new JLabel("0.25"));  
//	    labelTable.put(new Integer(0), new JLabel("0.0"));  
//	    slider.setLabelTable( labelTable );  
//	    slider.setPaintLabels(true);  
		slider.setBounds(370, 25, 100, 30);
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

	protected void showViewText(String descr, String title, Boolean isPlainText) {
		ViewerDialog dialog = new ViewerDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setContent("",isPlainText );
		dialog.setContent(descr, isPlainText);
		dialog.setLocationRelativeTo(null);
		dialog.setTitle(title);
		dialog.setVisible(true);
	}

	
	
	private String parse(String item) {
		int from = item.indexOf("[");
		int to = item.indexOf("]");
		return item.substring(from+1, to);
	}
	
	public JFrame getFrame() {
		return frmComposeServiceRecommender;
	}
	
	
	
}

