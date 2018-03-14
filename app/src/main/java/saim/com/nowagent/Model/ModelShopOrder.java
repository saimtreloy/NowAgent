package saim.com.nowagent.Model;

/**
 * Created by NREL on 3/14/18.
 */

public class ModelShopOrder {
    String id, order_user_id, order_user_name, order_user_phone, order_vendor_id, order_user_location, order_time, order_detail, order_total_price, order_service_chrge, order_type, order_status, order_user_message, order_vendor_message, order_bill_number;

    public ModelShopOrder(String id, String order_user_id, String order_user_name, String order_user_phone, String order_vendor_id, String order_user_location, String order_time, String order_detail, String order_total_price, String order_service_chrge, String order_type, String order_status, String order_user_message, String order_vendor_message, String order_bill_number) {
        this.id = id;
        this.order_user_id = order_user_id;
        this.order_user_name = order_user_name;
        this.order_user_phone = order_user_phone;
        this.order_vendor_id = order_vendor_id;
        this.order_user_location = order_user_location;
        this.order_time = order_time;
        this.order_detail = order_detail;
        this.order_total_price = order_total_price;
        this.order_service_chrge = order_service_chrge;
        this.order_type = order_type;
        this.order_status = order_status;
        this.order_user_message = order_user_message;
        this.order_vendor_message = order_vendor_message;
        this.order_bill_number = order_bill_number;
    }


    public String getId() {
        return id;
    }

    public String getOrder_user_id() {
        return order_user_id;
    }

    public String getOrder_user_name() {
        return order_user_name;
    }

    public String getOrder_user_phone() {
        return order_user_phone;
    }

    public String getOrder_vendor_id() {
        return order_vendor_id;
    }

    public String getOrder_user_location() {
        return order_user_location;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getOrder_detail() {
        return order_detail;
    }

    public String getOrder_total_price() {
        return order_total_price;
    }

    public String getOrder_service_chrge() {
        return order_service_chrge;
    }

    public String getOrder_type() {
        return order_type;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getOrder_user_message() {
        return order_user_message;
    }

    public String getOrder_vendor_message() {
        return order_vendor_message;
    }

    public String getOrder_bill_number() {
        return order_bill_number;
    }
}
