package com.shopping_cart_project.shopping_cart_project.Service;

import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    //引入ServiceRepository，讓我們可以操作商品資料庫
    private final ProductRepository productRepository;
    //建構式，用於依賴注入 ProductRepository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //新增商品
    //傳入一個商品實例
    public Product addProduct(Product product) {
        //product是我們傳入的商品實例
        //儲存商品到資料庫並返回儲存的商品
        return productRepository.save(product);
    }

    //刪除某商品
        //返回字串
    public String deleteProduct(Long id){
        productRepository.deleteById(id);
        return "已刪除該商品";
    }

    //查詢商品
        // 有可能要查詢的商品並不存在->拋出例外
    public Product getProductById(Long id) throws Exception{
        //Optional 本身永遠不會為 null
       Optional<Product> product =  productRepository.findById(id);
       //所以判斷式不使用是否等於null，而用.isPresent
        if (product.isPresent()) {
            return product.get();
        }
           throw new Exception("找不到該商品");
    }
    //其他寫法1
    /*
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("找不到該商品"));
    }
    */

    //其他寫法2，不用記，只是想嘗試看看本來的想法可不可行，但不實用。
    /*
    public Product getProductById(Long id){
                      //findById() 預設返回的是 Optional 這邊不想用，所以改為findOne(id)
       Product product =  productRepository.findOne(id);
        if (product != null) {
            return product;
        }else{
        throw new RuntimeException("找不到該商品");
        }
    }
        //如果 ProductRepository 沒有提供 findOne() 或類似的方法，
        //可以在 ProductRepository 介面中添加自定義查詢方法：
            // 在 ProductRepository 中新增
            /*
            @Query("SELECT p FROM Product p WHERE p.id = :id")
            Product findProductById(@Param("id") Long id);
         */
}
