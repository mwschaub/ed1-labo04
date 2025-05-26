package ed.lab.ed1labo04.model;

import java.util.List;

public class CreateCartRequest {
    private List<CartItemRequest> cartItems;

    // Constructor vacío
    public CreateCartRequest() {}

    public List<CartItemRequest> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemRequest> cartItems) {
        this.cartItems = cartItems;
    }
}
