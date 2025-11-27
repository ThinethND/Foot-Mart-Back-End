package nexawebcompany.foodmart.controller;

import nexawebcompany.foodmart.model.Product;
import nexawebcompany.foodmart.repository.ProductRepository;
import nexawebcompany.foodmart.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService service;
    private final ProductRepository productRepository;

    public ProductController(ProductService service, ProductRepository productRepository) {
        this.service = service;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

//    @PostMapping
//    public Product createProduct(@RequestBody Product product) {
//        return service.saveProduct(product);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
//        try {
//            // Folder inside your project
//            String uploadDir = System.getProperty("user.dir") + "/upload/";
//
//            // Create folder if not exist
//            File directory = new File(uploadDir);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            // Correct file path
//            String filePath = uploadDir + file.getOriginalFilename();
//
//            // Save file
//            file.transferTo(new File(filePath));
//
//            // Return public image URL
//            String imageUrl = "http://localhost:8081/upload/" + file.getOriginalFilename();
//
//            return ResponseEntity.ok(imageUrl);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
//        }
//    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProductWithImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/upload/";

            File directory = new File(uploadDir);
            if(!directory.exists()) directory.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            String filePath = uploadDir + fileName;

            imageFile.transferTo(new File(filePath));

            String imageUrl = "http://localhost:8081/upload/" + fileName;

            Product  product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);

            Product savedProduct = service.saveProduct(product);

            return ResponseEntity.ok(savedProduct);
        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
