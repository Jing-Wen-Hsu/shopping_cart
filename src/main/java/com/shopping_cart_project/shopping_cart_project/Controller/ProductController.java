package com.shopping_cart_project.shopping_cart_project.Controller;

import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    // 調用 service層方法添加商品
    private final ProductService productService;
    //建構式
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //新增(單一或批次)
    @PostMapping("/")
    public ResponseEntity<List<Product>> addProducts (@RequestBody Product[] products){
        //用陣列來裝，new出一個空間
        List<Product> newProducts = new ArrayList<>();
        //for迴圈 將要新增的商品一一放入
        for(Product product : products) {
            Product p = productService.addProduct(product);
            newProducts.add(p);
        }
        //返回新增的商品數據並設置HTTP狀態 201 (Created)
        return new ResponseEntity<>(newProducts,HttpStatus.CREATED);
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

    //商品過濾功能
    //根據條件篩選並分頁商品(種類/價格/sort排序條件(例如 "price" 或 "name")
    //pageNumber 和 pageSize 需要使用者傳入，這兩個參數是用來進行分頁的，表示要顯示哪一頁和每頁顯示多少項目。
    @GetMapping("/")
    //value用來定位URL中的參數，required = false代表可以不填，required = true是強制要填資料
    //不填就是全部都呈現
    public ResponseEntity<Page<Product>> findProductByFilter(
            @RequestParam(value = "category",required  = false) String category,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
            @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<Product> filterProductsPage = productService.getProductsByFilter(category, minPrice, maxPrice, sort, pageNumber, pageSize);
        return new ResponseEntity<>(filterProductsPage, HttpStatus.OK);
    }
}
