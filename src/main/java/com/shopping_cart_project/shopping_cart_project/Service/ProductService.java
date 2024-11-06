package com.shopping_cart_project.shopping_cart_project.Service;

import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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

    //查詢商品-其他寫法1
    /*
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("找不到該商品"));
    }
    */
    //查詢商品-其他寫法2，不用記，只是想嘗試看看本來的想法可不可行，但不實用。
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

    public Page<Product> getProductsByFilter(String category, Integer minPrice, Integer maxPrice, String sort, Integer pageNumber, Integer pageSize) {
        //取得第pageNumber（頁數是從0開始），每頁有pageSize個產品
            // PageRequest 是 Spring Data 提供的一個分頁工具
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

         //從資料庫取得符合條件的產品
        List<Product> products = productRepository.findProductsByFilter(category,minPrice,maxPrice,sort);

        //設定從哪裡開始取得資料；取得的指定頁數前面有多少資料，數值等於pageNumber*pageSize。
            //假設一頁有10筆，我要查詢第5頁，前幾頁的資料到第50筆(10*(5))->頁數從0開始 0-4頁的筆數=10*(1+4)，而我們查詢的起始筆數則是第51筆，但索引是從0開始，所以Index是50。
        int startIndex = (int)pageable.getOffset();

        //設定從哪裡結束，這樣可以確保不會超出清單的範圍
            // Math.min 用來取得剩餘資料數量與 pageSize 的最小值，這樣可以避免抓取超過資料清單長度的資料
            //如果剩餘的資料>=pageSize，就只取其中的pageSize筆。
            //如果剩餘的資料<pageSize，將剩下的資料全部取得。
                // Math.min 會選擇兩個數值中較小的一個；products.size()是資料的總長度
        int endIndex = Math.min((startIndex + pageable.getPageSize()),products.size());

        //從過濾後的產品列表，截取對應頁數和數量的產品
            // // `subList(startIndex, endIndex)` 會返回從 startIndex 到 endIndex 之間的子清單
        List<Product> pageContent = products.subList(startIndex,endIndex);


        // 最後，我們返回一個 PageImpl 物件，這是 Spring Data 提供的一個實現 Page 介面的類別
        // PageImpl 需要傳入：分頁資料內容（pageContent）、Pageable 物件（包含分頁資訊）、以及資料總數（總商品數量）
            //回傳內容、分頁資訊（頁碼、一頁有幾筆資料）、符合過濾條件的產品數量
        return new PageImpl<>(pageContent,pageable, products.size());
    }
}

