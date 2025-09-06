package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expense")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Double amount;
    @Column(name = "categoryId")
    private Integer categoryId;
    private Long userId;

    @Transient
    public Category getCategory() {
        return categoryId != null ? Category.fromCode(categoryId): null;
    }

    public void setCategory(Category category) {
        this.categoryId = category.getCode();
    }
}
