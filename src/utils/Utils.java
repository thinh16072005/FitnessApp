package utils;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String command, Scanner input) {
        System.out.print(command + " ");
        return input.nextLine().trim();
    }

    public static int getInt(String command, Scanner input) {
        int i = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(command + " : ");
                i = Integer.parseInt(input.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input [ require : integer type ]");
            }
        }
        return i;
    }

    public static int getPositiveInt(String command, Scanner input) {
        int i = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(command + " : ");
                i = Integer.parseInt(input.nextLine());

                if (i < 0) {
                    System.out.println("Only positive value accepted. Please try again.");
                } else {
                    validInput = true;  // Exit loop if valid positive number
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input [ require : integer type ]. Please try again.");
            }
        }
        return i;
    }

    public static String getValidFruitName(String prompt) {
        return getProperString(prompt);
    }

    public static double getDouble(String command, Scanner input) {
        double i = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(command + " : ");
                i = Double.parseDouble(input.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input [ require : Double type ]");
            }
        }
        return i;
    }

    public static double getPositiveDouble(String command, Scanner input) {
        double i = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(command + " : ");
                i = Double.parseDouble(input.nextLine());

                if (i < 0) {
                    System.out.println("Only positive value accepted. Please try again.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input [ require : Double type ]. Please try again.");
            }
        }
        return i;
    }

    public static boolean getBoolean(String command, Scanner input) {
        boolean result;
        System.out.print(command + "");
        String i = input.nextLine();
        if (i.equalsIgnoreCase("true")) {
            result = true;
        } else if (i.equalsIgnoreCase("false")) {
            result = false;
        } else {
            System.out.println("Invalid input [ require : boolean(true/false) ]");
            result = getBoolean(command, input);
        }
        return result;
    }

    public static boolean getBoolean_YN(String command, Scanner input) {
        boolean result;
        System.out.print(command + " : ");
        String i = input.nextLine();
        if (i.equalsIgnoreCase("Y")) {
            result = true;
        } else if (i.equalsIgnoreCase("N")) {
            result = false;
        } else {
            System.out.println("Invalid input [ require : Y/N ]");
            result = getBoolean_YN(command, input);
        }
        return result;
    }

    // date

    public static Date getDate(String command, Scanner input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);  // Ensure strict date parsing

        while (true) {
            System.out.print(command);
            String returnValue = input.nextLine();
            try {
                return dateFormat.parse(returnValue);
            } catch (ParseException e) {
                System.out.println("Invalid input - please enter the date in [dd/MM/yyyy] format.");
            }
        }

//    public static Date getDate(String command, Scanner input) {
//        String returnValue = getString(command, input);
//        try {
//            return new SimpleDateFormat("dd/MM/yyyy").parse(returnValue);
//        } catch (ParseException e) {
//            System.out.println("Invalid input - [dd/MM/yyyy]");
//            return getDate(command, input);
//        }
//    }
    }

    private static String capitalizeWords(String phrase) {
        String[] words = phrase.toLowerCase().split("\\s+ ");
        StringBuilder capitalizedPhrase = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedPhrase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalizedPhrase.toString().trim();
    }

    public static String getValue(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String getProperString(String prompt) {
        Pattern regex = Pattern.compile("^[A-Za-z ]+$");
        while (true) {
            String name = getValue(prompt);
            if (regex.matcher(name).matches() && !name.isEmpty()) {
                return capitalizeWords(name);
            } else {
                System.out.println("Invalid input. Name should only contain letters and spaces, with no special characters or numbers.");
            }
        }
    }

    public static String getValidDateOfBirth(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            String dateStr = getValue(prompt);
            try {
                LocalDate dateOfBirth = LocalDate.parse(dateStr, formatter);
                LocalDate currentDate = LocalDate.now();
                if (Period.between(dateOfBirth, currentDate).getYears() >= 18) {
                    return dateStr;
                } else {
                    System.out.println("Employee must be at least 18 years old.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
            }
        }
    }

    public static String getValidDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            String dateStr = getValue(prompt);
            try {
                LocalDate dateOfBirth = LocalDate.parse(dateStr, formatter);
                LocalDate currentDate = LocalDate.now();
                if (Period.between(dateOfBirth, currentDate).getYears() >= 0) {
                    return dateStr;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
            }
        }
    }

    //below is code for resort management

    public static String getProperName(String prompt) {
        Pattern regex = Pattern.compile("^[A-Za-z ]+$"); // Only letters and spaces
        while (true) {
            String name = getValue(prompt);
            if (regex.matcher(name).matches() && !name.isEmpty()) {
                return capitalizeWords(name); // Capitalize the first letter of each word
            } else {
                System.out.println("Invalid input. Name should only contain letters and spaces.");
            }
        }
    }

    public static String getValidEmail(String prompt) {
        Pattern regex = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        while (true) {
            String email = getValue(prompt);
            if (regex.matcher(email).matches()) {
                return email;
            } else {
                System.out.println("Invalid input. Please enter a valid email address.");
            }
        }
    }

    public static String getIdentityNumber(String prompt) {
        Pattern regex = Pattern.compile("^\\d{9}|\\d{12}$"); // Matches 9 or 12 digits
        while (true) {
            String idNumber = getValue(prompt);
            if (regex.matcher(idNumber).matches()) {
                return idNumber;
            } else {
                System.out.println("Invalid input. ID number must be either 9 or 12 digits.");
            }
        }
    }

    public static String getPhoneNumber(String prompt) {
        Pattern regex = Pattern.compile("^0\\d{9}$"); // Must start with 0 and be 10 digits
        while (true) {
            String phoneNumber = getValue(prompt);
            if (regex.matcher(phoneNumber).matches()) {
                return phoneNumber;
            } else {
                System.out.println("Invalid input. Phone number must start with 0 and be 10 digits.");
            }
        }
    }

    public static String getPassword(String prompt) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return Utils.getString(prompt, scanner);
        }
        char[] passwordArray = console.readPassword(prompt);
        return new String(passwordArray);
    }

    public static int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public static int calculateDuration(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

}
