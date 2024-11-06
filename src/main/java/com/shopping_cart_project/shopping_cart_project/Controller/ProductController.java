package com.shopping_cart_project.shopping_cart_project.Controller;

import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
public class ProductController {
    // 调用 service層方法添加商品
    private final ProductService productService;
    //建構式
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //新增
    @PostMapping("/")
    public ResponseEntity<Product> addProduct (@RequestBody Product product){
        Product newProduct = productService.addProduct(product);
        //返回新增的商品數據並設置HTTP狀態 201 (Created)
        return new ResponseEntity<>(newProduct,HttpStatus.CREATED);
    }

    //刪除
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct (@PathVariable("id") Long id){
        return new ResponseEntity<>(productService.deleteProduct(id),HttpStatus.OK);
    }

    //查詢
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById (@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }

}
