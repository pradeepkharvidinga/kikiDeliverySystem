package com.everestengineering.packages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Packages implements Comparable<Packages> {
    static int baseCost;
    String pkgId;
    int pkgWeight;
    int distance;
    String offerCode;
    float deliveryTime;
    float discount;
    float totalCost;

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public float getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(float deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public static int getBaseCost() {
        return baseCost;
    }

    public static void setBaseCost(int baseCost) {
        Packages.baseCost = baseCost;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public int getPkgWeight() {
        return pkgWeight;
    }

    public void setPkgWeight(int pkgWeight) {
        this.pkgWeight = pkgWeight;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public static List<Packages> calculateDiscount(List<Packages> packages) {
        JSONParser jsonParser = new JSONParser();
        Long discount;
        try {
            Object obj = jsonParser.parse(new FileReader(".\\data\\OfferCriteria.json"));
            JSONObject offerCriteriaData = (JSONObject) obj;
            for (Packages p : packages) {
                JSONObject offer = (JSONObject) offerCriteriaData.get(p.getOfferCode().toUpperCase());
                int deliveryCost = (Packages.getBaseCost() + p.getPkgWeight() * 10 + p.getDistance() * 5);
                p.setTotalCost(deliveryCost);
                if (offerCriteriaData.containsKey(p.getOfferCode().toUpperCase())) {
                    if (p.getPkgWeight() >= (Long) offer.get("minWeight") &&
                            p.getPkgWeight() <= (Long) offer.get("maxWeight") &&
                            p.getDistance() >= (Long) offer.get("minDistance") &&
                            p.getDistance() <= (Long) offer.get("maxDistance")) {
                        discount = deliveryCost * (Long) offer.get("discountPercentage") / 100;
                        p.setDiscount(discount);
                        p.setTotalCost(deliveryCost - discount);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return packages;
    }

    @Override
    public int compareTo(Packages o) {
        if(this.getPkgWeight() > o.getPkgWeight())
            return 1;
        else if(this.getPkgWeight()< o.getPkgWeight())
            return -1;
        else return 0;
    }
}

