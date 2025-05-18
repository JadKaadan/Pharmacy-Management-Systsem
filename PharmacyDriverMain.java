
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class PharmacyDriverMain implements ActionListener {
    private static StoragePharmacy inventory = new StoragePharmacy();
    private JFrame frame;
    private JButton[] buttons = new JButton[9];
    private static final String[] buttonLabels = {
            "Enter new Drug Name:", "Add a special description:", "Update Price:",
            "Update Quantity:", "Sell Item:", "Refer to a Doctor:",
            "View Inventory:", "Remove Drug:", "Display Sold Items and Exit:"
    };

    public PharmacyDriverMain() {
        initializeFrame();
        addBackground();
        addButtons();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame();
        ImageIcon image = new ImageIcon("logoo.png");
        frame.setIconImage(image.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, -3, 1000, 1000);
        frame.setLayout(null);
        frame.setResizable(false);
    }

    private void addBackground() {
        ImageIcon backgroundImage = new ImageIcon("pharmacy.jpg");
        Image img = backgroundImage.getImage().getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon(img);
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 1000, 1000);
        frame.setContentPane(backgroundLabel);
    }

    private void addButtons() {
        Container container = frame.getContentPane();
        container.setLayout(null);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBounds(420, 50 + (i * 50), 200, 40);
            buttons[i].addActionListener(this);
            container.add(buttons[i]);
        }
    }

    public static void main(String[] args) {
        new PharmacyDriverMain();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            if (e.getSource() == buttons[i]) {
                handleUserInput(i + 1);
                break;
            }
        }
    }

    private static void handleUserInput(int choice) {
        switch (choice) {
            case 1:
                handleNewDrugEntry();
                break;
            case 2:
                handleAddDescription();
                break;
            case 3:

                String updatePriceName = JOptionPane.showInputDialog(null, "Enter Drug Name to update price:");
                double newPrice = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter New Price: $"));
                inventory.updatePrice(updatePriceName, newPrice);
                break;
            case 4:

                String updateQuantityName = JOptionPane.showInputDialog(null,
                        "Enter Drug Name to update quantity:");
                int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter New Quantity:"));
                inventory.updateQuantity(updateQuantityName, newQuantity);
                break;
            case 5:
                handleSellItem();
                break;
            case 6:

                String customerSentence = JOptionPane.showInputDialog(null, "Enter your symptoms:");
                inventory.referToDoctor(customerSentence);
                break;
            case 7:

                inventory.displayInventory();
                return;

            case 8:
                String removeDrugName = JOptionPane.showInputDialog(null, "Enter Drug Name to remove:");
                inventory.removeDrug(removeDrugName);
                break;

            case 9:

                inventory.displaySoldItems();
                JOptionPane.showMessageDialog(null, "Exiting Pharmacy Management.");
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid choice. Please enter a number between 1 and 7.");
                break;
        }
        inventory.displayInventory();
    }

    private static void handleNewDrugEntry() {

        String drugName = JOptionPane.showInputDialog(null, "Enter Drug Name:");
        String inputDescription = JOptionPane.showInputDialog(null,
                "Enter the description of the Drug:");
        int initialQuantity = Integer
                .parseInt(JOptionPane.showInputDialog(null, "Enter Initial Quantity:"));
        double initialPrice = Double
                .parseDouble(JOptionPane.showInputDialog(null, "Enter Initial Price: $"));
        inventory.addItem(new item(drugName, inputDescription, initialQuantity, initialPrice));

    }

    private static void handleSellItem() {

        String[] drugNames = inventory.getItemNames();
        JComboBox<String> drugNameComboBox = new JComboBox<>(drugNames);
        JOptionPane.showMessageDialog(null, drugNameComboBox, "Select Drug Name to sell:",
                JOptionPane.QUESTION_MESSAGE);

        String selectedDrugName = (String) drugNameComboBox.getSelectedItem();

        if (selectedDrugName == null || selectedDrugName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a drug to sell.");
            return;
        }

        String quantityStr = JOptionPane.showInputDialog(null, "Enter Quantity to Sell:");
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            int sellQuantity = Integer.parseInt(quantityStr);
            double discountPercentage = 0.0;

            int discountChoice = JOptionPane.showConfirmDialog(null,
                    "Do you want to apply a discount?", "Discount Confirmation", JOptionPane.YES_NO_OPTION);

            if (discountChoice == JOptionPane.YES_OPTION) {
                String discountPercentageStr = JOptionPane.showInputDialog(null, "Enter the discount percentage:");
                discountPercentage = Double.parseDouble(discountPercentageStr);
            }

            double totalPriceBeforeVAT = inventory.sellItem(selectedDrugName, sellQuantity, discountPercentage);

            final double VAT_RATE = 0.20;
            double vatAmount = totalPriceBeforeVAT * VAT_RATE;

            double finalPrice = totalPriceBeforeVAT + vatAmount;

            String receipt = "Receipt for " + selectedDrugName + "\n" +
                    "Quantity sold: " + sellQuantity + "\n" +
                    "Total (before VAT): $" + String.format("%.2f", totalPriceBeforeVAT) + "\n" +
                    "VAT (" + (int) (VAT_RATE * 100) + "%): $" + String.format("%.2f", vatAmount) + "\n" +
                    "Total (after VAT): $" + String.format("%.2f", finalPrice);
            JOptionPane.showMessageDialog(null, receipt);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid quantity entered.");
        }
    }

    private static void handleAddDescription() {
        String descDrugName = JOptionPane.showInputDialog(null, "Enter Drug Name to add a description:");
        String drugDescription = JOptionPane.showInputDialog(null, "Enter Description:");
        inventory.addDescription(descDrugName, drugDescription);

    }
}
