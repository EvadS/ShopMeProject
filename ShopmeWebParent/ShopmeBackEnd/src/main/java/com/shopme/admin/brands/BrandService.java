package com.shopme.admin.brands;

import com.shopme.admin.error.BrandNotFoundException;
import com.shopme.admin.user.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandRepository repo;

    @Override
    public List<Brand> listAll() {
 //        List<Brand> brandsList = StreamSupport.stream(iterable.spliterator(), false).map(i -> {
//            i.setCategories(new HashSet<>());
//            return  i;
//        }).collect(Collectors.toList());

        return  ((List<Brand>)repo.findAll());
    }

    @Override
    public Brand save(Brand brand) {
        return repo.save(brand);
    }

    @Override
    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = repo.countById(id);

        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }

        repo.deleteById(id);
    }

    @Override
    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = repo.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null) return "Duplicate";
        } else {
            if (brandByName != null && brandByName.getId() != id) {
                return "Duplicate";
            }
        }

        return "OK";
    }
}
