package com.shopme.admin.brands;

import com.shopme.admin.error.BrandNotFoundException;
import com.shopme.admin.user.common.entity.Brand;

import java.util.List;

public interface IBrandService {
    List<Brand> listAll();

    Brand save(Brand brand);

    Brand get(Long id) throws BrandNotFoundException;

    void delete(Long id) throws BrandNotFoundException;

     String checkUnique(Integer id, String name);
}
