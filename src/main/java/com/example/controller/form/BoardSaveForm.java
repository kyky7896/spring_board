package com.example.controller.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.example.domain.BoardType;
import com.example.validation.ValidationSteps;

import lombok.Data;

@Data
@GroupSequence({ BoardSaveForm.class, ValidationSteps.Step1.class, ValidationSteps.Step2.class,
		ValidationSteps.Step3.class, ValidationSteps.Step4.class, ValidationSteps.Step5.class, })
public class BoardSaveForm {

	private int boardSeq;

	@NotNull(groups = ValidationSteps.Step4.class, message = "{BoardSaveForm.boardType.notEmpty}")
	private BoardType boardType; //enum

	@NotEmpty(groups = ValidationSteps.Step1.class, message = "{BoardSaveForm.title.notEmpty}")
	@Length(groups = ValidationSteps.Step3.class, min = 2, max = 10, message = "{BoardSaveForm.title.length}")
	private String title;

	@NotEmpty(groups = ValidationSteps.Step5.class, message = "{BoardSaveForm.contents.notEmpty}")
	private String contents;

	// 입력으로 받는것이 아니기 때문에 필드가 필요없음
//	private String regDate;
	// userName이제 안받음.
//	@NotEmpty(groups = ValidationSteps.Step2.class,message = "{BoardSaveForm.userName.notEmpty}")
//	private String userName;

}
