import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InventoryManager {
    private Product[] inventory;
    private static final int MAX_CAPACITY = 50;
    private int productCount;

    public InventoryManager() {
        this.inventory = new Product[MAX_CAPACITY];
        this.productCount = 0;
    }

    private int findProductIndex(String productId) {
        for (int i = 0; i < productCount; i++) {
            if (inventory[i].getProductId().equals(productId)) {
                return i;
            }
        }
        return -1;
    }
   public void addProduct(Product product) throws InvalidProductException {
        if (productCount >= MAX_CAPACITY) {
            throw new InvalidProductException("Inventory is full. Cannot add more products.");
        }
        if (findProductIndex(product.getProductId()) != -1) {
            throw new InvalidProductException("Error: Product ID " + product.getProductId() + " already exists!");
        }
        inventory[productCount] = product;
        productCount++;
        System.out.println(product.getName() + " has been added to inventory.");
        saveToFile(); 
    }
    public void viewAllProducts() throws ProductNotFoundException {
        if (productCount == 0) {
            throw new ProductNotFoundException("No products found in inventory.");
        } else {
            System.out.println("\n--- All Products ---");
            for (int i = 0; i < productCount; i++) {
                inventory[i].displayProductInfo();
                System.out.println("--------------------");
            }
        }
    }


    public void updateProduct(String productId, double newPrice, int newQuantity) throws ProductNotFoundException {
        int index = findProductIndex(productId);
        
        if (index == -1) {
            throw new ProductNotFoundException("Cannot update. Product ID " + productId + " not found.");
        }
        

        inventory[index].setPrice(newPrice);
        inventory[index].setQuantity(newQuantity);
        System.out.println("Product " + productId + " updated successfully.");
        saveToFile(); 
    }

 
    public void deleteProduct(String productId) throws ProductNotFoundException {
        int index = findProductIndex(productId);
        
        if (index == -1) {
            throw new ProductNotFoundException("Cannot delete. Product ID " + productId + " not found.");
        }


        for (int i = index; i < productCount - 1; i++) {
            inventory[i] = inventory[i + 1];
        }
        
        inventory[productCount - 1] = null; 
        productCount--; 
        System.out.println("Product " + productId + " deleted successfully.");
        saveToFile(); 
    }

    public void searchProductID(String productId) throws ProductNotFoundException {
        int index = findProductIndex(productId);

        if (index != -1) {
            System.out.println("\n--- Product Found ---");
            inventory[index].displayProductInfo();
        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }
    }


    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.txt"))) {
            for (int i = 0; i < productCount; i++) {
                Product p = inventory[i];
                String line;
                
                if (p instanceof PerishableProduct) {
                    line = "PERISHABLE," + p.getProductId() + "," + p.getName() + "," + 
                           p.getPrice() + "," + p.getQuantity() + "," + 
                           ((PerishableProduct) p).getExpirationDate();
                } else {
                    line = "REGULAR," + p.getProductId() + "," + p.getName() + "," + 
                           p.getPrice() + "," + p.getQuantity();
                }
                
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File("inventory.txt");
        if (!file.exists()) {
            return; 
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
              
                if (parts.length >= 5) {
                    String type = parts[0];
                    String id = parts[1];
                    String name = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int qty = Integer.parseInt(parts[4]);

                    try {
                        if (type.equals("PERISHABLE") && parts.length == 6) {
                            String expiry = parts[5];
                            addProduct(new PerishableProduct(id, name, price, qty, expiry));
                        } else {
                            addProduct(new Product(id, name, price, qty));
                        }
                    } catch (InvalidProductException e) {
                       // Ignore if full
                    }
                }
            }
            System.out.println("-> [System] Data loaded from file successfully.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}