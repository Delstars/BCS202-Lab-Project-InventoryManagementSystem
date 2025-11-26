
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== Inventory Management System ======");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Product by ID");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Is this a perishable product? (yes/no): ");
                        String type = scanner.nextLine();

                        System.out.print("Enter Product ID: ");
                        String id = scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter Quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            if (type.equalsIgnoreCase("yes")) {
                                System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
                                String date = scanner.nextLine();
                                PerishableProduct p = new PerishableProduct(id, name, price, quantity, date);
                                manager.addProduct(p);
                            } else {
                                Product p = new Product(id, name, price, quantity);
                                manager.addProduct(p);
                            }
                        } catch (InvalidProductException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;

                    case 2:
                        try {
                            manager.viewAllProducts();
                        } catch (ProductNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.print("Enter Product ID to update: ");
                        String updateId = scanner.nextLine();

                        // The manual specifically asks to update price and quantity 
                        System.out.print("Enter new price: ");
                        double newPrice = scanner.nextDouble();
                        System.out.print("Enter new quantity: ");
                        int newQty = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        try {
                            manager.updateProduct(updateId, newPrice, newQty);
                        } catch (ProductNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Enter Product ID to delete: ");
                        String deleteId = scanner.nextLine();

                        try {
                            manager.deleteProduct(deleteId);
                        } catch (ProductNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.print("Enter Product ID to search: ");
                        String searchId = scanner.nextLine();

                        try {
                            manager.searchProductID(searchId);
                        } catch (ProductNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.println("Exiting system...");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter 1-6.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter the correct format (numbers for price/quantity).");
                scanner.nextLine();
            }
        }
    }
}
