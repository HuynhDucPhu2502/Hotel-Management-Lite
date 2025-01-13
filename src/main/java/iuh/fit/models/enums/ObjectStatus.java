package iuh.fit.models.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ObjectStatus {
    ACTIVE("Hoạt động"),
    INACTIVE("Không hoạt động");

    public final String displayname;

    @Override
    public String toString() {
        return displayname;
    }
}
