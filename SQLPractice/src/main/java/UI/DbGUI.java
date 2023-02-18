package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import controller.APIController;

public class DbGUI {
	
	public static final String CATEGORYPANEL = "Category Page";
	public static final String ITEMPANEL = "Item Page";
	public static final String DISPLAYPANEL = "Display Page";
	
	private static APIController controller = new APIController();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                @SuppressWarnings("unused")
				final StartWindow startWindow = new StartWindow();
            }
        });
    }
    
    private static class StartWindow extends JFrame {

		private static final long serialVersionUID = 1L;

		public StartWindow() {
    		
    		super("Database Practice");
    		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		setVisible(true);
    		setLayout(new BorderLayout());
    		
            Pages pages = new Pages();
            add(pages, BorderLayout.CENTER);
    		
    		PageSelect cardsPanel = new PageSelect(pages);
    		add(cardsPanel, BorderLayout.NORTH);
    		
    		pack();
    		
    	}
    	
    }
    
    private static class Pages extends JPanel {
    	
		private static final long serialVersionUID = 1L;

		public Pages() {
    		
    		setVisible(true);
    		setLayout(new CardLayout());
    		
    		CategoryPage categoryPage = new CategoryPage();
    		add(categoryPage, CATEGORYPANEL);
    		ItemPage itemPage = new ItemPage();
    		add(itemPage, ITEMPANEL);
    		DisplayPage displayPage = new DisplayPage();
    		add(displayPage, DISPLAYPANEL);
    		
    	}
    	
    }
    
    private static class PageSelect extends JPanel {
    	
		private static final long serialVersionUID = 1L;

		public PageSelect(JPanel pages) {
    		
	    	String[] pageTitles = { CATEGORYPANEL, ITEMPANEL, DISPLAYPANEL };
	    	JComboBox<String> jComboBox = new JComboBox<>(pageTitles);
	    	jComboBox.setEditable(false);
	    	jComboBox.addActionListener(new ActionListener() {
	    		
	    		@Override
	    		public void actionPerformed(ActionEvent e) {
	    			//transfer current category to item page
	    			if ( ITEMPANEL.equals((String)jComboBox.getSelectedItem()) ) {
	    				ItemPage itemPage = null;
	    				CategoryPage categoryPage = null;
	    				Component[] components = pages.getComponents();
	    				for (Component c : components) {
	    					if (c instanceof ItemPage) {
	    						itemPage = (ItemPage)c;
	    					}
	    					if (c instanceof CategoryPage) {
	    						categoryPage = (CategoryPage)c;
	    					}
	    				}
	    				itemPage.setCategory(categoryPage.getCategory());
	    			}
	    			//transfer current category table data to display page
	    			if ( DISPLAYPANEL.equals((String)jComboBox.getSelectedItem()) ) {
	    				CategoryPage categoryPage = null;
	    				DisplayPage displayPage = null;
	    				Component[] components = pages.getComponents();
	    				for (Component c : components) {
	    					if (c instanceof CategoryPage) {
	    						categoryPage = (CategoryPage)c;
	    					}
	    					if (c instanceof DisplayPage) {
	    						displayPage = (DisplayPage)c;
	    					}
	    				}
	    				String[][] data = controller.getData(categoryPage.getCategory());
	    				displayPage.updateTable(data);
	    				displayPage.updateIdSelect(displayPage.getTable());
	    				displayPage.setCategory(categoryPage.getCategory());
	    			}
	    			
	    			CardLayout cardLayout = (CardLayout)(pages.getLayout());
	    			cardLayout.show(pages, (String)jComboBox.getSelectedItem());
	    		}
	    		
	    	});
	    	add(jComboBox);
    		
    	}
    	
    }
    
    private static class CategoryPage extends JPanel {
    	
		private static final long serialVersionUID = 1L;
		
		private JComboBox<String> categorySelect;

		public CategoryPage() {
    		
    		setVisible(true);
    		setLayout( new BorderLayout() );
    		
    		//load categories at start
    		String[] categoryTitles = new String[controller.getCategories().size()];
    		categoryTitles = controller.getCategories().toArray(categoryTitles);
    		
	    	categorySelect = new JComboBox<>(categoryTitles);
	    	categorySelect.setEditable(false);
	    	add(categorySelect, BorderLayout.NORTH);
    		
    		JPanel grid = new JPanel( new GridLayout(0, 2, 5, 0) );
    		
    		JTextField categoryInputText = new JTextField(30);
    		grid.add(categoryInputText);
    		
    		JButton categoryInputBtn = new JButton("add category");
    		categoryInputBtn.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				JOptionPane.showMessageDialog(CategoryPage.this, "category added", "info", JOptionPane.INFORMATION_MESSAGE);
    				controller.newCategory(categoryInputText.getText());
    				categorySelect.addItem(categoryInputText.getText());
    				categoryInputText.setText("");
    			}
    			
    		});
    		grid.add(categoryInputBtn);
    		
    		JButton deleteCategoryBtn = new JButton("delete current category");
    		deleteCategoryBtn.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				JOptionPane.showMessageDialog(CategoryPage.this, "category removed", "info", JOptionPane.INFORMATION_MESSAGE);
    				controller.deleteCategory(categorySelect.getSelectedItem().toString());
    				categorySelect.removeItem(categorySelect.getSelectedItem().toString());
    				categoryInputText.setText("");
    			}
    			
    		});
    		grid.add(new JPanel());
    		grid.add(deleteCategoryBtn);
    		
    		add(grid, BorderLayout.SOUTH);
    		
    	}
		
		public String getCategory() {
			return categorySelect.getSelectedItem().toString();
		}
    	
    }
    
    private static class ItemPage extends JPanel {
    	
		private static final long serialVersionUID = 1L;
		
		private JLabel categoryDisplay;
		
		private String currentCategory;

		public ItemPage() {
    		
    		setVisible(true);
    		setLayout( new BorderLayout() );
    		
    		categoryDisplay = new JLabel();
    		add(categoryDisplay, BorderLayout.NORTH);
    		
    		JPanel grid = new JPanel( new GridLayout(0, 1, 0, 5) );
    		
    		JLabel itemNameLabel = new JLabel("item name");
    		grid.add(itemNameLabel);
    		
    		JTextField itemInputText = new JTextField(30);
    		grid.add(itemInputText);
    		
    		JLabel itemDescriptionLabel = new JLabel("item description");
    		grid.add(itemDescriptionLabel);
    		
    		JTextField descriptionInputText = new JTextField(30);
    		grid.add(descriptionInputText);
    		
    		JButton itemInputBtn = new JButton("add item");
    		itemInputBtn.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				JOptionPane.showMessageDialog(ItemPage.this, "item added", "info", JOptionPane.INFORMATION_MESSAGE);
    				controller.addItem(currentCategory, itemInputText.getText(), descriptionInputText.getText());
    				itemInputText.setText("");
    				descriptionInputText.setText("");
    			}
    			
    		});
    		grid.add(itemInputBtn);
    		
    		add(grid, BorderLayout.SOUTH);
    		
    	}
		
		public void setCategory(String category) {
			categoryDisplay.setText("Current Category: " + category);
			currentCategory = category;
		}
    	
    }
    
    private static class DisplayPage extends JPanel {
    	
		private static final long serialVersionUID = 1L;
		
		private JTable displayTable;
		
		private JComboBox<Integer> idSelect;
		
		private String currentCategory;

		public DisplayPage() {
    		
    		setVisible(true);
    		setLayout( new BorderLayout() );
    		
    		String[] columnNames = { "Id", "Item", "Description" };
    		String[][] data = {
    				{ "id 1", "item 1", "description 1" },
    				{ "id 1", "item 2", "description 2" }
    		};
    		displayTable = new JTable(data, columnNames);
    		
    		JScrollPane jScrollPane = new JScrollPane(displayTable);
    		add(jScrollPane, BorderLayout.NORTH);
    		
    		JPanel editTable = new JPanel(new GridLayout(0, 2, 5, 0));
    		
	    	Integer[] ids = { 1, 2 };
	    	idSelect = new JComboBox<>(ids);
	    	idSelect.setEditable(false);
	    	editTable.add(idSelect);
    		
    		JButton deleteItemBtn = new JButton("delete item");
    		deleteItemBtn.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				JOptionPane.showMessageDialog(DisplayPage.this, "item removed", "info", JOptionPane.INFORMATION_MESSAGE);
    				controller.removeItem(currentCategory, (Integer)idSelect.getSelectedItem());
    				updateTable(controller.getData(currentCategory));
    				updateIdSelect(displayTable);
    			}
    			
    		});
    		editTable.add(deleteItemBtn);
    		
    		add(editTable, BorderLayout.SOUTH);
    		
    	}
		
		public void updateTable(String[][] data) {
    		String[] columnNames = { "Id", "Item", "Description" };
    		DefaultTableModel updateModel = new DefaultTableModel(data, columnNames);
    		displayTable.setModel(updateModel);
		}
		
		public void updateIdSelect(JTable table) {
			idSelect.removeAllItems();
			for (int i = 0; i < table.getRowCount(); i++) {
				idSelect.addItem(Integer.parseInt((String)table.getValueAt(i, 0)));
			}
		}
		
		public void setCategory(String category) {
			currentCategory = category;
		}
		
		public JTable getTable() {
			return displayTable;
		}
    	
    }

}
