package saim.com.nowagent.Model;

/**
 * Created by NREL on 3/21/18.
 */

public class ModelCategory {

    String id, service_shop_ic_id, service_shop_ic_name, service_shop_ic_icon, service_shop_ic_color, service_shop_id;

    public ModelCategory(String id, String service_shop_ic_id, String service_shop_ic_name, String service_shop_ic_icon, String service_shop_ic_color, String service_shop_id) {
        this.id = id;
        this.service_shop_ic_id = service_shop_ic_id;
        this.service_shop_ic_name = service_shop_ic_name;
        this.service_shop_ic_icon = service_shop_ic_icon;
        this.service_shop_ic_color = service_shop_ic_color;
        this.service_shop_id = service_shop_id;
    }

    public String getId() {
        return id;
    }

    public String getService_shop_ic_id() {
        return service_shop_ic_id;
    }

    public String getService_shop_ic_name() {
        return service_shop_ic_name;
    }

    public String getService_shop_ic_icon() {
        return service_shop_ic_icon;
    }

    public String getService_shop_ic_color() {
        return service_shop_ic_color;
    }

    public String getService_shop_id() {
        return service_shop_id;
    }
}
