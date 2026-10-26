package com.bagshop.config;

import com.bagshop.entity.Product;
import com.bagshop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class DataInitializer {

    private static final int PRODUCT_COUNT = 500;

    private static final String[] TYPES = {
            "Tote", "Backpack", "Crossbody", "Duffel", "Satchel", "Clutch", "Messenger",
            "Sling", "Weekender", "Laptop Bag", "Travel Bag", "Hobo", "Bucket Bag", "Belt Bag"
    };

    private static final String[] MATERIALS = {
            "Leather", "Canvas", "Nylon", "Vegan Leather", "Suede", "Recycled Polyester", "Cotton"
    };

    private static final String[] COLORS = {
            "Black", "Brown", "Tan", "Navy", "Olive", "Burgundy", "Grey", "Beige", "Coral", "Teal"
    };

    private static final String[] IMAGE_IDS = {
            "1590874103328-eac38a683ce7",
            "1553062407-98eeb64c6a62",
            "1548036328-c9fa89d12836",
            "1622560480605-d83c853a5e2c",
            "1491637639811-60e2756cc1c7",
            "1584917865448-9693cef04085",
            "1564422176984-893c053459de",
            "1520104820794-220cc4c8c365"
    };

    @Bean
    CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() >= PRODUCT_COUNT) {
                return;
            }
            productRepository.deleteAll();

            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            List<Product> batch = new ArrayList<>(PRODUCT_COUNT);

            for (int i = 1; i <= PRODUCT_COUNT; i++) {
                String type = TYPES[rnd.nextInt(TYPES.length)];
                String material = MATERIALS[rnd.nextInt(MATERIALS.length)];
                String color = COLORS[rnd.nextInt(COLORS.length)];
                String name = String.format("%s %s %s #%03d", color, material, type, i);
                String description = String.format(
                        "%s %s bag in %s. Durable stitching, multiple compartments, everyday carry.",
                        color, material.toLowerCase(), type.toLowerCase(), color.toLowerCase());
                BigDecimal price = BigDecimal.valueOf(rnd.nextDouble(19.99, 249.99))
                        .setScale(2, RoundingMode.HALF_UP);
                int stock = rnd.nextInt(5, 120);
                String imageId = IMAGE_IDS[rnd.nextInt(IMAGE_IDS.length)];
                String imageUrl = "https://images.unsplash.com/photo-" + imageId + "?w=400";

                batch.add(Product.builder()
                        .name(name)
                        .description(description)
                        .price(price)
                        .stock(stock)
                        .imageUrl(imageUrl)
                        .build());
            }

            productRepository.saveAll(batch);
        };
    }
}
