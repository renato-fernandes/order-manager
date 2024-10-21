package org.aubay.challenge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity(name = "orders")
@Table(name = "orders")
public class Orders {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @Column(name = "id_order", insertable = false, updatable = false )
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "item_id")
    @JsonBackReference(value="item-order")
    private Items item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value="user-order")
    private Users user;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_complete", nullable = false)
    private Boolean isComplete;

    @OneToMany(mappedBy = "order")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<StockMovements> stockMovements;
}
