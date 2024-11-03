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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String getString(String command, Scanner input) {
        System.out.print(command + " ");
        return input.nextLine().trim();
    }

    public static String getExerciseId(String command, Scanner input) {
        Pattern regex = Pattern.compile("EX\\d{3}"); // Must start with EX and be followed by 3 digits
        while (true) {
            String exerciseId = getString(command, input);
            if (regex.matcher(exerciseId).matches()) {
                return exerciseId;
            } else {
                System.out.println("Invalid input. Exercise ID must start with EX and be followed by 3 digits.");
            }
        }
    }

    public static String getWorkoutId(String command, Scanner input) {
        Pattern regex = Pattern.compile("W\\d{3}"); // Must start with W and be followed by 3 digits
        while (true) {
            String workoutId = getString(command, input);
            if (regex.matcher(workoutId).matches()) {
                return workoutId;
            } else {
                System.out.println("Invalid input. Workout ID must start with W and be followed by 3 digits.");
            }
        }
    }

    public static String getCourseId(String command, Scanner input) {
        Pattern regex = Pattern.compile("C\\d{3}");
        while (true) {
            String workoutId = getString(command, input);
            if (regex.matcher(workoutId).matches()) {
                return workoutId;
            } else {
                System.out.println("Invalid input. Course ID must start with C and be followed by 3 digits.");
            }
        }
    }

    public static String getCoachId(String command, Scanner input) {
        Pattern regex = Pattern.compile("CH\\d{3}");
        while (true) {
            String workoutId = getString(command, input);
            if (regex.matcher(workoutId).matches()) {
                return workoutId;
            } else {
                System.out.println("Invalid input. Coach ID must start with CH and be followed by 3 digits.");
            }
        }
    }

    public static String getLearnerId(String command, Scanner input) {
        Pattern regex = Pattern.compile("L\\d{3}");
        while (true) {
            String workoutId = getString(command, input);
            if (regex.matcher(workoutId).matches()) {
                return workoutId;
            } else {
                System.out.println("Invalid input. Learner ID must start with L and be followed by 3 digits.");
            }
        }
    }

    public static String getSubcriptionId(String command, Scanner input) {
        Pattern regex = Pattern.compile("S\\d{3}");
        while (true) {
            String workoutId = getString(command, input);
            if (regex.matcher(workoutId).matches()) {
                return workoutId;
            } else {
                System.out.println("Invalid input. Subcription ID must start with S and be followed by 3 digits.");
            }
        }
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

    public static String getValidStartDate(String prompt) {
        LocalDate currentDate = LocalDate.now();
        while (true) {
            String startDateStr = getValue(prompt);
            try {
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                if (!startDate.isBefore(currentDate)) {
                    return startDateStr;
                } else {
                    System.err.println("Start date cannot be in the past.");
                }
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
            }
        }
    }

    public static String getValidEndDate(String prompt, LocalDate startDateStr) {
        while (true) {
            String endDateStr = getValue(prompt);
            try {
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                if (endDate.isAfter(startDateStr)) {
                    return endDateStr;
                } else {
                    System.out.println("End date must be after start date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
            }
        }
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
                if (Period.between(dateOfBirth, currentDate).getYears() >= 12 && Period.between(dateOfBirth, currentDate).getYears() <= 100) {
                    return dateStr;
                } else {
                    System.out.println("Learner must be at least 12 years old and under 100 years old.");
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

    public static String getProperPassword(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String password;

        while (true) {
            System.out.print(prompt);
            password = scanner.nextLine();

            // Check if the password is at least 8 characters long
            if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long.");
                continue;
            }

            // Check if the password contains at least one capitalized letter
            if (!password.matches(".*[A-Z].*")) {
                System.out.println("Password must contain at least 1 capitalized letter.");
                continue;
            }

            // Check if the password contains at least one special character
            if (!password.matches(".*[,:./~!@#$%^&*\\-_?].*")) {
                System.out.println("Password must contain at least 1 special character: , . / ~ ! @ # $ % ^ & * - _ ?");
                continue;
            }

            // Check if the password contains at least one number
            if (!password.matches(".*[0-9].*")) {
                System.out.println("Password must contain at least 1 number.");
                continue;
            }

            // If all checks passed, break the loop and return the password
            break;
        }

        return password;
    }

    public static int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public static int calculateDuration(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static String getPlatform(String prompt, Scanner input) {
        Pattern regex = Pattern.compile("^(Online|Offline)$");
        while (true) {
            String platform = getString(prompt, input);
            if (regex.matcher(platform).matches()) {
                return platform;
            } else {
                System.out.println("Invalid input. Platform must be either 'Online' or 'Offline'.");
            }
        }
    }

}
