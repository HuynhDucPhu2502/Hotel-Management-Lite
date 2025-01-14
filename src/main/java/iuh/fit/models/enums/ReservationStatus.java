package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

/**
 * Admin 1/14/2025
 **/
@RequiredArgsConstructor
public enum ReservationStatus {
    RESERVATION("Đã đặt"),
    IN_USE("Đang sử dụng"),
    CANCEL("Đã hết hạn");

    private final String displayname;

    @Override
    public String toString() {
        return displayname;
    }
}
