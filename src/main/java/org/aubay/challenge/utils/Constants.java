package org.aubay.challenge.utils;

public class Constants {

    public static final String ITEM = "Item";
    public static final String ORDER = "Order";
    public static final String STOCK_MOVEMENT = "Stock Movement";
    public static final String USER = "User";

    public static final String INVALID_ID_ERROR = "There is no %s object with id = %d in Database.";

    public static final String CHECK_PAYLOAD = "Check your payload and try again.";
    public static final String NO_ITEM = "An Order must define an Item and a User before being created.";
    public static final String NO_INCOMPLETE_ORDER_FOR_ITEM = "There is no incomplete Order object for item %s, id = %d";

    public static final String CANNOT_ALTER_COMPLETED = "You cannot alter/delete a %s object if its Order status is completed";
    public static final String CANNOT_ALTER_ITEM = "You cannot alter an Item from a Stock Movement. Delete it and create a new one";
    public static final String CANNOT_BE_LESS_THAN_ZERO = "%s object quantity cannot be less than zero.";

    public static final String CREATE_ERROR = "Error creating new %s. ";
    public static final String UPDATE_ERROR = "Error updating %s. ";
    public static final String DELETE_ERROR = "Error deleting %s. ";

    public static final String ORDER_COMPLETED = "[ORDER COMPLETED] id = %d";
    public static final String STOCK_MOVEMENT_CREATED = "[STOCK MOVEMENT] item: %s (id = %d), order id = %d, stock movement quantity = %d ";
    public static final String EMAIL_SENT = "[EMAIL SENT] Notification email sent to user: %s, email: %s (order id = %d COMPLETED)";
}
