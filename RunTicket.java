
/**
 * @author Marco Antonio Garcia Fernandez
 * * The `RunTicket` class is responsible for managing event ticket purchases
 * and logging user activities.
 *
 * This program allows users to log in, buy event tickets, and generate purchase
 * invoices. It reads event and user data from CSV files and uses a logging
 * system to record user activities.
 *
 * Usage:
 * - Provide the CSV file paths for event and user data.
 * - Users can log in and buy event tickets, and their activities are logged.
 *
 * Logging:
 * - User logins and ticket purchases are logged to the "event_log.txt" file.
 *
 * 
 *
 */
import java.io.*;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.InputMismatchException;


public class RunTicket {
    //private static final String LOG_FILE_NAME = "event_log.txt";
    private static final String LOG_FILE_NAME = "event_log.txt";
    private static final Logger logger = Logger.getLogger("RunTicket");



    /**
     * @param args
     */
    public static void main(String[] args) {
         // Remove the default console handler
         try {
            // Remove the default console handler
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers.length > 0 && handlers[0] instanceof ConsoleHandler) {
                rootLogger.removeHandler(handlers[0]);
            }

            // Initialize logger and file handler

            FileHandler fileHandler = new FileHandler(LOG_FILE_NAME, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // CSV file
            String filePath = "EventListPA2.csv";
            String userLogingCSV = "CustomerListPA2.csv";
            // list to store Sport objects
            Map<Integer, List<Object>> eventlist = new HashMap<>();
            String [] user;
            user = loginUser(userLogingCSV, logger);
            if(user == null){
                System.out.println("we enter aadminview");
                readDataFromCSV(filePath, eventlist,logger);
                AdminView adminView = new AdminView(eventlist);
                adminView.displayObjects();

                System.exit(1);
            }
            
            Customer customer = new Customer(user[0], user[1], user[2],
            Double.parseDouble(user[3]), Integer.parseInt(user[4]),
            Boolean.parseBoolean(user[5]), user[6], userLogingCSV);

            readDataFromCSV(filePath, eventlist,logger);
            
            List <Object> invoice = new ArrayList<>();

            invoice = buyTicket(customer,eventlist,logger);
            NewCsvCreator csvCreator = new NewCsvCreator("events.csv");
            csvCreator.createCsvFile(eventlist);

            CsvProcessor processor = new CsvProcessor(userLogingCSV, "newcustomer.csv");
            processor.processAndSaveCsv();
            
            
            logger.info("User logged in: " + user[0]); // Log user login
            logger.info("Event tickets purchased: " + invoice.size()); // Log ticket purchase count

            // Close the file handler
            fileHandler.close();
        } catch (IOException e) {
        // Handle the IOException
        logger.log(java.util.logging.Level.SEVERE, "An I/O error occurred", e);
    }
    }

    /**
     * Reads and processes data from a CSV file containing event information and populates the eventlist.
     *
     * This method reads data from a CSV file, processes each line, and stores the event details
     * in a Hash Map with event IDs as keys and Lists of objects as values.
     *
     * @param filePath   The path to the CSV file containing event data.
     * @param eventlist  A Map to store event details, where the key is the event ID, and the value
     *                   is a List of event-related objects.
     * @param logger     The logger object used for logging events.
     * @return           A Map containing event details, where the key is the event ID, and the value
     *                   is a List of event-related objects.
     */

