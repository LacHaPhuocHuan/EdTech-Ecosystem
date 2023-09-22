package com.thanhha.edtechcosystem.courseservice.entity;

import lombok.Getter;

@Getter
public enum AssessmentStatus {
    NOT_STARTED("Chưa bắt đầu"),
    IN_PROGRESS("Đang thực hiện"),
    COMPLETED("Đã hoàn thành"),
    UNDER_REVIEW("Đang chấm điểm"),
    GRADED("Đã chấm điểm");

    private final String status;

    AssessmentStatus(String status) {
        this.status = status;
    }

}
