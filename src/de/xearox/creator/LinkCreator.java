package de.xearox.creator;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JProgressBar;
import java.awt.Color;

@SuppressWarnings("serial")
public class LinkCreator extends JFrame {
	private JPanel contentPane;
	private JCheckBox chckbxDebug = new JCheckBox("DEBUG");
	private JCheckBox chckbxFromUrl = new JCheckBox("From URL");
	private TextArea textArea = new TextArea();
	private JCheckBox chckbxSaveToFile = new JCheckBox("Save to File?");
	private JCheckBox chckbxFromMainpage = new JCheckBox("From Mainpage");
	private List<String> gameLinks = new ArrayList<String>();
	private Map<String, String> games = new HashMap<String, String>();
	private JSpinner fromSpinner = new JSpinner();
	private JSpinner toSpinner = new JSpinner();
	private JScrollPane scrollPane = new JScrollPane();
	private JList<String> gameList = new JList<String>();
	private final JProgressBar progressBar = new JProgressBar();
	@SuppressWarnings("unused")
	private String selectedGame = "";
	private JTextField textField;
	private String searchText = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LinkCreator frame = new LinkCreator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LinkCreator() {
		setResizable(false);
		setTitle("IGG-GAMES Download Link Creator - Created by Xearox - Version 1.3.1");
		setDefaultCloseOperation(3);
		setBounds(100, 100, 973, 724);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		final JTextPane textPane = new JTextPane();
		textPane.setToolTipText("Field for page sourcecode or the direct link of the game");
		textPane.setBounds(10, 11, 424, 23);
		this.contentPane.add(textPane);

		ButtonGroup radioGroup = new ButtonGroup();

		JRadioButton megaRadioButton = new JRadioButton("Mega.co.nz");
		megaRadioButton.setSelected(true);
		megaRadioButton.setBounds(10, 67, 109, 23);
		this.contentPane.add(megaRadioButton);
		radioGroup.add(megaRadioButton);

		JRadioButton openloadRadioButton = new JRadioButton("Openload.co");
		openloadRadioButton.setBounds(10, 249, 109, 23);
		this.contentPane.add(openloadRadioButton);
		radioGroup.add(openloadRadioButton);

		JRadioButton kumpulBagiRadioButton = new JRadioButton("KumpulBagi");
		kumpulBagiRadioButton.setBounds(10, 145, 109, 23);
		this.contentPane.add(kumpulBagiRadioButton);
		radioGroup.add(kumpulBagiRadioButton);

		JRadioButton upFileRadioButton = new JRadioButton("UpFile");
		upFileRadioButton.setBounds(10, 171, 109, 23);
		this.contentPane.add(upFileRadioButton);
		radioGroup.add(upFileRadioButton);

		JRadioButton downAceRadioButton = new JRadioButton("DownACE");
		downAceRadioButton.setVisible(false);
		downAceRadioButton.setBounds(10, 275, 109, 23);
		this.contentPane.add(downAceRadioButton);
		radioGroup.add(downAceRadioButton);

		JRadioButton go4UpRadioButton = new JRadioButton("Go4Up");
		go4UpRadioButton.setBounds(10, 197, 109, 23);
		this.contentPane.add(go4UpRadioButton);
		radioGroup.add(go4UpRadioButton);

		JRadioButton uploadedRadioButton = new JRadioButton("Uploaded");
		uploadedRadioButton.setBounds(10, 119, 109, 23);
		this.contentPane.add(uploadedRadioButton);
		radioGroup.add(uploadedRadioButton);

		JRadioButton upToBoxRadioButton = new JRadioButton("Uptobox");
		upToBoxRadioButton.setBounds(10, 223, 109, 23);
		this.contentPane.add(upToBoxRadioButton);
		radioGroup.add(upToBoxRadioButton);

		JRadioButton googleDriveRadioButton = new JRadioButton("Google Drive");
		googleDriveRadioButton.setBounds(10, 93, 109, 23);
		this.contentPane.add(googleDriveRadioButton);
		radioGroup.add(googleDriveRadioButton);

		JButton generateButton = new JButton("Generate Links");
		generateButton.setToolTipText("Click here, if you want to create the download links");
		generateButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				Component[] arrayOfComponent;
				int j = (arrayOfComponent = LinkCreator.this.contentPane.getComponents()).length;
				for (int i = 0; i < j; i++) {
					Object obj = arrayOfComponent[i];
					if (((obj instanceof JRadioButton)) && (((JRadioButton) obj).isSelected())) {
						LinkCreator.this.generateLinks(((JRadioButton) obj).getText(), textPane);
					}
				}
			}
		});
		generateButton.setBounds(124, 69, 128, 34);
		this.contentPane.add(generateButton);
		this.chckbxDebug.setVisible(false);
		this.chckbxDebug.setBounds(121, 232, 97, 23);
		this.contentPane.add(this.chckbxDebug);

		this.chckbxFromUrl.setBounds(121, 180, 97, 23);
		this.contentPane.add(this.chckbxFromUrl);

		this.textArea.setBounds(10, 353, 437, 324);
		this.contentPane.add(this.textArea);

		this.chckbxSaveToFile.setBounds(121, 206, 97, 23);
		this.contentPane.add(this.chckbxSaveToFile);

		this.chckbxFromMainpage.setBounds(121, 154, 123, 23);
		this.contentPane.add(this.chckbxFromMainpage);

		this.toSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		this.toSpinner.setBounds(181, 310, 40, 20);
		this.contentPane.add(this.toSpinner);

		this.fromSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		this.fromSpinner.setBounds(181, 285, 40, 20);
		this.contentPane.add(this.fromSpinner);

		JLabel lblPages = new JLabel("Page Range");
		lblPages.setHorizontalAlignment(SwingConstants.CENTER);
		lblPages.setBounds(125, 262, 96, 14);
		this.contentPane.add(lblPages);

		JLabel lblFrom = new JLabel("From");
		lblFrom.setBounds(125, 288, 46, 14);
		this.contentPane.add(lblFrom);

		JLabel lblTo = new JLabel("To");
		lblTo.setBounds(125, 313, 46, 14);
		this.contentPane.add(lblTo);

		this.scrollPane.setBounds(512, 42, 455, 653);
		this.contentPane.add(this.scrollPane);
		this.gameList.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) e.getSource();
				String gamename = (String) list.getSelectedValue();
				textPane.setText((String) LinkCreator.this.games.get(gamename));
				LinkCreator.this.chckbxFromUrl.setSelected(true);
				Component[] arrayOfComponent;
				int j = (arrayOfComponent = LinkCreator.this.contentPane.getComponents()).length;
				for (int i = 0; i < j; i++) {
					Object obj = arrayOfComponent[i];
					if (((obj instanceof JRadioButton)) && (((JRadioButton) obj).isSelected())) {
						LinkCreator.this.generateLinks(((JRadioButton) obj).getText(), textPane);
					}
				}
				System.out.println(gamename);
			}
		});
		this.scrollPane.setViewportView(this.gameList);

		JButton btnGetGames = new JButton("Get Games");
		btnGetGames.setToolTipText("Click here, if you want to get all games in a specific page range.");
		btnGetGames.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						gameLinks.clear();
						games.clear();
						if (LinkCreator.this.chckbxFromMainpage.isSelected()) {
							try {
								if (LinkCreator.this.getGameLinks()) {
									SwingUtilities.invokeLater(new Runnable() {
							            @Override
							            public void run() {
							            	DefaultListModel<String> listModel = new DefaultListModel<String>();
											for (String string : LinkCreator.this.games.keySet()) {
												listModel.addElement(string);
											}
											LinkCreator.this.gameList.setModel(listModel);
											LinkCreator.this.gameList.updateUI();
							            }
							        });
									
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						JOptionPane.showMessageDialog(LinkCreator.this, games.size()+" Games was found!");
					}
				});
				thread.start();
			}
		});
		btnGetGames.setBounds(124, 114, 128, 33);
		this.contentPane.add(btnGetGames);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				List<String>templist = new ArrayList<String>(games.keySet());
				if(e.getKeyCode() != 8){
					LinkCreator.this.searchText = LinkCreator.this.searchText + e.getKeyChar();
					System.out.println(LinkCreator.this.searchText);
					for(int i=0;i<templist.size();i++){
						if(templist.get(i).toLowerCase().contains(LinkCreator.this.searchText.toLowerCase())){
							gameList.setSelectedIndex(i);
							gameList.ensureIndexIsVisible(gameList.getSelectedIndex());
						}
					}
				} else {
					if(LinkCreator.this.searchText.length() > 0){
						LinkCreator.this.searchText = LinkCreator.this.searchText.substring(0, LinkCreator.this.searchText.length()-1);
						for(int i=0;i<templist.size();i++){
							if(templist.get(i).toLowerCase().contains(LinkCreator.this.searchText.toLowerCase())){
								gameList.setSelectedIndex(i);
								gameList.ensureIndexIsVisible(gameList.getSelectedIndex());
							}
						}
					} else {
						gameList.clearSelection();
						gameList.ensureIndexIsVisible(gameList.getSelectedIndex());
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(textField.getText().equalsIgnoreCase("")){
					LinkCreator.this.searchText = "";
					gameList.clearSelection();
					gameList.ensureIndexIsVisible(gameList.getSelectedIndex());
				}
			}
		});
		textField.setBounds(512, 11, 455, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblHoster = new JLabel("Hoster");
		lblHoster.setHorizontalAlignment(SwingConstants.CENTER);
		lblHoster.setBounds(20, 46, 88, 14);
		contentPane.add(lblHoster);
		progressBar.setForeground(new Color(102, 255, 102));
		
		progressBar.setBounds(10, 333, 437, 14);
		contentPane.add(progressBar);
		
		JLabel previewImage = new JLabel("");
		previewImage.setBounds(280, 67, 210, 210);
		contentPane.add(previewImage);
	}

	public boolean generateLinks(String providerName, JTextPane textPane) {
		if (providerName.equalsIgnoreCase("")) {
			return false;
		}
		if (textPane == null) {
			return false;
		}
		if (textPane.getText().equalsIgnoreCase("")) {
			return false;
		}
		this.textArea.setText("");

		String input = "";
		if (!this.chckbxFromUrl.isSelected()) {
			input = textPane.getText();
		} else {
			try {
				input = getPageSource(textPane.getText());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this.contentPane, "The entered URL is not VALID!", "Try again!", 0);
				e.printStackTrace();
			}
		}
		String gamename = input.substring(input.indexOf("<title>") + 7, input.indexOf("</title>"));
		gamename = gamename.replaceAll("Free Download - IGGGAMES", "");
		gamename = gamename.replaceAll(" ", "-");
		if ((this.chckbxDebug.isSelected()) && (this.chckbxFromUrl.isSelected())) {
			input = input.substring(input.lastIndexOf("<b>Link " + providerName));
			System.out.println(input);
			return true;
		}
		ProviderEnum provider = ProviderEnum.getProviderByName(providerName);
		if (provider == null) {
			return false;
		}
		input = input.substring(input.lastIndexOf("<b>Link " + providerName));
		input = input.replace("&nbsp;", " ");
		input = input.replace("<b>Link " + providerName + ":</b><br />Â Â Â Â ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br /> Â Â Â Â", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />       ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />Â Â Â Â", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />      ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />     ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />    ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />    ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />   ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br />  ", " &#8211; ");
		input = input.replace("<b>Link " + providerName + ":</b><br /> ", " &#8211; ");
		input = input.substring(0, input.indexOf("</p>"));
		input = input.replaceAll("</a>", "\n");
		if (!this.chckbxDebug.isSelected()) {
			for (String placeholder : provider.getPlaceholders()) {
				String replacement = (String) provider.getReplacer()
						.get(provider.getPlaceholders().indexOf(placeholder));
				input = input.replaceAll(placeholder, replacement);
			}
		}
		this.textArea.setText(input);
		if (this.chckbxSaveToFile.isSelected()) {
			String path = LinkCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.replace("linkcreator.jar", "");
			File file = new File(path + "/" + gamename + "-" + provider.name() + ".txt");
			try {
				PrintWriter pw = new PrintWriter(file);
				pw.write(input);
				pw.flush();
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("File saved to: " + file.getAbsolutePath());
		}
		return true;
	}

	public boolean getGameLinks() throws IOException {
		int pageFromNum = ((Integer) this.fromSpinner.getValue()).intValue();
		int pageToNum = ((Integer) this.toSpinner.getValue()).intValue();
		if (pageFromNum > pageToNum) {
			return false;
		}
		String url;
		for (int i = pageFromNum; i <= pageToNum; i++) {
			url = "http://igg-games.com/page/" + i;
			filterPageSource(url);
			double d1 = (double)i;
			double d2 = (double)pageToNum;
			Double progress = (d1/d2)*100;
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	progressBar.setValue(progress.intValue());
	            }
	        });
		}
		for (String string : this.gameLinks) {
			String gameurl = string.substring(string.indexOf("href=\"") + 6, string.indexOf("\" title="));
			String gamename = string.substring(string.indexOf("\" title=") + 9, string.length() - 2);
			gamename = gamename.replace("Free Download", "");
			gamename = gamename.replace("&#8217;", "'");
			gamename = gamename.replace("&#8211;", "-");
			this.games.put(gamename, gameurl);
		}
		games = new TreeMap<>(games);
		return true;
	}

	private void filterPageSource(String url) throws IOException {
		String endGameLink = "<!-- /post -->";
		String begin = "<a class=\"post-thumb \"";
		String end = "\">";
		String pagesource = getPageSource(url);
		for (int i = 0; i < 10; i++) {
			try{
				pagesource = pagesource.substring(pagesource.indexOf(begin));
				String result = pagesource.substring(pagesource.indexOf(begin), pagesource.indexOf(end) + 2);
				String replacement = pagesource.substring(pagesource.indexOf(begin), pagesource.indexOf(endGameLink));
				pagesource = pagesource.replace(replacement, "");
				this.gameLinks.add(result);
			} catch(Exception e){
				
			}
		}
	}

	public String getPageSource(String pageLink) throws IOException {
		URL url = new URL(pageLink);

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuffer response = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();
		return response.toString();
	}
	
	void updateProgress(final int newValue) {
        progressBar.setValue(newValue);
    }

    public void setValue(final int j) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateProgress(j);
            }
        });
    }
}
