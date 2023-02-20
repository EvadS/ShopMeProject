package com.shopme.admin.brands;

import com.shopme.admin.user.common.entity.Brand;
import com.shopme.admin.user.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class BrandRepositoryTest {

    @Autowired
    private BrandRepository repo;

   // @Test
    public void testCreateBrand1(){
        Category laptop = new Category(6L);
        Brand acer = new Brand("Acer");

        acer.getCategory().add(laptop);

        Brand savedBrand = repo.save(acer);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);


    }
}