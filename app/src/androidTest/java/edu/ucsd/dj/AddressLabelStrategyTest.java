package edu.ucsd.dj;

import android.location.Address;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.dj.interfaces.models.IAddressable;
import edu.ucsd.dj.strategies.AddressLabelStrategy;

import static org.junit.Assert.assertEquals;

/**
 * Created by nguyen on 5/13/2017.
 */
public class AddressLabelStrategyTest {
    private AddressableTest address1;
    private AddressableTest address2;
    private AddressableTest address3;
    private AddressableTest address4;
    private AddressableTest defaultAddress;
    private String result1, result2, result3, result4, defaultResult;
    @Before
    public void initialize(){
        //Shanghai, China
        address1 = new AddressableTest(31.0456, 121.3997, true);
        //New York, United States
        address2 = new AddressableTest(40.7528, -73.9725, true);
        //Hanoi, Vietnam
        address3 = new AddressableTest(21.0333, 105.85, true);
        //Paris, France
        address4 = new AddressableTest(48.8667, 2.3333, true);
        //Invalid address
        defaultAddress = new AddressableTest(-1,-1, true);
        result1 = "China";
        result2 = "729 3rd Avenue New York, NY 10017 USA";
        result3 = "18A Lương Văn Can Hàng Gai Hoàn Kiếm Hà Nội Vietnam";
        result4 = "34 Rue Saint-Roch 75001 Paris France";
        defaultResult = "";
    }
    @Test
    public void constructorTest() throws Exception {

    }
    @Test
    public void generateAddressTest() throws Exception {
        AddressLabelStrategy loader = new AddressLabelStrategy(InstrumentationRegistry.getTargetContext());
        Address location1 = loader.generateAddress(address1);
        Address location2 = loader.generateAddress(address2);
        Address location3 = loader.generateAddress(address3);
        Address location4 = loader.generateAddress(address4);
        Address defaultLocation = loader.generateAddress(defaultAddress);
        String generatedAddress1 = "";
        String generatedAddress2 = "";
        String generatedAddress3 = "";
        String generatedAddress4 = "";
        String generatedDefaultAddress = "";
        for(int i = 0; i <= location1.getMaxAddressLineIndex(); i++ ){
            generatedAddress1 = location1.getAddressLine(i);
            if(i < location1.getMaxAddressLineIndex()){
                generatedAddress1 += " ";
            }
        }
        for(int i = 0; i <= location2.getMaxAddressLineIndex(); i++ ){
            generatedAddress2 += location2.getAddressLine(i);
            if(i < location2.getMaxAddressLineIndex()){
                generatedAddress2 += " ";
            }
        }
        for(int i = 0; i <= location3.getMaxAddressLineIndex(); i++ ){
            generatedAddress3 += location3.getAddressLine(i);
            if(i < location3.getMaxAddressLineIndex()){
                generatedAddress3 += " ";
            }
        }
        for(int i = 0; i <= location4.getMaxAddressLineIndex(); i++ ){
            generatedAddress4 += location4.getAddressLine(i);
            if(i < location4.getMaxAddressLineIndex()){
                generatedAddress4 += " ";
            }
        }
        for(int i = 0; i <= defaultLocation.getMaxAddressLineIndex(); i++ ){
            generatedDefaultAddress += defaultLocation.getAddressLine(i);
            if(i < defaultLocation.getMaxAddressLineIndex()){
                generatedDefaultAddress += " ";
            }
        }

        assertEquals(result1, generatedAddress1);
        assertEquals(result2, generatedAddress2);
        assertEquals(result3, generatedAddress3);
        assertEquals(result4, generatedAddress4);
        Log.i("Address: ", generatedDefaultAddress);
        assertEquals(defaultResult, generatedDefaultAddress);



    }
    private class AddressableTest implements IAddressable {
        private boolean hasValidCoordinates;
        private double latitude;
        private double longitude;

        public AddressableTest(double lat, double longitude, boolean hvc){
            latitude = lat;
            this.longitude = longitude;
            hasValidCoordinates = hvc;
        }
        @Override
        public boolean getHasValidCoordinates() {
            return hasValidCoordinates;
        }

        @Override
        public void setHasValidCoordinates(boolean hvc) {
            this.hasValidCoordinates = hvc;
        }

        @Override
        public double getLatitude() {
            return latitude;
        }

        @Override
        public double getLongitude() {
            return longitude;
        }

        @Override
        public void setLatitude(double lat) {
            latitude = lat;
        }

        @Override
        public void setLongitude(double lng) {
            longitude = lng;
        }
    }

}
