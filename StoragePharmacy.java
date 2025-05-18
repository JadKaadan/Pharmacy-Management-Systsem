
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StoragePharmacy {

    ArrayList<item> itemList = new ArrayList<>();
    ArrayList<item> soldItems = new ArrayList<>();

    final double VAT_RATE = 0.20;

    public StoragePharmacy() {
        itemList.addAll(item.getPredefinedItems());
        System.out.println("Total items in inventory after initialization: " + itemList.size());
    }

    public void addItem(item drugProduct) {

        for (item existingItem : itemList) {
            if (existingItem.hasSameName(drugProduct.itemName)) {
                JOptionPane.showMessageDialog(null, "Item with name '" + drugProduct.itemName + "' already exists.");
                return;
            }
        }
        itemList.add(drugProduct);
        JOptionPane.showMessageDialog(null, "Drug product has just been added to the inventory.");
    }

    public String[] getItemNames() {
        String[] itemNames = new String[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            itemNames[i] = itemList.get(i).getItemName();
        }
        return itemNames;
    }

    public void updatePrice(String drugName, double newP) {
        drugName = drugName.toLowerCase();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).hasSameName(drugName)) {
                itemList.get(i).updateP(newP);
                JOptionPane.showMessageDialog(null, "Price updated for " + drugName + ": $" + newP);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Item not found in inventory.");
    }

    public void updateQuantity(String drugName, int newQuantity) {
        drugName = drugName.toLowerCase();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).hasSameName(drugName)) {
                itemList.get(i).updateQuan(newQuantity);
                JOptionPane.showMessageDialog(null, "Quantity updated for " + drugName + ": " + newQuantity);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Item not found in inventory.");
    }

    public void displaySoldItems() {
        StringBuilder message = new StringBuilder("\nSold Items:\n");
        for (item soldItem : soldItems) {
            message.append(soldItem.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, message.toString());
    }

    public void referToDoctor(String customerSentence) {
        customerSentence = customerSentence.toLowerCase();

        String[] words = customerSentence.split(
                "\\s+|\\bi\\b|\\bhave\\b|\\bthe\\b|\\band\\b|\\ba\\b|\\bor\\b|\\bis\\b|\\bit\\b|\\bof\\b|\\bto\\b|\\bin\\b|\\bfor\\b|\\bon\\b|\\bwith\\b|\\bas\\b|\\bbut\\b|\\bi\\b|\\byou\\b|\\bhe\\b|\\bshe\\b|\\bthis\\b|\\bthat\\b|\\bhello\\b|\\bdr\\b|\\b,\\b");

        ArrayList<item> relevantItems = new ArrayList<>();
        for (item product : itemList) {
            for (String word : words) {

                if (product.getDescription().toLowerCase().contains(word)) {
                    relevantItems.add(product);
                    System.out.println("Relevant item found: " + product.getItemName());
                    break;
                }
            }
        }

        if (relevantItems.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No relevant drug products found based on your sentence.");
            return;
        }

        String[] itemNames = new String[relevantItems.size()];
        for (int i = 0; i < relevantItems.size(); i++) {
            itemNames[i] = relevantItems.get(i).getItemName();
        }

        double totalAmount = 0;
        int totalQuantity = 0;
        boolean buyingMore = true;
        StringBuilder receipt = new StringBuilder("Your purchase:\n");

        while (buyingMore) {

            JComboBox<String> comboBox = new JComboBox<>(itemNames);
            JPanel panel = new JPanel();
            panel.add(comboBox);

            JOptionPane.showMessageDialog(null, panel, "Choose a Drug Product", JOptionPane.QUESTION_MESSAGE);
            String selectedProductName = (String) comboBox.getSelectedItem();

            item selectedProduct = null;
            for (item drugProduct : relevantItems) {
                if (drugProduct.getItemName().equals(selectedProductName)) {
                    selectedProduct = drugProduct;
                    break;
                }
            }

            if (selectedProduct != null) {
                String quantityStr = JOptionPane.showInputDialog(null, "Enter the quantity:");
                int quantity = Integer.parseInt(quantityStr);

                if (quantity > selectedProduct.getQuantity()) {
                    JOptionPane.showMessageDialog(null, "Not enough " + selectedProductName
                            + " in stock. Available quantity: " + selectedProduct.getQuantity());
                    continue;
                }

                double totalPrice = selectedProduct.getPrice() * quantity;

                int discountChoice = JOptionPane.showConfirmDialog(null,
                        "Do you want to apply a discount?", "Discount Confirmation", JOptionPane.YES_NO_OPTION);

                if (discountChoice == JOptionPane.YES_OPTION) {
                    String codeoutput = JOptionPane.showInputDialog("Enter code: ");
                    if (codeoutput.equalsIgnoreCase("yey code")) {

                        String discountPercentageStr = JOptionPane.showInputDialog(null,
                                "Enter the discount percentage:");
                        double discountPercentage = Double.parseDouble(discountPercentageStr);

                        double discountAmount = totalPrice * discountPercentage / 100;
                        totalPrice -= discountAmount;
                    }
                }
                selectedProduct.updateQuan(selectedProduct.getQuantity() - quantity);

                totalAmount += totalPrice;
                totalQuantity += quantity;

                receipt.append(selectedProduct.getItemName())
                        .append(" - Quantity: ").append(quantity)
                        .append(", Price: $").append(String.format("%.2f", totalPrice)).append("\n");
            }

            int response = JOptionPane.showConfirmDialog(null, "Do you want to purchase anything else?",
                    "Continue Shopping", JOptionPane.YES_NO_OPTION);
            buyingMore = (response == JOptionPane.YES_OPTION);
        }

        double vatAmount = totalAmount * VAT_RATE;
        double totalWithVAT = totalAmount + vatAmount;

        receipt.append("\nTotal quantity: ").append(totalQuantity);
        receipt.append("\nTotal amount (excl. VAT): $").append(String.format("%.2f", totalAmount));
        receipt.append("\nTotal VAT: $").append(String.format("%.2f", vatAmount));
        receipt.append("\nTotal amount (incl. VAT): $").append(String.format("%.2f", totalWithVAT));
        JOptionPane.showMessageDialog(null, receipt.toString());
    }

    public void addDescription(String drugName, String description) {
        drugName = drugName.toLowerCase();
        for (int i = 0; i < itemList.size(); i++) {
            item item = itemList.get(i);
            if (item.hasSameName(drugName)) {
                item.addDescription(description);
                JOptionPane.showMessageDialog(null,
                        "Description added for " + drugName + ": " + description);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Item not found in inventory.");
    }

    public void displayInventory() {
        StringBuilder message = new StringBuilder("\nInventory:\n");
        for (item it : itemList) {
            message.append(it.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, message.toString());
    }

    public void removeDrug(String drugName) {
        drugName = drugName.toLowerCase();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).hasSameName(drugName)) {
                itemList.remove(i);
                JOptionPane.showMessageDialog(null, drugName + " has been removed from inventory.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Drug not found in inventory.");
    }

    public double sellItem(String drugName, int soldQuant, double discountRate) {
        double totalPrice = 0.0;
        item sellingItem = null;

        for (item it : itemList) {
            if (it.hasSameName(drugName)) {
                sellingItem = it;
                break;
            }
        }
        if (sellingItem == null) {
            System.out.println("Item not found in inventory.");
            return 0.0;
        }
        if (soldQuant <= sellingItem.getQuantity()) {
            double pricePerItem = sellingItem.getPrice();
            totalPrice = pricePerItem * soldQuant;

            if (discountRate > 0) {
                double discountAmount = totalPrice * discountRate / 100;
                totalPrice -= discountAmount;
            }

            final double VAT_RATE = 0.20;
            double vatAmount = totalPrice * VAT_RATE;
            totalPrice += vatAmount;

            sellingItem.updateQuan(sellingItem.getQuantity() - soldQuant);
            soldItems.add(sellingItem);
            String receipt = soldQuant + " " + sellingItem.getItemName() + "(s) sold.\n" +
                    "Total price (excl. VAT): $" + String.format("%.2f", totalPrice - vatAmount) + "\n" +
                    "VAT: $" + String.format("%.2f", vatAmount) + "\n" +
                    "Total price (incl. VAT): $" + String.format("%.2f", totalPrice) + "\n" +
                    "Remaining quantity: " + sellingItem.getQuantity();
            System.out.println(receipt);
        } else {
            System.out.println("Not enough " + sellingItem.getItemName() + " in stock.");
        }
        return totalPrice;
    }

}
