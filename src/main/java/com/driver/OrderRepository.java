package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap = new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap = new HashMap<>();
    Map<String,String> orderPartnerMap = new HashMap<>();
    public void addOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public void addPartner(DeliveryPartner deliveryPartner) {
        deliveryPartnerMap.put(deliveryPartner.getId(), deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(partnerId)) {
            orderPartnerMap.put(orderId, partnerId);
            DeliveryPartner dp = deliveryPartnerMap.get(partnerId);
            dp.setNumberOfOrders(dp.getNumberOfOrders() + 1);
            deliveryPartnerMap.put(partnerId, dp);
        }
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerMap.get(partnerId);
    }

    public List<String> getAllAssignedPartners() {
        return new ArrayList<String>(orderPartnerMap.values());
    }

    public Map<String,String> getOrderPartnerMap() {
        return new HashMap<>(orderPartnerMap);
    }

    public List<String> getAllOrders() {
        return new ArrayList<String>(orderMap.keySet());
    }

    public Map<String, Order> getOrderMap() {
        return new HashMap<>(orderMap);
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnerMap.remove(partnerId);
        Set<Map.Entry<String,String>> set = orderPartnerMap.entrySet();
        Iterator<Map.Entry<String,String>> itr = set.iterator();

        while(itr.hasNext()){
            Map.Entry<String,String> entry = itr.next();
            if(entry.getValue().equals(partnerId))
                itr.remove();
        }

    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);
        String dpId= orderPartnerMap.get(orderId);
        if(dpId != null){
            DeliveryPartner dp = deliveryPartnerMap.get(dpId);
            dp.setNumberOfOrders(dp.getNumberOfOrders() - 1);
            deliveryPartnerMap.put(dpId,dp);
        }
        orderPartnerMap.remove(orderId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return deliveryPartnerMap.get(partnerId).getNumberOfOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderMap.size() - orderPartnerMap.size();
    }
}