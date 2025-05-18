import java.util.ArrayList;

class item {
    String itemName;
    String description;
    int quantity;
    double price;

    static ArrayList<item> predefinedItems = new ArrayList<>();

    static {
        predefinedItems.add(new item("panadol", "headache migraine muscleache ", 100, 2.99));
        predefinedItems.add(new item("Aspirin", "fever muscleaches cold ", 100, 3.49));
        predefinedItems.add(new item("Strepsils", "mouth infections, sore throat", 50, 3.49));

    }

    public static ArrayList<item> getPredefinedItems() {
        return predefinedItems;
    }

    public item(String itemName, String description, int quantity, double price) {
        this.itemName = itemName.toLowerCase();
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public item(String itemName, int soldQuant, double sellPrice) {
        this.itemName = itemName.toLowerCase();
        this.quantity = soldQuant;
        this.price = sellPrice;
    }

    public boolean hasSameName(String otherName) {
        return this.itemName.equalsIgnoreCase(otherName);
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return "Item: " + itemName + "\nDescription: " + description + "\nRemaining Quantity: " + quantity
                + "\nPrice: $" + price + "\n";
    }

    public void addDescription(String newDescription) {
        this.description = newDescription;
        System.out.println("Description added for " + itemName + ": " + description);
    }

    public void updatedesc(int newdesc) {
        this.quantity = newdesc;
        System.out.println("Quantity updated for " + itemName + ": " + description);
    }

    public void updateQuan(int newQuan) {
        this.quantity = newQuan;
        System.out.println("Quantity updated for " + itemName + ": " + quantity);
    }

    public void updateP(double newP) {
        this.price = newP;
        System.out.println("Price updated for " + itemName + ": $" + newP);
    }

    public void sellitem(int soldQuant) {
        if (soldQuant <= quantity) {
            quantity -= soldQuant;
            System.out.println(
                    soldQuant + " " + itemName + "(s) sold to a new customer. Remaining quantity: " + quantity);

        } else {
            System.out.println("Not enough " + itemName + " in stock.");
        }
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

}
