package com.shopme.admin.category;

import com.shopme.admin.user.ResourceNotFoundException;
import com.shopme.admin.user.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    public static final int CATEGORIES_PER_PAGE = 4;


    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);
        if (keyword != null) {
            return categoryRepository.findAll(keyword, pageable);
        }

        return categoryRepository.findAll(pageable);
    }

    public Category save(Category category) {
        boolean isUpdating = (category.getId() != null);

        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        boolean existsByById = categoryRepository.existsById(id);

        if (!existsByById) {
            throw new ResourceNotFoundException("Could not find user by id:" + id);
        }

        categoryRepository.deleteById(id);
    }

    public void updateCategory(Long id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }
}
