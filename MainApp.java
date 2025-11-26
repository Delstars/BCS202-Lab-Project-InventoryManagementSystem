import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();
        manager.loadFromFile();
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
                        System.out.println("\n--- Add New Product ---");
                        String id = getValidProductID(scanner, "Enter Product ID (1-50): ");
                        String name = getValidString(scanner, "Enter Name: ");
                        double price = getValidDouble(scanner, "Enter Price: ");
                        int quantity = getValidInt(scanner, "Enter Quantity: ");
                        String type = getValidString(scanner, "Is this a perishable product? (yes/no): ");

                        try {
                            if (type.equalsIgnoreCase("yes")) {
                                String date = getValidDate(scanner, "Enter Expiry Date (YYYY-MM-DD): ");
                                manager.addProduct(new PerishableProduct(id, name, price, quantity, date));
                            } else {
                                manager.addProduct(new Product(id, name, price, quantity));
                            }
                        } catch (InvalidProductException e) {
                            System.err.println("Failure: " + e.getMessage());
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
                        System.out.println("\n--- Update Product ---");
                        String updateId = getValidProductID(scanner, "Enter Product ID to update (1-50): ");
                        double newPrice = getValidDouble(scanner, "Enter new price: ");
                        int newQty = getValidInt(scanner, "Enter new quantity: ");

                        try {
                            manager.updateProduct(updateId, newPrice, newQty);
                        } catch (ProductNotFoundException e) {
                            System.err.println("Failure: " + e.getMessage());
                        }
                        break;

                    case 4: 
                        System.out.println("\n--- Delete Product ---");
                        String deleteId = getValidProductID(scanner, "Enter Product ID to delete (1-50): ");

                        try {
                            manager.deleteProduct(deleteId);
                        } catch (ProductNotFoundException e) {
                            System.err.println("Failure: " + e.getMessage());
                        }
                        break;

                    case 5: 
                        System.out.println("\n--- Search Product ---");
                        String searchId = getValidProductID(scanner, "Enter Product ID to search (1-50): ");

                        try {
                            manager.searchProductID(searchId);
                        } catch (ProductNotFoundException e) {
                            System.err.println("Failure: " + e.getMessage());
                        }
                        break;

                    case 6:
                        manager.saveToFile();
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
    private static String getValidString(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: Input cannot be empty. Please try again.");
        }
    }
    private static int getValidInt(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (input >= 0) {
                    return input;
                }
                System.out.println("Error: Value cannot be negative.");
            } else {
                System.out.println("Error: Invalid input. Please enter a whole number.");
                scanner.next(); // Clear invalid input
            }
        }
    }
    private static double getValidDouble(Scanner scanner, String prompt) {
        double input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                input = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                if (input >= 0) {
                    return input;
                }
                System.out.println("Error: Price cannot be negative.");
            } else {
                System.out.println("Error: Invalid input. Please enter a number (e.g., 10.50).");
                scanner.next(); // Clear invalid input
            }
        }
    }
    private static String getValidProductID(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine(); 
                
                if (input >= 1 && input <= 50) {
                    return String.valueOf(input); 
                }
                System.out.println("Error: Product ID must be between 1 and 50.");
            } else {
                System.out.println("Error: Invalid input. Please enter a number (1-50).");
                scanner.next(); 
            }
        }
    }
    private static String getValidDate(Scanner scanner, String prompt) {
        String dateInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        while (true) {
            System.out.print(prompt);
            dateInput = scanner.nextLine().trim();
            
            try {
                LocalDate date = LocalDate.parse(dateInput, formatter);
                return date.toString();
                
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date. Please use YYYY-MM-DD (e.g., 2025-12-09).");
            }
        }
    }
}
