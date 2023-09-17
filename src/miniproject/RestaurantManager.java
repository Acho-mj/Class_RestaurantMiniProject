package miniproject;

import java.io.*;

public class RestaurantManager {
	Restaurant restaurant = new Restaurant();
	
	// 객체 직렬화 -> 데이터 저장하기 
	public void saveData(Restaurant restaurant) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("restaurant.dat"))) {
            outputStream.writeObject(restaurant); // 레스토랑 객체를 직렬화하여 파일에 저장
            System.out.println("Restaurant 객체 직렬화 성공");
        } catch (IOException e) {
           
        }
    }
	
	// 객체 역직렬화 -> 데이터 불러오기
	public Restaurant loadData() {
	    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("restaurant.dat"))) {
	        Restaurant deserializedRestaurant = (Restaurant) inputStream.readObject(); 
	        System.out.println("\n" + deserializedRestaurant);
	        return deserializedRestaurant;
	    } catch (IOException | ClassNotFoundException e) {
	        System.out.println(e.getMessage());
	        return null; 
	    }
	}

}

