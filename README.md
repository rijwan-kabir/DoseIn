# DoseIn

A comprehensive medicine management application built with JavaFX.

## Features

- **Dashboard**: View upcoming medicine reminders and manage your schedule
- **Manage Schedule**: Add, edit, and delete medicine reminders
- **Cost Estimator**: Search for medicines and calculate total costs with shopping cart functionality
- **Doctor Contacts**: Manage your doctor contact information
- **Emergency**: Quick access to emergency contacts and information
- **User Management**: Login and signup functionality

## Cost Estimator Feature

The Cost Estimator allows you to:
- Search through a comprehensive database of medicines
- View medicine prices
- Add medicines to a shopping cart with specified quantities
- Calculate total costs automatically
- Manage your cart (add, remove, clear items)

### How to Use Cost Estimator

1. Navigate to "Cost Estimator" from the sidebar menu
2. Use the search field to find medicines by name
3. Select a medicine from the list
4. Choose the quantity using the spinner
5. Click "Add to Cart" to add the medicine to your shopping cart
6. View your cart on the right side with total cost calculation
7. Remove individual items or clear the entire cart as needed

## Running the Application

```bash
./mvnw javafx:run
```

## Building the Application

```bash
./mvnw compile
```

## Technologies Used

- Java 21
- JavaFX 17
- Maven
- FXML for UI design
- CSS for styling

## Project Structure

### CSS Files
- `dashboard.css` - Styles for Dashboard and other main screens
- `cost_estimator.css` - Dedicated styles for Cost Estimator feature
- `manage_schedule.css` - Styles for Schedule Management
- `set_reminder.css` - Styles for Reminder functionality
- Other feature-specific CSS files

### Key Features
- **Modular CSS Architecture**: Each major feature has its own stylesheet
- **Responsive Design**: Optimized for 800x600 screen resolution
- **Consistent Theming**: Unified color scheme and typography across all features
- **BDT Currency Support**: All prices displayed in Bangladeshi Taka (৳)
