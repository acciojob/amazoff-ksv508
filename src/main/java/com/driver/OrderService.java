package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    //@Autowired
    OrderRepository orderRepo = new OrderRepository();
    public void addOrder(Order order) {
        orderRepo.addOrder(order);

    }

    public void addPartner(String partnerId) {

        orderRepo.addPartner(new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepo.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        if(orderId == null || orderId.isEmpty())
            throw new RuntimeException();
        return orderRepo.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if(partnerId == null || partnerId.isEmpty())
            throw new RuntimeException();
        return orderRepo.getPartnerById(partnerId);
    }


    public Integer getOrderCountByPartnerId(String partnerId) {
        return orderRepo.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> list = new ArrayList<>();
        Map<String,String> map = orderRepo.getOrderPartnerMap();

        for(Map.Entry<String,String> entry : map.entrySet()){
            if(entry.getValue().equals(partnerId))
                list.add(entry.getKey());
        }
        return list;
    }

    public List<String> getAllOrders() {
        return orderRepo.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {

        return orderRepo.getCountOfUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Map<String,Order> orderMap = orderRepo.getOrderMap();
        List<String> orders = getOrdersByPartnerId(partnerId);

        String[] res = time.split(":");
        Integer t = Integer.parseInt(res[0]) * 60 + Integer.parseInt(res[1]);

        Integer count = 0;
        for(String order : orders){
            Order object = orderMap.get(order);
            if(object.getDeliveryTime() > t)
                count++;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        Map<String,Order> orderMap = orderRepo.getOrderMap();
        List<String> orders = getOrdersByPartnerId(partnerId);

        Integer last = Integer.MIN_VALUE ;
        for(String order : orders){
            Order object = orderMap.get(order);
            last = Math.max(last,object.getDeliveryTime());
        }

        String h = (last / 60)+"";
        if(h.length() == 1)
            h = "0"+h;
        String m = (last % 60)+"";
        if(m.length() == 1)
            m = "0"+m;

        return h+":"+m;


    }

    public void deletePartnerById(String partnerId) {
        orderRepo.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepo.deleteOrderById(orderId);
    }
}