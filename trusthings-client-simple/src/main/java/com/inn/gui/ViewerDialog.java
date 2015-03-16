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
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JEditorPane;

public class ViewerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	boolean isNeedCursorChange=true;

	private final JScrollPane contentPanel = new JScrollPane();
	
	JEditorPane editorPane ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ViewerDialog dialog = new ViewerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ViewerDialog() {
		setBounds(100, 100, 800, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		//contentPanel.setLayout(new BorderLayout(0, 0));
		{

			editorPane = new JEditorPane();
			contentPanel.getViewport().add(editorPane);
			editorPane.addHyperlinkListener(new HyperlinkListener() {
				
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				//public void hyperlinkUpdate(HyperlinkEvent e) {
		        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            try {
		            	openWebpage(e.getURL());
//		                editorPane.setPage(e.getURL());
		            } catch (Exception e1) {
		                e1.printStackTrace();
		            }
		        }
				
			}
		});
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
			//	buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				//buttonPane.add(cancelButton);
			}
		}
		
	}
	
	public void setContent(String content, Boolean isPlainText){
		if (isPlainText){
			editorPane.setEditorKit(null);
			editorPane.setContentType("text/plain");
		}
		else{
			editorPane.setContentType("text/html");
			prepareForHTML();
		}
		editorPane.setText(content);
	}
	
	
	
	

	 private void prepareForHTML() {
		 MyHTMLEditorKit kit=new MyHTMLEditorKit();
			editorPane.setEditorKit(kit);
	}





	public class MyHTMLEditorKit extends HTMLEditorKit {

	        MyLinkController handler=new MyLinkController();
	        public void install(JEditorPane c) {
	            MouseListener[] oldMouseListeners=c.getMouseListeners();
	            MouseMotionListener[] oldMouseMotionListeners=c.getMouseMotionListeners();
	            super.install(c);
	            //the following code removes link handler added by original
	            //HTMLEditorKit

	            for (MouseListener l: c.getMouseListeners()) {
	                c.removeMouseListener(l);
	            }
	            for (MouseListener l: oldMouseListeners) {
	                c.addMouseListener(l);
	            }

	            for (MouseMotionListener l: c.getMouseMotionListeners()) {
	                c.removeMouseMotionListener(l);
	            }
	            for (MouseMotionListener l: oldMouseMotionListeners) {
	                c.addMouseMotionListener(l);
	            }
	 
	            //add out link handler instead of removed one
	            c.addMouseListener(handler);
	            c.addMouseMotionListener(handler);
	        }
	 
	        public class MyLinkController extends LinkController {

	            public void mouseClicked(MouseEvent e) {
	                JEditorPane editor = (JEditorPane) e.getSource();
	 
	                if (editor.isEditable() && SwingUtilities.isLeftMouseButton(e)) {
	                    if (e.getClickCount()==1) {
	                        editor.setEditable(false);
	                        super.mouseClicked(e);
	                        editor.setEditable(true);
	                    }
	                }

	            }
	            public void mouseMoved(MouseEvent e) {
	                JEditorPane editor = (JEditorPane) e.getSource();
	 
	                if (editor.isEditable()) {
	                    isNeedCursorChange=false;
	                    editor.setEditable(false);
	                    isNeedCursorChange=true;
	                    super.mouseMoved(e);
	                    isNeedCursorChange=false;
	                    editor.setEditable(true);
	                    isNeedCursorChange=true;
	                }
	            }

	        }
	    }
	 public static void openWebpage(URI uri) {
		    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		        try {
		            desktop.browse(uri);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}

		public static void openWebpage(URL url) {
		    try {
		        openWebpage(url.toURI());
		    } catch (URISyntaxException e) {
		        e.printStackTrace();
		    }
		}
}
