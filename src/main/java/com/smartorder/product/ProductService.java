package com.smartorder.product;

import com.smartorder.product.dto.ProductRequest;
import com.smartorder.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.Instant;

public interface ProductService {

     ProductResponse createProduct(ProductRequest productRequest);
     ProductResponse updateProduct(Long id,ProductRequest productRequest);
     ProductResponse getByProductId(Long id);
     void  deleteById(Long id);
      Page<ProductResponse> search(
             String name,
             BigDecimal minprice,
             BigDecimal maxprice,
             Boolean active,
             Instant createdFrom,
             Instant createdTo,
             int page,
             int size
     );
}
