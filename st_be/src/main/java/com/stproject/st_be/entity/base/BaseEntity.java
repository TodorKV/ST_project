package com.stproject.st_be.entity.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {

        /**
         * Base ID for all entities.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entity_id_seq")
        @SequenceGenerator(name = "entity_id_seq", sequenceName = "global_id_sequence", allocationSize = 1)
        @Column(name = "id", unique = true, updatable = false, nullable = false)
        private Integer id;
}
