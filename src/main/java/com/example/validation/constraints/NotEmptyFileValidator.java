package com.example.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class NotEmptyFileValidator implements ConstraintValidator<NotEmptyFile, MultipartFile>{
	
	@Override
	public void initialize(NotEmptyFile annotation) {}
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return multipartFile != null && !multipartFile.isEmpty();
	}
	
	
}
