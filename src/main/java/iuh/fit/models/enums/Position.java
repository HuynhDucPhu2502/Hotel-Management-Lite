package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Position {
    RECEPTIONIST("Nhân viên tiếp tân"),
    MANAGER("Nhân viên quản lý");

    private final String position;

    @Override
    public String toString() {
        return position;
    }
}
