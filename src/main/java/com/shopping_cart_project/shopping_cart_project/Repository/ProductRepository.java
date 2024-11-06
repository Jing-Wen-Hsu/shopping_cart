package com.shopping_cart_project.shopping_cart_project.Repository;

import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {

    @Query(" SELECT p FROM Product p " +
            " WHERE (p.category = :category OR :category LIKE '') " +
            " AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            " AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            " ORDER BY " +
            " CASE :sort WHEN 'price_low' THEN p.price END ASC, " +
            " CASE :sort WHEN 'price_high' THEN p.price END DESC")
    public List<Product> findProductsByFilter(@Param("category") String category,
                                              @Param("minPrice") Integer minPrice,
                                              @Param("maxPrice") Integer maxPrice,
                                              @Param("sort") String sort);

/*
    // OR前的部分：如果指定了類別，就只找這個類別的商品
    // OR後：沒有指定類別，那就把所有商品都找出來。
    //p.category = :category：這個條件表示查詢的 Product 物件中的 category 屬性要等於傳入的參數 category。category 是在查詢中使用的命名參數（:後跟參數名）。當這個條件成立時，查詢結果將會包含符合該類別的 Product。
    //:category LIKE ''：這個條件表示如果 category 參數為空字串（""），則選擇所有的產品。也就是說，當 category 參數為空字串時，不會對 category 屬性進行過濾，所有 Product 都會被選出。這是通過 LIKE '' 來達到的，LIKE '' 會匹配空字串，意思就是「如果 category 是空字串，則不對 category 進行過濾條件」。
   @Query(" SELECT p FROM Product p " + " WHERE (p.category = :category OR :category LIKE '')" + //Like ''空字串可以替換為category IS NULL

    //如果指定了最低價格，就只找價格比這個最低價格高的商品
   "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
    //如果指定了最高價格，就只找價格比這個最高價格低的商品
    //如果使用者兩個都沒指定，那就把所有商品都找出來。
    "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
    //依據指定的排序方式，對商品價格進行排序。
    "ORDER BY " +
    //如果sort是price_low，按照價格由低到高（ASC）排序
    "CASE :sort WHEN 'price_low' THEN p.price END ASC, " +
    //sort是price_high，按照價格由高到低（DESC）排序
           //price_low和price_high是我們自己設定的，不是SQL規定的語句
    "CASE :sort WHEN 'price_high' THEN p.price END DESC")
   public List<Product> findProductsByFilter(@Param("category") String category,
                                             @Param("minPrice") Integer minPrice,
                                             @Param("maxPrice") Integer maxPrice,
                                             @Param("sort") String sort);
*/
}
