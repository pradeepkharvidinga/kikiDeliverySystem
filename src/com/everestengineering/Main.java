package com.everestengineering;

import com.everestengineering.packages.Packages;
import com.everestengineering.vehicle.Vehicle;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(" ----------------------------------------------------------------");
        System.out.println("| Line 1       : Base_Cost No_Of_Packages(n)                     |");
        System.out.println("| Next n lines : Package_ID Package_Weight Distance Offer_Code   |");
        System.out.println("| Next Line    : No_Of_Vehicles Max_Speed Max_Carriable_Weight   |");
        System.out.println(" ----------------------------------------------------------------");

        int no_of_packages;
        int noOfVehicles;

        List<Packages> deliveryPackages = new ArrayList<>();
        List<List<Packages>> possiblePackageCombinations;
        List<Packages> selectedPackagesToDeliver;
        List<Packages> deliveredPackages = new ArrayList<>();
        List<Vehicle> vehicles = new ArrayList<>();

        String inputLine = sc.nextLine();
        Packages.setBaseCost(Integer.parseInt(inputLine.split(" ")[0]));
        no_of_packages = Integer.parseInt(inputLine.split(" ")[1]);
        for (int i = 0; i < no_of_packages; i++) {
            inputLine = sc.nextLine();
            Packages p = new Packages();
            p.setPkgId(inputLine.split(" ")[0]);
            p.setPkgWeight(Integer.parseInt(inputLine.split(" ")[1]));
            p.setDistance(Integer.parseInt(inputLine.split(" ")[2]));
            p.setOfferCode(inputLine.split(" ")[3]);
            deliveryPackages.add(p);
        }
        inputLine = sc.nextLine();
        noOfVehicles = Integer.parseInt(inputLine.split(" ")[0]);
        Vehicle.setMaxSpeed(Integer.parseInt(inputLine.split(" ")[1]));
        Vehicle.setMaxCarriableWeight(Integer.parseInt(inputLine.split(" ")[2]));

        List<Packages> deliveryCost = Packages.calculateDiscount(deliveryPackages);

        System.out.println();
        System.out.println("-Delivery Cost Estimation-");
        for (Packages p: deliveryCost )
            System.out.println(p.getPkgId() + " " + p.getDiscount() + " " + p.getTotalCost());

        for (int i = 0; i < noOfVehicles; i++) {
            Vehicle v = new Vehicle(0, true);
            vehicles.add(v);
        }
        while (deliveryPackages.size() > 0) {
            possiblePackageCombinations = new ArrayList<>();

            Collections.sort(deliveryPackages);
            int maxPackages = calculateNoOfPackagesToDeliver(deliveryPackages);

            findPossiblePackageCombinations(deliveryPackages, deliveryPackages.size(), maxPackages, possiblePackageCombinations);

            selectedPackagesToDeliver = selectPackageToDeliver(possiblePackageCombinations);
            selectVehicleAndDeliver(vehicles, deliveryPackages, selectedPackagesToDeliver, deliveredPackages);

        }
        deliveredPackages = Packages.calculateDiscount(deliveredPackages);
        System.out.println();
        System.out.println("-Delivery Time Estimation-");
        for (Packages p : deliveredPackages) {
            System.out.println(p.getPkgId() + " " + p.getDiscount() + " " + p.getTotalCost() + " " + p.getDeliveryTime());
        }
    }

    static int calculateNoOfPackagesToDeliver(List<Packages> deliveryPackages){
        int delMaxWeight = 0,maxPackages=0;
        for (Packages p: deliveryPackages){
            delMaxWeight += p.getPkgWeight();
            if(delMaxWeight < Vehicle.getMaxCarriableWeight()){
                maxPackages++;
            }else
                break;
        }
        return maxPackages;
    }


    static void findPossiblePackageCombinations(List<Packages> deliveryPackages, int nof, int maxPackSize, List<List<Packages>> possiblePackageCombinations) {
        List<Packages> pkg =new ArrayList<>();
        combinationUtil(deliveryPackages, pkg, 0, nof-1, 0, maxPackSize, possiblePackageCombinations);
    }
    static void combinationUtil(List<Packages> deliveryPackages, List<Packages> pkg, int start, int end, int index, int maxPackSize, List<List<Packages>> possiblePackageCombinations) {
        if (index == maxPackSize)
        {
            int totalWeight = 0;
            for (int j=0; j<maxPackSize; j++) {
                totalWeight += pkg.get(j).getPkgWeight();
            }
            if( totalWeight <= Vehicle.getMaxCarriableWeight()) {
                List<Packages> pk = new ArrayList<>();
                for(int i =0;i<maxPackSize;i++)
                    pk.add(pkg.get(i));
                possiblePackageCombinations.add(pk);
            }
            return;
        }
        for (int i=start; i<=end && end-i+1 >= maxPackSize-index; i++)
        {
            pkg.add(index,deliveryPackages.get(i));
            combinationUtil(deliveryPackages, pkg, i+1, end, index+1, maxPackSize, possiblePackageCombinations);
        }
    }
    static List<Packages> selectPackageToDeliver(List<List<Packages>> possiblePackageCombinations) {
        List<Packages> selectedPackagesToDeliver;
        int selectedIndex = -1;
        int maxWeight = 0;
        int deliveryMaxDistance;
        int index = 0;
        for (List<Packages> pl : possiblePackageCombinations) {
            int totalWeight = 0;
            deliveryMaxDistance = 0;
            for (Packages p : pl) {
                totalWeight += p.getPkgWeight();
                if (deliveryMaxDistance < p.getDistance()) {
                    deliveryMaxDistance = p.getDistance();
                }
                if (totalWeight > maxWeight) {
                    maxWeight = totalWeight;
                    selectedIndex = index;
                }
            }
            index++;
        }


        selectedPackagesToDeliver = new ArrayList<>(possiblePackageCombinations.get(selectedIndex));
        return selectedPackagesToDeliver;
    }

    static void selectVehicleAndDeliver(List<Vehicle> vehicles, List<Packages> deliveryPackages,
                                 List<Packages> selectedPackagesToDeliver, List<Packages> deliveredPackages){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        int maxDistance = 0;
        int maxDistancePackageIndex = 0;
        for (int i =0 ;i<selectedPackagesToDeliver.size();i++){
            if(selectedPackagesToDeliver.get(i).getDistance() > maxDistance ) {
                maxDistance = selectedPackagesToDeliver.get(i).getDistance();
                maxDistancePackageIndex = i;
            }
        }

        Vehicle vehicle = new Vehicle();
        for (Vehicle v: vehicles){
            if(Vehicle.getCurrentTime() >= v.getAvailableTime() ){
                vehicle = v;
                Vehicle.setCurrentTime(v.getAvailableTime());
            }
        }

        for (Packages p : selectedPackagesToDeliver){
            p.setDeliveryTime(Float.parseFloat(df.format(p.getDistance()/(float) Vehicle.getMaxSpeed())) +  Vehicle.getCurrentTime());
            deliveredPackages.add(p);
        }

        float returnTime = Float.parseFloat(df.format((float)selectedPackagesToDeliver.get(maxDistancePackageIndex).getDistance()/Vehicle.getMaxSpeed())) * 2;
        vehicle.setAvailableTime( Vehicle.getCurrentTime()+ returnTime );
        Vehicle.setCurrentTime(vehicle.getAvailableTime());

        deliveryPackages.removeAll(selectedPackagesToDeliver);
    }
}
