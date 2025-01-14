package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author Le Tran Gia Huy
 * @created 14/01/2025 - 10:56 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.models.enums
 */

@RequiredArgsConstructor
public enum AccountStatus {
    ACTIVE("Hoạt động"), LOCKED("Đã khóa");

    public final String accountStatus;

    @Override
    public String toString() {
        return accountStatus;
    }
}
