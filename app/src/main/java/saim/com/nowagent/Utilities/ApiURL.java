package saim.com.nowagent.Utilities;

/**
 * Created by NREL on 2/5/18.
 */

public class ApiURL {

    public static String link_header = "http://www.globalearnmoney.com/now_agent_api/";

    public static String Login = link_header + "login.php";                 //user_email, user_pass
    public static String OrderList = link_header + "order_list.php";                 //user_email, user_pass
    public static String OrderUpdate = link_header + "order_update.php";                 //user_email, user_pass
    public static String ItemList = link_header + "item_list.php";                 //user_email, user_pass service_shop_vendor_id
    public static String Logout = link_header + "logout.php";                 //service_shop_vendor_id
    public static String Category_List = link_header + "category_list.php";                 //service_shop_vendor_id addItem.php
    public static String AddItem = link_header + "addItem.php";                 //service_shop_vendor_id addItem.php
    public static String GetItem = link_header + "item_detail.php";                 //service_shop_vendor_id addItem.php

}
