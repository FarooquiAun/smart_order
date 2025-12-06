package com.smartorder.product;

import com.smartorder.product.dto.ProductRequest;
import com.smartorder.product.dto.ProductResponse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product=new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setActive(productRequest.getActive() != null ? productRequest.getActive() : true);
        product.setCreatedAt(Instant.now());
        Product saved= productRepository.save(product);
        return toDto(saved);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product=productRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("product with this Id Not found")
        );
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());

        if (productRequest.getActive() != null) {
            product.setActive(productRequest.getActive());
        }
        Product updated=productRepository.save(product);
        return toDto(updated);
    }

    @Override
    public ProductResponse getByProductId(Long id) {
        Product product=productRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Product with this Id not found")
        );
        return toDto(product);
    }

    @Override
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)){
            throw new RuntimeException("Product with this Id not found");
        }
        productRepository.deleteById(id);
    }
    public Page<ProductResponse> search(
            String name,
            BigDecimal minprice,
            BigDecimal maxprice,
            Boolean active,
            Instant createdFrom,
            Instant createdTo,
            int page,
            int size
    ){
        var spec=ProductSpecification.withFilters(name, minprice, maxprice, active, createdFrom, createdTo);
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Product> resultPage=productRepository.findAll(spec,pageable);
        List<ProductResponse> content=resultPage.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content,pageable,resultPage.getTotalElements());
    }



    private ProductResponse toDto(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getActive(),
                product.getCreatedAt()
        );
    }

}
