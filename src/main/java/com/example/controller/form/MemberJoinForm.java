package com.example.controller.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.example.validation.ValidationSteps;
import com.example.validation.constraints.NotEmptyFile;

import lombok.Data;

@Data
@GroupSequence({
	MemberJoinForm.class,
	ValidationSteps.Step1.class,
	ValidationSteps.Step2.class,
	ValidationSteps.Step3.class,
	ValidationSteps.Step4.class,
	ValidationSteps.Step5.class,
	ValidationSteps.Step6.class,
	ValidationSteps.Step7.class 
})
public class MemberJoinForm {
	
	@NotEmpty(groups = ValidationSteps.Step1.class, message = "{MemberJoinForm.account.notEmpty}")
	@Email(groups = ValidationSteps.Step2.class, message = "{MemberJoinForm.account.pattern}")
	private String account;

	@NotEmpty(groups = ValidationSteps.Step3.class, message = "{MemberJoinForm.password.notEmpty}")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,12}$",groups = ValidationSteps.Step4.class, message = "{MemberJoinForm.password.pattern}")
	private String password;
	 
	@NotEmpty(groups = ValidationSteps.Step5.class, message = "{MemberJoinForm.nickname.notEmpty}")
	@Length(groups = ValidationSteps.Step6.class, min = 2, max = 10, message = "{MemberJoinForm.nickname.length}")
	private String nickname;
	
	@NotEmptyFile(groups=ValidationSteps.Step7.class, message = "{MemberJoinForm.profileImage.notNull}")
	private MultipartFile profileImage; 


}
