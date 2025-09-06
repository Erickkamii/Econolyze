package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "income")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Long userId;
    private LocalDate date;
    @Column(name = "categoryId")
    private Integer categoryId;
    private Long financialGoalId;

    @Transient
    public Category getCategory() {
        return categoryId != null ? Category.fromCode(categoryId): null;
    }

    public void setCategory(Category category) {
        this.categoryId = category.getCode();
    }
}
