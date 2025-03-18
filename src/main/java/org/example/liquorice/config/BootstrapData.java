package org.example.liquorice.config;

import org.example.liquorice.models.Product;
import org.example.liquorice.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BootstrapData {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BootstrapData.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepo) {
        return args -> {
            String electronics = "Electronics";
            String accessories = "Accessories";
            String clothing = "Clothing";
            String home = "Home Goods";
            String sports = "Sports Equipment";
            String books = "Books";
            String food = "Food & Beverages";

            List<Product> products = new ArrayList<>();

            Product gamepad = new Product();
            gamepad.setName("Gamepad Controller");
            gamepad.setDescription("Wireless gamepad controller for gaming consoles");
            gamepad.setPrice(59.99);
            gamepad.setCategories(List.of(electronics, accessories));
            gamepad.setAmountLeft(50);
            products.add(gamepad);


            Product smartphone = new Product();
            smartphone.setName("SmartPhone Pro");
            smartphone.setDescription("Latest model smartphone with high-resolution camera");
            smartphone.setPrice(899.99);
            smartphone.setCategories(List.of(electronics));
            smartphone.setAmountLeft(30);
            products.add(smartphone);


            Product laptop = new Product();
            laptop.setName("UltraBook Laptop");
            laptop.setDescription("Lightweight laptop for productivity and entertainment");
            laptop.setPrice(1299.99);
            laptop.setCategories(List.of(electronics));
            laptop.setAmountLeft(20);
            products.add(laptop);


            Product headphones = new Product();
            headphones.setName("Noise Cancelling Headphones");
            headphones.setDescription("Wireless headphones with active noise cancellation");
            headphones.setPrice(199.99);
            headphones.setCategories(List.of(electronics, accessories));
            headphones.setAmountLeft(45);
            products.add(headphones);


            Product tshirt = new Product();
            tshirt.setName("Cotton T-Shirt");
            tshirt.setDescription("Comfortable cotton t-shirt in various colors");
            tshirt.setPrice(24.99);
            tshirt.setCategories(List.of(clothing));
            tshirt.setAmountLeft(100);
            products.add(tshirt);


            Product smartWatch = new Product();
            smartWatch.setName("Smart Fitness Watch");
            smartWatch.setDescription("Track your fitness and receive notifications on the go");
            smartWatch.setPrice(149.99);
            smartWatch.setCategories(List.of(electronics, accessories, sports));
            smartWatch.setAmountLeft(35);
            products.add(smartWatch);


            Product coffeeMaker = new Product();
            coffeeMaker.setName("Programmable Coffee Maker");
            coffeeMaker.setDescription("Brew your coffee automatically with programmable settings");
            coffeeMaker.setPrice(89.99);
            coffeeMaker.setCategories(List.of(home));
            coffeeMaker.setAmountLeft(25);
            products.add(coffeeMaker);


            Product yogaMat = new Product();
            yogaMat.setName("Professional Yoga Mat");
            yogaMat.setDescription("Non-slip yoga mat perfect for all types of yoga");
            yogaMat.setPrice(45.99);
            yogaMat.setCategories(List.of(sports));
            yogaMat.setAmountLeft(60);
            products.add(yogaMat);


            Product novel = new Product();
            novel.setName("Best-selling Novel");
            novel.setDescription("Award-winning fiction book by a renowned author");
            novel.setPrice(19.99);
            novel.setCategories(List.of(books));
            novel.setAmountLeft(75);
            novel.setImage(loadImage("src/main/resources/bootstrap/novel.jpg"));
            products.add(novel);


            Product coffee = new Product();
            coffee.setName("Gourmet Coffee Beans");
            coffee.setDescription("Freshly roasted premium coffee beans from Colombia");
            coffee.setPrice(29.99);
            coffee.setCategories(List.of(food));
            coffee.setAmountLeft(40);
            products.add(coffee);


            Product speaker = new Product();
            speaker.setName("Portable Bluetooth Speaker");
            speaker.setDescription("Waterproof speaker with 20-hour battery life");
            speaker.setPrice(79.99);
            speaker.setCategories(List.of(electronics));
            speaker.setAmountLeft(55);
            products.add(speaker);


            Product shoes = new Product();
            shoes.setName("Performance Running Shoes");
            shoes.setDescription("Lightweight running shoes with cushioning technology");
            shoes.setPrice(129.99);
            shoes.setCategories(List.of(clothing, sports));
            shoes.setAmountLeft(30);
            products.add(shoes);


            Product homeHub = new Product();
            homeHub.setName("Smart Home Control Hub");
            homeHub.setDescription("Central control for all your smart home devices");
            homeHub.setPrice(149.99);
            homeHub.setCategories(List.of(electronics, home));
            homeHub.setAmountLeft(20);
            products.add(homeHub);


            Product cookBook = new Product();
            cookBook.setName("International Cuisine Cookbook");
            cookBook.setDescription("Collection of recipes from around the world");
            cookBook.setPrice(34.99);
            cookBook.setCategories(List.of(books, food));
            cookBook.setAmountLeft(45);
            products.add(cookBook);


            Product lamp = new Product();
            lamp.setName("Adjustable Desk Lamp");
            lamp.setDescription("LED desk lamp with multiple brightness settings");
            lamp.setPrice(49.99);
            lamp.setCategories(List.of(home));
            lamp.setAmountLeft(65);
            products.add(lamp);


            Product phoneCase = new Product();
            phoneCase.setName("Protective Phone Case");
            phoneCase.setDescription("Shock-absorbing case for popular smartphone models");
            phoneCase.setPrice(24.99);
            phoneCase.setCategories(List.of(accessories));
            phoneCase.setAmountLeft(80);
            products.add(phoneCase);


            Product fitTracker = new Product();
            fitTracker.setName("Advanced Fitness Tracker");
            fitTracker.setDescription("Track steps, sleep, and workout performance");
            fitTracker.setPrice(99.99);
            fitTracker.setCategories(List.of(electronics, sports));
            fitTracker.setAmountLeft(40);
            products.add(fitTracker);


            Product chocolate = new Product();
            chocolate.setName("Premium Chocolate Box");
            chocolate.setDescription("Assortment of fine chocolates in an elegant box");
            chocolate.setPrice(39.99);
            chocolate.setCategories(List.of(food));
            chocolate.setAmountLeft(50);
            products.add(chocolate);


            Product jacket = new Product();
            jacket.setName("Winter Insulated Jacket");
            jacket.setDescription("Warm and water-resistant jacket for cold weather");
            jacket.setPrice(179.99);
            jacket.setCategories(List.of(clothing));
            jacket.setAmountLeft(25);
            products.add(jacket);


            Product hardDrive = new Product();
            hardDrive.setName("2TB External Hard Drive");
            hardDrive.setDescription("Portable storage solution with high transfer speeds");
            hardDrive.setPrice(89.99);
            hardDrive.setCategories(List.of(electronics, accessories));
            hardDrive.setAmountLeft(30);
            products.add(hardDrive);

            productRepo.saveAll(products);
        };
    }

    private byte[] loadImage(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.error("Error loading image from path: {}", path, e);
            return null;
        }
    }
}