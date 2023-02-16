package com.shopme.admin.user.common.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    @OneToOne
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public static  Category copyIdAndName(Long id, String name){
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setAlias(name);

        return  category;
    }

    public static  Category copyFull (Category category){
        Category copyCategory = new Category();

        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setImage(category.getImage());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setHasChildren(category.getChildren().size() > 0);

        return copyCategory;
    }

    public static  Category copyFull (Category category, String name){
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);

        return copyCategory;
    }

    @Transient
    public String getImagePath (){
        if(id == null || image == null){
            return "/images/image-tumbnail.png";
        }

        return  "/category-images/" + this.id + "/" + this.image;
    }


    @Transient
    private boolean hasChildren;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
