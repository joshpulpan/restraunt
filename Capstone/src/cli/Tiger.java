package cli;

import domain.Card;
import domain.Location;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import domain.Menu;
import domain.Order;
import domain.Store;
import domain.User;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import services.CardService;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import services.MenuServices;
import services.OrderService;
import services.StoreService;
import services.UserService;
import services.LocationService;

public class Tiger{

	public static ServiceWrapper sw;
	public static Connection con;
	public static User currentUser;
	public static Order currentOrder;
	public static Store currentStore;
        public static Location currentLoc = new Location();
	
	static Scanner sc;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
                Class.forName("oracle.jdbc.OracleDriver");
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "db_uSpring", "pass");
                sw  = new ServiceWrapper(con);
                sc = new Scanner(System.in);
                firstScreen();
                sc.close();
	}
	
	public static void firstScreen(){
System.out.println("______                    _ \n|  _  \\                  | | \n| | | |___  ___  ___ _ __| |_\n| | | / _ \\/ __|/ _ \\ '__| __|\n| |/ /  __/\\__ \\  __/ |  | |_\n|___/ \\___||___/\\___|_|   \\__|\n______ _\n|  _  (_)\n| | | |_ _ __   ___ _ __\n| | | | | '_ \\ / _ \\ '__|\n| |/ /| | | | |  __/ |  \n|___/ |_|_| |_|\\___|_|  \n");
                ArrayList<String> options = new ArrayList<>();
		options.add("Login");
		options.add("Register");
		options.add("Quit");
                options.add("Admin");
		int count = 0;
		for(String option : options) {
			count++;
			System.out.println(count + ". " + option);
		}

            int input = sc.nextInt();
            
                switch(input){
                    case 1:
                            loginScreen();
                            
                            break;
                    case 2:
                            registerScreen();
                          
                            break;
                    case 3:
                            System.out.println("Goodbye");
                            
                            break;
                    case 4:
                            AdminAndManager aam = new AdminAndManager(con);
                            aam.adminScreen();
                            
                            break;
                    default:
                            firstScreen();
                }
            
	}
		
	public static void loginScreen(){
		System.out.println("\n*Login*");
		System.out.println("Enter email:");
	    String email = sc.next();
		System.out.println("Enter password:");
	    String password = sc.next();
	    
		UserService us = new UserService(con);
		User candidate = us.getByEmail(email);
		if(candidate == null){
			System.out.println("Wrong email");
			firstScreen();
                        return;
		}
		if(password.equals(candidate.getPassword())){
			currentUser = candidate;
			currentOrder = new Order();
			currentOrder.setOrder_id(Double.toString(Math.random()* 10001));
			currentOrder.setUser_id(currentUser.getUserId());
			currentOrder.setDelivery_status_id("0");
			//currentOrder.setCard_id();
			StoreService ss = new StoreService(con);
			currentStore = ss.getById("0");
	    	homeScreen();
	    }
	    else{
	    	System.out.println("Wrong email or password");
	    	try {
				TimeUnit.SECONDS.sleep(1);
				firstScreen();
			} catch (InterruptedException e) {
				e.printStackTrace(System.out);
			}
	    }


	}
	public static void registerScreen() {
        System.out.println("\n*Register*");
        System.out.println("Please enter zip code to see if we deliver to your area:");
        String zip = sc.next();
        if (zip.equals(85054) == true) {
            System.out.println("Sorry, we do not deliver to your address at this time!");
            firstScreen();
        }
        System.out.println("Enter email:");
        String email = sc.next();
        System.out.println("Enter password:");
        String password = sc.next();
        System.out.println("Enter password again:");
        String passwordConfirm = sc.next();
        System.out.println("Enter first name:");
        String first = sc.next();
        System.out.println("Enter last name:");
        String last = sc.next();
        System.out.println("Enter phone:");
        String phone = sc.next();
        sc.nextLine();
        System.out.println("Enter street:");
        String street = sc.nextLine();
        System.out.println("Enter city:");
        String city = sc.nextLine();
        System.out.println("Enter state:");
        String state = sc.nextLine();
        System.out.println("Enter country:");
        String country = sc.nextLine();
        // System.out.println("Enter status:");
        //    String status = sc.next();
        //, street, city, state, country, zip, status
        if (password.equals(passwordConfirm)) {
            System.out.println("Registered");
            currentUser = sw.register(first, last, phone, email, password, street, city, state, country, zip);
            currentOrder = new Order();
            currentOrder.setOrder_id(Double.toString(Math.random() * 10001));
            currentOrder.setUser_id(currentUser.getUserId());
            currentOrder.setDelivery_status_id("0");
            homeScreen();
        } else {
            System.out.println("Mismatching passwords, try again");
            firstScreen();
        }
    }

	public static void homeScreen(){
    	System.out.println("Welcome " + currentUser.getFirstName());
		System.out.println("\n*Home*");
		ArrayList<String> options = new ArrayList<>();
		options.add("Menu");
		options.add("Order");
		options.add("Account");
		options.add("Store Details");
		options.add("Logout");
		options.add("Quit");
		int count = 0;
		for(String option : options) {
			count++;
			System.out.println(count + ". " + option);
		}
                
                int input = sc.nextInt();
                switch (input) {
                    case 1:
                        menuScreen();
                        break;
                    case 2:
                        currentOrderScreen();
                        break;
                    case 3:
                        accountScreen();
                        break;
                    case 4:
                        storeDetailsScreen();
                        break;
                    case 5:
                        firstScreen();
                        break;
                    case 6:
                        System.out.println("Goodbye");
                        System.exit(0);
                    default:
                        homeScreen();
                }   
	}
	
	public static void menuScreen(){
		System.out.println("\n*Menu*");
		MenuServices ms = new MenuServices(con);
		ArrayList<Menu> menus = ms.getAll();
		ServiceWrapper.printMenuItems(menus);
	    int input = sc.nextInt();
	    if(input==menus.size()+1) homeScreen();
            else if(input > menus.size()+1 || input < 1) menuScreen();
	    else menuItemScreen(menus.get(input-1));
	}
	public static void menuItemScreen(Menu menu) {
		System.out.println("\n*" + menu.getName() + "*");
		System.out.println(menu.getDescription());
		System.out.println("$" + menu.getPrice());
		System.out.println("1. Enter Quantity");
		System.out.println("2. Go Back");
	    int input = sc.nextInt();
	    if(input==1) itemQuantityScreen(menu);
	    else if(input==2) menuScreen();
            else menuItemScreen(menu);
	}
	//TODO finish this
	public static void itemQuantityScreen(Menu menu){
		System.out.println("Enter Quantity");
	    int input = sc.nextInt();
	    for(int i=0;i<input;i++) currentOrder.addItem_id(menu.getId());
		System.out.println("Item(s) added");
		menuScreen();
	}
	public static void currentOrderScreen() {
		System.out.println("\n*Current Order*");
		System.out.println("Placed: " +currentOrder.getPlaced_timestamp());
		System.out.println("Delivered: " +currentOrder.getDelivery_timestamp());
		currentOrder.setTotal_price(sw.calculateTotalPrice(currentOrder.getItem_ids()));
		System.out.println("Total price: $" +currentOrder.getTotal_price());
		System.out.println("Method: " +currentOrder.getDelivery_method_id());
		System.out.println("Status: " +currentOrder.getDelivery_status_id());
		System.out.println("1. Cancel");
		System.out.println("2. View\\Edit Items");
		System.out.println("3. Edit Order");
		System.out.println("4. Submit Order");
		System.out.println("5. Go Back");
	    int input = sc.nextInt();
	    if(input==1 && confirm()) {
	    	currentOrder = new Order();
			currentOrder.setOrder_id(Double.toString(Math.random()* 10001));
			currentOrder.setUser_id(currentUser.getUserId());
			currentOrder.setDelivery_status_id("0");
                        currentOrderScreen();
	    }
	    if(input==2) viewEditOrderItems(currentOrder);
	    if(input==3) editOrder(currentOrder);
	    if(input==4 && confirm()) {

                ArrayList<String> itemIds = currentOrder.getItem_ids();
		ArrayList<Menu> items = sw.getMenuItems(itemIds);
                homeScreen();
		if(items.isEmpty()){
                    System.out.println("No items in cart!");
                    currentOrderScreen();
                }
                else{
                    sw.submitOrder(currentOrder);
                    orderConfirmationMessage(currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), currentOrder);
                
                    currentOrder = new Order();
                    currentOrder.setOrder_id(Double.toString(Math.random()* 10001));
                    currentOrder.setUser_id(currentUser.getUserId());
		    currentOrder.setDelivery_status_id("0");
                    homeScreen();
                }

            }
	    else if(input==5) homeScreen();
            else currentOrderScreen();
	}
	

        
        private static void editOrder(Order currentOrder2) {
        System.out.println("\n*Edit Order*");
        ArrayList<String> options = new ArrayList<>();
        options.add("Edit Tip");
        options.add("Edit delivery time");
        options.add("Edit Instructions");
        options.add("Edit Delivery Method");
        options.add("Edit Store");
        options.add("Go Back");
        int count = 0;
        for(String option : options) {
            count++;
            System.out.println(count + ". " + option);
        }
        int input = sc.nextInt();
            if(input==1){
                        try{
                            int newTip = Integer.parseInt(editString());
                            currentOrder.setTip(newTip);
                            System.out.println("Tip Changed to: $" + newTip); 
                        }catch(Exception e){
                            System.out.println(e);
                        }
                
            }
            if(input==2){
                        try{
                            int newDelivery_timestamp = Integer.parseInt(editString());
                            currentOrder.setDelivery_timestamp(newDelivery_timestamp);
                            System.out.println("Delivery Time Changed to: " + newDelivery_timestamp);    
                        }catch(Exception e){
                            System.out.println(e);  
                        }
                
            }
            if(input==3){
                    try{
                String newInstructions = editString();
                currentOrder.setInstuctions(newInstructions);
                System.out.println("Instructions Changed to: " + newInstructions);                            
                    }catch(Exception e){
                            System.out.println(e);    
                    }

            }
            if(input==4){
                        try{
                            String newDelivery_method = editString();
                            currentOrder.setDelivery_method_id(newDelivery_method);
                            System.out.println("Delivery Method Changed to: " + newDelivery_method);
                        }catch(Exception e){
                            System.out.println(e);    
                        }                    

            }
            if(input==5){
                        try{
                            String newStore = editString();
                            currentOrder.setStore_id(newStore);
                            System.out.println("Delivery Method Changed to: " + newStore);                           
                        }catch(Exception e){
                            System.out.println(e);
                        }                   

            }

            if(input==6) homeScreen();
        
        currentOrderScreen();
        
    }
        
 

	//TODO get item from item id here
	private static void viewEditOrderItems(Order order) {
		System.out.println("*View Items*");
		ArrayList<String> itemIds = currentOrder.getItem_ids();
		ArrayList<Menu> items = sw.getMenuItems(itemIds);
		if(items.isEmpty()) System.out.println("No items");
		ServiceWrapper.printMenuItems(items);
	    int input = sc.nextInt();
	    if(input==items.size()+1) currentOrderScreen();
            else if(input > items.size()+1 || input < 1) viewEditOrderItems(order);
	    else orderItemScreen(items.get(input));
	}
	public static void orderItemScreen(Menu menu){
		/*System.out.println(menu.getName());
		System.out.println(menu.getDescription());
		System.out.println(menu.getPrice());
		System.out.println("1. Enter Quantity");
		System.out.println("2. Go Back");
		Scanner sc = new Scanner(System.in);
	    int input = sc.nextInt();
	    if(input==1) itemQuantityScreen(menu);
	    else if(input==2) System.exit(0);*/
	}

	//TODO
	public static void submitOrder(){
		System.out.println("\n*Submit*");

	    //OrderService os = new OrderService(con);
	    //input should be equal to number of items in order
	    //Menu menu = null;
	   // int input = 0;
	    //for(int i=0;i<input;i++){
	    	//create order item and add to item
	    	//os.addItem_id(menu.getId(), currentOrder.getOrder_id());
	   // }
	    //OrderService os = new OrderService(con);
	    //os.update(currentOrder);
            homeScreen();
	}
	
	public static void accountScreen(){
		System.out.println("\n*Account*");
		ArrayList<String> options = new ArrayList<>();
		options.add("Edit First Name");
		options.add("Edit Last Name");
		options.add("Edit Email");
		options.add("Edit Password");
		options.add("Edit Phone Number");
		options.add("Edit Payment Options");
		options.add("Edit Saved Locations");
		options.add("View Orders");
		options.add("Go Back");
		int count = 0;
		for(String option : options) {
			count++;
			System.out.println(count + ". " + option);
		}
	    int input = sc.nextInt();
    		if(input==1){
    			String newFirstName = editString();
    			currentUser.setFirstName(newFirstName);
    			System.out.println("First Name Changed to: " + newFirstName);
    		}
                else if(input==2){
    			String newLastName = editString();
    			currentUser.setLastName(newLastName);
    			System.out.println("Last Name Changed to: " + newLastName);
    		}
                else if(input==3){
    			String newEmail = editString();
    			currentUser.setEmail(newEmail);
    			System.out.println("Email Changed to: " + newEmail);
    		}
                else if(input==4){
    			String newPassword = editString();
    			currentUser.setPassword(newPassword);
    			System.out.println("Password Changed to: " + newPassword);
    		}
                else if(input==5){

    			String newPhoneNumber = editString();
    			currentUser.setPhone(newPhoneNumber);
    			System.out.println("Phone Number Changed to: " + newPhoneNumber);
    		}
                else if(input==6) modifyPaymentMenu();
                else if(input==7) editLocations();
                else if(input==8) allOrdersScreen();
                else if(input==9) homeScreen();
                else {
                    accountScreen();
                    return;
                }
	    
	    UserService us = new UserService(con);
	    us.update(currentUser);
	    accountScreen();
	}
	
           public static void editLocations() {
            LocationService ls = new LocationService(con);
            currentLoc = ls.getById(currentUser.getUserId());
            System.out.println("Current Address:\n" + currentLoc.getStreet() + "\n" + currentLoc.getCity() + ", " + currentLoc.getState() + " " + currentLoc.getZip() + "\n" + currentLoc.getCountry());
            System.out.println("Would you like to edit lcoation? (yes/no)");
            String option = sc.next().toLowerCase();
            if (option.equals("yes")) {
                System.out.println("Enter zip code to see if we deliver to your new location:");
                String zip = sc.next();
                if (zip.equals(85054) == true) {
                    System.out.println("Sorry, we do not deliver to your new address at this time!");
                    firstScreen();
                }
                currentLoc.setZip(zip);
                sc.nextLine();
                System.out.println("Enter street:");
                String street = sc.nextLine();
                currentLoc.setStreet(street);
                System.out.println("Enter city:");
                String city = sc.nextLine();
                currentLoc.setCity(city);
                System.out.println("Enter state:");
                String state = sc.nextLine();
                currentLoc.setState(state);
                System.out.println("Enter country:");
                String country = sc.nextLine();
                currentLoc.setCountry(country);
                //System.out.println(currentLoc);
                ls.update(currentLoc);
            }
        }
	private static void modifyPaymentMenu() {
            Scanner sc = new Scanner(System.in);
            
            CardService cardService = new CardService(con); 
            ArrayList<Card> allCards = cardService.getUserCards(currentUser.getUserId());
            
            if (allCards.isEmpty()) {
                System.out.println();
                System.out.println("You have no credit cards.");
                System.out.println();
                System.out.println("1. Add a credit card");
                System.out.println("2. Go back");
                
                switch (sc.nextInt()) {
                case 1:
                    addCreditCardMenu();
                    break;
                default:
                    accountScreen();
                    break;
                }
            } else {
                System.out.println();
                System.out.println("Your credit cards");
                for (int i = 0; i < allCards.size(); i++) {
                    System.out.println(i+1 + ". " + allCards.get(i).getCardNumber());
                }
                System.out.println();
                System.out.println("1. Delete a card");
                System.out.println("2. Add a new card");
                System.out.println("3. Go back");
                
                switch (sc.nextInt()) {
                case 1:
                    deleteCreditCardMenu(allCards);
                    break;
                case 2:
                    addCreditCardMenu();
                    break;
                case 3:
                    accountScreen();
                    break;
                }
            }
            
            modifyPaymentMenu();
	}
        
        private static void addCreditCardMenu() {
            boolean inputIsValid;
            do {
                try {
                    Scanner sc = new Scanner(System.in);
                    
                    System.out.println();
                    System.out.println("Enter your card number (12 digits): ");
                    String cardNumber = String.valueOf(sc.next("[0-9]{12}"));
                    
                    System.out.println("Enter your security code (3 digits): ");
                    String securityCode = String.valueOf(sc.next("[0-9]{3}"));
                    
                    System.out.println("Enter your expiry date (mm/dd/yyyy): ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    formatter = formatter.withLocale(Locale.US);
                    Date expiryDate = Date.valueOf(LocalDate.parse(sc.next(), formatter));
                    
                    inputIsValid = true;
                    sw.addCreditCard(currentUser.getUserId(), cardNumber, securityCode, expiryDate);
                } catch (InputMismatchException | DateTimeParseException e) {
                    System.out.println();
                    System.out.println("Invalid input. Reenter card details.");
                    inputIsValid = false;
                }
            } while(!inputIsValid);
        }
        
        private static void deleteCreditCardMenu(List<Card> allCards) {
            Scanner sc = new Scanner(System.in);
            
            System.out.println();
            System.out.println("Select which card you want to delete (Use the list #):");
            int choice;
            do {
                choice = sc.nextInt()-1;
            } while (choice < 0 || choice >= allCards.size());
            Card cardToDelete = allCards.get(choice);
            
            CardService cardService = new CardService(con);
            cardService.deleteById(cardToDelete.getCardId());
        }

	private static String editString() {
		System.out.println("Enter new value");
	    String inp = sc.next();
		return inp;
	}

	public static void allOrdersScreen(){
		System.out.println("\n*All orders*");
		OrderService os = new OrderService(con);
		ArrayList<Order> orders = os.getUserOrders(currentUser.getUserId());
		ServiceWrapper.printOrders(orders);
	    int input = sc.nextInt();
	    if(input==orders.size()) homeScreen();
	    else oldOrderScreen(orders.get(input));
	}
	public static void oldOrderScreen(Order order) {
		System.out.println("Placed: " +order.getPlaced_timestamp());
		System.out.println("Delivered: " +order.getDelivery_timestamp());
		System.out.println("Total price: " +order.getTotal_price());
		System.out.println("Method: " +order.getDelivery_method_id());
		System.out.println("Status: " +order.getDelivery_status_id());
		System.out.println("1. Reorder");
		System.out.println("2. Go Back");
	    int input = sc.nextInt();
	    if(input==1 && confirm()) {
	    	currentOrder=order;
	    	//TODO find out what the status id this thing needs is
	    	currentOrder.setDelivery_status_id("1");
	    }
	    else if(input==2) accountScreen();
	}
	public static void storeDetailsScreen(){
		System.out.println("\n*Store*");
		System.out.println("Name: " + currentStore.getStoreName());
		System.out.println("Phone Number: " + currentStore.getPhoneNumber());
		System.out.println("Location: " + currentStore.getLocationId());
		System.out.println("Open: " + currentStore.getOpenTime());
		System.out.println("Close: " + currentStore.getCloseTime());
		homeScreen();
	}

	public static boolean confirm(){        
            System.out.println("\n1Confirm");    
            System.out.println("1. Yes");     
            System.out.println("2. No");      
            
            String input = "";
            boolean check = false;
            int answer = 2;
            while(check == false){
                input = sc.next();
                if(isInteger(input)){
                    answer = Integer.parseInt(input);
                    
                    break;
                }
                else{
                    System.out.println("NOT A NUMBER, PLEASE ENTER A NUMBER.");
                }
            }
            
            
            
            return 1 == answer;
            
        }
        
        public static boolean isInteger(String s){
            boolean answer = false;
            try{
                Integer.parseInt(s);
                answer = true;
            }catch(NumberFormatException ex){
                
            }
            return answer;
        }
        public static void accountActivationMessage(String firstName, String lastName, String email){
            String to = email;
            String from = "testmummybusiness@gmail.com";
            
            Properties prop = System.getProperties();
            prop.put("mail.smtp.host","smtp.gmail.com");
            prop.put("mailsmtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.port", "465");
            
            Session session = Session.getDefaultInstance(prop, 
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("testmummybusiness@gmail.com", "test123@");
                        }
                    });
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Thank you for creating you account!");
                message.setText("Hello" + firstName + " " + lastName + ", \n\n " +
                        "First off I want to welcome you to the Mummy family. You can check out " + 
                        "our specials and menu options on your homepage. We hope you enjoy our food " +
                        "you leave with a food coma. So enjoy your access to our food. \n\n" + 
                        "Thank you for creating your account, \n Mama Mummy");
                
                Transport.send(message);
                System.out.println("Account creation message sent...");
                
            }catch(MessagingException me){
                me.printStackTrace(System.out);
            }
        }
        
        public static void orderConfirmationMessage(String firstName, String lastName, String email, Order order){
            String to = email;
            String from = "testmummybusiness@gmail.com";
            
            Properties prop = System.getProperties();
            prop.put("mail.smtp.host","smtp.gmail.com");
            prop.put("mailsmtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.port", "465");
            
            Session session = Session.getDefaultInstance(prop, 
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("testmummybusiness@gmail.com", "test123@");
                        }
                    });
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Thank you for placing your orders !");
                
                String text = "Hello " + firstName + " " + lastName + ", \n\n " +
                        "Thank you for placing an order with us! Hopefully the food is up to your " +
                        "standards and we hope to have another order from you! Also attached to this " +
                        "email is your receipt for this order. \n\n" + 
                        "Thank you for ordering from Mummy's, \n Mama Mummy";
                
                MimeBodyPart messageBody = new MimeBodyPart();
                messageBody.setContent(text,"text/html");
                
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBody);
                
                ArrayList<String> itemIds = currentOrder.getItem_ids();
		ArrayList<Menu> items = sw.getMenuItems(itemIds);
                ServiceWrapper.printReceipt(items,order);
                
                MimeBodyPart attachment = new MimeBodyPart();
                
                Path currentRelativePath = Paths.get("");
                String s = currentRelativePath.toAbsolutePath().toString();
                String finalPath = s + "\\src\\cli\\receipt.txt";
                //System.out.println("Current relative path is: " + finalPath);
                
                
                String file = finalPath;
                DataSource source = new FileDataSource(file);
                attachment.setDataHandler(new DataHandler(source));
                attachment.setFileName(file);
                multipart.addBodyPart(attachment);
                
                message.setContent(multipart);
                Transport.send(message);
                System.out.println("Order receipt creation message sent...");
                
            }catch(MessagingException me){
                me.printStackTrace(System.out);
            }
        }
}
