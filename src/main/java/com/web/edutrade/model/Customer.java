package com.web.edutrade.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Vui lòng nhập họ và tên")
    private String name;
    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^\\+?[0-9]{9,14}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    @AssertTrue(message = "Bạn cần đồng ý điều khoản để tiếp tục")
    private Boolean flagProvision ;





}
