package de.xearox.creator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.apache.commons.codec.binary.Base64;

import de.xearox.creator.Game;
import de.xearox.creator.LinkCreator;
import de.xearox.creator.Parser;
import de.xearox.creator.ProviderEnum;
import de.xearox.creator.Utilz;

public class LinkCreatorWindow extends JPanel {
	
public static final String VERSION = "1.5.2";
	
	private JPanel contentPane;
	private JButton generateButton = new JButton("Generate Links");
	private JCheckBox chckbxDebug = new JCheckBox("DEBUG");
	private JCheckBox chckbxFromUrl = new JCheckBox("From URL");
	private TextArea textArea = new TextArea();
	private JCheckBox chckbxSaveToFile = new JCheckBox("Save to File?");
	private JCheckBox chckbxFromMainpage = new JCheckBox("From Mainpage");
	private JCheckBox chckbxFromBrowser = new JCheckBox("From Browser");
	private JLabel previewImage = new JLabel("");
	private List<String> gameLinks = new ArrayList<String>();
	private Map<String, Game> games = new HashMap<String, Game>();
	private JSpinner fromSpinner = new JSpinner();
	private JSpinner toSpinner = new JSpinner();
	private JScrollPane scrollPane = new JScrollPane();
	private JList<String> gameList = new JList<String>();
	private final JProgressBar progressBar = new JProgressBar();
	@SuppressWarnings("unused")
	private String selectedGame = "";
	private JTextField textField;
	private String searchText = "";
	public Properties prop;
	
	public static LinkCreator instance;
	public static final String changelogURL = "http://pastebin.com/raw/y51jHcb5";
	
	
	public LinkCreatorWindow() {
		setupWindow();
	}
	
