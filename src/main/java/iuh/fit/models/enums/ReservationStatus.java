package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

/**
 * Admin 1/14/2025
 **/
@RequiredArgsConstructor
public enum ReservationStatus {
    RESERVATION("Đã đặt"),
    CHECKED_IN("Đã nhận phòng"),
    CHECKED_OUT("Đã trả phòng"),
    CANCEL("Đã hủy");

    private final String displayname;

    @Override
    public String toString() {
        return displayname;
    }
}
