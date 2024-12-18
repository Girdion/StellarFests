package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import util.Database;
import util.UserSession;

public class User {
	private String user_id;
	private String username;
	private String password;
	private String email;
	private String role;

	private Database connect;

	public User() {
		connect = Database.getInstance();
	}

	public User(String user_id, String username, String password, String email, String role) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public Vector<User> getAllUsers() {
		Vector<User> users = new Vector<>();
		String query = "SELECT * FROM users";
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String id = connect.rs.getString("id");  // Updated to 'id'
				String name = connect.rs.getString("name");  // Updated to 'name'
				String password = connect.rs.getString("password");
				String email = connect.rs.getString("email");
				String role = connect.rs.getString("role");
				users.add(new User(id, name, password, email, role));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}



	public String register(String name, String password, String email, String role) {
		String validationResult = checkRegisterInput(name, password, email, role);
		if (!validationResult.equals("Validation successful")) {
			return validationResult;
		}


		String query = "INSERT INTO users (name, password, email, role) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement ps = connect.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.setString(4, role);
			ps.executeUpdate();
			return "Registration successful";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Registration failed: Database error";
		}
	}

	// 6. Check change profile input
	public String checkChangeProfileInput(String email, String name, String oldPassword, String newPassword) {
		if (!validateEmail(email).isEmpty()) {
			return validateEmail(email);
		}
		if (name == null || name.trim().isEmpty()) {
			return "Username is required";
		}
		if (!validatePassword(newPassword).isEmpty()) {
			return validatePassword(newPassword);
		}
		if (oldPassword == null || oldPassword.trim().isEmpty()) {
			return "Old password is required";
		}
		return "Validation successful";
	}

	public User validateUserCredentials(String email, String password) {
	    String query = "SELECT * FROM users WHERE email = ? AND password = ?";
	    try (PreparedStatement ps = connect.prepareStatement(query)) {
	        ps.setString(1, email);  // Use email instead of username
	        ps.setString(2, password);  // Use password
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            String user_id = rs.getString("id");
	            String userName = rs.getString("name");
	            String pass = rs.getString("password");
	            String emailFromDb = rs.getString("email");
	            String role = rs.getString("role");

	            return new User(user_id, userName, pass, emailFromDb, role);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}


	public String login(String email, String password) {
	    // Check if the email and password are admin credentials
	    if (email.equals("admin") && password.equals("admin")) {
	        return "Admin";  // Return the role for admin login
	    }

	    // Validate email (this can be optional if you want to do other checks)
	    String emailValidation = validateEmailLogin(email);
	    if (!emailValidation.isEmpty()) {
	        return emailValidation;  // Return the validation error if email is invalid
	    }

	    // Validate password
	    String passwordValidation = validatePassword(password);
	    if (!passwordValidation.isEmpty()) {
	        return passwordValidation;  // Return the validation error if password is invalid
	    }

	    // Validate the user credentials (this connects to the database or service)
	    User validatedUser = validateUserCredentials(email, password);  // Use email instead of username
	    if (validatedUser != null) {
	    	 UserSession.setLoggedInUser(validatedUser);
	        return validatedUser.getRole();  // Return the role of the validated user (e.g., "admin", "guest", etc.)
	    } else {
	        return "Invalid email or password. Please try again.";  // Return an error if credentials are incorrect
	    }
	}



	public String changeProfile(String newName, String newEmail, String oldPassword, String newPassword) {
	    // Step 1: Validate the inputs first
	    if (newEmail == null || newName == null || oldPassword == null || newPassword == null) {
	        return "All fields are required.";
	    }

	    // Step 2: Get the current logged-in user from the UserSession
	    User currentUser = UserSession.getLoggedInUser();
	    if (currentUser == null) {
	        return "No user is logged in.";
	    }

	    // Check if the old password matches the current user's password
	    if (!currentUser.getPassword().equals(oldPassword)) {
	        return "Incorrect old password.";
	    }

	    // Step 3: Validate the email change (i.e., can't be the same as the current email)
	    if (newEmail.equals(currentUser.getEmail())) {
	        return "The new email must be different from the current one.";
	    }

	    // Step 4: Perform the update query with the logged-in user's email
	    String query = "UPDATE users SET name = ?, email = ?, password = ? WHERE email = ? AND password = ?";
	    try (PreparedStatement ps = connect.prepareStatement(query)) {
	        // Check if the database connection is null
	        if (connect == null) {
	            return "Database connection is not available.";
	        }

	        // Set the parameters for the query (name, new email, new password, old email, and old password)
	        ps.setString(1, newName);         // Set the new name
	        ps.setString(2, newEmail);        // Set the new email
	        ps.setString(3, newPassword);     // Set the new password
	        ps.setString(4, currentUser.getEmail());  // Current logged-in user's email
	        ps.setString(5, oldPassword);     // The old password entered by the user

	        // Execute the update
	        int rowsUpdated = ps.executeUpdate();
	        if (rowsUpdated > 0) {
	            // Update the UserSession with the new email after successful update
	            currentUser.setEmail(newEmail);
	            currentUser.setName(newName);
	            currentUser.setPassword(newPassword);  // Update password as well

	            return "Profile updated successfully.";
	        } else {
	            return "Update failed: No changes were made.";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Update failed: Database error - " + e.getMessage();
	    }
	}

	  public void setEmail(String email) {
	        this.email = email;
	    }

	    public void setName(String username) {
	        this.username = username;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }


	// Method to check if the user exists with the given email and old password
	private boolean doesUserExist(String email, String oldPassword) {
	    String query = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?";
	    try (PreparedStatement ps = connect.prepareStatement(query)) {
	        ps.setString(1, email);
	        ps.setString(2, oldPassword);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}



	// 3. Get user by email
	public User getUserByEmail(String email) {
		String query = "SELECT * FROM users WHERE email = ?";
		try {
			PreparedStatement ps = connect.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new User(
						rs.getString("id"),       
						rs.getString("name"),     
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("role")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 4. Get user by name
	public User getUserByName(String name) {   // Updated method name to match 'name' column
		String query = "SELECT * FROM users WHERE name = ?";  // Updated 'username' to 'name'
		try {
			PreparedStatement ps = connect.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new User(
						rs.getString("id"),       
						rs.getString("name"),     
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("role")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 5. Check registration input
	public String checkRegisterInput(String name, String password, String email, String role) {
		if (!validateName(name).isEmpty()) {
			return validateName(name); // Uses the correct validation method for name
		}
		if (!validatePassword(password).isEmpty()) {
			return validatePassword(password);
		}
		if (!validateEmail(email).isEmpty()) {
			return validateEmail(email);
		}
		if (!validateRole(role).isEmpty()) {
			return validateRole(role);
		}
		return "Validation successful";
	}


	public String validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return "Name is required";
		}
		if (name.trim().length() < 3) {
			return "Name must be at least 3 characters";
		}
		if (!isNameUnique(name)) {
			return "Name already taken";
		}
		return "";
	}


	private boolean isNameUnique(String name) {
		String query = "SELECT COUNT(*) FROM users WHERE name = ?";
		try (PreparedStatement ps = connect.prepareStatement(query)) {
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return count == 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	// Validate password
	public String validatePassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			return "Password is required";
		}
		if (password.trim().length() < 5) {
			return "Password must be at least 5 characters";
		}
		return "";
	}
	
	private String validateEmailLogin(String email) {
	    if (email == null || email.isEmpty()) {
	        return "Email cannot be empty.";
	    }
	    
	
	    String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
	    if (!email.matches(emailRegex)) {
	        return "Invalid email format.";
	    }


	    return "";  // Return empty string if email is valid
	}

	// Validate email
	public String validateEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return "Email is required";
		}
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return "Invalid email format";
		}
		if (!isEmailUnique(email)) {
			return "Email already taken";
		}
		return "";
	}

	public boolean isEmailUnique(String email) {
	    if (email == null) {
	        throw new IllegalArgumentException("Email cannot be null");
	    }

	    if (connect == null) {
	        throw new IllegalStateException("Database connection is not available");
	    }

	    String query = "SELECT COUNT(*) FROM users WHERE email = ?";
	    try (PreparedStatement ps = connect.prepareStatement(query)) {
	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            int count = rs.getInt(1);
	            return count == 0; // Email is unique if count is 0
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false; // If any error occurs or email isn't unique
	}


	// Validate role
	public String validateRole(String role) {
		if (role == null || role.trim().isEmpty()) {
			return "Role is required";
		}
		if (!role.equals("Event Organizer") && !role.equals("Vendor") && !role.equals("Guest")) {
			return "Role must be Event Organizer, Vendor, or Guest";
		}
		return "";
	}

	public String getUser_id() {
		return user_id;
	}

	public String getUsername() {
		return username;
	}

	public Database getConnect() {
		return connect;
	}

	public void setConnect(Database connect) {
		this.connect = connect;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}
}
