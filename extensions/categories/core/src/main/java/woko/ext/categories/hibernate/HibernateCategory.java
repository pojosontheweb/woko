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
    private HibernateCategory parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<HibernateCategory> subCategs;

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
        this.parent = parent;
    }

    @Override
    public HibernateCategory getParentCategory() {
        return parent;
    }

    public void setSubCategories(List<HibernateCategory> subCategs) {
        this.subCategs = subCategs;
    }

    @Override
    public List<? extends Category> getSubCategories() {
        return subCategs;
    }

}
