package org.aubay.challenge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity(name = "stock_movements")
@Table(name = "stock_movements")
public class StockMovements {

    @Id
    @SequenceGenerator(name = "stock_movement_sequence", sequenceName = "stock_movement_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_movement_sequence")
    @Column(name = "id_stock", insertable = false, updatable = false )
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "item_id")
    @JsonBackReference(value="item-stock")
    private Items item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;
}
