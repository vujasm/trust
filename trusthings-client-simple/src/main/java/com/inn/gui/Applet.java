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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.LineBorder;

import com.inn.gui.util.ResourceReader;


public class Applet extends JApplet{
	
	
	private JTextField textFieldAttribute;
	private JTextField textFieldRelevance;
	private JTextField textFieldValue;
	JPanel mainpanel = new JPanel(new BorderLayout());
	JPanel frame = new JPanel();
	ViewerDialog dialog = new ViewerDialog();
	
	public Applet() {
		initialize();
		add(frame);
////		frame.getContentPane().setVisible(true);
	}
	

	

    
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
    	
    	frame.setLayout(null);
		
		frame.setSize(400, 400);
		
		JTree tree = new JTree();
		tree.setBounds(10, 10, 159, 389);
		frame.add(tree);
		
		final JList list = new JList();
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setBounds(169, 31, 425, 58);
		frame.add(list);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBounds(169, 342, 510, -253);
		frame.add(scrollPane);
		
		JButton btnGetTrustScore = new JButton("Get Trust Score");
		btnGetTrustScore.setBounds(325, 376, 127, 23);
		frame.add(btnGetTrustScore);
		
		JLabel lblNewLabel = new JLabel("Discovered service matching functional goal..");
		lblNewLabel.setBounds(169, 11, 300, 14);
		frame.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBounds(169, 100, 510, 50);
		frame.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Attribute");
		lblNewLabel_1.setBounds(7, 7, 156, 14);
		panel.add(lblNewLabel_1);
		
		textFieldAttribute = new JTextField();
		textFieldAttribute.setBounds(7, 25, 256, 20);
		panel.add(textFieldAttribute);
		textFieldAttribute.setColumns(10);
		
		JLabel lblValue = new JLabel("Value");
		lblValue.setBounds(272, 7, 88, 14);
		panel.add(lblValue);
		
		textFieldRelevance = new JTextField();
		textFieldRelevance.setBounds(370, 25, 86, 20);
		panel.add(textFieldRelevance);
		textFieldRelevance.setColumns(10);
		
		JLabel lblRelevance = new JLabel("Relevance");
		lblRelevance.setBounds(370, 7, 88, 14);
		panel.add(lblRelevance);
		
		textFieldValue = new JTextField();
		textFieldValue.setColumns(10);
		textFieldValue.setBounds(273, 25, 86, 20);
		panel.add(textFieldValue);
		
		JButton btnView = new JButton("View..");
		btnView.setBounds(598, 31, 89, 23);
		frame.add(btnView);
		btnView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String descr = ""; 
				String item = (String) list.getSelectedValue();
				if (item !=null){
					String id = parse(item);
//					try {
//						URL urlToProps = new URL("http://127.0.0.1:8888/web/request.json");
//						descr = urlToProps.toString();
//						URLConnection connection = urlToProps.openConnection(); 
//						InputStream is = connection.getInputStream();
//						descr = CharStreams.toString(new InputStreamReader(is));
//						is.close();
//					} catch (Exception e1) {
//						descr = descr+" exception "+e1.getMessage();
//						e1.printStackTrace();
//					}  
					
					descr = ResourceReader.getTrustDescr(id);
				}
				showAgentDescr(descr);
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frame.add(menuBar);
		
		JMenu menu = new JMenu("Choose demo..");
		menuBar.add(menu);
		
		JMenuItem menuItem1 = new JMenuItem("Demo 1");
		menu.add(menuItem1);
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.setModel(getDemo1Model());
			}
		});
		
		JMenuItem menuItem2 = new JMenuItem("Demo 2");
		menu.add(menuItem2);
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.setModel(getDemo2Model());
			}
		});
		
		JMenuItem menuItem3 = new JMenuItem("Demo 3");
		menu.add(menuItem3);
		menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.setModel(getDemo3Model());
			}
		});
		
		setJMenuBar(menuBar);
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
			String[] values = new String[] {"Wind Sensor API A (http://localhost/services/Wind_Sensor_A)",
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
	
	protected void showAgentDescr(String descr) {
		dialog.setContent("", true);
		dialog.setContent(descr, true);
		dialog.setVisible(true);
	}

}
