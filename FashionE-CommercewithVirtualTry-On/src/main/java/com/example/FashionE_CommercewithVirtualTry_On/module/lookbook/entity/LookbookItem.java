package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "lookbook_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lookbook_id", "product_id"})
})
public class LookbookItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lookbookItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookbook_id", nullable = false)
    private Lookbook lookbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Optional: styling note for this product in the lookbook
    @Column(length = 500)
    private String stylingNote;

    // Display order within the lookbook
    @Column(nullable = false)
    private Integer displayOrder = 0;

    public LookbookItem() {}

    public Long getLookbookItemId() { return lookbookItemId; }
    public void setLookbookItemId(Long lookbookItemId) { this.lookbookItemId = lookbookItemId; }

    public Lookbook getLookbook() { return lookbook; }
    public void setLookbook(Lookbook lookbook) { this.lookbook = lookbook; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getStylingNote() { return stylingNote; }
    public void setStylingNote(String stylingNote) { this.stylingNote = stylingNote; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}