    // Scaner for CSV
    private static Map<Integer, List<Object>> readDataFromCSV(String filePath, Map<Integer, List<Object>> eventlist, Logger logger) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            // Skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine(); 
            }

            // Read and process each line of the CSV file
            while (scanner.hasNextLine()) {
                
                // Loop through each line of data
                String line = scanner.nextLine();
                String[] fields = line.split(",");
            
                // Initialize variables with default values
                int id = 0;
                String type = null;
                String name = null;
                String date = null;
                String time = null;
                double vip_price = 0.0;
                double gold_price = 0.0;
                double silver_price = 0.0;
                double bronze_price = 0.0;
                double general_price = 0.0;
                String venue_name = null;
                double pct_seats_unavailable = 0.0;
                String venue_type = null;
                int capacity = 0;
                double cost = 0.0;
                double vip_pct = 0.0;
                double gold_pct = 0.0;
                double silver_pct = 0.0;
                double bronze_pct = 0.0;
                double general_ad_pct = 0.0;
                double reseved_pct = 0.0;
                String planned_fire = null;
                double fire_cost = 0.0;
  
            
                // Assign values from fields array if they exist
                for (int i = 0; i < fields.length; i++) {
                    if (!fields[i].isEmpty()) {
                        switch (i) {
                            case 0:
                                id = Integer.parseInt(fields[i].trim());
                                break;
                            case 1:
                                type = fields[i].trim();
                                break;
                            case 2:
                                name = fields[i].trim();
                                break;
                            case 3:
                                date = fields[i].trim();
                                break;
                            case 4:
                                time = fields[i].trim();
                                break;
                            case 5:
                                vip_price = Double.parseDouble(fields[i].trim());
                                break;
                            case 6:
                                gold_price = Double.parseDouble(fields[i].trim());
                                
                                break;
                            case 7:
                                silver_price = Double.parseDouble(fields[i].trim());
                                break;
                            case 8:
                                bronze_price = Double.parseDouble(fields[i].trim());
                                break;
                            case 9:
                                general_price = Double.parseDouble(fields[i].trim());
                                break;
                            case 10:
                                venue_name = fields[i].trim();
                                break;
                            case 11:
                                pct_seats_unavailable = Double.parseDouble(fields[i].trim());
                                break;
                            case 12:
                                venue_type = fields[i].trim();
                                break;
                            case 13:
                                capacity = Integer.parseInt(fields[i].trim());
                                break;
                            case 14:
                                cost = Double.parseDouble(fields[i].trim());
                                break;
                            case 15:
                                vip_pct =  Double.parseDouble(fields[i].trim());
                                break;
                            case 16:
                                gold_pct =  Double.parseDouble(fields[i].trim());
                                break;
                            case 17:
                                silver_pct =  Double.parseDouble(fields[i].trim());
                                break;
                            case 18:
                                bronze_pct =  Double.parseDouble(fields[i].trim());
                                
                                break;
                            case 19:
                                general_ad_pct =  Double.parseDouble(fields[i].trim());
                                break;
                            case 20:
                                reseved_pct =  Double.parseDouble(fields[i].trim());
                                break;
                            case 21:
                                planned_fire = fields[i].trim();
                                break;
                            case 22:
                                fire_cost = Double.parseDouble(fields[i].trim());
                                break;
                        }
                    }
                }
                logger.info("Event read from CSV: ID=" + id + ", Name=" + name + ", Type=" + type);

                  //System.out.println("" +id+ "" +type+ "" +name+ "" +date+ "" +time+ "" +vip_price);                                                                                     
                Helper_readDataFromCSV(filePath,eventlist,id,type,name,date,time,vip_price,gold_price,silver_price,bronze_price,general_price,venue_name,pct_seats_unavailable,venue_type,capacity,cost,vip_pct,gold_pct,silver_pct,bronze_pct,general_ad_pct,reseved_pct,planned_fire,fire_cost,logger);
                
            }
            
            
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Handle parsing errors if necessary
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions if necessary
            e.printStackTrace();
        }
        return eventlist;

    }

    /**
     * Helper method for processing and creating event objects based on CSV data and adding them to the eventlist.
     *
     * This method takes the CSV data and eventlist as input, processes the data to create event objects, and adds them
     * to the eventlist based on the event type and venue type.
     *
     * @param filePath           The path to the CSV file containing event data.
     * @param eventlist          A Map to store event details, where the key is the event ID, and the value
     *                           is a List of event-related objects.
     * @param id                 The ID of the event.
     * @param type               The type of the event.
     * @param name               The name of the event.
     * @param date               The date of the event.
     * @param time               The time of the event.
     * @param vip_price          The price of VIP seats for the event.
     * @param gold_price         The price of Gold seats for the event.
     * @param silver_price       The price of Silver seats for the event.
     * @param bronze_price       The price of Bronze seats for the event.
     * @param general_price      The price of General Admission seats for the event.
     * @param venue_name         The name of the venue where the event takes place.
     * @param pct_seats_unavailable The percentage of seats unavailable for the event.
     * @param venue_type         The type of venue where the event takes place.
     * @param capacity           The capacity of the venue.
     * @param cost               The cost of hosting the event at the venue.
     * @param vip_pct            The percentage of VIP seats available.
     * @param gold_pct           The percentage of Gold seats available.
     * @param silver_pct         The percentage of Silver seats available.
     * @param bronze_pct         The percentage of Bronze seats available.
     * @param general_ad_pct     The percentage of General Admission seats available.
     * @param reseved_pct        The percentage of reserved extra seats.
     * @param planned_fire       The planned fireworks for the event.
     * @param fireworks_Cost     The cost of fireworks for the event.
     * @param logger             The logger object used for logging events.
     */

    private static void Helper_readDataFromCSV(String filePath, Map<Integer, List<Object>> eventlist, int id,String type,
            String name, String date, String time, double vip_price, double gold_price, double silver_price,
            double bronze_price, double general_price, String venue_name, double pct_seats_unavailable, String venue_type,
            int capacity, double cost, double vip_pct, double gold_pct, double silver_pct, double bronze_pct, double general_ad_pct,
            double reseved_pct, String planned_fire, double fireworks_Cost, Logger logger) {
        
                

                List<Object> newEvent = new ArrayList<>();
               
                            
                // Create a Festival object and add it to the list if the condition is met
                        if (type != null && venue_type != null) {
                            if (type.equals("Festival")) {
                                if (venue_type.equals("Arena")) {
                                    
                                    

                                    Festival festivalEvent = new Festival(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Arena areanaplace = new Arena(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);

                                        
                                    SalesReport adminInfo = new SalesReport(festivalEvent, areanaplace);

                                    newEvent.add(festivalEvent);
                                    newEvent.add(areanaplace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                    
                                } else if (venue_type.equals("Auditorium")) {
                                    // Create a Festival event at an Auditorium
                                    // Add event to eventlist


                                    Festival festivalEvent = new Festival(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Auditorium auditoriumPlace = new Auditorium(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(festivalEvent  ,auditoriumPlace);

                                    newEvent.add(festivalEvent);
                                    newEvent.add(auditoriumPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("Stadium")) {
                                    // Create a Festival event at a Stadium
                                    // Add event to eventlist

                                    Festival festivalEvent = new Festival(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Stadium stadiumplace = new Stadium(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                     
                                    
                                    SalesReport adminInfo = new SalesReport(festivalEvent, stadiumplace);
                                
                                    newEvent.add(festivalEvent);
                                    newEvent.add(stadiumplace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("OpenAir")) {
                                    // Create a Festival event at an OpenAir venue
                                    // Add event to eventlist

                                    Festival festivalEvent = new Festival(id, type,name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    OpenAir openAirPlace = new OpenAir(capacity, planned_fire, planned_fire, capacity, fireworks_Cost, planned_fire, fireworks_Cost, fireworks_Cost);
                                    
                                    SalesReport adminInfo = new SalesReport(festivalEvent,openAirPlace);
                                    
                                    newEvent.add(festivalEvent);
                                    newEvent.add(openAirPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                }
                            } else if (type.equals("Concert")) {
                                if (venue_type.equals("Arena")) {
                                    // Create a Concert event at an Arena
                                    // Add event to eventlist
                                    Concert concertEvent = new Concert(id, type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Arena areanaplace = new Arena(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(concertEvent,areanaplace);

                                    newEvent.add(concertEvent);
                                    newEvent.add(areanaplace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("Auditorium")) {
                                    // Create a Concert event at an Auditorium
                                    // Add event to eventlist
                                    Concert concertEvent = new Concert(id, type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Auditorium auditoriumPlace = new Auditorium(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(concertEvent,auditoriumPlace);
                                    
                                    newEvent.add(concertEvent);
                                    newEvent.add(auditoriumPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("Stadium")) {
                                    // Create a Concert event at a Stadium
                                    // Add event to eventlist
                                    Concert concertEvent = new Concert(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Stadium stadiumplace = new Stadium(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(concertEvent,stadiumplace);

                                    newEvent.add(concertEvent);
                                    newEvent.add(stadiumplace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("OpenAir")) {
                                    // Create a Concert event at an OpenAir venue
                                    // Add event to eventlist
                                    Concert concertEvent = new Concert(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    OpenAir openAirPlace = new OpenAir(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(concertEvent,openAirPlace);

                                    newEvent.add(concertEvent);
                                    newEvent.add(openAirPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                }
                            } else if (type.equals("Sport")) {
                                if (venue_type.equals("Arena")) {
                                    // Create a Sport event at an Arena
                                    // Add event to eventlist
                                    Sport sportEvent = new Sport(id, type,name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Arena arenaPlace = new Arena(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(sportEvent,arenaPlace);

                                    newEvent.add(sportEvent);
                                    newEvent.add(arenaPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);

                                } else if (venue_type.equals("Auditorium")) {
                                    // Create a Sport event at an Auditorium
                                    // Add event to eventlist
                                    Sport sportEvent = new Sport(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    Auditorium auditoriumAirPlace = new Auditorium(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(sportEvent,auditoriumAirPlace);

                                    newEvent.add(sportEvent);
                                    newEvent.add(auditoriumAirPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("Stadium")) {
                                    // Create a Sport event at a Stadium
                                    // Add event to eventlist
                                    Sport sportEvent = new Sport(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    OpenAir stadiumplace = new OpenAir(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(sportEvent,stadiumplace);

                                    newEvent.add(sportEvent);
                                    newEvent.add(stadiumplace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                } else if (venue_type.equals("OpenAir")) {
                                    // Create a Sport event at an OpenAir venue
                                    // Add event to eventlist
                                    Sport sportEvent = new Sport(id,type, name, date, time, vip_price, gold_price, silver_price,
                                    bronze_price, general_price, pct_seats_unavailable, vip_pct, gold_pct, silver_pct,
                                    bronze_pct, general_ad_pct, planned_fire, fireworks_Cost);
                                    OpenAir openAirPlace = new OpenAir(id, venue_name, date, capacity, pct_seats_unavailable, venue_name, cost, reseved_pct);
                                    
                                    SalesReport adminInfo = new SalesReport(sportEvent,openAirPlace);

                                    newEvent.add(sportEvent);
                                    newEvent.add(openAirPlace);
                                    newEvent.add(adminInfo);
                                    eventlist.put(id, newEvent);
                                }
                            }
                         logger.info("Event created: ID=" + id + ", Name=" + name + ", Type=" + type + ", Venue=" + venue_type);
                            
                        }
            

    }

        /**
         * Attempts to log in a user based on provided credentials from a CSV file.
         *
         * This method prompts the user for their username and password, checks against the provided CSV file containing login
         * credentials, and allows a maximum number of login attempts. It logs login attempts and exits the program if the
         * maximum attempts are reached.
         *
         * @param loginPasswordsFile The path to the CSV file containing login credentials.
         * @param logger             The logger object used for logging login attempts.
         * @return An array containing user information if login is successful, null if login attempts are exhausted.
         * @throws FileNotFoundException If the CSV file containing login credentials is not found.
         * @throws IOException           If an I/O error occurs while reading the CSV file.
         */
    private static String[] loginUser(String loginPasswordsFile,Logger logger) throws FileNotFoundException, IOException  {
        Scanner scanner = new Scanner(System.in);
        int maxAttempts = 3;
        String [] adminarray ;
        
        File file = new File(loginPasswordsFile);
        Scanner fileScanner = new Scanner(file);
        
        // Skip header if present
        if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
        }
        
        int remainingAttempts = maxAttempts;
        boolean isTrue = true;
        while (isTrue) {
            System.out.print("Are you an administrator? (yes/no): ");
            String isAdmin = scanner.nextLine().trim().toLowerCase();
    
            if (isAdmin.equals("yes")) {
                // Proceed with administrator login
                System.out.println("helloadmin");
                return null;
            } else if (isAdmin.equals("no")) {
                // Proceed with regular user login
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                continue;
            }
        }   
        
    
             


        while (remainingAttempts > 0) {
            
            System.out.print("Hello customer what is your name? : ");
            String name = scanner.nextLine();
            System.out.print("Hello customer what is your Last name? : ");
            String lastname = scanner.nextLine(); 
            

            System.out.print("Enter your username: ");
            String enteredUsername = scanner.nextLine();
        
            System.out.print("Enter your password: ");
            String enteredPassword = scanner.nextLine();
        
            // Reset the file scanner to the beginning of the file
            fileScanner.close();
            fileScanner = new Scanner(file);
            
            // Skip header if present
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
            }
        
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Split the CSV line into username and password
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String username = parts[6].trim();
                    String password = parts[7].trim();
                    if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                        System.out.println("Login successful!");
                        fileScanner.close();
                        return parts;
                    }
                }
            }
            
            remainingAttempts--;
            if (remainingAttempts > 0) {
                System.out.println("Login failed. Please try again.");
                System.out.println("You have " + remainingAttempts + " attempts remaining.");
                logger.warning("Login failed for user: " + enteredUsername);

            } else {
                System.out.println("Maximum login attempts reached for this user.");
                logger.warning("Maximum login attempts reached for user: " + enteredUsername);

            }
        }
        
        System.out.println("Maximum login attempts reached for all users. Exiting...");
        logger.severe("Maximum login attempts reached for all users. Exiting...");
        System.exit(1);
    
        // This return statement is added to satisfy the method signature
        return null;
    }
    
    
    /**
     * Allows a customer to buy tickets for events and perform related actions.
     *
     * This method provides a user interface for a customer to interact with the ticket buying system. It allows the customer
     * to buy tickets for available events, check invoices for purchased tickets, and view their user information. The method
     * uses a provided `Customer` object and a map of events to manage ticket purchases and record transactions.
     *
     * @param customer   The customer for whom tickets are being purchased.
     * @param eventList  A map containing event details, including available tickets.
     * @param logger     The logger object used for logging purchase and system activities.
     * @return A list of invoices for purchased tickets.
     */
    
    

    private static List<Object> buyTicket(Customer customer, Map<Integer, List<Object>> eventList,Logger logger) {
        List<Object> finalReceiptList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
    
        while (!exit){

    
            System.out.println("\nOptions:");
            System.out.println("1. Buy Ticket");
            System.out.println("2. Check Invoice");
            System.out.println("3. User Information");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
    
            String input = scanner.nextLine();
    
            if ("1".equals(input)) {
                try{

                
                System.out.print("Enter the Event ID: ");
                int eventId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (eventList.containsKey(eventId)) {
                    List<Object> eventDetails = eventList.get(eventId);

                    

                    Event event = (Event) eventDetails.get(0);
                    System.out.println(event);
                    // Access other properties of the event    
                    Venue venue = (Venue) eventDetails.get(1);
                    System.out.println(venue);
                    // Access other properties of the venue
                    SalesReport report =(SalesReport) eventDetails.get(2);
                    System.out.println(report);
                            // Access and print other attributes as needed
                            if (event.getPercent_seats_Unavailable() < 100) {
                                System.out.println("Available Seats: ");
                                
                                System.out.print("Enter the number of tickets to purchase (1-6): ");
                                int numTickets = scanner.nextInt();
                                scanner.nextLine();
                                    if (numTickets >= 1 && numTickets <= 6) {
                                    // Ask the user which type of ticket they want to buy
                                    System.out.println("Select Ticket Type:");
                                    System.out.println("1. VIP seats Price: $" + event.getPrice_Vip() );
                                    System.out.println("2. Gold seats Price: $" + event.getGold_price()  );
                                    System.out.println("3. Silver seats Price: $" + event.getPrice_Silver() );
                                    System.out.println("3. Bronze seats Price: $" + event.getPrice_Bronze() );
                                    System.out.println("4. General Admission seats Price: $" + event.getPrice_General_adm() );
                                    System.out.print("Enter your choice: ");

        
                                    // Calculate the price and mark the seat as unavailable
                                    double ticketPrice = 0;
                                    String ticketType = "";
                                
                                    
                                    System.out.println("Select Ticket Type:");
                                    System.out.println("1. VIP seats remaining: " + event.getPercent_Vip_seat() + "%");
                                    System.out.println("2. Gold seats remaining: " + event.getPercent_Gold_seat() + "%");
                                    System.out.println("3. Silver seats remaining: " + event.getPercent_Silver_seat() + "%");
                                    System.out.println("4. Bronze seats remaining: " + event.getPercent_Bronze_seat() + "%");
                                    System.out.println("5. General Admission seats remaining: " + event.getPercent_General_seat() + "%");
                                    System.out.print("Enter your choice: ");
                                    int ticketChoice = scanner.nextInt();
                                    scanner.nextLine(); // Consume newline
                                    
                                    switch (ticketChoice) {
                                        case 1:
                                            ticketPrice = event.getPrice_Vip();
                                            ticketType = "VIP";
                                            double vipSeatsAvailable = venue.getCapacity() * (event.getPercent_Vip_seat() / 100) - numTickets;
                                            //event.setPercent_Vip_seat(event.getPercent_Vip_seat() - (numTickets * (100 / vipSeatsAvailable))); // Update available VIP seats
                                            report.setVip_seats_remaining((int)vipSeatsAvailable);
                                            report.setRevenue_vip(ticketPrice * numTickets);
                                            report.setVipSeatsSold(report.getVipSeatsSold() + numTickets);
                                            break;
                                        case 2:
                                            ticketPrice = event.getGold_price();
                                            ticketType = "Gold";
                                            double goldSeatsAvailable = venue.getCapacity() * (event.getPercent_Gold_seat() / 100)- numTickets;
                                            //event.setPercent_Gold_seat(event.getPercent_Gold_seat() - (numTickets * (100 / goldSeatsAvailable))); // Update available Gold seats
                                            report.setGold_seats_remaining((int)goldSeatsAvailable);
                                            report.setRevenue_gold(ticketPrice * numTickets);
                                            report.setGoldSeatsSold(report.getGoldSeatsSold() + numTickets);

                                            break;
                                        case 3:
                                            ticketPrice = event.getPrice_Silver();
                                            ticketType = "Silver";
                                            double silverSeatsAvailable = venue.getCapacity() *(event.getPercent_Silver_seat() / 100) - numTickets;
                                            //event.setPercent_Silver_seat(event.getPercent_Silver_seat() - (numTickets * (100 / silverSeatsAvailable))); // Update available Silver seats
                                            report.setSilver_seats_remaining((int)silverSeatsAvailable);
                                            report.setRevenue_silver(ticketPrice * numTickets);
                                            report.setSilverSeatsSold(report.getSilverSeatsSold() + numTickets);

                                            break;
                                        case 4:
                                            ticketPrice = event.getPrice_Bronze();
                                            ticketType = "Bronze";
                                            double bronzeSeatsAvailable = venue.getCapacity() *  (event.getPercent_Bronze_seat() / 100)-numTickets;
                                            //event.setPercent_Bronze_seat(event.getPercent_Bronze_seat() - (numTickets * (100 / bronzeSeatsAvailable))); // Update available Bronze seats
                                            report.setBronze_seats_remaining((int)bronzeSeatsAvailable);
                                            report.setRevenue_bronze(ticketPrice * numTickets);
                                            report.setBronzeSeatsSold(report.getBronzeSeatsSold() + numTickets);
                                            

                                            break;
                                        case 5:
                                            ticketPrice = event.getPrice_General_adm();
                                            ticketType = "General Admission";
                                            double generalSeatsAvailable = venue.getCapacity() *  (event.getPercent_General_seat() / 100)-numTickets;
                                            //event.setPercent_General_seat(event.getPercent_General_seat() - (numTickets * (100 / generalSeatsAvailable))); // Update available General Admission seats
                                            report.setGeneral_seats_remaining((int)generalSeatsAvailable);
                                            report.setRevenue_general(ticketPrice * numTickets);
                                            report.setGeneralSeatsSold(report.getGeneralSeatsSold() + numTickets);

                                            break;
                                        default:
                                            System.out.println("Invalid choice.");
                                            continue; // Restart the loop
                                    }
                                    
                                    
                                    // Check if the user has enough money to buy the ticket
                                    double userMoney = customer.getMoneyAvailable();
                                    double charge = numTickets * ticketPrice;
                                    if (userMoney >= charge) {
                                            // Deduct the ticket price from the user's money
                                            userMoney -= numTickets * ticketPrice;
                                            customer.setMoneyAvailable(userMoney);


                                            //update percetn tickets 

            
                                            // Update the user's concerts purchased
                                            int concertsPurchased = customer.getConcertsPurchased() + 1;
                                            customer.setConcertsPurchased(concertsPurchased);

                                            
            
                                            // Generate a receipt and add it to the list
                                            Invoice invoice = new Invoice(concertsPurchased, ticketType, numTickets, ticketType, userMoney);
                                            invoice.setEventId(eventId);
                                            invoice.setEventName(event.getEvent_Name());
                                            invoice.setTicketType(ticketType);
                                            invoice.setTicketPrice(ticketPrice);
                                            logger.info("Ticket purchased successfully for Event ID: " + eventId +" by User ID: " + customer.getId());

                                            // Add the invoice to the finalReceiptList
                                            finalReceiptList.add(invoice);
            
                                            // Print a success message
                                            System.out.println("Ticket purchased successfully!");
                                        } else {
                                        System.out.println("Insufficient funds to buy this ticket.");
                                        logger.warning("No available seats for Event ID: " + eventId);

                                        }
                                
                                     
                                
                                }else{
                                    System.out.println("The maximun amount you can buy is 6 tickets");
                                }
                            }
                        
                }else{
                    System.out.println("No such Event exist try again");
                    logger.warning("Invalid Event ID: " + eventId);

                }
                }
                catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Consume the invalid input
                    continue; // Restart the loop
                }
            }

             else if ("2".equals(input)) {
                // Implement invoice checking logic and print invoice
                System.out.println("Invoice:");
                for (Object obj : finalReceiptList) {
                    System.out.println(obj);
                }
             } else if ("4".equals(input)) {
                System.out.println("Exiting Program...");
                return finalReceiptList;
            }else if("3".equals(input)){
                System.out.println("User Information:");
                System.out.println("ID: " + customer.getId());
                System.out.println("Name: " + customer.getFirstName());
                System.out.println("Email: " + customer.getLastName());
                System.out.println("Money Available: " + customer.getMoneyAvailable());
                System.out.println("Concerts Purchased: " + customer.getConcertsPurchased());
                System.out.println("TicketMiner Membership: " + customer.isTicketMinerMember());
                System.out.println("Username: " + customer.getUsername());
            }
            else{
                System.out.println("Sorry, Invalid option, try again\n");
            }

        
        
    }
       
        return finalReceiptList;
}
        
    

    
}