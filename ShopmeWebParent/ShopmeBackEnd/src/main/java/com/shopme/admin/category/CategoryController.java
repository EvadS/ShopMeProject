package com.shopme.admin.category;


import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.ResourceNotFoundException;
import com.shopme.admin.user.common.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(Model model) {
     //   return listByPage(1, model, "id", "asc",null);
        List<Category> listCategories = categoryService.listAll();
        model.addAttribute("listCategories",listCategories );
        return "categories/categories";
    }


    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {
        logger.info("Sort field: {}", sortField);
        logger.info("Sort field: {}", sortDir);

        final Page<Category> page = categoryService.listByPage(pageNum, sortField, sortDir,keyword);
        final List<Category> listCategories = page.getContent();

        long startCount = (pageNum - 1) * CategoryService.CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.CATEGORIES_PER_PAGE - 1;

        if (endCount > page.getTotalPages()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCategories", listCategories);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newUser(Model model) {

        List<Category>  listCategories  = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @RequestMapping(value = "/categories/save", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveUser(Category category, RedirectAttributes redirectAttributes
            , @RequestParam(value = "fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory= categoryService.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (category.getImage().isEmpty()) {
                category.setImage(null);
            }
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has benn saved successfully");
        // return getRedirectURLtoAffectedUser(category);
        return "redirect:/categories";
    }

    private String getRedirectURLtoAffectedUser(Category category) {
        String firstPartOfEmail = category.getName().split("@")[0];
        return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id,
                             Model model, RedirectAttributes redirectAttributes) {
        // TODO: not implement
        try {
            categoryService.delete(id);
            String categoryDir = "../category-images" + id;
            FileUploadUtil.removeDit(categoryDir);

            redirectAttributes.addFlashAttribute("message", "The category ID " + id +
                    "has been deleted successfully");
        } catch (CategoryNotFoundException  ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Long id,
                                          @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {

        categoryService.updateCategory(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID:" + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory (@PathVariable (name="id") Long id,
                                Model model, RedirectAttributes ra){
        try{

            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category",category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID:" +id +")");

            return  "categories/category_form";
        }
        catch (CategoryNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
            return  "redirect:/categories";
        }
    }
}
