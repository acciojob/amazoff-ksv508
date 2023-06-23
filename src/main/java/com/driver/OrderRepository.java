package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    Map<String,Order> orderDb = new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerDb = new HashMap<>();
    Map<String,List<String>> partnerOrdersDb = new HashMap<>();
    Map<String,String> orderPartnerDb = new HashMap<>();

    public void addOrder(Order order) {
        orderDb.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        deliveryPartnerDb.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderDb.containsKey(orderId) && deliveryPartnerDb.containsKey(partnerId)){
            orderPartnerDb.put(orderId,partnerId);
            List<String> currentOrder = new ArrayList<>();
            if(partnerOrdersDb.containsKey(partnerId)){
                currentOrder = partnerOrdersDb.get(partnerId);
            }
            currentOrder.add(orderId);
            partnerOrdersDb.put(partnerId,currentOrder);
            // increase the no of partner
            DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(currentOrder.size());
        }
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrdersDb.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrdersDb.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for(String order : orderDb.keySet())
            orders.add(order);
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderDb.size() - orderPartnerDb.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int count = 0;
        List<String> orders = partnerOrdersDb.get(partnerId);

        for(String orderId : orders){
            int deliveryTime = orderDb.get(orderId).getDeliveryTime();
            if(deliveryTime >time)
                count++;
        }
        return count;
    }

    public Integer getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxTime = 0;
        List<String> orders = partnerOrdersDb.get(partnerId);
        for(String orderId : orders){
            int currentTime = orderDb.get(orderId).getDeliveryTime();
            maxTime = Math.max(maxTime,currentTime);
        }
        return maxTime;
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnerDb.remove(partnerId);
        List<String> listOfOrders = partnerOrdersDb.get(partnerId);
        partnerOrdersDb.remove(partnerId);
        for(String order : listOfOrders){
            orderPartnerDb.remove(order);
        }
    }

    public void deleteOrderById(String orderId) {
        orderDb.remove(orderId);

        String partnerId = orderPartnerDb.get(orderId);
        orderPartnerDb.remove(orderId);

        partnerOrdersDb.get(partnerId).remove(orderId);
        deliveryPartnerDb.get(partnerId).setNumberOfOrders(partnerOrdersDb.get(partnerId).size());

    }
}
