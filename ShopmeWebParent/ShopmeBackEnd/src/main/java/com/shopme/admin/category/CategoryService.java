package com.shopme.admin.category;

import com.shopme.admin.user.ResourceNotFoundException;
import com.shopme.admin.user.common.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

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
        return categoryRepository.save(category);
    }

    public void delete(Long id) throws CategoryNotFoundException {
        boolean existsByById = categoryRepository.existsById(id);

        if (!existsByById) {
            throw new CategoryNotFoundException("Could not find user by id:" + id);
        }

        categoryRepository.deleteById(id);
    }

    public void updateCategory(Long id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }


    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = rootCategory.getChildren();

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
        Set<Category> children = parent.getChildren();
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += " --";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));
            // iterate each subcategory
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
        }

    }

    public Category get(Long id) throws CategoryNotFoundException {
        try{
            return categoryRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("Could not found category with Id"+id);
        }
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                logger.info(category.getName());
                categoriesUsedInForm.add(Category.copyIdAndName(category.getId(), category.getName()));
                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    logger.info(name);

                    categoriesUsedInForm.add(Category.copyIdAndName(category.getId(), name));
                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }
}
