import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.InputMismatchException;

public class EnhancedCalculator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        displayWelcomeMessage();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1 -> performBasicOperation();
                    case 2 -> performScientificOperation();
                    case 3 -> performUnitConversion();
                    case 4 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the invalid input
            }
            System.out.println();
        }
        
        System.out.println("Thank you for using the Enhanced Calculator. Goodbye!");
        scanner.close();
    }

    private static void displayWelcomeMessage() {
        System.out.println("*********************************************");
        System.out.println("*      ENHANCED CONSOLE CALCULATOR          *");
        System.out.println("*  Top-Level Scientific & Conversion Tool   *");
        System.out.println("*********************************************");
        System.out.println();
    }

    private static void displayMainMenu() {
        System.out.println("MAIN MENU:");
        System.out.println("1. Basic Arithmetic Operations");
        System.out.println("2. Scientific Calculations");
        System.out.println("3. Unit Conversions");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    private static void performBasicOperation() {
        System.out.println("\nBASIC ARITHMETIC OPERATIONS:");
        System.out.println("1. Addition (+)");
        System.out.println("2. Subtraction (-)");
        System.out.println("3. Multiplication (*)");
        System.out.println("4. Division (/)");
        System.out.print("Select operation: ");
        
        try {
            int operation = scanner.nextInt();
            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            
            double result = 0;
            String operationSymbol = "";
            
            switch (operation) {
                case 1 -> {
                    result = num1 + num2;
                    operationSymbol = "+";
                }
                case 2 -> {
                    result = num1 - num2;
                    operationSymbol = "-";
                }
                case 3 -> {
                    result = num1 * num2;
                    operationSymbol = "*";
                }
                case 4 -> {
                    if (num2 == 0) {
                        System.out.println("Error: Division by zero is not allowed.");
                        return;
                    }
                    result = num1 / num2;
                    operationSymbol = "/";
                }
                default -> {
                    System.out.println("Invalid operation selected.");
                    return;
                }
            }
            
            System.out.printf("%s %s %s = %s%n", 
                df.format(num1), operationSymbol, df.format(num2), df.format(result));
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void performScientificOperation() {
        System.out.println("\nSCIENTIFIC CALCULATIONS:");
        System.out.println("1. Square Root (√x)");
        System.out.println("2. Exponentiation (x^y)");
        System.out.println("3. Natural Logarithm (ln(x))");
        System.out.println("4. Power of 10 (10^x)");
        System.out.print("Select operation: ");
        
        try {
            int operation = scanner.nextInt();
            double result = 0;
            String description = "";
            
            switch (operation) {
                case 1 -> {
                    System.out.print("Enter number: ");
                    double num = scanner.nextDouble();
                    if (num < 0) {
                        System.out.println("Error: Cannot calculate square root of negative number.");
                        return;
                    }
                    result = Math.sqrt(num);
                    description = "√" + df.format(num);
                }
                case 2 -> {
                    System.out.print("Enter base: ");
                    double base = scanner.nextDouble();
                    System.out.print("Enter exponent: ");
                    double exponent = scanner.nextDouble();
                    result = Math.pow(base, exponent);
                    description = df.format(base) + "^" + df.format(exponent);
                }
                case 3 -> {
                    System.out.print("Enter number: ");
                    double num = scanner.nextDouble();
                    if (num <= 0) {
                        System.out.println("Error: Natural logarithm is only defined for positive numbers.");
                        return;
                    }
                    result = Math.log(num);
                    description = "ln(" + df.format(num) + ")";
                }
                case 4 -> {
                    System.out.print("Enter exponent: ");
                    double exponent = scanner.nextDouble();
                    result = Math.pow(10, exponent);
                    description = "10^" + df.format(exponent);
                }
                default -> {
                    System.out.println("Invalid operation selected.");
                    return;
                }
            }
            
            System.out.printf("%s = %s%n", description, df.format(result));
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void performUnitConversion() {
        System.out.println("\nUNIT CONVERSIONS:");
        System.out.println("1. Temperature (Celsius ↔ Fahrenheit)");
        System.out.println("2. Currency (USD ↔ EUR)");
        System.out.print("Select conversion type: ");
        
        try {
            int conversionType = scanner.nextInt();
            
            switch (conversionType) {
                case 1 -> convertTemperature();
                case 2 -> convertCurrency();
                default -> System.out.println("Invalid conversion type selected.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void convertTemperature() {
        System.out.println("\nTEMPERATURE CONVERSION:");
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.print("Select direction: ");
        
        try {
            int direction = scanner.nextInt();
            System.out.print("Enter temperature: ");
            double temp = scanner.nextDouble();
            
            double convertedTemp;
            String fromTo;
            
            if (direction == 1) {
                convertedTemp = (temp * 9/5) + 32;
                fromTo = df.format(temp) + "°C = " + df.format(convertedTemp) + "°F";
            } else if (direction == 2) {
                convertedTemp = (temp - 32) * 5/9;
                fromTo = df.format(temp) + "°F = " + df.format(convertedTemp) + "°C";
            } else {
                System.out.println("Invalid direction selected.");
                return;
            }
            
            System.out.println(fromTo);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void convertCurrency() {
        // Using a fixed exchange rate for simplicity
        final double USD_TO_EUR_RATE = 0.85;
        final double EUR_TO_USD_RATE = 1.18;
        
        System.out.println("\nCURRENCY CONVERSION (Using sample rates):");
        System.out.println("1. USD to EUR");
        System.out.println("2. EUR to USD");
        System.out.print("Select direction: ");
        
        try {
            int direction = scanner.nextInt();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            
            if (amount < 0) {
                System.out.println("Error: Amount cannot be negative.");
                return;
            }
            
            double convertedAmount;
            String fromTo;
            
            if (direction == 1) {
                convertedAmount = amount * USD_TO_EUR_RATE;
                fromTo = "$" + df.format(amount) + " = €" + df.format(convertedAmount);
            } else if (direction == 2) {
                convertedAmount = amount * EUR_TO_USD_RATE;
                fromTo = "€" + df.format(amount) + " = $" + df.format(convertedAmount);
            } else {
                System.out.println("Invalid direction selected.");
                return;
            }
            
            System.out.println(fromTo);
            System.out.println("Note: Using sample exchange rates. For real rates, connect to a financial API.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
            scanner.nextLine(); // clear the invalid input
        }
    }
}