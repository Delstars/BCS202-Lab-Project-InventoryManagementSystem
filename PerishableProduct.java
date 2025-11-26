public class PerishableProduct extends Product {
    private String expirationDate;

    public PerishableProduct(String productId, String name, double price, int quantity, String expirationDate) {
        super(productId, name, price, quantity);
        this.expirationDate = expirationDate;
    }
    
    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public void displayProductInfo() {
        super.displayProductInfo();
        System.out.println("Expiration Date: " + expirationDate);
    }
}