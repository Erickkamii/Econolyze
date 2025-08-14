package com.econolyze.dev.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()\\[\\]{}:;',?/*~$^+=<>]).{8,20}$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, " +
                "um número, um caractere especial e ter entre 8 e 20 caracteres."
)

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface StrongPassword {
    String message() default "Senha invalida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
