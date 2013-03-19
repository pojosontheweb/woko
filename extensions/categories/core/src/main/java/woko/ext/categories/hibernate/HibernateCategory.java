package woko.ext.categories.hibernate;

import woko.ext.categories.Category;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class HibernateCategory implements Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private HibernateCategory parentCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory")
    private List<HibernateCategory> subCategories;

    private Integer index;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentCategory(HibernateCategory parent) {
        this.parentCategory = parent;
    }

    @Override
    public HibernateCategory getParentCategory() {
        return parentCategory;
    }

    public void setSubCategories(List<HibernateCategory> subCategs) {
        this.subCategories = subCategs;
    }

    @Override
    public List<? extends Category> getSubCategories() {
        return subCategories;
    }

    @Override
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
