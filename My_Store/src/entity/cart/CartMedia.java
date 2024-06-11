package entity.cart;

import entity.media.Media;
import entity.order.OrderMedia;

public class CartMedia {

    private Media media;
    private int quantity;
    private int price;
    private boolean isSelected;

    public CartMedia() {

    }

    public CartMedia(Media media, Cart cart, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }


    /**
     * @return Media
     */
    public Media getMedia() {
        return this.media;
    }


    /**
     * @param media
     */
    public void setMedia(Media media) {
        this.media = media;
    }


    /**
     * @return int
     */
    public int getQuantity() {
        return this.quantity;
    }


    /**
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * @return int
     */
    public int getPrice() {
        return this.price;
    }


    /**
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{"
                + " media='" + media + "'"
                + ", quantity='" + quantity + "'"
                + "}";
    }

    public OrderMedia toOrderMedia() {
        // Tạo một đối tượng OrderMedia mới và thiết lập thông tin từ CartMedia
        OrderMedia orderMedia = new OrderMedia( media,  quantity, price);
        orderMedia.setMedia(this.getMedia());  // Giả sử getMedia() trả về đối tượng Media
        orderMedia.setPrice(this.getPrice());
        orderMedia.setQuantity(this.getQuantity());
        // Các thông tin khác mà bạn muốn chuyển đổi từ CartMedia sang OrderMedia

        return orderMedia;
    }
}
