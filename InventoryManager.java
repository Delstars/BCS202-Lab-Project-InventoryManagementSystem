public class InventoryManager {
    private Product[] inventory;
    private static final int MAX_CAPACITY = 50;
    private int productCount;

    public InventoryManager() {
        this.inventory = new Product[MAX_CAPACITY];
        this.productCount = 0;
    }
    public void addProduct(Product product) throws InvalidProductException {
        if (productCount < MAX_CAPACITY) {
            inventory[productCount] = product;
            productCount++;
            System.out.println(product.getName() + " has been added to inventory.");
        } else {
            throw new InvalidProductException("Inventory is full. Cannot add more products.");
        }
    }
    public void viewAllProducts() throws ProductNotFoundException {
        if (productCount == 0) {
            throw new ProductNotFoundException("No products found in inventory.");
        }
        else {
            for (int i = 0; i < productCount; i++) {
                inventory[i].displayProductInfo();
            }
        }
}
    public void searchProductID(String productId) throws ProductNotFoundException {
        for (int i = 0; i < productCount; i++) {
            if (inventory[i].getProductId().equals(productId)) {
                inventory[i].displayProductInfo();
                return;
            }
            else {
                throw new ProductNotFoundException("Product with ID " + productId + " not found.");
            }
        }
    }
}