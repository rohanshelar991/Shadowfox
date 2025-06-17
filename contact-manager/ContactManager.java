import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ContactManager {
    private static final ArrayList<Contact> contacts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcomeMessage();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1 -> addContact();
                    case 2 -> viewAllContacts();
                    case 3 -> searchContact();
                    case 4 -> updateContact();
                    case 5 -> deleteContact();
                    case 6 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the invalid input
            }
            System.out.println();
        }
        
        System.out.println("Thank you for using the Contact Manager. Goodbye!");
        scanner.close();
    }

    private static void displayWelcomeMessage() {
        System.out.println("*********************************************");
        System.out.println("*       CONTACT MANAGEMENT SYSTEM          *");
        System.out.println("*    Top-Level Personal Organizer Tool      *");
        System.out.println("*********************************************");
        System.out.println();
    }

    private static void displayMainMenu() {
        System.out.println("MAIN MENU:");
        System.out.println("1. Add New Contact");
        System.out.println("2. View All Contacts");
        System.out.println("3. Search Contact");
        System.out.println("4. Update Contact");
        System.out.println("5. Delete Contact");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private static void addContact() {
        System.out.println("\nADD NEW CONTACT:");
        
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Enter email address: ");
        String email = scanner.nextLine().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Error: Name and phone number are required fields.");
            return;
        }
        
        Contact newContact = new Contact(name, phone, email);
        contacts.add(newContact);
        System.out.println("Contact added successfully!");
    }

    private static void viewAllContacts() {
        System.out.println("\nALL CONTACTS:");
        
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        
        System.out.printf("%-5s %-20s %-15s %-25s%n", "ID", "Name", "Phone", "Email");
        System.out.println("------------------------------------------------------------");
        
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.printf("%-5d %-20s %-15s %-25s%n", 
                i + 1, 
                contact.getName(), 
                contact.getPhone(), 
                contact.getEmail());
        }
    }

    private static void searchContact() {
        System.out.println("\nSEARCH CONTACT:");
        System.out.print("Enter name or phone number to search: ");
        String searchTerm = scanner.nextLine().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Error: Please enter a search term.");
            return;
        }
        
        ArrayList<Contact> results = new ArrayList<>();
        
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(searchTerm) || 
                contact.getPhone().toLowerCase().contains(searchTerm)) {
                results.add(contact);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("No matching contacts found.");
            return;
        }
        
        System.out.println("\nSEARCH RESULTS:");
        System.out.printf("%-20s %-15s %-25s%n", "Name", "Phone", "Email");
        System.out.println("------------------------------------------------");
        
        for (Contact contact : results) {
            System.out.printf("%-20s %-15s %-25s%n", 
                contact.getName(), 
                contact.getPhone(), 
                contact.getEmail());
        }
    }

    private static void updateContact() {
        viewAllContacts();
        
        if (contacts.isEmpty()) {
            return;
        }
        
        System.out.print("\nEnter the ID of the contact to update: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (id < 1 || id > contacts.size()) {
                System.out.println("Invalid contact ID.");
                return;
            }
            
            Contact contact = contacts.get(id - 1);
            
            System.out.println("\nCurrent contact details:");
            System.out.println("1. Name: " + contact.getName());
            System.out.println("2. Phone: " + contact.getPhone());
            System.out.println("3. Email: " + contact.getEmail());
            
            System.out.print("\nEnter field number to update (1-3) or 0 to cancel: ");
            int field = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (field) {
                case 0 -> System.out.println("Update cancelled.");
                case 1 -> {
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        contact.setName(newName);
                        System.out.println("Name updated successfully.");
                    } else {
                        System.out.println("Name cannot be empty. Update cancelled.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter new phone number: ");
                    String newPhone = scanner.nextLine().trim();
                    if (!newPhone.isEmpty()) {
                        contact.setPhone(newPhone);
                        System.out.println("Phone number updated successfully.");
                    } else {
                        System.out.println("Phone number cannot be empty. Update cancelled.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter new email address: ");
                    String newEmail = scanner.nextLine().trim();
                    contact.setEmail(newEmail);
                    System.out.println("Email updated successfully.");
                }
                default -> System.out.println("Invalid field number. Update cancelled.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void deleteContact() {
        viewAllContacts();
        
        if (contacts.isEmpty()) {
            return;
        }
        
        System.out.print("\nEnter the ID of the contact to delete: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (id < 1 || id > contacts.size()) {
                System.out.println("Invalid contact ID.");
                return;
            }
            
            Contact contact = contacts.get(id - 1);
            System.out.println("\nContact to delete:");
            System.out.println("Name: " + contact.getName());
            System.out.println("Phone: " + contact.getPhone());
            System.out.println("Email: " + contact.getEmail());
            
            System.out.print("\nAre you sure you want to delete this contact? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                contacts.remove(id - 1);
                System.out.println("Contact deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    static class Contact {
        private String name;
        private String phone;
        private String email;

        public Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        // Getters and setters
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }

        public void setName(String name) { this.name = name; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setEmail(String email) { this.email = email; }
    }
}