package nexawebcompany.foodmart.repository;

import nexawebcompany.foodmart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
