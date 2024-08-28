package com.klaatus.mall.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "tbl_error_messages")
@Getter
@Setter
@ToString
public class ErrorMessage {

    @Id
    private String code;

    private String message;

}