	private void setupWindow(){
		this.contentPane = this;
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		final JTextPane textPane = new JTextPane();
		textPane.setToolTipText("Field for page sourcecode or the direct link of the game");
		textPane.setBounds(10, 11, 424, 23);
		add(textPane);

		ButtonGroup radioGroup = new ButtonGroup();

		JRadioButton megaRadioButton = new JRadioButton("Mega.co.nz");
		megaRadioButton.setSelected(true);
		megaRadioButton.setBounds(10, 67, 109, 23);
		add(megaRadioButton);
		radioGroup.add(megaRadioButton);

		JRadioButton openloadRadioButton = new JRadioButton("Openload.co");
		openloadRadioButton.setBounds(10, 249, 109, 23);
		add(openloadRadioButton);
		radioGroup.add(openloadRadioButton);

		JRadioButton kumpulBagiRadioButton = new JRadioButton("KumpulBagi");
		kumpulBagiRadioButton.setBounds(10, 145, 109, 23);
		add(kumpulBagiRadioButton);
		radioGroup.add(kumpulBagiRadioButton);

		JRadioButton upFileRadioButton = new JRadioButton("UpFile");
		upFileRadioButton.setBounds(10, 171, 109, 23);
		add(upFileRadioButton);
		radioGroup.add(upFileRadioButton);

		JRadioButton downAceRadioButton = new JRadioButton("DownACE");
		downAceRadioButton.setVisible(false);
		downAceRadioButton.setBounds(10, 275, 109, 23);
		add(downAceRadioButton);
		radioGroup.add(downAceRadioButton);

		JRadioButton go4UpRadioButton = new JRadioButton("Go4Up");
		go4UpRadioButton.setBounds(10, 197, 109, 23);
		add(go4UpRadioButton);
		radioGroup.add(go4UpRadioButton);

		JRadioButton uploadedRadioButton = new JRadioButton("Uploaded");
		uploadedRadioButton.setBounds(10, 119, 109, 23);
		add(uploadedRadioButton);
		radioGroup.add(uploadedRadioButton);

		JRadioButton upToBoxRadioButton = new JRadioButton("Uptobox");
		upToBoxRadioButton.setBounds(10, 223, 109, 23);
		add(upToBoxRadioButton);
		radioGroup.add(upToBoxRadioButton);

		JRadioButton googleDriveRadioButton = new JRadioButton("Google Drive");
		googleDriveRadioButton.setBounds(10, 93, 109, 23);
		add(googleDriveRadioButton);
		radioGroup.add(googleDriveRadioButton);

		generateButton.setToolTipText("Click here, if you want to create the download links");
		generateButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				Component[] arrayOfComponent;
				int j = (arrayOfComponent = contentPane.getComponents()).length;
				for (int i = 0; i < j; i++) {
					Object obj = arrayOfComponent[i];
					if (((obj instanceof JRadioButton)) && (((JRadioButton) obj).isSelected())) {
						generateLinks(((JRadioButton) obj).getText(), textPane);
					}
				}
			}
		});
		generateButton.setBounds(125, 45, 128, 34);
		add(generateButton);
		chckbxDebug.setVisible(false);
		this.chckbxDebug.setBounds(121, 232, 97, 23);
		add(this.chckbxDebug);

		this.chckbxFromUrl.setBounds(121, 180, 97, 23);
		add(this.chckbxFromUrl);

		this.textArea.setBounds(10, 353, 437, 324);
		add(this.textArea);

		this.chckbxSaveToFile.setBounds(121, 206, 97, 23);
		add(this.chckbxSaveToFile);

		this.chckbxFromMainpage.setBounds(121, 154, 123, 23);
		add(this.chckbxFromMainpage);

		this.toSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		this.toSpinner.setBounds(181, 310, 40, 20);
		add(this.toSpinner);

		this.fromSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		this.fromSpinner.setBounds(181, 285, 40, 20);
		add(this.fromSpinner);

		JLabel lblPages = new JLabel("Page Range");
		lblPages.setHorizontalAlignment(SwingConstants.CENTER);
		lblPages.setBounds(125, 262, 96, 14);
		add(lblPages);

		JLabel lblFrom = new JLabel("From");
		lblFrom.setBounds(125, 288, 46, 14);
		add(lblFrom);

		JLabel lblTo = new JLabel("To");
		lblTo.setBounds(125, 313, 46, 14);
		add(lblTo);

		this.scrollPane.setBounds(512, 42, 455, 653);
		add(this.scrollPane);
		this.gameList.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) e.getSource();
				String gamename = (String) list.getSelectedValue();
				if(gamename == null) return;
				textPane.setText((String) games.get(gamename).getGameURL());
				previewImage.setIcon(new ImageIcon(games.get(gamename).getImage()));
				chckbxFromUrl.setSelected(true);
				Component[] arrayOfComponent;
				int j = (arrayOfComponent = contentPane.getComponents()).length;
				for (int i = 0; i < j; i++) {
					Object obj = arrayOfComponent[i];
					if (((obj instanceof JRadioButton)) && (((JRadioButton) obj).isSelected())) {
						generateLinks(((JRadioButton) obj).getText(), textPane);
					}
				}
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
						if (chckbxFromMainpage.isSelected()) {
							try {
								if (getGameLinks()) {
									SwingUtilities.invokeLater(new Runnable() {
							            @Override
							            public void run() {
							            	fillList();
							            }
							        });
									
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						JOptionPane.showMessageDialog(null, games.size()+" Games was found!");
					}
				});
				thread.start();
			}
		});
		btnGetGames.setBounds(125, 88, 128, 33);
		add(btnGetGames);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				List<String>templist = new ArrayList<String>(games.keySet());
				if(e.getKeyCode() != 8){
					searchText = searchText + e.getKeyChar();
					System.out.println(searchText);
					for(int i=0;i<templist.size();i++){
						if(templist.get(i).toLowerCase().contains(searchText.toLowerCase())){
							gameList.setSelectedIndex(i);
							gameList.ensureIndexIsVisible(gameList.getSelectedIndex());
						}
					}
				} else {
					if(searchText.length() > 0){
						searchText = searchText.substring(0, searchText.length()-1);
						for(int i=0;i<templist.size();i++){
							if(templist.get(i).toLowerCase().contains(searchText.toLowerCase())){
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
					searchText = "";
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
		
		previewImage.setBounds(285, 67, 210, 210);
		contentPane.add(previewImage);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showConfirmDialog(null, Utilz.optionMenu(instance), "Options", JOptionPane.CLOSED_OPTION);
				} catch (HeadlessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});
		btnOptions.setBounds(10, 309, 88, 23);
		contentPane.add(btnOptions);
		
		chckbxFromBrowser.setBounds(121, 128, 97, 23);
		add(chckbxFromBrowser);
	}
	
	

	public boolean generateLinks(String providerName, JTextPane textPane) {
		if (providerName.equalsIgnoreCase("")) {
			return false;
		}
		if (textPane == null && !this.chckbxFromBrowser.isSelected()) {
			return false;
		}
		if (textPane.getText().equalsIgnoreCase("") && !this.chckbxFromBrowser.isSelected()) {
			return false;
		}
		this.textArea.setText("");

		String input = "";
		if (!this.chckbxFromUrl.isSelected()) {
			input = textPane.getText();
		} else {
			try {
				input = Utilz.getPageSource(textPane.getText(), false);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this.contentPane, "The entered URL is not VALID!", "Try again!", 0);
				Utilz.logger(this, e);
			}
		}
		if(this.chckbxFromBrowser.isSelected()){
			input = LinkCreator.browser.getHTML();
		}
		
		if(input.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(this.contentPane, "Address Box Empty", "Try again!", 0);
			return false;
		}
		
		String gamename = input.substring(input.indexOf("<title>") + 7, input.indexOf("</title>"));
		gamename = gamename.replaceAll("Free Download - IGGGAMES", "");
		gamename = gamename.replaceAll(" ", "-");
		if ((this.chckbxDebug.isSelected()) && (this.chckbxFromUrl.isSelected())) {
			input = input.substring(input.lastIndexOf("<b>Link " + providerName));
			return true;
		}
		ProviderEnum provider = ProviderEnum.getProviderByName(providerName);
		if (provider == null) {
			return false;
		}
		
		input = input.replace("&nbsp;", " ");
		input = input.substring(input.lastIndexOf("<b>Link " + providerName));
		input = input.replaceAll("<b>Link " + providerName + ":</b><br>.*?<a href", " &#8211; <a href");
		input = input.replaceAll("<b>Link " + providerName + ":</b><br>.*?<a rel=\"nofollow\"", " &#8211; <a href");
		//input = input.replaceAll(" – <a rel=\"nofollow\"/>.*?html?", " &#8211; <a href");
		input = input.substring(0, input.indexOf("</p>"));
		
		input = input.replaceAll("</a> – ", "\n</a>");
		input = input.replaceAll("<b>.*?<a", "</a><a");
		
		String originalInput = input;
		
		input = Parser.mode1(input);
		input = Parser.finalStep(provider, input);
		
		if(input.equalsIgnoreCase("")){
			input = originalInput;
			input = Parser.mode2(provider, input);
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
				Utilz.logger(this, e);
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
			String gamename = string.substring(string.indexOf("\" title=") + 9, string.indexOf("\">"));
			String imagePath = string.substring(string.indexOf("<img width=")+35, string.indexOf(" class=\"attachment")-1);
			gamename = gamename.replace("Free Download", "");
			gamename = gamename.replace("&#8217;", "'");
			gamename = gamename.replace("&#8211;", "-");
			this.games.put(gamename, new Game(gamename, gameurl, imagePath));
		}
		games = new TreeMap<>(games);
		return true;
	}

	private void filterPageSource(String url) throws IOException {
		String endGameLink = "<!-- /post -->";
		String begin = "<a class=\"post-thumb \"";
		String end = "</a>";
		String pagesource = Utilz.getPageSource(url, false);
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
	
	public Map<String, Game> getGames() {
		return games;
	}

	public void setGames(Map<String, Game> games) {
		this.games = games;
	}
	
	public void fillList(){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String string : this.games.keySet()) {
			listModel.addElement(string);
		}
		this.gameList.setModel(listModel);
		this.gameList.updateUI();
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
